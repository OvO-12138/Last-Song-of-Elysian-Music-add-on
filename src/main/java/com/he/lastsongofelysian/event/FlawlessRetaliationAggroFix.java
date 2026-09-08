package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.item.FlawlessBenedictionLegacyItem;
import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class FlawlessRetaliationAggroFix {

    private static final String NBT_RETALIATION_PLAYER =
            "FlawlessRetaliationPlayer";

    private static final String NBT_RETALIATION_UNTIL =
            "FlawlessRetaliationUntil";

    private static final String NBT_DOMAIN_ARROW =
            "FlawlessDomainArrow";

    private static final String NBT_DOMAIN_PETAL_DAMAGE =
            "FlawlessDomainPetalDamage";

    private static final int RETALIATION_DURATION =
            20 * 10;

    private FlawlessRetaliationAggroFix() {
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onMobHurt(
            LivingHurtEvent event
    ) {
        if (
                event.getEntity()
                        .level()
                        .isClientSide() ||
                        !(event.getEntity()
                                instanceof Mob mob) ||
                        event.getAmount() <= 0.0F
        ) {
            return;
        }

        if (
                mob.getPersistentData()
                        .getBoolean(
                                NBT_DOMAIN_PETAL_DAMAGE
                        )
        ) {
            return;
        }

        Player player =
                getAttackingPlayer(
                        event.getSource()
                );

        if (
                player == null ||
                        !isManualFlawlessAttack(
                                event.getSource(),
                                player
                        )
        ) {
            return;
        }

        markRetaliation(
                mob,
                player
        );
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onTargetChange(
            LivingChangeTargetEvent event
    ) {
        if (
                event.getEntity()
                        .level()
                        .isClientSide() ||
                        !(event.getEntity()
                                instanceof Mob mob) ||
                        !(event.getNewTarget()
                                instanceof Player player) ||
                        !hasRetaliation(
                                mob,
                                player
                        )
        ) {
            return;
        }

        event.setCanceled(false);
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onRetaliationAttack(
            LivingAttackEvent event
    ) {
        if (
                !(event.getEntity()
                        instanceof Player player)
        ) {
            return;
        }

        Mob attacker =
                getSourceMob(
                        event.getSource()
                );

        if (
                attacker == null ||
                        !hasRetaliation(
                                attacker,
                                player
                        )
        ) {
            return;
        }

        event.setCanceled(false);
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onRetaliationHurt(
            LivingHurtEvent event
    ) {
        if (
                !(event.getEntity()
                        instanceof Player player)
        ) {
            return;
        }

        Mob attacker =
                getSourceMob(
                        event.getSource()
                );

        if (
                attacker == null ||
                        !hasRetaliation(
                                attacker,
                                player
                        )
        ) {
            return;
        }

        event.setCanceled(false);
    }

    @SubscribeEvent
    public static void onServerTick(
            TickEvent.ServerTickEvent event
    ) {
        if (
                event.phase !=
                        TickEvent.Phase.END
        ) {
            return;
        }

        MinecraftServer server =
                ServerLifecycleHooks
                        .getCurrentServer();

        if (server == null) {
            return;
        }

        for (
                ServerPlayer player :
                server.getPlayerList()
                        .getPlayers()
        ) {
            if (
                    !player.isAlive() ||
                            player.isSpectator()
            ) {
                continue;
            }

            ServerLevel level =
                    player.serverLevel();

            List<Mob> mobs =
                    level.getEntitiesOfClass(
                            Mob.class,
                            player.getBoundingBox()
                                    .inflate(64.0D),
                            mob ->
                                    mob.isAlive() &&
                                            hasRetaliation(
                                                    mob,
                                                    player
                                            )
                    );

            for (Mob mob : mobs) {
                mob.setTarget(player);
                mob.setAggressive(true);

                if (
                        mob instanceof
                                NeutralMob neutralMob
                ) {
                    neutralMob
                            .setPersistentAngerTarget(
                                    player.getUUID()
                            );

                    neutralMob
                            .startPersistentAngerTimer();
                }
            }
        }
    }

    private static void markRetaliation(
            Mob mob,
            Player player
    ) {
        CompoundTag data =
                mob.getPersistentData();

        data.putUUID(
                NBT_RETALIATION_PLAYER,
                player.getUUID()
        );

        data.putLong(
                NBT_RETALIATION_UNTIL,
                mob.level()
                        .getGameTime()
                        + RETALIATION_DURATION
        );

        mob.setTarget(player);
        mob.setAggressive(true);

        if (
                mob instanceof
                        NeutralMob neutralMob
        ) {
            neutralMob
                    .setPersistentAngerTarget(
                            player.getUUID()
                    );

            neutralMob
                    .startPersistentAngerTimer();
        }
    }

    private static boolean hasRetaliation(
            Mob mob,
            Player player
    ) {
        CompoundTag data =
                mob.getPersistentData();

        if (
                !data.hasUUID(
                        NBT_RETALIATION_PLAYER
                )
        ) {
            return false;
        }

        long until =
                data.getLong(
                        NBT_RETALIATION_UNTIL
                );

        if (
                until <=
                        mob.level()
                                .getGameTime()
        ) {
            data.remove(
                    NBT_RETALIATION_PLAYER
            );

            data.remove(
                    NBT_RETALIATION_UNTIL
            );

            return false;
        }

        return data.getUUID(
                NBT_RETALIATION_PLAYER
        ).equals(
                player.getUUID()
        );
    }

    private static boolean isManualFlawlessAttack(
            DamageSource source,
            Player player
    ) {
        Entity directEntity =
                source.getDirectEntity();

        if (
                directEntity instanceof
                        AbstractArrow arrow
        ) {
            if (
                    arrow.getPersistentData()
                            .getBoolean(
                                    NBT_DOMAIN_ARROW
                            )
            ) {
                return false;
            }

            return arrow.getPersistentData()
                    .getBoolean(
                            FlawlessBenedictionLegacyItem
                                    .NBT_ARROW_MARKER
                    );
        }

        return directEntity == player &&
                player.getMainHandItem().is(
                        ModItems
                                .FLAWLESS_BENEDICTION_LEGACY
                                .get()
                );
    }

    private static Player getAttackingPlayer(
            DamageSource source
    ) {
        Entity sourceEntity =
                source.getEntity();

        if (
                sourceEntity instanceof
                        Player player
        ) {
            return player;
        }

        Entity directEntity =
                source.getDirectEntity();

        if (
                directEntity instanceof
                        Projectile projectile &&
                        projectile.getOwner()
                                instanceof Player player
        ) {
            return player;
        }

        return null;
    }

    private static Mob getSourceMob(
            DamageSource source
    ) {
        Entity sourceEntity =
                source.getEntity();

        if (
                sourceEntity instanceof
                        Mob mob
        ) {
            return mob;
        }

        Entity directEntity =
                source.getDirectEntity();

        if (
                directEntity instanceof
                        Mob mob
        ) {
            return mob;
        }

        if (
                directEntity instanceof
                        Projectile projectile &&
                        projectile.getOwner()
                                instanceof Mob mob
        ) {
            return mob;
        }

        return null;
    }
}
