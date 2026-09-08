package com.he.lastsongofelysian.menu;

import com.he.lastsongofelysian.registry.ModItems;
import com.he.lastsongofelysian.registry.ModMenus;
import com.he.lastsongofelysian.util.SignetUpgradeData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class PardofelisShopMenu extends AbstractContainerMenu {

    public static final int OFFER_COUNT = 10;
    public static final int UPGRADE_PRICE = 256;
    public static final int SECONDARY_SIGNET_PRICE = 386;
    public static final int DELIVERANCE_CORE_PRICE = 824;
    public static final int DELIVERANCE_CORE_SIGNET_PRICE = 496;
    public static final int REROLL_PRICE = 50;
    public static final int REROLL_BUTTON = OFFER_COUNT;
    private static final int EMPTY_OFFER = -1;

    private static final int REVERIE_BASE_PRICE = 1300;
    private static final int DATA_OFFER_START = 0;
    private static final int DATA_PURCHASED_START = DATA_OFFER_START + OFFER_COUNT;
    private static final int DATA_SILVER = DATA_PURCHASED_START + OFFER_COUNT;
    private static final int DATA_SHOP_PERCENT = DATA_SILVER + 1;
    private static final int DATA_COUNT = DATA_SHOP_PERCENT + 1;
    private static final String NBT_PLAYER_PERSISTED = "PlayerPersisted";
    private static final String NBT_STOCK = "LSEPardofelisUpgradeStock";
    private static final String NBT_PURCHASED_MASK = "LSEPardofelisUpgradePurchasedMask";
    private static final String NBT_PURCHASED_REVERIE = "LSEPardofelisPurchasedReverie";
    private static final String NBT_PURCHASED_DELIVERANCE_SECONDARIES =
            "LSEPardofelisPurchasedDeliveranceSecondaries";

    private final Inventory playerInventory;
    private final ContainerData shopData;

    public PardofelisShopMenu(int containerId, Inventory playerInventory) {
        super(ModMenus.PARDOFELIS_SHOP_MENU.get(), containerId);
        this.playerInventory = playerInventory;
        this.shopData = new SimpleContainerData(DATA_COUNT);
        this.addDataSlots(this.shopData);
        if (!playerInventory.player.level().isClientSide()) {
            ensureStock(playerInventory.player);
            refreshData();
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (player.level().isClientSide()) return true;
        if (buttonId >= 0 && buttonId < OFFER_COUNT) {
            buyOffer(player, buttonId);
            return true;
        }
        if (buttonId == REROLL_BUTTON) {
            reroll(player);
            return true;
        }
        return false;
    }

    private void buyOffer(Player player, int offerIndex) {
        int[] stock = getStock(player);
        if (offerIndex < 0 || offerIndex >= stock.length) return;

        ShopProduct product = ShopProduct.fromOrdinal(stock[offerIndex]);
        if (product == null || product.isUnavailable(player)) {
            generateStock(player);
            refreshData();
            this.broadcastChanges();
            return;
        }
        int mask = getPersistedData(player).getInt(NBT_PURCHASED_MASK);
        boolean alreadyPurchased = (mask & (1 << offerIndex)) != 0
                || product == ShopProduct.REVERIE_SIGNET && hasPurchasedReverie(player);
        if (alreadyPurchased) {
            refreshData();
            return;
        }

        int price = product.price(player);
        int silver = SignetUpgradeData.countSilver(player);
        if (silver < price) {
            player.displayClientMessage(
                    Component.literal("银币不足：" + silver + "/" + price)
                            .withStyle(ChatFormatting.YELLOW),
                    true
            );
            return;
        }
        if (!SignetUpgradeData.removeSilver(player, price)) {
            refreshData();
            return;
        }

        ItemStack purchased = new ItemStack(product.item());
        if (product.signet == SignetUpgradeData.Signet.DELIVERANCE) {
            SignetUpgradeData.setShopPaidSilverPerItem(purchased, price);
        }
        if (!player.addItem(purchased)) {
            player.drop(purchased, false);
        }

        if (product.unlockSkill != null) {
            markSecondaryPurchased(player, product.unlockSkill);
            player.displayClientMessage(
                    Component.literal("已购买：" + product.unlockSkill.displayName())
                            .withStyle(ChatFormatting.DARK_BLUE),
                    true
            );
        }

        if (product.deliveranceCore != null) {
            SignetUpgradeData.recordDeliveranceCorePurchase(
                    player,
                    product.deliveranceCore,
                    price
            );
        }
        if (product.deliveranceCoreSignet != null) {
            SignetUpgradeData.recordDeliveranceCoreSignetPurchase(
                    player,
                    product.deliveranceCoreSignet,
                    price
            );
        }

        CompoundTag persisted = getPersistedData(player);
        persisted.putInt(NBT_PURCHASED_MASK, mask | (1 << offerIndex));
        if (product == ShopProduct.REVERIE_SIGNET) {
            persisted.putBoolean(NBT_PURCHASED_REVERIE, true);
        }
        if (product.deliveranceCore != null) {
            generateStock(player);
        }
        refreshData();
        this.broadcastChanges();
    }

    private void reroll(Player player) {
        int silver = SignetUpgradeData.countSilver(player);
        if (silver < REROLL_PRICE) {
            player.displayClientMessage(
                    Component.literal("银币不足：" + silver + "/" + REROLL_PRICE)
                            .withStyle(ChatFormatting.YELLOW),
                    true
            );
            return;
        }
        if (!SignetUpgradeData.removeSilver(player, REROLL_PRICE)) {
            refreshData();
            return;
        }
        generateStock(player);
        refreshData();
        this.broadcastChanges();
    }

    private void refreshData() {
        Player player = this.playerInventory.player;
        if (player.level().isClientSide()) return;
        int[] stock = getStock(player);
        int mask = getPersistedData(player).getInt(NBT_PURCHASED_MASK);
        for (int i = 0; i < OFFER_COUNT; i++) {
            ShopProduct product = ShopProduct.fromOrdinal(stock[i]);
            this.shopData.set(DATA_OFFER_START + i, product == null ? EMPTY_OFFER : product.ordinal());
            boolean purchased = product == null
                    || (mask & (1 << i)) != 0
                    || product == ShopProduct.REVERIE_SIGNET && hasPurchasedReverie(player);
            this.shopData.set(DATA_PURCHASED_START + i, purchased ? 1 : 0);
        }
        this.shopData.set(DATA_SILVER, SignetUpgradeData.countSilver(player));
        this.shopData.set(DATA_SHOP_PERCENT, getReverieShopPercent(player));
    }

    private static void ensureStock(Player player) {
        int[] stock = getPersistedData(player).getIntArray(NBT_STOCK);
        if (!isValidStock(player, stock)) {
            generateStock(player);
        }
    }

    private static boolean isValidStock(Player player, int[] stock) {
        if (stock.length != OFFER_COUNT) return false;
        boolean[] seen = new boolean[ShopProduct.values().length];
        int purchasedMask = getPersistedData(player).getInt(NBT_PURCHASED_MASK);
        for (int index = 0; index < stock.length; index++) {
            int value = stock[index];
            if (value == EMPTY_OFFER) continue;
            if (value < 0 || value >= seen.length || seen[value]) return false;
            ShopProduct product = ShopProduct.fromOrdinal(value);
            boolean purchasedPermanentProduct = product != null
                    && product.isPermanentProduct()
                    && (purchasedMask & (1 << index)) != 0;
            if (product == null || product.isUnavailable(player) && !purchasedPermanentProduct) {
                return false;
            }
            seen[value] = true;
        }
        return true;
    }

    private static void generateStock(Player player) {
        List<Integer> products = new ArrayList<>();
        for (ShopProduct product : ShopProduct.values()) {
            if (product.isUnavailable(player)) continue;
            products.add(product.ordinal());
        }
        for (int i = products.size() - 1; i > 0; i--) {
            int swap = player.getRandom().nextInt(i + 1);
            int value = products.get(i);
            products.set(i, products.get(swap));
            products.set(swap, value);
        }
        int[] stock = new int[OFFER_COUNT];
        Arrays.fill(stock, EMPTY_OFFER);
        for (int i = 0; i < Math.min(OFFER_COUNT, products.size()); i++) {
            stock[i] = products.get(i);
        }
        CompoundTag persisted = getPersistedData(player);
        persisted.putIntArray(NBT_STOCK, stock);
        persisted.putInt(NBT_PURCHASED_MASK, 0);
    }

    private static int[] getStock(Player player) {
        ensureStock(player);
        return getPersistedData(player).getIntArray(NBT_STOCK);
    }

    public ItemStack getDisplayedOffer(int index) {
        if (index < 0 || index >= OFFER_COUNT) return ItemStack.EMPTY;
        ShopProduct product = ShopProduct.fromOrdinal(
                this.shopData.get(DATA_OFFER_START + index)
        );
        return product == null ? ItemStack.EMPTY : new ItemStack(product.item());
    }

    public int getDisplayedOfferPrice(int index) {
        if (index < 0 || index >= OFFER_COUNT) return 0;
        ShopProduct product = ShopProduct.fromOrdinal(
                this.shopData.get(DATA_OFFER_START + index)
        );
        if (product == null) return 0;
        return product.price * this.shopData.get(DATA_SHOP_PERCENT) / 100;
    }

    public boolean isDisplayedOfferEmpty(int index) {
        return getDisplayedOffer(index).isEmpty();
    }

    public boolean isDisplayedOfferPurchased(int index) {
        return index >= 0
                && index < OFFER_COUNT
                && this.shopData.get(DATA_PURCHASED_START + index) != 0;
    }

    public int getDisplayedSilver() {
        return this.shopData.get(DATA_SILVER);
    }

    private static int getReverieShopPercent(Player player) {
        boolean hasReverie = CuriosApi.getCuriosHelper()
                .findFirstCurio(
                        player,
                        stack -> stack.is(ModItems.SIGNET_OF_REVERIE.get())
                )
                .isPresent();
        return hasReverie
                ? SignetUpgradeData.reverieShopPercent(player)
                : 100;
    }

    private static int getShopPrice(Player player, int basePrice) {
        return basePrice * getReverieShopPercent(player) / 100;
    }

    private static CompoundTag getPersistedData(Player player) {
        CompoundTag root = player.getPersistentData();
        if (!root.contains(NBT_PLAYER_PERSISTED)) {
            root.put(NBT_PLAYER_PERSISTED, new CompoundTag());
        }
        return root.getCompound(NBT_PLAYER_PERSISTED);
    }

    public static void invalidateStock(Player player) {
        if (player == null) return;
        CompoundTag persisted = getPersistedData(player);
        persisted.remove(NBT_STOCK);
        persisted.remove(NBT_PURCHASED_MASK);
    }

    private static boolean hasPurchasedSecondary(
            Player player,
            SignetUpgradeData.Skill skill
    ) {
        if (skill == null || !skill.isDeliveranceSecondary()) return false;
        int purchasedMask = getPersistedData(player)
                .getInt(NBT_PURCHASED_DELIVERANCE_SECONDARIES);
        return (purchasedMask & skill.deliveranceSecondaryBit()) != 0
                || SignetUpgradeData.isSecondarySkillUnlocked(player, skill);
    }

    private static void markSecondaryPurchased(
            Player player,
            SignetUpgradeData.Skill skill
    ) {
        if (skill == null || !skill.isDeliveranceSecondary()) return;
        CompoundTag persisted = getPersistedData(player);
        int purchasedMask = persisted.getInt(NBT_PURCHASED_DELIVERANCE_SECONDARIES);
        persisted.putInt(
                NBT_PURCHASED_DELIVERANCE_SECONDARIES,
                purchasedMask | skill.deliveranceSecondaryBit()
        );
    }

    private static boolean hasPurchasedReverie(Player player) {
        return getPersistedData(player).getBoolean(NBT_PURCHASED_REVERIE);
    }

    @Override
    public void broadcastChanges() {
        refreshData();
        super.broadcastChanges();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    private enum ShopProduct {
        REVERIE_SIGNET(
                () -> ModItems.SIGNET_OF_REVERIE.get(),
                REVERIE_BASE_PRICE,
                (SignetUpgradeData.Signet) null
        ),
        REVERIE_UPGRADE(() -> ModItems.REVERIE_SIGNET_UPGRADE.get(), UPGRADE_PRICE, SignetUpgradeData.Signet.REVERIE),
        VICISSITUDE_UPGRADE(() -> ModItems.VICISSITUDE_SIGNET_UPGRADE.get(), UPGRADE_PRICE, SignetUpgradeData.Signet.VICISSITUDE),
        STARS_UPGRADE(() -> ModItems.STARS_SIGNET_UPGRADE.get(), UPGRADE_PRICE, SignetUpgradeData.Signet.STARS),
        INFINITY_UPGRADE(() -> ModItems.INFINITY_SIGNET_UPGRADE.get(), UPGRADE_PRICE, SignetUpgradeData.Signet.INFINITY),
        DAYBREAK_UPGRADE(() -> ModItems.DAYBREAK_SIGNET_UPGRADE.get(), UPGRADE_PRICE, SignetUpgradeData.Signet.DAYBREAK),
        SETSURA_UPGRADE(() -> ModItems.SETSURA_SIGNET_UPGRADE.get(), UPGRADE_PRICE, SignetUpgradeData.Signet.SETSURA),
        BODHI_UPGRADE(() -> ModItems.BODHI_SIGNET_UPGRADE.get(), UPGRADE_PRICE, SignetUpgradeData.Signet.BODHI),
        DECIMATION_UPGRADE(() -> ModItems.DECIMATION_SIGNET_UPGRADE.get(), UPGRADE_PRICE, SignetUpgradeData.Signet.DECIMATION),
        HELIX_UPGRADE(() -> ModItems.HELIX_SIGNET_UPGRADE.get(), UPGRADE_PRICE, SignetUpgradeData.Signet.HELIX),
        GOLD_UPGRADE(() -> ModItems.GOLD_SIGNET_UPGRADE.get(), UPGRADE_PRICE, SignetUpgradeData.Signet.GOLD),
        DISCIPLINE_UPGRADE(() -> ModItems.DISCIPLINE_SIGNET_UPGRADE.get(), UPGRADE_PRICE, SignetUpgradeData.Signet.DISCIPLINE),
        DELIVERANCE_UPGRADE(() -> ModItems.DELIVERANCE_SIGNET_UPGRADE.get(), UPGRADE_PRICE, SignetUpgradeData.Signet.DELIVERANCE),
        DELIVERANCE_HUNTERS_MASK(
                () -> ModItems.DELIVERANCE_HUNTERS_MASK.get(),
                SECONDARY_SIGNET_PRICE,
                SignetUpgradeData.Skill.DELIVERANCE_HUNTERS_MASK,
                true
        ),
        DELIVERANCE_RESTRAINERS_RELIC(
                () -> ModItems.DELIVERANCE_RESTRAINERS_RELIC.get(),
                SECONDARY_SIGNET_PRICE,
                SignetUpgradeData.Skill.DELIVERANCE_RESTRAINERS_RELIC,
                true
        ),
        DELIVERANCE_SEEKERS_ROBE(
                () -> ModItems.DELIVERANCE_SEEKERS_ROBE.get(),
                SECONDARY_SIGNET_PRICE,
                SignetUpgradeData.Skill.DELIVERANCE_SEEKERS_ROBE,
                true
        ),
        DELIVERANCE_KINGS_SWORD(
                () -> ModItems.DELIVERANCE_KINGS_SWORD.get(),
                DELIVERANCE_CORE_PRICE,
                SignetUpgradeData.DeliveranceCore.KINGS_SWORD
        ),
        DELIVERANCE_LONE_SHADOW(
                () -> ModItems.DELIVERANCE_LONE_SHADOW.get(),
                DELIVERANCE_CORE_PRICE,
                SignetUpgradeData.DeliveranceCore.LONE_SHADOW
        ),
        DELIVERANCE_KINGS_MUSTER(
                () -> ModItems.DELIVERANCE_KINGS_MUSTER.get(),
                DELIVERANCE_CORE_SIGNET_PRICE,
                SignetUpgradeData.Skill.DELIVERANCE_KINGS_MUSTER
        ),
        DELIVERANCE_KINGS_EXPEDITION(
                () -> ModItems.DELIVERANCE_KINGS_EXPEDITION.get(),
                DELIVERANCE_CORE_SIGNET_PRICE,
                SignetUpgradeData.Skill.DELIVERANCE_KINGS_EXPEDITION
        ),
        DELIVERANCE_KINGS_ECHO(
                () -> ModItems.DELIVERANCE_KINGS_ECHO.get(),
                DELIVERANCE_CORE_SIGNET_PRICE,
                SignetUpgradeData.Skill.DELIVERANCE_KINGS_ECHO
        ),
        DELIVERANCE_LONE_RESIDUAL_DREAM(
                () -> ModItems.DELIVERANCE_LONE_RESIDUAL_DREAM.get(),
                DELIVERANCE_CORE_SIGNET_PRICE,
                SignetUpgradeData.Skill.DELIVERANCE_LONE_RESIDUAL_DREAM
        ),
        DELIVERANCE_LONE_DECISION(
                () -> ModItems.DELIVERANCE_LONE_DECISION.get(),
                DELIVERANCE_CORE_SIGNET_PRICE,
                SignetUpgradeData.Skill.DELIVERANCE_LONE_DECISION
        ),
        DELIVERANCE_LONE_TRIUMPH(
                () -> ModItems.DELIVERANCE_LONE_TRIUMPH.get(),
                DELIVERANCE_CORE_SIGNET_PRICE,
                SignetUpgradeData.Skill.DELIVERANCE_LONE_TRIUMPH
        );

        private final Supplier<Item> item;
        private final int price;
        private final SignetUpgradeData.Signet signet;
        private final SignetUpgradeData.Skill unlockSkill;
        private final SignetUpgradeData.DeliveranceCore deliveranceCore;
        private final SignetUpgradeData.Skill deliveranceCoreSignet;

        ShopProduct(Supplier<Item> item, int price, SignetUpgradeData.Signet signet) {
            this.item = item;
            this.price = price;
            this.signet = signet;
            this.unlockSkill = null;
            this.deliveranceCore = null;
            this.deliveranceCoreSignet = null;
        }

        ShopProduct(Supplier<Item> item, int price, SignetUpgradeData.Skill unlockSkill,
                    boolean directUnlock) {
            this.item = item;
            this.price = price;
            this.signet = null;
            this.unlockSkill = unlockSkill;
            this.deliveranceCore = null;
            this.deliveranceCoreSignet = null;
        }

        ShopProduct(
                Supplier<Item> item,
                int price,
                SignetUpgradeData.DeliveranceCore deliveranceCore
        ) {
            this.item = item;
            this.price = price;
            this.signet = null;
            this.unlockSkill = null;
            this.deliveranceCore = deliveranceCore;
            this.deliveranceCoreSignet = null;
        }

        ShopProduct(Supplier<Item> item, int price, SignetUpgradeData.Skill coreSignet) {
            this.item = item;
            this.price = price;
            this.signet = null;
            this.unlockSkill = null;
            this.deliveranceCore = null;
            this.deliveranceCoreSignet = coreSignet;
        }

        Item item() {
            return this.item.get();
        }

        int price(Player player) {
            return getShopPrice(player, this.price);
        }

        boolean isUnavailable(Player player) {
            if (this == REVERIE_SIGNET) return hasPurchasedReverie(player);
            if (this.unlockSkill != null) {
                return hasPurchasedSecondary(player, this.unlockSkill);
            }
            if (this.deliveranceCore != null) {
                return SignetUpgradeData.getSelectedDeliveranceCore(player) != null;
            }
            if (this.deliveranceCoreSignet != null) {
                SignetUpgradeData.DeliveranceCore selected =
                        SignetUpgradeData.getSelectedDeliveranceCore(player);
                return selected != this.deliveranceCoreSignet.deliveranceCore()
                        || SignetUpgradeData.hasPurchasedDeliveranceCoreSignet(
                        player,
                        this.deliveranceCoreSignet
                )
                        || SignetUpgradeData.hasDeliveranceCoreSignet(
                        player,
                        this.deliveranceCoreSignet
                );
            }
            return this.signet != null && SignetUpgradeData.isSignetMaxed(player, this.signet);
        }

        boolean isPermanentProduct() {
            return this == REVERIE_SIGNET
                    || this.unlockSkill != null
                    || this.deliveranceCore != null
                    || this.deliveranceCoreSignet != null;
        }

        static ShopProduct fromOrdinal(int ordinal) {
            ShopProduct[] products = values();
            return ordinal >= 0 && ordinal < products.length
                    ? products[ordinal]
                    : null;
        }
    }
}
