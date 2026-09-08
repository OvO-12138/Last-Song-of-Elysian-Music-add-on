package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class ConcealmentBraceletCompatibilityEvents {

    private static final ResourceLocation HIDDEN_BRACELET_ID =
            new ResourceLocation(
                    "celestial_artifacts",
                    "hidden_bracelet"
            );

    private static final int PROVOKED_TICKS = 200;
    private static final double CONTROL_RANGE = 64.0D;

    private static final Map<UUID, ProvokedTarget> PROVOKED_MOBS =
            new HashMap<>();

    private ConcealmentBraceletCompatibilityEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerAttack(
            AttackEntityEvent event
    ) {
        Player player = event.getEntity();
        Entity target = event.getTarget();

        if (
                player.level().isClientSide() ||
                !(target instanceof Mob mob) ||
                !hasHiddenBracelet(player) ||
                !canRetaliate(mob)
        ) {
            return;
        }

        provoke(mob, player);
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST,
            receiveCanceled = true
    )
    public static void onLivingAttack(
            LivingAttackEvent event
    ) {
        if (
                event.getEntity().level().isClientSide() ||
                !(event.getEntity() instanceof Mob mob) ||
                !(event.getSource().getEntity() instanceof Player player) ||
                !hasHiddenBracelet(player) ||
                !canRetaliate(mob)
        ) {
            return;
        }

        provoke(mob, player);
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onTargetChange(
            LivingChangeTargetEvent event
    ) {
        if (
                event.getEntity().level().isClientSide() ||
                !(event.getEntity() instanceof Mob mob) ||
                !(event.getNewTarget() instanceof Player player) ||
                !hasHiddenBracelet(player)
        ) {
            return;
        }

        if (isProvoked(mob, player)) {
            event.setCanceled(false);
            return;
        }

        event.setCanceled(true);
        clearHostileIntent(mob, player);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPiglinInteract(
            PlayerInteractEvent.EntityInteract event
    ) {
        Player player = event.getEntity();

        if (
                player.level().isClientSide() ||
                !(event.getTarget() instanceof Piglin piglin) ||
                !event.getItemStack().is(Items.GOLD_INGOT) ||
                !hasHiddenBracelet(player) ||
                isProvoked(piglin, player)
        ) {
            return;
        }

        clearHostileIntent(piglin, player);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onGoldToss(
            ItemTossEvent event
    ) {
        Player player = event.getPlayer();
        ItemEntity itemEntity = event.getEntity();

        if (
                player.level().isClientSide() ||
                !itemEntity.getItem().is(Items.GOLD_INGOT) ||
                !hasHiddenBracelet(player)
        ) {
            return;
        }

        for (Piglin piglin : player.level().getEntitiesOfClass(
                Piglin.class,
                player.getBoundingBox().inflate(16.0D),
                candidate -> candidate.isAlive()
                        && !isProvoked(candidate, player)
        )) {
            clearHostileIntent(piglin, player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerTick(
            TickEvent.PlayerTickEvent event
    ) {
        if (
                event.phase != TickEvent.Phase.END ||
                event.side != LogicalSide.SERVER
        ) {
            return;
        }

        Player player = event.player;
        long now = player.level().getGameTime();
        cleanupExpired(now);

        if (!hasHiddenBracelet(player)) {
            removePlayerProvocations(player.getUUID());
            return;
        }

        for (Mob mob : player.level().getEntitiesOfClass(
                Mob.class,
                player.getBoundingBox().inflate(CONTROL_RANGE),
                Mob::isAlive
        )) {
            if (isProvoked(mob, player)) {
                applyRetaliationTarget(mob, player);
            } else if (hasHostileIntentTowards(mob, player)) {
                clearHostileIntent(mob, player);
            }
        }
    }

    @SubscribeEvent
    public static void onLogout(
            PlayerEvent.PlayerLoggedOutEvent event
    ) {
        removePlayerProvocations(event.getEntity().getUUID());
    }

    private static boolean hasHiddenBracelet(
            Player player
    ) {
        return CuriosApi.getCuriosHelper()
                .findFirstCurio(
                        player,
                        stack -> HIDDEN_BRACELET_ID.equals(
                                ForgeRegistries.ITEMS.getKey(
                                        stack.getItem()
                                )
                        )
                )
                .isPresent();
    }

    private static boolean canRetaliate(
            Mob mob
    ) {
        return mob instanceof Enemy ||
                mob instanceof NeutralMob ||
                mob instanceof AbstractPiglin;
    }

    private static void provoke(
            Mob mob,
            Player player
    ) {
        PROVOKED_MOBS.put(
                mob.getUUID(),
                new ProvokedTarget(
                        player.getUUID(),
                        player.level().getGameTime()
                                + PROVOKED_TICKS
                )
        );

        applyRetaliationTarget(mob, player);
    }

    private static boolean isProvoked(
            Mob mob,
            Player player
    ) {
        ProvokedTarget provoked =
                PROVOKED_MOBS.get(mob.getUUID());

        if (provoked == null) {
            return false;
        }

        long now = player.level().getGameTime();

        if (provoked.expiresAt <= now) {
            PROVOKED_MOBS.remove(mob.getUUID());
            return false;
        }

        return provoked.playerId.equals(player.getUUID());
    }

    private static void applyRetaliationTarget(
            Mob mob,
            Player player
    ) {
        mob.setTarget(player);
        mob.setAggressive(true);

        ProvokedTarget provoked =
                PROVOKED_MOBS.get(mob.getUUID());

        long remaining = provoked == null
                ? PROVOKED_TICKS
                : Math.max(
                        1L,
                        provoked.expiresAt
                                - player.level().getGameTime()
                );

        if (mob instanceof NeutralMob neutralMob) {
            neutralMob.setPersistentAngerTarget(
                    player.getUUID()
            );
            neutralMob.startPersistentAngerTimer();
        }

        if (mob instanceof AbstractPiglin piglin) {
            piglin.getBrain().setMemoryWithExpiry(
                    MemoryModuleType.ANGRY_AT,
                    player.getUUID(),
                    remaining
            );
            piglin.getBrain().setMemory(
                    MemoryModuleType.ATTACK_TARGET,
                    player
            );
        }
    }

    private static boolean hasHostileIntentTowards(
            Mob mob,
            Player player
    ) {
        if (mob.getTarget() == player) {
            return true;
        }

        if (
                mob instanceof NeutralMob neutralMob &&
                player.getUUID().equals(
                        neutralMob.getPersistentAngerTarget()
                )
        ) {
            return true;
        }

        if (mob instanceof AbstractPiglin piglin) {
            if (piglin.getBrain()
                    .getMemory(MemoryModuleType.ANGRY_AT)
                    .filter(player.getUUID()::equals)
                    .isPresent()) {
                return true;
            }

            return piglin.getBrain()
                    .getMemory(MemoryModuleType.ATTACK_TARGET)
                    .filter(player::equals)
                    .isPresent();
        }

        return false;
    }

    private static void clearHostileIntent(
            Mob mob,
            Player player
    ) {
        if (mob.getTarget() == player) {
            mob.setTarget(null);
        }

        if (mob instanceof NeutralMob neutralMob) {
            neutralMob.stopBeingAngry();
        }

        if (mob instanceof AbstractPiglin piglin) {
            piglin.getBrain().eraseMemory(
                    MemoryModuleType.ANGRY_AT
            );
            piglin.getBrain().eraseMemory(
                    MemoryModuleType.ATTACK_TARGET
            );
        }

        mob.setAggressive(false);
        mob.getNavigation().stop();
    }

    private static void cleanupExpired(
            long now
    ) {
        Iterator<Map.Entry<UUID, ProvokedTarget>> iterator =
                PROVOKED_MOBS.entrySet().iterator();

        while (iterator.hasNext()) {
            if (iterator.next().getValue().expiresAt <= now) {
                iterator.remove();
            }
        }
    }

    private static void removePlayerProvocations(
            UUID playerId
    ) {
        PROVOKED_MOBS.entrySet().removeIf(
                entry -> entry.getValue().playerId.equals(playerId)
        );
    }

    private record ProvokedTarget(
            UUID playerId,
            long expiresAt
    ) {
    }
}
