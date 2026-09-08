package com.he.lastsongofelysian.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class VoidRiftSovereignEntity extends PathfinderMob {

    private static final String NBT_HOVER_Y =
            "VoidRiftSovereignHoverY";

    private double hoverY = Double.NaN;

    public VoidRiftSovereignEntity(
            EntityType<? extends PathfinderMob> type,
            Level level
    ) {
        super(type, level);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ARMOR, 12.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.65D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(
                1,
                new LookAtPlayerGoal(
                        this,
                        Player.class,
                        18.0F,
                        1.0F
                )
        );
        this.goalSelector.addGoal(
                2,
                new RandomLookAroundGoal(this)
        );
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.setNoGravity(true);

        if (this.level().isClientSide()) {
            return;
        }

        if (
                !Double.isFinite(this.hoverY) ||
                Math.abs(this.getY() - this.hoverY) > 4.0D
        ) {
            this.hoverY = this.getY() + 0.48D;
        }

        double targetY = this.hoverY
                + Math.sin(this.tickCount * 0.105D) * 0.075D;
        Vec3 movement = this.getDeltaMovement();
        double vertical = Mth.clamp(
                (targetY - this.getY()) * 0.18D,
                -0.055D,
                0.055D
        );

        this.setDeltaMovement(
                movement.x * 0.72D,
                vertical,
                movement.z * 0.72D
        );
        this.fallDistance = 0.0F;
    }

    @Override
    public boolean causeFallDamage(
            float distance,
            float multiplier,
            DamageSource source
    ) {
        return false;
    }

    @Override
    public Component getName() {
        return Component.literal("虚空裂相");
    }

    @Override
    public boolean removeWhenFarAway(
            double distanceToClosestPlayer
    ) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        if (Double.isFinite(this.hoverY)) {
            tag.putDouble(NBT_HOVER_Y, this.hoverY);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.hoverY = tag.contains(NBT_HOVER_Y)
                ? tag.getDouble(NBT_HOVER_Y)
                : Double.NaN;
        this.setNoGravity(true);
    }
}
