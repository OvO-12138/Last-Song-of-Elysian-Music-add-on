package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.network.FlawlessVisualPacket;
import com.he.lastsongofelysian.network.ModNetwork;
import com.he.lastsongofelysian.util.FlawlessWeaponSkill;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public final class FlawlessVisualEffects {

    private FlawlessVisualEffects() {
    }

    public static void spawnModeShift(
            Player player,
            FlawlessWeaponSkill.Mode mode
    ) {
        FlawlessVisualPacket.Type type = switch (mode) {
            case WEATHER_CHILD ->
                    FlawlessVisualPacket.Type.MODE_WEATHER;
            case SUN_MOON_DAUGHTER ->
                    FlawlessVisualPacket.Type.MODE_SUN_MOON;
            case FLAWLESS ->
                    FlawlessVisualPacket.Type.MODE_FLAWLESS;
        };

        send(player, type, 30, mode.ordinal());
        playSound(
                player,
                SoundEvents.AMETHYST_BLOCK_CHIME,
                1.0F,
                1.45F
        );
    }

    public static void spawnWeatherSkill(Player player) {
        send(
                player,
                FlawlessVisualPacket.Type.WEATHER_SKILL,
                80,
                0
        );
        playSound(
                player,
                SoundEvents.BEACON_ACTIVATE,
                1.1F,
                1.55F
        );
    }

    public static void spawnSunMoonSkill(Player player) {
        send(
                player,
                FlawlessVisualPacket.Type.SUN_MOON_SKILL,
                80,
                0
        );
        playSound(
                player,
                SoundEvents.RESPAWN_ANCHOR_CHARGE,
                1.0F,
                1.65F
        );
        playSound(
                player,
                SoundEvents.AMETHYST_BLOCK_CHIME,
                0.9F,
                0.80F
        );
    }

    public static void spawnFlawlessBloom(
            LivingEntity target,
            int stacks
    ) {
        send(
                target,
                FlawlessVisualPacket.Type.FLAWLESS_BLOOM,
                60,
                Math.max(1, Math.min(3, stacks))
        );
    }

    public static void spawnBloomStackSigil(
            LivingEntity target,
            int stacks
    ) {
        send(
                target,
                FlawlessVisualPacket.Type.BLOOM_STACK,
                18,
                Math.max(1, Math.min(3, stacks))
        );
    }

    public static void spawnUltimateOpening(Player player) {
        send(
                player,
                FlawlessVisualPacket.Type.ULTIMATE,
                400,
                0
        );
        playSound(
                player,
                SoundEvents.BEACON_ACTIVATE,
                1.5F,
                1.35F
        );
        playSound(
                player,
                SoundEvents.FIREWORK_ROCKET_BLAST,
                1.0F,
                1.45F
        );
    }

    public static void spawnUltimateClosing(
            Player player,
            Vec3 position,
            int remainingTicks
    ) {
        sendAt(
                player,
                position,
                FlawlessVisualPacket.Type.ULTIMATE_CLOSE,
                80,
                Math.max(1, remainingTicks)
        );
        playSound(
                player,
                SoundEvents.BEACON_DEACTIVATE,
                1.8F,
                0.72F
        );
        playSound(
                player,
                SoundEvents.WITHER_SPAWN,
                0.85F,
                1.45F
        );
    }

    public static void spawnArrowTrail(
            AbstractArrow arrow,
            boolean domainArrow
    ) {
        send(
                arrow,
                FlawlessVisualPacket.Type.ARROW_TRAIL,
                domainArrow ? 60 : 100,
                domainArrow ? 1 : 0
        );
    }

    public static void spawnArrowImpact(
            AbstractArrow arrow,
            boolean domainArrow
    ) {
        send(
                arrow,
                FlawlessVisualPacket.Type.ARROW_IMPACT,
                domainArrow ? 32 : 26,
                domainArrow ? 1 : 0
        );
        playSound(
                arrow,
                SoundEvents.AMETHYST_BLOCK_CHIME,
                domainArrow ? 1.1F : 0.8F,
                domainArrow ? 1.75F : 1.95F
        );
    }

    private static void send(
            Entity anchor,
            FlawlessVisualPacket.Type type,
            int duration,
            int variant
    ) {
        if (!(anchor.level() instanceof ServerLevel)) {
            return;
        }

        ModNetwork.CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(
                        () -> anchor
                ),
                new FlawlessVisualPacket(
                        type,
                        anchor.getId(),
                        anchor.getX(),
                        anchor.getY(),
                        anchor.getZ(),
                        duration,
                        variant
                )
        );
    }

    private static void sendAt(
            Player anchor,
            Vec3 position,
            FlawlessVisualPacket.Type type,
            int duration,
            int variant
    ) {
        if (!(anchor.level() instanceof ServerLevel)) {
            return;
        }

        ModNetwork.CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(
                        () -> anchor
                ),
                new FlawlessVisualPacket(
                        type,
                        anchor.getId(),
                        position.x,
                        position.y,
                        position.z,
                        duration,
                        variant
                )
        );
    }

    private static void playSound(
            Entity source,
            net.minecraft.sounds.SoundEvent sound,
            float volume,
            float pitch
    ) {
        if (!(source.level() instanceof ServerLevel level)) {
            return;
        }

        level.playSound(
                null,
                source.blockPosition(),
                sound,
                SoundSource.PLAYERS,
                volume,
                pitch
        );
    }
}
