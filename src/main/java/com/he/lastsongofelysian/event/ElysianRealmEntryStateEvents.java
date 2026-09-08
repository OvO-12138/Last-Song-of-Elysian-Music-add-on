package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.dimension.ModDimensions;
import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class ElysianRealmEntryStateEvents {

    private static final int RESET_TICKS = 20;

    private static final Map<UUID, PendingState>
            PENDING_STATES = new HashMap<>();

    private ElysianRealmEntryStateEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onChangedDimension(
            PlayerEvent.PlayerChangedDimensionEvent event
    ) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        UUID uuid = player.getUUID();

        if (
                event.getTo().equals(
                        ModDimensions.ELYSIAN_REALM
                )
        ) {

            if (
                    player.isSpectator()
            ) {
                return;
            }

            PendingState old =
                    PENDING_STATES.remove(uuid);

            if (old != null) {
                old.restore(player);
            }

            PendingState pending =
                    PendingState.capture(player);

            PENDING_STATES.put(uuid, pending);
            resetTemporaryState(player);
            return;
        }

        if (
                event.getFrom().equals(
                        ModDimensions.ELYSIAN_REALM
                )
        ) {
            restoreNow(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTick(
            TickEvent.PlayerTickEvent event
    ) {
        if (
                event.phase != TickEvent.Phase.END
                        || event.side != LogicalSide.SERVER
        ) {
            return;
        }

        Player player = event.player;
        PendingState pending =
                PENDING_STATES.get(player.getUUID());

        if (pending == null) {
            return;
        }

        if (
                !player.level().dimension().equals(
                        ModDimensions.ELYSIAN_REALM
                )
        ) {
            restoreNow(player);
            return;
        }

        pending.ticksRemaining--;

        if (pending.ticksRemaining <= 0) {
            restoreNow(player);
            return;
        }

        keepTemporaryStateReset(player);
    }

    @SubscribeEvent
    public static void onLogout(
            PlayerEvent.PlayerLoggedOutEvent event
    ) {
        if (!event.getEntity().level().isClientSide()) {
            restoreNow(event.getEntity());
        }
    }

    private static void resetTemporaryState(Player player) {
        player.removeAllEffects();
        player.setHealth(player.getMaxHealth());
        player.setAbsorptionAmount(0.0F);
        player.getFoodData().setFoodLevel(20);
        player.getFoodData().setSaturation(5.0F);
        player.setAirSupply(player.getMaxAirSupply());
        player.clearFire();
        player.setTicksFrozen(0);
        player.setDeltaMovement(0.0D, 0.0D, 0.0D);
        player.fallDistance = 0.0F;
        player.invulnerableTime = RESET_TICKS;
        player.stopUsingItem();
    }

    private static void keepTemporaryStateReset(
            Player player
    ) {
        player.removeAllEffects();
        player.setAbsorptionAmount(0.0F);
        player.setAirSupply(player.getMaxAirSupply());
        player.clearFire();
        player.setTicksFrozen(0);
    }

    private static void restoreNow(Player player) {
        PendingState pending =
                PENDING_STATES.remove(
                        player.getUUID()
                );

        if (pending != null) {
            pending.restore(player);
        }
    }

    private static final class PendingState {

        private int ticksRemaining = RESET_TICKS;
        private final float health;
        private final float absorption;
        private final int food;
        private final float saturation;
        private final int air;
        private final int fireTicks;
        private final int frozenTicks;
        private final int invulnerableTicks;
        private final List<MobEffectInstance> effects;

        private PendingState(
                float health,
                float absorption,
                int food,
                float saturation,
                int air,
                int fireTicks,
                int frozenTicks,
                int invulnerableTicks,
                List<MobEffectInstance> effects
        ) {
            this.health = health;
            this.absorption = absorption;
            this.food = food;
            this.saturation = saturation;
            this.air = air;
            this.fireTicks = fireTicks;
            this.frozenTicks = frozenTicks;
            this.invulnerableTicks = invulnerableTicks;
            this.effects = effects;
        }

        private static PendingState capture(Player player) {
            List<MobEffectInstance> effects =
                    new ArrayList<>();

            for (
                    MobEffectInstance effect :
                    player.getActiveEffects()
            ) {
                effects.add(
                        new MobEffectInstance(effect)
                );
            }

            return new PendingState(
                    player.getHealth(),
                    player.getAbsorptionAmount(),
                    player.getFoodData().getFoodLevel(),
                    player.getFoodData()
                            .getSaturationLevel(),
                    player.getAirSupply(),
                    player.getRemainingFireTicks(),
                    player.getTicksFrozen(),
                    player.invulnerableTime,
                    effects
            );
        }

        private void restore(Player player) {
            player.removeAllEffects();

            for (MobEffectInstance effect : effects) {
                player.addEffect(
                        new MobEffectInstance(effect)
                );
            }

            player.setHealth(
                    Math.max(
                            1.0F,
                            Math.min(
                                    health,
                                    player.getMaxHealth()
                            )
                    )
            );
            player.setAbsorptionAmount(absorption);
            player.getFoodData().setFoodLevel(food);
            player.getFoodData().setSaturation(saturation);
            player.setAirSupply(
                    Math.min(
                            air,
                            player.getMaxAirSupply()
                    )
            );
            player.setRemainingFireTicks(
                    Math.max(0, fireTicks)
            );
            player.setTicksFrozen(
                    Math.max(0, frozenTicks)
            );
            player.invulnerableTime =
                    Math.max(0, invulnerableTicks);
        }
    }
}
