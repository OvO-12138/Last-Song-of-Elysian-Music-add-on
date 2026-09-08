package com.he.lastsongofelysian.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class CorrosionMirrorEntity extends PathfinderMob {

    public static final String NBT_MIRROR_MARKER = "LSECorrosionMirror";
    public static final String NBT_MIRROR_OWNER = "LSECorrosionMirrorOwner";
    public static final String NBT_MIRROR_FRIENDLY = "LSECorrosionMirrorFriendly";
    public static final String NBT_MIRROR_EXPIRE = "LSECorrosionMirrorExpire";

    private static final String SAVE_OWNER = "MirrorOwner";
    private static final String SAVE_FRIENDLY = "MirrorFriendly";
    private static final String SAVE_EXPIRE = "MirrorExpire";

    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID =
            SynchedEntityData.defineId(
                    CorrosionMirrorEntity.class,
                    EntityDataSerializers.OPTIONAL_UUID
            );

    private static final EntityDataAccessor<Boolean> DATA_FRIENDLY =
            SynchedEntityData.defineId(
                    CorrosionMirrorEntity.class,
                    EntityDataSerializers.BOOLEAN
            );

    private long expireGameTime;
    private int manualAttackCooldown;
    private int rangedAttackCooldown;
    private int rangedUseTicks;
    private int ownerMissingTicks;

    public CorrosionMirrorEntity(
            EntityType<? extends CorrosionMirrorEntity> type,
            Level level
    ) {
        super(type, level);
        this.setPersistenceRequired();
        this.setCanPickUpLoot(false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.31D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.15D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
        this.entityData.define(DATA_FRIENDLY, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.85D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public void configureFromPlayer(
            Player owner,
            boolean friendly,
            long expireGameTime
    ) {
        this.setOwnerUUID(owner.getUUID());
        this.setFriendly(friendly);
        this.expireGameTime = expireGameTime;

        this.setCustomName(
                Component.literal(friendly ? "协同镜像" : "侵蚀镜像")
                        .withStyle(friendly
                                ? ChatFormatting.AQUA
                                : ChatFormatting.DARK_PURPLE)
        );
        this.setCustomNameVisible(true);
        this.setPersistenceRequired();
        this.setCanPickUpLoot(false);

        copyEquipment(owner);

        setBaseAttribute(
                Attributes.MAX_HEALTH,
                Math.max(
                        friendly ? 20.0D : 10.0D,
                        owner.getMaxHealth() * (friendly ? 0.50D : 0.30D)
                )
        );
        setBaseAttribute(
                Attributes.ATTACK_DAMAGE,
                Math.max(
                        friendly ? 4.0D : 2.0D,
                        owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.50D
                )
        );
        setBaseAttribute(
                Attributes.ARMOR,
                Math.max(0.0D, owner.getAttributeValue(Attributes.ARMOR) * 0.50D)
        );
        setBaseAttribute(
                Attributes.ARMOR_TOUGHNESS,
                Math.max(
                        0.0D,
                        owner.getAttributeValue(Attributes.ARMOR_TOUGHNESS) * 0.50D
                )
        );
        this.setHealth(this.getMaxHealth());

        CompoundTag data = this.getPersistentData();
        data.putBoolean(NBT_MIRROR_MARKER, true);
        data.putUUID(NBT_MIRROR_OWNER, owner.getUUID());
        data.putBoolean(NBT_MIRROR_FRIENDLY, friendly);
        data.putLong(NBT_MIRROR_EXPIRE, expireGameTime);
    }

    private void copyEquipment(Player owner) {
        this.setItemSlot(
                EquipmentSlot.MAINHAND,
                safeCopy(owner.getMainHandItem())
        );
        this.setItemSlot(
                EquipmentSlot.OFFHAND,
                safeCopy(owner.getOffhandItem())
        );
        this.setItemSlot(
                EquipmentSlot.HEAD,
                safeCopy(owner.getItemBySlot(EquipmentSlot.HEAD))
        );
        this.setItemSlot(
                EquipmentSlot.CHEST,
                safeCopy(owner.getItemBySlot(EquipmentSlot.CHEST))
        );
        this.setItemSlot(
                EquipmentSlot.LEGS,
                safeCopy(owner.getItemBySlot(EquipmentSlot.LEGS))
        );
        this.setItemSlot(
                EquipmentSlot.FEET,
                safeCopy(owner.getItemBySlot(EquipmentSlot.FEET))
        );

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            this.setDropChance(slot, 0.0F);
        }
    }

    private static ItemStack safeCopy(ItemStack stack) {
        return stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
    }

    private void setBaseAttribute(
            net.minecraft.world.entity.ai.attributes.Attribute attribute,
            double value
    ) {
        AttributeInstance instance = this.getAttribute(attribute);
        if (instance != null) {
            instance.setBaseValue(value);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if (this.manualAttackCooldown > 0) {
            this.manualAttackCooldown--;
        }
        if (this.rangedAttackCooldown > 0) {
            this.rangedAttackCooldown--;
        }

        Player owner = this.getOwnerPlayer();
        if (owner == null) {
            this.setTarget(null);
            this.getNavigation().stop();
            cancelRangedUse();

            ownerMissingTicks++;
            if (ownerMissingTicks >= 100) {
                this.discard();
            }
            return;
        }

        ownerMissingTicks = 0;
        if (!owner.isAlive()) {
            this.setTarget(null);
            this.getNavigation().stop();
            cancelRangedUse();
            return;
        }

        if (this.isFriendly()) {
            handleFriendlyAi(owner);
        } else if (this.getTarget() != owner) {

            this.setTarget(owner);
        }

        runManualCombatStep();
    }

    private void runManualCombatStep() {
        LivingEntity target = this.getTarget();

        if (target == null || !this.canAttack(target)) {
            this.setTarget(null);
            cancelRangedUse();
            return;
        }

        this.getLookControl().setLookAt(target, 35.0F, 35.0F);

        ItemStack weapon = this.getMainHandItem();
        if (weapon.getItem() instanceof BowItem) {
            runBowCombat(target);
            return;
        }
        if (weapon.getItem() instanceof CrossbowItem) {
            runCrossbowCombat(target);
            return;
        }

        cancelRangedUse();
        runMeleeCombat(target);
    }

    private void runMeleeCombat(LivingEntity target) {
        double distanceSqr = this.distanceToSqr(target);
        double attackReachSqr = getManualAttackReachSqr(target);
        double movementSpeed = this.isFriendly() ? 1.18D : 1.25D;

        if (distanceSqr > attackReachSqr * 0.85D) {
            this.getNavigation().moveTo(target, movementSpeed);
        } else {
            this.getNavigation().stop();
        }

        if (distanceSqr <= attackReachSqr
                && this.manualAttackCooldown <= 0
                && this.getSensing().hasLineOfSight(target)) {
            this.manualAttackCooldown = 20;
            this.swing(InteractionHand.MAIN_HAND);
            super.doHurtTarget(target);
        }
    }

    private void runBowCombat(LivingEntity target) {
        runRangedCombat(
                target,
                20,
                30,
                1.75F,
                3.0F,
                false
        );
    }

    private void runCrossbowCombat(LivingEntity target) {
        runRangedCombat(
                target,
                25,
                40,
                3.15F,
                1.0F,
                true
        );
    }

    private void runRangedCombat(
            LivingEntity target,
            int requiredUseTicks,
            int cooldownAfterShot,
            float arrowVelocity,
            float arrowInaccuracy,
            boolean crossbow
    ) {
        double distanceSqr = this.distanceToSqr(target);
        boolean hasLineOfSight = this.getSensing().hasLineOfSight(target);
        double preferredRangeSqr = crossbow ? 400.0D : 324.0D;
        double maximumRangeSqr = 900.0D;
        double movementSpeed = this.isFriendly() ? 1.10D : 1.16D;

        if (distanceSqr > preferredRangeSqr || !hasLineOfSight) {
            cancelRangedUse();
            this.getNavigation().moveTo(target, movementSpeed);
            return;
        }

        this.getNavigation().stop();

        if (distanceSqr > maximumRangeSqr || this.rangedAttackCooldown > 0) {
            cancelRangedUse();
            return;
        }

        if (!this.isUsingItem()) {
            this.startUsingItem(InteractionHand.MAIN_HAND);
            this.rangedUseTicks = 0;
            return;
        }

        this.rangedUseTicks++;
        if (this.rangedUseTicks < requiredUseTicks) {
            return;
        }

        fireMirrorArrow(
                target,
                arrowVelocity,
                arrowInaccuracy,
                crossbow
        );

        this.stopUsingItem();
        this.rangedUseTicks = 0;
        this.rangedAttackCooldown = cooldownAfterShot;
        this.swing(InteractionHand.MAIN_HAND);
    }

    private void fireMirrorArrow(
            LivingEntity target,
            float velocity,
            float inaccuracy,
            boolean crossbow
    ) {
        ItemStack arrowStack = new ItemStack(Items.ARROW);
        ArrowItem arrowItem = (ArrowItem) arrowStack.getItem();
        AbstractArrow arrow = arrowItem.createArrow(
                this.level(),
                arrowStack,
                this
        );

        arrow.setOwner(this);
        arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
        arrow.setBaseDamage(
                Math.max(
                        2.0D,
                        this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.75D
                )
        );

        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        double dy = target.getY(0.3333333333333333D) - arrow.getY();
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        arrow.shoot(
                dx,
                dy + horizontalDistance * 0.20D,
                dz,
                velocity,
                inaccuracy
        );

        this.level().addFreshEntity(arrow);
        this.playSound(
                crossbow
                        ? SoundEvents.CROSSBOW_SHOOT
                        : SoundEvents.ARROW_SHOOT,
                1.0F,
                0.9F + this.random.nextFloat() * 0.2F
        );
    }

    private void cancelRangedUse() {
        if (this.isUsingItem()) {
            this.stopUsingItem();
        }
        this.rangedUseTicks = 0;
    }

    private double getManualAttackReachSqr(LivingEntity target) {
        double ownReach = this.getBbWidth() * 2.0D;
        return ownReach * ownReach + target.getBbWidth();
    }

    private void handleFriendlyAi(Player owner) {
        double ownerDistance = this.distanceToSqr(owner);

        if (ownerDistance > 256.0D) {
            double offsetX = (this.random.nextDouble() - 0.5D) * 2.0D;
            double offsetZ = (this.random.nextDouble() - 0.5D) * 2.0D;
            this.teleportTo(
                    owner.getX() + offsetX,
                    owner.getY(),
                    owner.getZ() + offsetZ
            );
            this.getNavigation().stop();
        }

        LivingEntity target = this.getTarget();
        if (!isValidFriendlyTarget(target)) {
            target = chooseOwnerCombatTarget(owner);
            if (target == null) {
                target = findNearestEnemy(24.0D);
            }
            this.setTarget(target);
        }

        if (this.getTarget() == null && ownerDistance > 12.0D) {
            this.getNavigation().moveTo(owner, 1.12D);
        }
    }

    @Nullable
    private LivingEntity chooseOwnerCombatTarget(Player owner) {
        LivingEntity target = owner.getLastHurtMob();
        if (isValidFriendlyTarget(target) && this.distanceToSqr(target) <= 1024.0D) {
            return target;
        }

        target = owner.getLastHurtByMob();
        if (isValidFriendlyTarget(target) && this.distanceToSqr(target) <= 1024.0D) {
            return target;
        }

        return null;
    }

    @Nullable
    private LivingEntity findNearestEnemy(double range) {
        List<LivingEntity> candidates = this.level().getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(range),
                this::isValidFriendlyTarget
        );

        LivingEntity nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (LivingEntity candidate : candidates) {
            double distance = this.distanceToSqr(candidate);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearest = candidate;
            }
        }

        return nearest;
    }

    private boolean isValidFriendlyTarget(@Nullable LivingEntity target) {
        if (target == null || !target.isAlive() || target == this) {
            return false;
        }
        if (target instanceof Player) {
            return false;
        }
        if (target instanceof CorrosionMirrorEntity mirror) {
            return !mirror.isFriendly();
        }
        if (target instanceof Enemy) {
            return true;
        }

        if (target.getType().getCategory() == MobCategory.MONSTER) {
            return true;
        }

        if (target instanceof Mob mob) {
            Player owner = this.getOwnerPlayer();
            return owner != null
                    && (mob.getTarget() == owner
                    || owner.getLastHurtByMob() == mob
                    || owner.getLastHurtMob() == mob);
        }

        return false;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (target == null || !target.isAlive() || target == this) {
            return false;
        }

        if (this.isFriendly()) {
            return isValidFriendlyTarget(target) && !this.isAlliedTo(target);
        }

        UUID ownerUUID = this.getOwnerUUID();
        if (ownerUUID == null || !ownerUUID.equals(target.getUUID())) {
            return false;
        }

        return !(target instanceof Player player) || !player.isSpectator();
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (!(target instanceof LivingEntity living) || !this.canAttack(living)) {
            return false;
        }
        return super.doHurtTarget(target);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (this.isFriendly()) {
            Player owner = this.getOwnerPlayer();

            if (entity == owner) {
                return true;
            }
            if (owner != null && owner.isAlliedTo(entity)) {
                return true;
            }
            if (entity instanceof CorrosionMirrorEntity mirror) {
                return mirror.isFriendly()
                        && Objects.equals(
                                this.getOwnerUUID(),
                                mirror.getOwnerUUID()
                        );
            }
        }

        return super.isAlliedTo(entity);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity attacker = source.getEntity();
        if (this.isFriendly() && attacker != null && this.isAlliedTo(attacker)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()
                && this.expireGameTime > 0L
                && this.level().getGameTime() >= this.expireGameTime) {
            this.discard();
        }
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(ownerUUID));
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UUID).orElse(null);
    }

    @Nullable
    public Player getOwnerPlayer() {
        UUID ownerUUID = this.getOwnerUUID();
        return ownerUUID == null
                ? null
                : this.level().getPlayerByUUID(ownerUUID);
    }

    public void setFriendly(boolean friendly) {
        this.entityData.set(DATA_FRIENDLY, friendly);
    }

    public boolean isFriendly() {
        return this.entityData.get(DATA_FRIENDLY);
    }

    public boolean belongsTo(Player player) {
        return player != null
                && player.getUUID().equals(this.getOwnerUUID());
    }

    public boolean isExpired(long gameTime) {
        return this.expireGameTime > 0L && gameTime >= this.expireGameTime;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean causeFallDamage(
            float distance,
            float damageMultiplier,
            DamageSource source
    ) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        UUID ownerUUID = this.getOwnerUUID();
        if (ownerUUID != null) {
            tag.putUUID(SAVE_OWNER, ownerUUID);
        }
        tag.putBoolean(SAVE_FRIENDLY, this.isFriendly());
        tag.putLong(SAVE_EXPIRE, this.expireGameTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.hasUUID(SAVE_OWNER)) {
            this.setOwnerUUID(tag.getUUID(SAVE_OWNER));
        }
        this.setFriendly(tag.getBoolean(SAVE_FRIENDLY));
        this.expireGameTime = tag.getLong(SAVE_EXPIRE);

        CompoundTag data = this.getPersistentData();
        data.putBoolean(NBT_MIRROR_MARKER, true);

        UUID ownerUUID = this.getOwnerUUID();
        if (ownerUUID != null) {
            data.putUUID(NBT_MIRROR_OWNER, ownerUUID);
        }
        data.putBoolean(NBT_MIRROR_FRIENDLY, this.isFriendly());
        data.putLong(NBT_MIRROR_EXPIRE, this.expireGameTime);
    }
}
