package com.he.lastsongofelysian.menu;

import com.he.lastsongofelysian.event.SignetEffects;
import com.he.lastsongofelysian.registry.ModMenus;
import com.he.lastsongofelysian.util.SignetUpgradeData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SpiralWorkshopMenu extends AbstractContainerMenu {

    public static final int SIGNET_SLOT_X = 42;
    public static final int SIGNET_SLOT_Y = 67;
    public static final int SECONDARY_SLOT_X = 42;
    public static final int SECONDARY_SLOT_Y = 116;
    public static final int PLAYER_INVENTORY_X = 112;
    public static final int PLAYER_INVENTORY_Y = 224;
    public static final int PLAYER_HOTBAR_Y = 282;
    public static final int MAX_SKILL_ROWS = 3;
    public static final int FUSE_BUTTON = MAX_SKILL_ROWS;
    public static final int PAGE_BUTTON = FUSE_BUTTON + 1;
    public static final int RESET_BUTTON = PAGE_BUTTON + 1;

    private static final int SIGNET_SLOT = 0;
    private static final int SECONDARY_SLOT = 1;
    private static final int PLAYER_INVENTORY_START = 2;
    private static final int PLAYER_INVENTORY_END = 29;
    private static final int PLAYER_HOTBAR_START = 29;
    private static final int PLAYER_HOTBAR_END = 38;
    private static final int DATA_UPGRADE_ITEMS = 0;
    private static final int DATA_EXPERIENCE_LEVEL = 1;
    private static final int DATA_DELIVERANCE_SECONDARY_UNLOCKS = 2;
    private static final int DATA_DELIVERANCE_CORE = 3;
    private static final int DATA_DELIVERANCE_CORE_SIGNETS = 4;
    private static final int DATA_SKILL_PAGE = 5;
    private static final int DATA_HAS_CORE_PROGRESS = 6;
    private static final int DATA_COUNT = 7;

    private final Inventory playerInventory;
    private final Container signetInput;
    private final Container secondaryInput;
    private final ContainerData workshopData;

    public SpiralWorkshopMenu(int containerId, Inventory playerInventory) {
        super(ModMenus.SPIRAL_WORKSHOP_MENU.get(), containerId);
        this.playerInventory = playerInventory;
        this.signetInput = new SimpleContainer(1) {
            @Override
            public void setChanged() {
                super.setChanged();
                SpiralWorkshopMenu.this.slotsChanged(this);
            }
        };
        this.secondaryInput = new SimpleContainer(1) {
            @Override
            public void setChanged() {
                super.setChanged();
                SpiralWorkshopMenu.this.slotsChanged(this);
            }
        };
        this.workshopData = new SimpleContainerData(DATA_COUNT);

        this.addSlot(new Slot(this.signetInput, 0, SIGNET_SLOT_X, SIGNET_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return SignetUpgradeData.isUpgradeableSignet(stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        this.addSlot(new Slot(this.secondaryInput, 0, SECONDARY_SLOT_X, SECONDARY_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (!SignetUpgradeData.isDeliveranceFusionItem(stack)) return false;
                SignetUpgradeData.DeliveranceCore itemCore =
                        SignetUpgradeData.deliveranceCoreFromItem(stack);
                SignetUpgradeData.DeliveranceCore selectedCore =
                        SignetUpgradeData.getSelectedDeliveranceCore(playerInventory.player);
                if (itemCore != null) {
                    return selectedCore == null || selectedCore == itemCore;
                }
                SignetUpgradeData.Skill coreSignet =
                        SignetUpgradeData.deliveranceCoreSignetFromItem(stack);
                return coreSignet == null
                        || selectedCore == null
                        || selectedCore == coreSignet.deliveranceCore();
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        PLAYER_INVENTORY_X + column * 18,
                        PLAYER_INVENTORY_Y + row * 18
                ));
            }
        }

        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(
                    playerInventory,
                    column,
                    PLAYER_INVENTORY_X + column * 18,
                    PLAYER_HOTBAR_Y
            ));
        }

        this.addDataSlots(this.workshopData);
        refreshData();
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (buttonId == FUSE_BUTTON) {
            if (!player.level().isClientSide()) {
                fuseSecondarySignet(player);
            }
            return true;
        }
        if (buttonId == PAGE_BUTTON) {
            if (!player.level().isClientSide()) {
                int skillCount = SignetUpgradeData.workshopSkills(
                        player,
                        SignetUpgradeData.fromStack(this.signetInput.getItem(0))
                ).size();
                int pageCount = Math.max(1, (skillCount + MAX_SKILL_ROWS - 1) / MAX_SKILL_ROWS);
                this.workshopData.set(
                        DATA_SKILL_PAGE,
                        (this.workshopData.get(DATA_SKILL_PAGE) + 1) % pageCount
                );
                this.broadcastChanges();
            }
            return true;
        }
        if (buttonId == RESET_BUTTON) {
            if (!player.level().isClientSide()) {
                resetDeliveranceCore(player);
            }
            return true;
        }
        if (buttonId < 0 || buttonId >= MAX_SKILL_ROWS) return false;
        if (player.level().isClientSide()) return true;

        ItemStack stack = this.signetInput.getItem(0);
        SignetUpgradeData.Signet signet = SignetUpgradeData.fromStack(stack);
        int skillIndex = this.workshopData.get(DATA_SKILL_PAGE) * MAX_SKILL_ROWS + buttonId;
        SignetUpgradeData.Skill skill = SignetUpgradeData.skillAt(player, signet, skillIndex);
        if (signet == null || skill == null) return true;

        SignetUpgradeData.synchronizeLevels(player, stack, signet);
        int level = SignetUpgradeData.getPlayerLevel(player, skill);
        if (level >= SignetUpgradeData.MAX_LEVEL) {
            player.displayClientMessage(
                    Component.literal(skill.displayName() + " 已满级")
                            .withStyle(ChatFormatting.GOLD),
                    true
            );
            return true;
        }

        boolean creative = player.getAbilities().instabuild;
        int trackedSilverCost = 0;
        if (!creative) {
            int upgradeItems = SignetUpgradeData.countUpgradeItems(player, signet);
            if (upgradeItems < SignetUpgradeData.UPGRADE_ITEM_COST) {
                player.displayClientMessage(
                        Component.literal("缺少" + signet.displayName() + "刻印升级")
                                .withStyle(ChatFormatting.YELLOW),
                        true
                );
                return true;
            }

            if (player.experienceLevel < SignetUpgradeData.EXPERIENCE_LEVEL_COST) {
                player.displayClientMessage(
                        Component.literal("等级不足：" + player.experienceLevel + "/" + SignetUpgradeData.EXPERIENCE_LEVEL_COST)
                                .withStyle(ChatFormatting.YELLOW),
                        true
                );
                return true;
            }

            if (skill.isDeliveranceCoreSignet()) {
                trackedSilverCost = SignetUpgradeData.removeUpgradeItemsWithTrackedCost(
                        player,
                        signet,
                        SignetUpgradeData.UPGRADE_ITEM_COST
                );
                if (trackedSilverCost < 0) {
                    refreshData();
                    return true;
                }
            } else if (!SignetUpgradeData.removeUpgradeItems(
                    player,
                    signet,
                    SignetUpgradeData.UPGRADE_ITEM_COST
            )) {
                refreshData();
                return true;
            }

            player.giveExperienceLevels(-SignetUpgradeData.EXPERIENCE_LEVEL_COST);
        }

        if (!SignetUpgradeData.upgradeStack(player, stack, skill)) return true;

        if (!creative && skill.isDeliveranceCoreSignet()) {
            SignetUpgradeData.recordDeliveranceCoreUpgrade(
                    player,
                    trackedSilverCost,
                    SignetUpgradeData.EXPERIENCE_LEVEL_COST
            );
        }

        this.signetInput.setChanged();
        player.getInventory().setChanged();
        refreshData();
        this.broadcastChanges();
        player.displayClientMessage(
                Component.literal(skill.displayName() + "  +" + (level + 1))
                        .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD),
                true
        );
        return true;
    }

    private void fuseSecondarySignet(Player player) {
        ItemStack deliveranceStack = this.signetInput.getItem(0);
        if (SignetUpgradeData.fromStack(deliveranceStack) != SignetUpgradeData.Signet.DELIVERANCE) {
            player.displayClientMessage(
                    Component.literal("请先放入救世刻印").withStyle(ChatFormatting.YELLOW),
                    true
            );
            return;
        }

        ItemStack secondaryStack = this.secondaryInput.getItem(0);
        SignetUpgradeData.Skill skill = SignetUpgradeData.secondarySkillFromItem(secondaryStack);
        SignetUpgradeData.DeliveranceCore core =
                SignetUpgradeData.deliveranceCoreFromItem(secondaryStack);
        SignetUpgradeData.Skill coreSignet =
                SignetUpgradeData.deliveranceCoreSignetFromItem(secondaryStack);
        if (skill == null && core == null && coreSignet == null) {
            player.displayClientMessage(
                    Component.literal("请放入救世附加刻印").withStyle(ChatFormatting.YELLOW),
                    true
            );
            return;
        }

        boolean fused;
        String displayName;
        if (core != null) {
            fused = SignetUpgradeData.fuseDeliveranceCore(player, deliveranceStack, core);
            displayName = core.displayName();
        } else if (coreSignet != null) {
            fused = SignetUpgradeData.fuseDeliveranceCoreSignet(
                    player,
                    deliveranceStack,
                    coreSignet
            );
            displayName = coreSignet.displayName();
        } else {
            fused = SignetUpgradeData.fuseSecondarySkill(player, deliveranceStack, skill);
            displayName = skill.displayName();
        }

        if (!fused) {
            String reason = coreSignet != null
                    && SignetUpgradeData.getDeliveranceCore(player) != coreSignet.deliveranceCore()
                    ? "需要先融合对应核心"
                    : displayName + " 已融合或与当前核心冲突";
            player.displayClientMessage(
                    Component.literal(reason).withStyle(ChatFormatting.GOLD),
                    true
            );
            return;
        }

        secondaryStack.shrink(1);
        if (secondaryStack.isEmpty()) {
            this.secondaryInput.setItem(0, ItemStack.EMPTY);
        } else {
            this.secondaryInput.setChanged();
        }
        this.signetInput.setChanged();
        player.getInventory().setChanged();
        refreshData();
        this.broadcastChanges();
        player.displayClientMessage(
                Component.literal("已融合：" + displayName)
                        .withStyle(ChatFormatting.DARK_BLUE),
                true
        );
    }

    private void resetDeliveranceCore(Player player) {
        if (SignetUpgradeData.fromStack(this.signetInput.getItem(0))
                != SignetUpgradeData.Signet.DELIVERANCE) {
            player.displayClientMessage(
                    Component.literal("请先放入救世刻印")
                            .withStyle(ChatFormatting.YELLOW),
                    true
            );
            return;
        }
        if (!SignetUpgradeData.hasDeliveranceCoreProgress(player)) {
            player.displayClientMessage(
                    Component.literal("没有可重置的救世核心刻印")
                            .withStyle(ChatFormatting.YELLOW),
                    true
            );
            return;
        }

        if (hasUnfusedDeliveranceCoreItem(player)) {
            player.displayClientMessage(
                    Component.literal("背包或融合槽中仍有未融合的核心刻印，请先处理后再重置")
                            .withStyle(ChatFormatting.YELLOW),
                    true
            );
            return;
        }

        SignetUpgradeData.ResetRefund refund =
                SignetUpgradeData.resetDeliveranceCoreProgress(
                        player,
                        this.signetInput.getItem(0)
                );
        PardofelisShopMenu.invalidateStock(player);
        SignetEffects.resetDeliveranceCoreRuntime(player);
        this.workshopData.set(DATA_SKILL_PAGE, 0);
        refreshData();
        this.broadcastChanges();
        player.displayClientMessage(
                Component.literal(
                        "救世核心已重置，返还 " + refund.silver()
                                + " 银币与 " + refund.experienceLevels() + " 级经验"
                ).withStyle(ChatFormatting.AQUA),
                true
        );
    }

    private boolean hasUnfusedDeliveranceCoreItem(Player player) {
        if (SignetUpgradeData.isDeliveranceCoreSystemItem(this.secondaryInput.getItem(0))) {
            return true;
        }
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (SignetUpgradeData.isDeliveranceCoreSystemItem(inventory.getItem(slot))) {
                return true;
            }
        }
        return false;
    }

    private void refreshData() {
        if (this.playerInventory.player.level().isClientSide()) return;
        ItemStack stack = getInputStack();
        SignetUpgradeData.Signet signet = SignetUpgradeData.fromStack(stack);
        SignetUpgradeData.synchronizeLevels(this.playerInventory.player, stack, signet);
        this.workshopData.set(
                DATA_UPGRADE_ITEMS,
                SignetUpgradeData.countUpgradeItems(
                        this.playerInventory.player,
                        signet
                )
        );
        this.workshopData.set(
                DATA_EXPERIENCE_LEVEL,
                this.playerInventory.player.experienceLevel
        );
        this.workshopData.set(
                DATA_DELIVERANCE_SECONDARY_UNLOCKS,
                SignetUpgradeData.getDeliveranceSecondaryUnlockMask(this.playerInventory.player)
        );
        SignetUpgradeData.DeliveranceCore core = signet == SignetUpgradeData.Signet.DELIVERANCE
                ? SignetUpgradeData.getDeliveranceCore(this.playerInventory.player)
                : null;
        this.workshopData.set(DATA_DELIVERANCE_CORE, core == null ? 0 : core.id());
        this.workshopData.set(
                DATA_DELIVERANCE_CORE_SIGNETS,
                signet == SignetUpgradeData.Signet.DELIVERANCE
                        ? SignetUpgradeData.getDeliveranceCoreSignetMask(this.playerInventory.player)
                        : 0
        );
        this.workshopData.set(
                DATA_HAS_CORE_PROGRESS,
                SignetUpgradeData.hasDeliveranceCoreProgress(this.playerInventory.player) ? 1 : 0
        );
        int skillCount = SignetUpgradeData.workshopSkills(
                this.playerInventory.player,
                signet
        ).size();
        int maxPage = Math.max(0, (skillCount - 1) / MAX_SKILL_ROWS);
        if (this.workshopData.get(DATA_SKILL_PAGE) > maxPage) {
            this.workshopData.set(DATA_SKILL_PAGE, maxPage);
        }
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        refreshData();
    }

    @Override
    public void broadcastChanges() {
        refreshData();
        super.broadcastChanges();
    }

    public ItemStack getInputStack() {
        return this.signetInput.getItem(0);
    }

    public ItemStack getSecondaryInputStack() {
        return this.secondaryInput.getItem(0);
    }

    public SignetUpgradeData.Skill getSecondaryInputSkill() {
        return SignetUpgradeData.secondarySkillFromItem(getSecondaryInputStack());
    }

    public boolean canFuseSecondary() {
        SignetUpgradeData.Skill skill = getSecondaryInputSkill();
        SignetUpgradeData.DeliveranceCore core =
                SignetUpgradeData.deliveranceCoreFromItem(getSecondaryInputStack());
        SignetUpgradeData.Skill coreSignet =
                SignetUpgradeData.deliveranceCoreSignetFromItem(getSecondaryInputStack());
        if (getInputSignet() != SignetUpgradeData.Signet.DELIVERANCE) return false;
        if (skill != null) {
            return (this.workshopData.get(DATA_DELIVERANCE_SECONDARY_UNLOCKS)
                    & skill.deliveranceSecondaryBit()) == 0;
        }
        SignetUpgradeData.DeliveranceCore selected = getDisplayedDeliveranceCore();
        if (core != null) return selected == null;
        return coreSignet != null
                && selected == coreSignet.deliveranceCore()
                && (this.workshopData.get(DATA_DELIVERANCE_CORE_SIGNETS)
                & coreSignet.deliveranceCoreSignetBit()) == 0;
    }

    public boolean isSecondaryInputFused() {
        SignetUpgradeData.Skill skill = getSecondaryInputSkill();
        if (skill != null) {
            return (this.workshopData.get(DATA_DELIVERANCE_SECONDARY_UNLOCKS)
                    & skill.deliveranceSecondaryBit()) != 0;
        }
        SignetUpgradeData.DeliveranceCore core =
                SignetUpgradeData.deliveranceCoreFromItem(getSecondaryInputStack());
        if (core != null) return getDisplayedDeliveranceCore() != null;
        SignetUpgradeData.Skill coreSignet =
                SignetUpgradeData.deliveranceCoreSignetFromItem(getSecondaryInputStack());
        return coreSignet != null
                && (this.workshopData.get(DATA_DELIVERANCE_CORE_SIGNETS)
                & coreSignet.deliveranceCoreSignetBit()) != 0;
    }

    public SignetUpgradeData.Signet getInputSignet() {
        return SignetUpgradeData.fromStack(getInputStack());
    }

    public SignetUpgradeData.Skill getDisplayedSkill(int index) {
        return SignetUpgradeData.skillAt(
                getInputSignet(),
                this.workshopData.get(DATA_DELIVERANCE_SECONDARY_UNLOCKS),
                getDisplayedDeliveranceCore(),
                this.workshopData.get(DATA_DELIVERANCE_CORE_SIGNETS),
                this.workshopData.get(DATA_SKILL_PAGE) * MAX_SKILL_ROWS + index
        );
    }

    public SignetUpgradeData.DeliveranceCore getDisplayedDeliveranceCore() {
        return SignetUpgradeData.DeliveranceCore.fromId(
                this.workshopData.get(DATA_DELIVERANCE_CORE)
        );
    }

    public int getDisplayedSkillPage() {
        return this.workshopData.get(DATA_SKILL_PAGE);
    }

    public int getDisplayedSkillPageCount() {
        int count = SignetUpgradeData.workshopSkills(
                getInputSignet(),
                this.workshopData.get(DATA_DELIVERANCE_SECONDARY_UNLOCKS),
                getDisplayedDeliveranceCore(),
                this.workshopData.get(DATA_DELIVERANCE_CORE_SIGNETS)
        ).size();
        return Math.max(1, (count + MAX_SKILL_ROWS - 1) / MAX_SKILL_ROWS);
    }

    public boolean hasDisplayedCoreProgress() {
        return this.workshopData.get(DATA_HAS_CORE_PROGRESS) != 0;
    }

    public int getDisplayedLevel(int index) {
        SignetUpgradeData.Skill skill = getDisplayedSkill(index);
        return skill == null ? 0 : SignetUpgradeData.getLevel(getInputStack(), skill);
    }

    public int getDisplayedUpgradeItemCount() {
        return this.workshopData.get(DATA_UPGRADE_ITEMS);
    }

    public int getDisplayedExperienceLevel() {
        return this.workshopData.get(DATA_EXPERIENCE_LEVEL);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        if (index == SIGNET_SLOT || index == SECONDARY_SLOT) {
            if (!this.moveItemStackTo(
                    stack,
                    PLAYER_INVENTORY_START,
                    PLAYER_HOTBAR_END,
                    true
            )) {
                return ItemStack.EMPTY;
            }
        } else if (SignetUpgradeData.isUpgradeableSignet(stack)
                && !this.slots.get(SIGNET_SLOT).hasItem()) {
            if (!this.moveItemStackTo(stack, SIGNET_SLOT, SIGNET_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (SignetUpgradeData.isDeliveranceFusionItem(stack)
                && !this.slots.get(SECONDARY_SLOT).hasItem()) {
            if (!this.moveItemStackTo(stack, SECONDARY_SLOT, SECONDARY_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= PLAYER_INVENTORY_START && index < PLAYER_INVENTORY_END) {
            if (!this.moveItemStackTo(
                    stack,
                    PLAYER_HOTBAR_START,
                    PLAYER_HOTBAR_END,
                    false
            )) {
                return ItemStack.EMPTY;
            }
        } else if (index >= PLAYER_HOTBAR_START && index < PLAYER_HOTBAR_END) {
            if (!this.moveItemStackTo(
                    stack,
                    PLAYER_INVENTORY_START,
                    PLAYER_INVENTORY_END,
                    false
            )) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == copy.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return copy;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.clearContainer(player, this.signetInput);
        this.clearContainer(player, this.secondaryInput);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
