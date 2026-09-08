package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.dimension.ElysianRealmBlockMutationGuard;
import com.he.lastsongofelysian.dimension.ModDimensions;
import com.mojang.brigadier.ParseResults;
import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.PistonEvent;
import net.minecraftforge.event.level.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class ElysianRealmProtectionEvents {

    private static final Map<
            ResourceKey<Level>,
            Map<BlockPos, BlockSnapshot>
            > PENDING_RESTORES =
            new LinkedHashMap<>();

    private static final Set<String>
            DESTRUCTIVE_BLOCK_COMMANDS =
            Set.of(
                    "setblock",
                    "fill",
                    "clone",
                    "place",
                    "brush",
                    "replacenear",
                    "removenear",
                    "drain",
                    "fixwater",
                    "forest",
                    "flora",
                    "generate",
                    "green",
                    "hollow",
                    "hsphere",
                    "line",
                    "move",
                    "overlay",
                    "regen",
                    "replace",
                    "set",
                    "smooth",
                    "sphere",
                    "stack",
                    "walls"
            );

    private ElysianRealmProtectionEvents() {
    }

    private static boolean isElysianRealm(
            Level level
    ) {
        return level.dimension().equals(
                ModDimensions.ELYSIAN_REALM
        );
    }

    private static boolean isElysianRealm(
            LevelAccessor level
    ) {
        return level instanceof Level actualLevel
                && isElysianRealm(actualLevel);
    }

    private static boolean isMapLike(
            ItemStack stack
    ) {
        if (stack.isEmpty()) {
            return false;
        }

        if (
                stack.is(Items.MAP) ||
                stack.is(Items.FILLED_MAP)
        ) {
            return true;
        }

        ResourceLocation id =
                ForgeRegistries.ITEMS.getKey(
                        stack.getItem()
                );

        String registryName =
                id == null
                        ? ""
                        : id.toString();

        String className =
                stack.getItem()
                        .getClass()
                        .getName();

        return containsMapKeyword(
                registryName
        ) || containsMapKeyword(
                className
        );
    }

    private static boolean containsMapKeyword(
            String value
    ) {
        String lower =
                value.toLowerCase(
                        Locale.ROOT
                );

        return lower.contains("map")
                || lower.contains("worldmap")
                || lower.contains("minimap")
                || lower.contains("waypoint")
                || lower.contains("atlas")
                || lower.contains("cartography")
                || lower.contains("xaero")
                || lower.contains("journey")
                || lower.contains("voxel")
                || lower.contains("ftbchunks")
                || lower.contains("mapfrontiers")
                || lower.contains("地图")
                || lower.contains("地圖")
                || lower.contains("路径点");
    }

    private static boolean isDestructiveBlockCommand(
            String command
    ) {
        String normalized =
                command.trim()
                        .toLowerCase(
                                Locale.ROOT
                        );

        if (normalized.startsWith("//")) {
            normalized =
                    normalized.substring(2);
        } else if (normalized.startsWith("/")) {
            normalized =
                    normalized.substring(1);
        }

        if (normalized.isBlank()) {
            return false;
        }

        String[] tokens =
                normalized.split("\\s+");

        String root =
                stripCommandNamespace(
                        tokens[0]
                );

        if (
                DESTRUCTIVE_BLOCK_COMMANDS
                        .contains(root)
        ) {
            return true;
        }

        if (!root.equals("execute")) {
            return false;
        }

        String padded =
                " " + normalized + " ";

        for (
                String destructive :
                DESTRUCTIVE_BLOCK_COMMANDS
        ) {
            if (
                    padded.contains(
                            " run "
                                    + destructive
                                    + " "
                    ) ||
                    padded.contains(
                            " run minecraft:"
                                    + destructive
                                    + " "
                    )
            ) {
                return true;
            }
        }

        return false;
    }

    private static String stripCommandNamespace(
            String token
    ) {
        int colon =
                token.lastIndexOf(':');

        return colon >= 0
                ? token.substring(colon + 1)
                : token;
    }

    private static void queueSnapshot(
            Level level,
            BlockPos pos
    ) {
        queueSnapshot(
                BlockSnapshot.create(
                        level.dimension(),
                        level,
                        pos
                )
        );
    }

    private static void queueSnapshot(
            BlockSnapshot snapshot
    ) {
        LevelAccessor level =
                snapshot.getLevel();

        if (
                !(level instanceof Level actualLevel) ||
                !isElysianRealm(actualLevel)
        ) {
            return;
        }

        PENDING_RESTORES
                .computeIfAbsent(
                        actualLevel.dimension(),
                        ignored ->
                                new LinkedHashMap<>()
                )
                .putIfAbsent(
                        snapshot.getPos().immutable(),
                        snapshot
                );
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBreakSpeed(
            PlayerEvent.BreakSpeed event
    ) {
        if (
                isElysianRealm(
                        event.getEntity().level()
                )
        ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLeftClickBlock(
            PlayerInteractEvent.LeftClickBlock event
    ) {
        if (isElysianRealm(event.getLevel())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBreakBlock(
            BlockEvent.BreakEvent event
    ) {
        if (
                !(event.getLevel()
                        instanceof Level level) ||
                !isElysianRealm(level)
        ) {
            return;
        }

        queueSnapshot(
                level,
                event.getPos()
        );

        event.setCanceled(true);
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onBreakBlockFinal(
            BlockEvent.BreakEvent event
    ) {
        if (
                !(event.getLevel()
                        instanceof Level level) ||
                !isElysianRealm(level)
        ) {
            return;
        }

        queueSnapshot(
                level,
                event.getPos()
        );

        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlaceBlock(
            BlockEvent.EntityPlaceEvent event
    ) {
        if (!isElysianRealm(event.getLevel())) {
            return;
        }

        if (
                event instanceof
                        BlockEvent.EntityMultiPlaceEvent
                        multiPlaceEvent
        ) {
            for (
                    BlockSnapshot snapshot :
                    multiPlaceEvent
                            .getReplacedBlockSnapshots()
            ) {
                queueSnapshot(snapshot);
            }
        } else {
            queueSnapshot(
                    event.getBlockSnapshot()
            );
        }

        event.setCanceled(true);
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onPlaceBlockFinal(
            BlockEvent.EntityPlaceEvent event
    ) {
        if (!isElysianRealm(event.getLevel())) {
            return;
        }

        if (
                event instanceof
                        BlockEvent.EntityMultiPlaceEvent
                        multiPlaceEvent
        ) {
            for (
                    BlockSnapshot snapshot :
                    multiPlaceEvent
                            .getReplacedBlockSnapshots()
            ) {
                queueSnapshot(snapshot);
            }
        } else {
            queueSnapshot(event.getBlockSnapshot());
        }

        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBlock(
            PlayerInteractEvent.RightClickBlock event
    ) {
        if (!isElysianRealm(event.getLevel())) {
            return;
        }

        BlockState state =
                event.getLevel().getBlockState(
                        event.getPos()
                );

        ItemStack held =
                event.getItemStack();

        boolean blockedInteraction =
                state.is(BlockTags.BEDS)
                        || state.is(
                                Blocks.RESPAWN_ANCHOR
                        )
                        || held.getItem() instanceof BlockItem
                        || held.getItem() instanceof BucketItem
                        || held.getItem() instanceof BoatItem
                        || held.getItem() instanceof MinecartItem
                        || held.getItem() instanceof SpawnEggItem
                        || held.is(Items.PAINTING)
                        || held.is(Items.ITEM_FRAME)
                        || held.is(Items.GLOW_ITEM_FRAME)
                        || held.is(Items.ARMOR_STAND)
                        || held.is(Items.FLINT_AND_STEEL)
                        || held.is(Items.FIRE_CHARGE)
                        || held.is(Items.BONE_MEAL)
                        || isMapLike(held);

        if (!blockedInteraction) {
            return;
        }

        event.setCanceled(true);
        event.setCancellationResult(
                InteractionResult.FAIL
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickItem(
            PlayerInteractEvent.RightClickItem event
    ) {
        if (!isElysianRealm(event.getLevel())) {
            return;
        }

        ItemStack stack =
                event.getItemStack();

        if (
                !(stack.getItem() instanceof BlockItem)
                        && !(stack.getItem() instanceof BucketItem)
                        && !(stack.getItem() instanceof BoatItem)
                        && !(stack.getItem() instanceof MinecartItem)
                        && !(stack.getItem() instanceof SpawnEggItem)
                        && !stack.is(Items.PAINTING)
                        && !stack.is(Items.ITEM_FRAME)
                        && !stack.is(Items.GLOW_ITEM_FRAME)
                        && !stack.is(Items.ARMOR_STAND)
                        && !isMapLike(stack)
        ) {
            return;
        }

        event.setCanceled(true);
        event.setCancellationResult(
                InteractionResult.FAIL
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onToolModify(
            BlockEvent.BlockToolModificationEvent event
    ) {
        if (
                !(event.getLevel()
                        instanceof Level level) ||
                !isElysianRealm(level)
        ) {
            return;
        }

        queueSnapshot(
                level,
                event.getPos()
        );

        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onFarmlandTrample(
            BlockEvent.FarmlandTrampleEvent event
    ) {
        if (
                !(event.getLevel()
                        instanceof Level level) ||
                !isElysianRealm(level)
        ) {
            return;
        }

        queueSnapshot(
                level,
                event.getPos()
        );

        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onFluidPlace(
            BlockEvent.FluidPlaceBlockEvent event
    ) {
        if (
                !(event.getLevel()
                        instanceof Level level) ||
                !isElysianRealm(level)
        ) {
            return;
        }

        queueSnapshot(
                level,
                event.getPos()
        );

        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCreateFluidSource(
            BlockEvent.CreateFluidSourceEvent event
    ) {
        if (isElysianRealm(event.getLevel())) {
            event.setResult(
                    Event.Result.DENY
            );
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDestructiveCommand(
            CommandEvent event
    ) {
        ParseResults<?> parseResults =
                event.getParseResults();

        Object rawSource =
                parseResults.getContext()
                        .getSource();

        if (
                !(rawSource
                        instanceof CommandSourceStack
                        source) ||
                !(source.getEntity()
                        instanceof Player player) ||
                !isElysianRealm(
                        player.level()
                )
        ) {
            return;
        }

        String command =
                parseResults.getReader()
                        .getString();

        if (isDestructiveBlockCommand(command)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPistonMove(
            PistonEvent.Pre event
    ) {
        if (isElysianRealm(event.getLevel())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onSaplingGrow(
            SaplingGrowTreeEvent event
    ) {
        if (isElysianRealm(event.getLevel())) {
            event.setResult(
                    Event.Result.DENY
            );
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCropGrow(
            BlockEvent.CropGrowEvent.Pre event
    ) {
        if (isElysianRealm(event.getLevel())) {
            event.setResult(
                    Event.Result.DENY
            );
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPortalSpawn(
            BlockEvent.PortalSpawnEvent event
    ) {
        if (isElysianRealm(event.getLevel())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDestroyBlock(
            LivingDestroyBlockEvent event
    ) {
        if (!isElysianRealm(
                event.getEntity().level()
        )) {
            return;
        }

        queueSnapshot(
                event.getEntity().level(),
                event.getPos()
        );

        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onExplosion(
            ExplosionEvent.Detonate event
    ) {
        if (!isElysianRealm(event.getLevel())) {
            return;
        }

        for (
                BlockPos pos :
                event.getAffectedBlocks()
        ) {
            queueSnapshot(
                    event.getLevel(),
                    pos
            );
        }

        event.getAffectedBlocks().clear();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onSleep(
            PlayerSleepInBedEvent event
    ) {
        Player player = event.getEntity();

        if (
                isElysianRealm(
                        player.level()
                )
        ) {
            event.setResult(
                    Player.BedSleepingProblem
                            .NOT_POSSIBLE_HERE
            );
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onSetSpawn(
            PlayerSetSpawnEvent event
    ) {
        if (
                event.getNewSpawn() != null &&
                event.getSpawnLevel()
                        .equals(
                                ModDimensions
                                        .ELYSIAN_REALM
                        )
        ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLevelTick(
            TickEvent.LevelTickEvent event
    ) {

        Level level = event.level;

        if (
                event.phase != TickEvent.Phase.END ||
                !isElysianRealm(level)
        ) {
            return;
        }

        Map<BlockPos, BlockSnapshot> snapshots =
                PENDING_RESTORES.remove(
                        level.dimension()
                );

        if (snapshots == null) {
            return;
        }

        for (
                BlockSnapshot snapshot :
                snapshots.values()
        ) {
            ElysianRealmBlockMutationGuard
                    .runWithBypass(
                            () -> snapshot.restore(
                                    true,
                                    false
                            )
                    );
        }
    }
}
