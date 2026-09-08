package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.event.FlawlessBenedictionLegacyEvents;
import com.he.lastsongofelysian.dimension.ModDimensions;
import com.he.lastsongofelysian.event.SignetEffects;
import com.he.lastsongofelysian.registry.ModItems;
import com.he.lastsongofelysian.util.FlawlessWeaponEnergy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public final class FlawlessWeaponSkill {

    private static final String NBT_MODE = "FlawlessMode";

    private static final String
            NBT_ULTIMATE_COOLDOWN_UNTIL =
            "FlawlessUltimateCooldownUntil";

    private static final int
            ULTIMATE_COOLDOWN_TICKS =
            20 * 20;

    public static final UUID DELIVERANCE_ULTIMATE_BUFF_UUID =
            UUID.fromString(
                    "f9a1b2c3-d4e5-6f7a-8b9c-0d1e2f3a4b5c"
            );

    private FlawlessWeaponSkill() {
    }

    public enum Mode {
        WEATHER_CHILD("天气之子"),
        SUN_MOON_DAUGHTER("日月的女儿"),
        FLAWLESS("无瑕");

        private final String displayName;

        Mode(String displayName) {
            this.displayName = displayName;
        }

        public String displayName() {
            return displayName;
        }
    }

    public static Mode getMode(ItemStack stack) {
        int index = stack.getOrCreateTag()
                .getInt(NBT_MODE);

        Mode[] values = Mode.values();

        if (index < 0 || index >= values.length) {
            index = 0;
        }

        return values[index];
    }

    public static String getModeDisplayName(
            ItemStack stack
    ) {
        return getMode(stack).displayName();
    }

    public static void switchMode(
            Player player,
            ItemStack stack
    ) {
        Mode current = getMode(stack);
        Mode[] values = Mode.values();

        Mode next = values[
                (current.ordinal() + 1)
                        % values.length
                ];

        stack.getOrCreateTag().putInt(
                NBT_MODE,
                next.ordinal()
        );

        player.displayClientMessage(
                Component.literal(
                        "真我之境形态："
                                + next.displayName()
                ).withStyle(
                        ChatFormatting.LIGHT_PURPLE,
                        ChatFormatting.BOLD
                ),
                true
        );
    }

    public static void useWeaponSkill(
            Player player,
            ItemStack weapon
    ) {
        if (
                player.level().isClientSide() ||
                !weapon.is(
                        ModItems.FLAWLESS_BENEDICTION_LEGACY.get()
                )
        ) {
            return;
        }

        if (hasHelix(player)) {
            SignetEffects.onWeaponSkillActivated(player);
        }

        Mode mode = getMode(weapon);

        switch (mode) {
            case WEATHER_CHILD ->
                    toggleWeather(player);
            case SUN_MOON_DAUGHTER ->
                    toggleTime(player);
            case FLAWLESS ->
                    useFlawlessBloom(player, weapon);
        }
    }

    public static void useUltimate(
            Player player,
            ItemStack weapon
    ) {
        if (
                player.level().isClientSide() ||
                !weapon.is(
                        ModItems.FLAWLESS_BENEDICTION_LEGACY.get()
                )
        ) {
            return;
        }

        long gameTime =
                player.level().getGameTime();

        long cooldownUntil =
                player.getPersistentData()
                        .getLong(
                                NBT_ULTIMATE_COOLDOWN_UNTIL
                        );

        if (cooldownUntil > gameTime) {
            long remainingTicks =
                    cooldownUntil - gameTime;

            long remainingSeconds =
                    Math.max(
                            1L,
                            (remainingTicks + 19L) / 20L
                    );

            player.displayClientMessage(
                    Component.literal(
                            "无瑕乐土尚在冷却："
                                    + remainingSeconds
                                    + "秒"
                    ).withStyle(ChatFormatting.GRAY),
                    true
            );
            return;
        }

        player.getPersistentData().remove(
                NBT_ULTIMATE_COOLDOWN_UNTIL
        );

        if (
                !FlawlessWeaponEnergy.consumeEnergy(
                        player,
                        weapon,
                        80
                )
        ) {
            return;
        }

        if (hasDeliverance(player)) {
            AttributeInstance attackDamage =
                    player.getAttribute(
                            Attributes.ATTACK_DAMAGE
                    );

            if (attackDamage != null) {
                attackDamage.removeModifier(
                        DELIVERANCE_ULTIMATE_BUFF_UUID
                );
            }

            SignetEffects.activateDeliveranceUltimateBuff(player);
        }

        FlawlessBenedictionLegacyEvents.activateDomain(
                player,
                weapon
        );

        player.getPersistentData().putLong(
                NBT_ULTIMATE_COOLDOWN_UNTIL,
                player.level().getGameTime()
                        + ULTIMATE_COOLDOWN_TICKS
        );
    }

    private static void toggleWeather(Player player) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        if (
                level.dimension().equals(
                        ModDimensions.ELYSIAN_REALM
                )
        ) {
            level.setWeatherParameters(
                    6000,
                    0,
                    false,
                    false
            );
        }

        if (level.isThundering()) {
            level.setWeatherParameters(
                    12000,
                    0,
                    false,
                    false
            );

            message(
                    player,
                    "天气之子：雷暴散去，天空放晴"
            );
            return;
        }

        if (level.isRaining()) {
            level.setWeatherParameters(
                    0,
                    12000,
                    true,
                    true
            );

            message(
                    player,
                    "风雨汇聚为雷暴"
            );
            return;
        }

        level.setWeatherParameters(
                0,
                12000,
                true,
                false
        );

        message(
                player,
                "晴空化作细雨"
        );
    }

    private static void toggleTime(Player player) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        long dayTime = level.getDayTime() % 24000L;

        if (dayTime < 13000L) {
            level.setDayTime(
                    level.getDayTime()
                            - dayTime
                            + 18000L
            );

            message(
                    player,
                    "白昼沉入星夜"
            );
        } else {
            level.setDayTime(
                    level.getDayTime()
                            - dayTime
                            + 1000L
            );

            message(
                    player,
                    "长夜迎来朝阳"
            );
        }
    }

    private static void useFlawlessBloom(
            Player player,
            ItemStack weapon
    ) {
        LivingEntity target =
                findLookTarget(player, 32.0D);

        if (target == null) {
            player.displayClientMessage(
                    Component.literal(
                            "无暇绽放：未锁定目标"
                    ).withStyle(ChatFormatting.RED),
                    true
            );
            return;
        }

        int stacks =
                FlawlessBenedictionLegacyEvents
                        .getBloomStacks(target);

        if (
                stacks <
                        FlawlessBenedictionLegacyEvents
                                .MAX_BLOOM_STACKS
        ) {
            player.displayClientMessage(
                    Component.literal(
                            "真我之绽不足："
                                    + stacks
                                    + "/3"
                    ).withStyle(ChatFormatting.RED),
                    true
            );
            return;
        }

        FlawlessBenedictionLegacyEvents
                .clearBloomStacks(target);

        float baseAttack = (float) Math.max(
                1.0D,
                player.getAttributeValue(
                        Attributes.ATTACK_DAMAGE
                )
        );

        float damage = baseAttack * 13.14F;

        List<LivingEntity> victims =
                player.level().getEntitiesOfClass(
                        LivingEntity.class,
                        target.getBoundingBox()
                                .inflate(4.0D),
                        entity ->
                                entity.isAlive()
                                        && entity != player
                                        && !entity.isAlliedTo(player)
                );

        int hitCount = 0;

        for (LivingEntity victim : victims) {
            if (victim.hurt(
                    player.damageSources()
                            .playerAttack(player),
                    damage
            )) {
                hitCount++;
            }
        }

        target.setTicksFrozen(
                Math.max(
                        target.getTicksFrozen(),
                        300
                )
        );

        target.addEffect(
                new MobEffectInstance(
                        MobEffects.GLOWING,
                        40,
                        0,
                        false,
                        false,
                        true
                )
        );

        playFlawlessBloomFeedback(
                player,
                target
        );

        FlawlessWeaponEnergy.addEnergy(
                player,
                weapon,
                25
        );

    }

    private static void playFlawlessBloomFeedback(
            Player player,
            LivingEntity target
    ) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        double centerY =
                target.getY()
                        + target.getBbHeight() * 0.55D;

        level.sendParticles(
                ParticleTypes.END_ROD,
                target.getX(),
                centerY,
                target.getZ(),
                34,
                1.25D,
                Math.max(0.45D, target.getBbHeight() * 0.45D),
                1.25D,
                0.08D
        );

        level.sendParticles(
                ParticleTypes.ENCHANTED_HIT,
                target.getX(),
                centerY,
                target.getZ(),
                42,
                1.55D,
                Math.max(0.55D, target.getBbHeight() * 0.55D),
                1.55D,
                0.12D
        );

        level.playSound(
                null,
                target.blockPosition(),
                SoundEvents.AMETHYST_BLOCK_CHIME,
                SoundSource.PLAYERS,
                1.6F,
                1.25F
        );

        level.playSound(
                null,
                target.blockPosition(),
                SoundEvents.FIREWORK_ROCKET_BLAST,
                SoundSource.PLAYERS,
                0.85F,
                1.55F
        );
    }

    @Nullable
    private static LivingEntity findLookTarget(
            Player player,
            double range
    ) {
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle()
                .normalize();
        Vec3 end = eye.add(look.scale(range));

        AABB searchBox =
                player.getBoundingBox()
                        .expandTowards(
                                look.scale(range)
                        )
                        .inflate(2.5D);

        return player.level()
                .getEntitiesOfClass(
                        LivingEntity.class,
                        searchBox,
                        entity ->
                                entity.isAlive()
                                        && entity != player
                                        && !entity.isAlliedTo(player)
                                        && player.hasLineOfSight(entity)
                )
                .stream()
                .filter(entity ->
                        entity.getBoundingBox()
                                .inflate(0.35D)
                                .clip(eye, end)
                                .isPresent()
                )
                .min(
                        Comparator.comparingDouble(
                                entity -> eye.distanceToSqr(
                                        entity.getBoundingBox()
                                                .getCenter()
                                )
                        )
                )
                .orElse(null);
    }

    private static boolean hasDeliverance(
            Player player
    ) {
        return CuriosApi.getCuriosHelper()
                .findFirstCurio(
                        player,
                        stack -> stack.is(
                                ModItems.SIGNET_OF_DELIVERANCE.get()
                        )
                )
                .isPresent();
    }

    private static boolean hasHelix(Player player) {
        return CuriosApi.getCuriosHelper()
                .findFirstCurio(
                        player,
                        stack -> stack.is(
                                ModItems.SIGNET_OF_HELIX.get()
                        )
                )
                .isPresent();
    }

    private static void message(
            Player player,
            String text
    ) {
        player.displayClientMessage(
                Component.literal(text)
                        .withStyle(
                                ChatFormatting.LIGHT_PURPLE,
                                ChatFormatting.BOLD
                        ),
                true
        );
    }
}
