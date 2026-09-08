package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.network.CocoonStageSyncPacket;
import com.he.lastsongofelysian.network.ModNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PacketDistributor;

import java.util.Locale;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class CocoonCoreProgress {

    private CocoonCoreProgress() {
    }

    private static final String NBT_PLAYER_PERSISTED = "PlayerPersisted";
    private static final String NBT_CORE_STAGE = "CocoonHerrscherCoreStage";
    private static final String NBT_ACQUIRED_CORE_MASK = "CocoonAcquiredCoreMask";
    private static final String NBT_CURSES_REVERSED = "CocoonCursesReversed";
    private static final ResourceLocation[] ALL_CORE_IDS = {
            coreId("core_of_reason"), coreId("core_of_void"),
            coreId("core_of_thunder"), coreId("core_of_wind"),
            coreId("core_of_ice"), coreId("core_of_death"),
            coreId("core_of_fire"), coreId("core_of_sentience"),
            coreId("core_of_earth"), coreId("core_of_dominance"),
            coreId("core_of_binding"), coreId("core_of_corrosion"),
            coreId("core_of_origin")
    };
    private static final int ALL_CORE_MASK = (1 << ALL_CORE_IDS.length) - 1;

    public static final int REASON_STAGE = 1;
    public static final int VOID_STAGE = 2;
    public static final int THUNDER_STAGE = 3;
    public static final int WIND_STAGE = 4;
    public static final int ICE_STAGE = 5;
    public static final int DEATH_STAGE = 6;
    public static final int FIRE_STAGE = 7;
    public static final int SENTIENCE_STAGE = 8;
    public static final int EARTH_STAGE = 9;
    public static final int DOMINANCE_STAGE = 10;
    public static final int BINDING_STAGE = 11;
    public static final int CORROSION_STAGE = 12;
    public static final int MAX_STAGE = 12;

    private enum CoreEntry {
        REASON(REASON_STAGE, "理之核心", "core_of_reason", ChatFormatting.DARK_BLUE),
        VOID(VOID_STAGE, "空之核心", "core_of_void", ChatFormatting.GOLD),
        THUNDER(THUNDER_STAGE, "雷之核心", "core_of_thunder", ChatFormatting.LIGHT_PURPLE),
        WIND(WIND_STAGE, "风之核心", "core_of_wind", ChatFormatting.DARK_GREEN),
        ICE(ICE_STAGE, "冰之核心", "core_of_ice", ChatFormatting.AQUA),
        DEATH(DEATH_STAGE, "死之核心", "core_of_death", ChatFormatting.DARK_GRAY),
        FIRE(FIRE_STAGE, "炎之核心", "core_of_fire", ChatFormatting.RED),
        SENTIENCE(SENTIENCE_STAGE, "识之核心", "core_of_sentience", ChatFormatting.DARK_RED),
        EARTH(EARTH_STAGE, "岩之核心", "core_of_earth", ChatFormatting.GOLD),
        DOMINANCE(DOMINANCE_STAGE, "支配之核心", "core_of_dominance", ChatFormatting.DARK_PURPLE),
        BINDING(BINDING_STAGE, "约束之核心", "core_of_binding", ChatFormatting.YELLOW),
        CORROSION(CORROSION_STAGE, "侵蚀之核心", "core_of_corrosion", ChatFormatting.LIGHT_PURPLE);

        private final int stage;
        private final String displayName;
        private final ResourceLocation itemId;
        private final ChatFormatting color;

        CoreEntry(
                int stage,
                String displayName,
                String registryName,
                ChatFormatting color
        ) {
            this.stage = stage;
            this.displayName = displayName;
            this.itemId = new ResourceLocation(lastsongofelysian.MODID, registryName);
            this.color = color;
        }

        private static CoreEntry byStage(int stage) {
            for (CoreEntry entry : values()) {
                if (entry.stage == stage) {
                    return entry;
                }
            }
            return null;
        }
    }

    private static CompoundTag getData(Player player) {
        CompoundTag root = player.getPersistentData();

        if (!root.contains(NBT_PLAYER_PERSISTED, Tag.TAG_COMPOUND)) {
            root.put(NBT_PLAYER_PERSISTED, new CompoundTag());
        }

        return root.getCompound(NBT_PLAYER_PERSISTED);
    }

    public static int getStage(Player player) {
        return Mth.clamp(
                getData(player).getInt(NBT_CORE_STAGE),
                0,
                MAX_STAGE
        );
    }

    public static void setStage(Player player, int stage) {
        getData(player).putInt(
                NBT_CORE_STAGE,
                Mth.clamp(stage, 0, MAX_STAGE)
        );
    }

    public static boolean unlocked(Player player, int requiredStage) {
        return getStage(player) >= requiredStage;
    }

    public static boolean isReversed(Player player) {
        return getData(player).getBoolean(NBT_CURSES_REVERSED);
    }

    public static float getMultiplier(Player player) {
        return 1.0F + getStage(player) * 0.10F;
    }

    public static float scaleValue(Player player, float value) {
        return value * getMultiplier(player);
    }

    public static double scaleValue(Player player, double value) {
        return value * getMultiplier(player);
    }

    public static float scaleChance(Player player, float chance) {
        return Mth.clamp(
                chance * getMultiplier(player),
                0.0F,
                0.95F
        );
    }

    public static float scalePercent(Player player, float percent) {
        return Mth.clamp(
                percent * getMultiplier(player),
                0.0F,
                0.90F
        );
    }

    public static int scaleDuration(Player player, int ticks) {
        return Math.max(
                1,
                Math.round(ticks * Math.min(3.0F, getMultiplier(player)))
        );
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Player player = event.player;

        if (player.level().isClientSide() || player.tickCount % 20 != 0) {
            return;
        }

        try {
            updateAcquiredCores(player);
            advanceInOrder(player);
        } catch (Throwable throwable) {
            System.err.println(
                    "[LastSongOfElysian] CocoonCoreProgress player tick failed:"
            );
            throwable.printStackTrace(System.err);
        }
    }

    private static void updateAcquiredCores(Player player) {
        CompoundTag data = getData(player);
        int mask = data.getInt(NBT_ACQUIRED_CORE_MASK);
        int completedStages = Math.min(getStage(player), 12);
        if (completedStages > 0) mask |= (1 << completedStages) - 1;
        for (int index = 0; index < ALL_CORE_IDS.length; index++) {
            if (containsItemId(player, ALL_CORE_IDS[index])) mask |= 1 << index;
        }
        data.putInt(NBT_ACQUIRED_CORE_MASK, mask);

        if (mask == ALL_CORE_MASK && !data.getBoolean(NBT_CURSES_REVERSED)) {
            data.putBoolean(NBT_CURSES_REVERSED, true);
            SignetEffects.clearCorruption(player);
            syncToClient(player);
            player.displayClientMessage(
                    Component.literal("以人类的意志，拥抱明天。")
                            .withStyle(ChatFormatting.LIGHT_PURPLE),
                    false
            );
            player.displayClientMessage(
                    Component.literal("终焉之茧诅咒均已反转")
                            .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD),
                    false
            );
        }
    }

    private static ResourceLocation coreId(String path) {
        return new ResourceLocation(lastsongofelysian.MODID, path);
    }

    private static void advanceInOrder(Player player) {
        int stage = getStage(player);

        while (stage < MAX_STAGE) {
            CoreEntry next = CoreEntry.byStage(stage + 1);

            if (next == null || !containsItemId(player, next.itemId)) {
                return;
            }

            stage++;
            setStage(player, stage);
            syncToClient(player);

            player.displayClientMessage(
                    Component.literal("已获得 ")
                            .append(
                                    Component.literal(next.displayName)
                                            .withStyle(next.color, ChatFormatting.BOLD)
                            )
                            .append(
                                    Component.literal(
                                            "：终焉强度提高至 ×"
                                                    + String.format(
                                                            Locale.ROOT,
                                                            "%.1f",
                                                            getMultiplier(player)
                                                    )
                                    ).withStyle(ChatFormatting.RED)
                            ),
                    false
            );

            if (next == CoreEntry.CORROSION) {
                CorrosionCurseEvents.syncToClient(player);
            } else {
                player.displayClientMessage(
                        Component.literal(next.displayName + "对应的诅咒已开启。")
                                .withStyle(ChatFormatting.DARK_RED),
                        false
                );
            }
        }
    }

    private static boolean containsItemId(
            Player player,
            ResourceLocation requiredId
    ) {
        for (
                int slot = 0;
                slot < player.getInventory().getContainerSize();
                slot++
        ) {
            ItemStack stack = player.getInventory().getItem(slot);

            if (stack.isEmpty()) {
                continue;
            }

            ResourceLocation actualId =
                    ForgeRegistries.ITEMS.getKey(stack.getItem());

            if (requiredId.equals(actualId)) {
                return true;
            }
        }

        return false;
    }

    private static void syncToClient(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        ModNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> serverPlayer),
                new CocoonStageSyncPacket(getStage(player), isReversed(player))
        );
    }

    @SubscribeEvent
    public static void onPlayerLogin(
            PlayerEvent.PlayerLoggedInEvent event
    ) {
        syncToClient(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(
            PlayerEvent.PlayerRespawnEvent event
    ) {
        syncToClient(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        try {
            int oldStage = getStage(event.getOriginal());
            setStage(event.getEntity(), oldStage);
            CompoundTag oldData = getData(event.getOriginal());
            CompoundTag newData = getData(event.getEntity());
            newData.putInt(NBT_ACQUIRED_CORE_MASK, oldData.getInt(NBT_ACQUIRED_CORE_MASK));
            newData.putBoolean(NBT_CURSES_REVERSED, oldData.getBoolean(NBT_CURSES_REVERSED));
        } catch (Throwable throwable) {
            System.err.println(
                    "[LastSongOfElysian] Failed to copy cocoon core progress:"
            );
            throwable.printStackTrace(System.err);
        }
    }
}
