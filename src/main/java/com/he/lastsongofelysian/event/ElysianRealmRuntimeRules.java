package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.dimension.ModDimensions;
import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.registry.ModEntities;
import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class ElysianRealmRuntimeRules {

    private static final double ENTRY_X =
            18.75D;

    private static final double ENTRY_Y =
            -49.0D;

    private static final double ENTRY_Z =
            2.63D;

    private static final double
            MIN_ALLOWED_MOVEMENT_PER_TICK =
            0.75D;

    private static final int
            TELEPORT_GUARD_GRACE_TICKS =
            5;

    private static final Set<UUID>
            PENDING_ENTRY_RETURNS =
            new HashSet<>();

    private static final Map<
            UUID,
            PendingCommandTeleport
            > PENDING_COMMAND_TELEPORTS =
            new HashMap<>();

    private static final Map<UUID, SafePosition>
            LAST_SAFE_POSITIONS =
            new HashMap<>();

    private static final Map<UUID, Long>
            TELEPORT_GUARD_GRACE_UNTIL =
            new HashMap<>();

    private static final Set<String>
            TELEPORT_ROOT_COMMANDS =
            Set.of(
                    "tp",
                    "teleport",
                    "spreadplayers",
                    "warp",
                    "warps",
                    "home",
                    "back",
                    "rtp",
                    "tpa",
                    "tpaccept",
                    "tphere",
                    "waypoint",
                    "waypoints",
                    "spawn",
                    "dimension",
                    "portal"
            );

    private ElysianRealmRuntimeRules() {
    }

    private static boolean isElysianRealm(
            Level level
    ) {
        return level.dimension().equals(
                ModDimensions.ELYSIAN_REALM
        );
    }

    private static boolean isElysianRealmAccessor(
            ServerLevelAccessor level
    ) {
        return level.getLevel()
                .dimension()
                .equals(
                        ModDimensions
                                .ELYSIAN_REALM
                );
    }

    private static boolean isAllowedNaturalMob(
            EntityType<?> entityType
    ) {
        return entityType ==
                ModEntities
                        .PARDOFELIS_MERCHANT
                        .get();
    }

    private static boolean isNaturalWorldSpawn(
            MobSpawnType spawnType
    ) {
        return spawnType
                == MobSpawnType.NATURAL
                || spawnType
                == MobSpawnType.CHUNK_GENERATION;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onChangedDimension(
            PlayerEvent.PlayerChangedDimensionEvent event
    ) {
        if (
                !(event.getEntity()
                        instanceof ServerPlayer player)
        ) {
            return;
        }

        UUID uuid = player.getUUID();

        if (
                event.getFrom().equals(
                        ModDimensions.ELYSIAN_REALM
                ) &&
                !event.getTo().equals(
                        ModDimensions.ELYSIAN_REALM
                )
        ) {
            PENDING_COMMAND_TELEPORTS.remove(uuid);
            LAST_SAFE_POSITIONS.remove(uuid);
            TELEPORT_GUARD_GRACE_UNTIL.remove(uuid);
            return;
        }

        if (
                !event.getTo().equals(
                        ModDimensions.ELYSIAN_REALM
                )
        ) {
            return;
        }

        if (
                player.gameMode
                        .getGameModeForPlayer()
                        == GameType.SPECTATOR
        ) {
            player.setGameMode(
                    GameType.ADVENTURE
            );
        }

        returnToEntry(player);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onChangeGameMode(
            PlayerEvent.PlayerChangeGameModeEvent event
    ) {
        if (
                isElysianRealm(
                        event.getEntity().level()
                ) &&
                event.getNewGameMode()
                        == GameType.SPECTATOR
        ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityTeleport(
            EntityTeleportEvent event
    ) {
        if (
                !(event.getEntity()
                        instanceof ServerPlayer player) ||
                !isElysianRealm(
                        player.level()
                )
        ) {
            return;
        }

        if (
                event instanceof
                        EntityTeleportEvent.TeleportCommand ||
                event instanceof
                        EntityTeleportEvent.SpreadPlayersCommand
        ) {
            return;
        }

        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCommand(
            CommandEvent event
    ) {
        ParseResults parseResults =
                event.getParseResults();

        Object rawSource =
                parseResults.getContext()
                        .getSource();

        if (
                !(rawSource
                        instanceof CommandSourceStack
                        source) ||
                !(source.getEntity()
                        instanceof ServerPlayer player) ||
                !isElysianRealm(
                        player.level()
                )
        ) {
            return;
        }

        String command =
                parseResults.getReader()
                        .getString()
                        .trim()
                        .toLowerCase(
                                Locale.ROOT
                        );

        if (!isPotentialTeleportCommand(command)) {
            return;
        }

        PENDING_COMMAND_TELEPORTS.put(
                player.getUUID(),
                new PendingCommandTeleport(
                        player.position(),
                        player.level()
                                .getGameTime()
                                + 10L
                )
        );

    }

    private static boolean isPotentialTeleportCommand(
            String command
    ) {
        String normalized =
                command.startsWith("/")
                        ? command.substring(1)
                        : command;

        if (normalized.isBlank()) {
            return false;
        }

        String[] tokens =
                normalized.split("\s+");

        String root =
                stripNamespace(
                        tokens[0]
                );

        if (
                TELEPORT_ROOT_COMMANDS
                        .contains(root) ||
                root.contains("teleport") ||
                root.contains("waypoint") ||
                root.contains("warp")
        ) {
            return true;
        }

        if (!root.equals("execute")) {
            return false;
        }

        String padded =
                " " + normalized + " ";

        return padded.contains(" run tp ")
                || padded.contains(
                        " run minecraft:tp "
                )
                || padded.contains(
                        " run teleport "
                )
                || padded.contains(
                        " run minecraft:teleport "
                )
                || padded.contains(
                        " run spreadplayers "
                );
    }

    private static String stripNamespace(
            String token
    ) {
        int colon =
                token.lastIndexOf(':');

        return colon >= 0
                ? token.substring(colon + 1)
                : token;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerTick(
            TickEvent.PlayerTickEvent event
    ) {
        if (
                event.phase != TickEvent.Phase.END ||
                !(event.player
                        instanceof ServerPlayer player)
        ) {
            return;
        }

        UUID uuid = player.getUUID();

        if (!isElysianRealm(player.level())) {
            PENDING_COMMAND_TELEPORTS.remove(uuid);
            LAST_SAFE_POSITIONS.remove(uuid);
            TELEPORT_GUARD_GRACE_UNTIL.remove(uuid);
            return;
        }

        if (
                player.gameMode
                        .getGameModeForPlayer()
                        == GameType.SPECTATOR
        ) {
            player.setGameMode(
                    GameType.ADVENTURE
            );
        }

        long now =
                player.level().getGameTime();

        PendingCommandTeleport pending =
                PENDING_COMMAND_TELEPORTS.get(uuid);

        if (pending != null) {
            if (now > pending.expiresAt) {
                PENDING_COMMAND_TELEPORTS.remove(uuid);
            } else if (
                    player.position()
                            .distanceToSqr(
                                    pending.origin
                            ) >
                            0.01D
            ) {
                PENDING_COMMAND_TELEPORTS.remove(uuid);

                restorePosition(
                        player,
                        pending.origin,
                        player.getYRot(),
                        player.getXRot()
                );

                return;
            }
        }

        LAST_SAFE_POSITIONS.put(
                uuid,
                SafePosition.capture(player)
        );
    }

    private static void restorePosition(
            ServerPlayer player,
            Vec3 position,
            float yaw,
            float pitch
    ) {
        player.teleportTo(
                position.x,
                position.y,
                position.z
        );

        player.setYRot(yaw);
        player.setXRot(pitch);
        player.fallDistance = 0.0F;

        UUID uuid = player.getUUID();

        LAST_SAFE_POSITIONS.put(
                uuid,
                new SafePosition(
                        position,
                        yaw,
                        pitch
                )
        );

        TELEPORT_GUARD_GRACE_UNTIL.put(
                uuid,
                player.level()
                        .getGameTime()
                        + TELEPORT_GUARD_GRACE_TICKS
        );
    }

    private static void returnToEntry(
            ServerPlayer player
    ) {
        MinecraftServer server =
                player.getServer();

        if (server == null) {
            return;
        }

        UUID uuid = player.getUUID();

        if (!PENDING_ENTRY_RETURNS.add(uuid)) {
            return;
        }

        server.execute(
                () -> {
                    try {
                        if (
                                player.isRemoved() ||
                                !isElysianRealm(
                                        player.level()
                                )
                        ) {
                            return;
                        }

                        player.teleportTo(
                                ENTRY_X,
                                ENTRY_Y,
                                ENTRY_Z
                        );

                        player.fallDistance = 0.0F;

                        Vec3 entryPosition =
                                new Vec3(
                                        ENTRY_X,
                                        ENTRY_Y,
                                        ENTRY_Z
                                );

                        LAST_SAFE_POSITIONS.put(
                                uuid,
                                new SafePosition(
                                        entryPosition,
                                        player.getYRot(),
                                        player.getXRot()
                                )
                        );

                        TELEPORT_GUARD_GRACE_UNTIL.put(
                                uuid,
                                player.level()
                                        .getGameTime()
                                        + TELEPORT_GUARD_GRACE_TICKS
                        );
                    } finally {
                        PENDING_ENTRY_RETURNS.remove(
                                uuid
                        );
                    }
                }
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLevelTick(
            TickEvent.LevelTickEvent event
    ) {
        if (
                event.phase != TickEvent.Phase.END ||
                !(event.level
                        instanceof ServerLevel level) ||
                !isElysianRealm(level) ||
                level.getGameTime() % 20L != 0L
        ) {
            return;
        }

        level.setWeatherParameters(
                6000,
                0,
                false,
                false
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onSpawnPlacementCheck(
            MobSpawnEvent.SpawnPlacementCheck event
    ) {
        if (
                !isElysianRealmAccessor(
                        event.getLevel()
                ) ||
                !isNaturalWorldSpawn(
                        event.getSpawnType()
                ) ||
                isAllowedNaturalMob(
                        event.getEntityType()
                )
        ) {
            return;
        }

        event.setResult(
                Event.Result.DENY
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onFinalizeSpawn(
            MobSpawnEvent.FinalizeSpawn event
    ) {
        if (
                !isElysianRealmAccessor(
                        event.getLevel()
                ) ||
                !isNaturalWorldSpawn(
                        event.getSpawnType()
                ) ||
                isAllowedNaturalMob(
                        event.getEntity()
                                .getType()
                )
        ) {
            return;
        }

        event.setSpawnCancelled(true);
    }

    @SubscribeEvent
    public static void onLogout(
            PlayerEvent.PlayerLoggedOutEvent event
    ) {
        UUID uuid =
                event.getEntity().getUUID();

        PENDING_ENTRY_RETURNS.remove(uuid);
        PENDING_COMMAND_TELEPORTS.remove(uuid);
        LAST_SAFE_POSITIONS.remove(uuid);
        TELEPORT_GUARD_GRACE_UNTIL.remove(uuid);
    }

    private static final class PendingCommandTeleport {

        private final Vec3 origin;
        private final long expiresAt;

        private PendingCommandTeleport(
                Vec3 origin,
                long expiresAt
        ) {
            this.origin = origin;
            this.expiresAt = expiresAt;
        }
    }

    private static final class SafePosition {

        private final Vec3 position;
        private final float yaw;
        private final float pitch;

        private SafePosition(
                Vec3 position,
                float yaw,
                float pitch
        ) {
            this.position = position;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        private static SafePosition capture(
                ServerPlayer player
        ) {
            return new SafePosition(
                    player.position(),
                    player.getYRot(),
                    player.getXRot()
            );
        }
    }

}
