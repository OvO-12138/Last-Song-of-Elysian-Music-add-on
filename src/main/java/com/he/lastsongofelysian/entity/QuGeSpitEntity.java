package com.he.lastsongofelysian.entity;

import com.he.lastsongofelysian.registry.ModEntities;
import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class QuGeSpitEntity extends ThrowableItemProjectile {

    private static final float HEAL_AMOUNT = 9.1F;
    private static final int NAUSEA_DURATION_TICKS = 8 * 20;

    public QuGeSpitEntity(
            EntityType<? extends QuGeSpitEntity> type,
            Level level
    ) {
        super(type, level);
    }

    public QuGeSpitEntity(
            LivingEntity owner,
            Level level
    ) {
        super(ModEntities.QU_GE_SPIT.get(), owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.QU_GE_SPIT.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        if (
                this.level().isClientSide() ||
                !(result.getEntity() instanceof LivingEntity target) ||
                target == this.getOwner()
        ) {
            return;
        }

        target.addEffect(
                new MobEffectInstance(
                        MobEffects.CONFUSION,
                        NAUSEA_DURATION_TICKS,
                        0,
                        false,
                        true,
                        true
                ),
                this.getOwner()
        );

        target.heal(HEAL_AMOUNT);

        if (target instanceof ServerPlayer player) {
            player.displayClientMessage(
                    Component.literal(
                            "区哥的吐沫：反胃，并恢复 9.1 点生命值"
                    ).withStyle(ChatFormatting.LIGHT_PURPLE),
                    true
            );
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!(this.level() instanceof ServerLevel level)) {
            return;
        }

        level.sendParticles(
                ParticleTypes.WITCH,
                this.getX(),
                this.getY(),
                this.getZ(),
                18,
                0.30D,
                0.30D,
                0.30D,
                0.05D
        );

        level.playSound(
                null,
                this.blockPosition(),
                SoundEvents.SLIME_SQUISH_SMALL,
                SoundSource.NEUTRAL,
                0.8F,
                0.85F
        );

        this.discard();
    }
}
