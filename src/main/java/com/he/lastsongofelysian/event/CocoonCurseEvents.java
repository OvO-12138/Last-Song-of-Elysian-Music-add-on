package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.*;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class CocoonCurseEvents {

    private CocoonCurseEvents() {
    }

    private static final Random RAND = new Random();

    private static final Map<UUID, PendingCraftRollback>
            PENDING_CRAFT_ROLLBACKS = new HashMap<>();

    private static final Map<UUID, Long>
            PRECHECKED_CRAFT_UNTIL = new HashMap<>();

    private static final float REASON_CRAFT_EXPLOSION_POWER =
            2.0F;

    public static final String NBT_HIDDEN_ATTRIBUTE_SLOT =
            "CocoonHiddenAttributeSlot";

    private static final String NBT_NEXT_HALLUCINATION =
            "CocoonNextHallucination";

    private static final int HIDDEN_ATTRIBUTE_SLOT_COUNT = 6;

    private static final int VOID_SUMMON_COOLDOWN_TICKS =
            10 * 20;

    private static final double VOID_SUMMON_BASE_HEALTH_MULTIPLIER =
            2.0D;

    private static final float VOID_SUMMON_BASE_CORRUPTION_PERCENT =
            0.02F;

    private static final int VOID_SUMMON_WEAKNESS_TICKS =
            5 * 20;

    private static final int VOID_SUMMON_WEAKNESS_AMPLIFIER =
            1;

    private static final float VOID_SUMMON_MAX_DAMAGE_PERCENT =
            0.80F;

    private static final float
            VOID_SUMMON_MAX_INCOMING_DAMAGE_PERCENT =
            0.20F;

    private static final int
            VOID_SUMMON_DAMAGE_WINDOW_TICKS =
            20;

    private static final int
            VOID_SUMMON_TELEPORT_COOLDOWN_TICKS =
            100;

    private static final ResourceKey<DamageType>
            VOID_SUMMON_DAMAGE_TYPE =
            ResourceKey.create(
                    Registries.DAMAGE_TYPE,
                    new ResourceLocation(
                            lastsongofelysian.MODID,
                            "void_curse_summon"
                    )
            );

    private static final String NBT_VOID_SUMMONED_MOB =
            "LSEVoidCurseSummonedMob";

    private static final String NBT_VOID_SUMMON_OWNER =
            "LSEVoidCurseSummonOwner";

    private static final String
            NBT_VOID_SUMMON_DAMAGE_WINDOW_UNTIL =
            "LSEVoidCurseDamageWindowUntil";

    private static final String
            NBT_VOID_SUMMON_DAMAGE_TAKEN =
            "LSEVoidCurseDamageTaken";

    private static final String
            NBT_VOID_SUMMON_TELEPORT_COOLDOWN_UNTIL =
            "LSEVoidCurseTeleportCooldownUntil";

    private static final Map<UUID, Long>
            VOID_SUMMON_COOLDOWN_UNTIL =
            new HashMap<>();
    
    private static final Map<String, Integer> CHUNK_ZOMBIE_COUNT =
            new HashMap<>();
    
    private static final int MAX_ZOMBIES_PER_CHUNK = 5;

    private static final UUID THUNDER_SPEED_UUID =
            UUID.fromString("31b077cc-3e91-49cd-8ef3-33bb187b65a1");

    private static final UUID THUNDER_ATTACK_SPEED_UUID =
            UUID.fromString("6e4d1407-c48d-41db-803e-b29956d98733");

    private static final UUID DEATH_HEALTH_UUID =
            UUID.fromString("13680a6a-6d4f-4ec0-8caa-e4ae87434fb1");

    private static final UUID ICE_SPEED_UUID =
            UUID.fromString("23a7f505-444d-44fc-b17a-aed532b2ce03");

    private static final UUID DOMINANCE_ATTACK_UUID =
            UUID.fromString("8c436d96-f5ca-451d-b50a-cd721ca18bfd");

    private static final UUID DOMINANCE_ARMOR_UUID =
            UUID.fromString("78da3d1e-da33-487e-821f-a75bdb65b741");

    private static boolean isWearingCocoon(Player player) {
        return CuriosApi.getCuriosHelper()
                .findFirstCurio(
                        player,
                        stack -> stack.is(ModItems.COCOON_OF_FINALITY.get())
                )
                .isPresent();
    }

    private static boolean hasCocoon(Player player) {
        return isWearingCocoon(player) && !CocoonCoreProgress.isReversed(player);
    }

    private static boolean hasEgoGold(Player player) {
        boolean hasEgo = CuriosApi.getCuriosHelper()
                .findFirstCurio(
                        player,
                        stack -> stack.is(ModItems.SIGNET_OF_EGO.get())
                )
                .isPresent();

        if (!hasEgo) {
            return false;
        }

        return CuriosApi.getCuriosHelper()
                .findFirstCurio(
                        player,
                        stack -> stack.is(ModItems.SIGNET_OF_GOLD.get())
                )
                .isPresent();
    }

    private static boolean hasEgoVicissitude(Player player) {
        boolean hasEgo = CuriosApi.getCuriosHelper()
                .findFirstCurio(
                        player,
                        stack -> stack.is(ModItems.SIGNET_OF_EGO.get())
                )
                .isPresent();

        if (!hasEgo) {
            return false;
        }

        return CuriosApi.getCuriosHelper()
                .findFirstCurio(
                        player,
                        stack -> stack.is(
                                ModItems.SIGNET_OF_VICISSITUDE.get()
                        )
                )
                .isPresent();
    }

    private static float multiplier(Player player) {
        return CocoonCoreProgress.getMultiplier(player);
    }

    private static float chance(Player player, float baseChance) {
        return CocoonCoreProgress.scaleChance(player, baseChance);
    }

    private static float percent(Player player, float basePercent) {
        return CocoonCoreProgress.scalePercent(player, basePercent);
    }

    private static int duration(Player player, int baseTicks) {
        return CocoonCoreProgress.scaleDuration(player, baseTicks);
    }

    @SubscribeEvent
    public static void onZombieDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Zombie zombie) {
            CompoundTag data = zombie.getPersistentData();
            String chunkKey = data.getString("LSE_Cocoon_Chunk_Key");
            if (!chunkKey.isEmpty()) {
                CHUNK_ZOMBIE_COUNT.computeIfPresent(chunkKey, (k, v) -> Math.max(0, v - 1));
                data.remove("LSE_Cocoon_Chunk_Key");
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (
                event.phase != TickEvent.Phase.END ||
                        event.side != LogicalSide.SERVER
        ) {
            return;
        }

        Player player = event.player;

        if (!isWearingCocoon(player)) {
            removeModifiers(player);
            return;
        }

        if (CocoonCoreProgress.isReversed(player)) {
            applyReversedEffects(player);
            return;
        }

        applyBaseAttributes(player);
        applyWindCurse(player);
        applyIceCurse(player);
        applyThunderRestoredEffect(player);
        applyFireRestoredEffect(player);
        applySentienceRestoredEffect(player);
        applyDominanceRestoredEffect(player);
        applyBindingCurse(player);
        applyCorrosionRestoredEffect(player);
    }

    private static void applyReversedEffects(Player player) {
        setReversedModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), THUNDER_SPEED_UUID,
                "cocoon_reversed_speed", 0.15D);
        setReversedModifier(player.getAttribute(Attributes.ATTACK_SPEED), THUNDER_ATTACK_SPEED_UUID,
                "cocoon_reversed_attack_speed", 0.15D);
        setReversedModifier(player.getAttribute(Attributes.MAX_HEALTH), DEATH_HEALTH_UUID,
                "cocoon_reversed_health", 0.20D);
        setReversedModifier(player.getAttribute(Attributes.ATTACK_DAMAGE), DOMINANCE_ATTACK_UUID,
                "cocoon_reversed_attack", 0.10D);
        setReversedModifier(player.getAttribute(Attributes.ARMOR), DOMINANCE_ARMOR_UUID,
                "cocoon_reversed_armor", 0.10D);

        if (player.isInWater() || isColdBiome(player)) {
            setReversedModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), ICE_SPEED_UUID,
                    "cocoon_reversed_water_speed", 0.10D);
        } else {
            removeModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), ICE_SPEED_UUID);
        }

        player.clearFire();
        player.setTicksFrozen(0);
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 40, 0, false, false, true));

        if (player.tickCount % 20 == 0) {
            List<Zombie> summons = player.level().getEntitiesOfClass(
                    Zombie.class,
                    player.getBoundingBox().inflate(256.0D),
                    zombie -> zombie.getPersistentData().getBoolean(NBT_VOID_SUMMONED_MOB)
                            && zombie.getPersistentData().hasUUID(NBT_VOID_SUMMON_OWNER)
                            && player.getUUID().equals(
                            zombie.getPersistentData().getUUID(NBT_VOID_SUMMON_OWNER))
            );
            summons.forEach(Entity::discard);
        }
    }

    private static void applyBaseAttributes(Player player) {

        setModifier(
                player.getAttribute(Attributes.MOVEMENT_SPEED),
                THUNDER_SPEED_UUID,
                "cocoon_thunder_speed",
                -percent(player, 0.20F)
        );

        setModifier(
                player.getAttribute(Attributes.ATTACK_SPEED),
                THUNDER_ATTACK_SPEED_UUID,
                "cocoon_thunder_attack_speed",
                -percent(player, 0.30F)
        );

        setModifier(
                player.getAttribute(Attributes.MAX_HEALTH),
                DEATH_HEALTH_UUID,
                "cocoon_death_health",
                -percent(player, 0.25F)
        );

        player.setHealth(
                Math.min(player.getHealth(), player.getMaxHealth())
        );

        setModifier(
                player.getAttribute(Attributes.ATTACK_DAMAGE),
                DOMINANCE_ATTACK_UUID,
                "cocoon_dominance_attack",
                -percent(player, 0.10F)
        );

        setModifier(
                player.getAttribute(Attributes.ARMOR),
                DOMINANCE_ARMOR_UUID,
                "cocoon_dominance_armor",
                -percent(player, 0.10F)
        );

        boolean coldBiome = isColdBiome(player);

        if (coldBiome) {
            setModifier(
                    player.getAttribute(Attributes.MOVEMENT_SPEED),
                    ICE_SPEED_UUID,
                    "cocoon_ice_speed",
                    -percent(player, 0.10F)
            );
        } else {
            removeModifier(
                    player.getAttribute(Attributes.MOVEMENT_SPEED),
                    ICE_SPEED_UUID
            );
        }
    }

    private static void applyWindCurse(Player player) {

        if (player.tickCount % 20 == 0) {
            player.causeFoodExhaustion(
                    0.15F * multiplier(player)
            );
        }
    }

    private static void applyIceCurse(Player player) {

        if (player.isUnderWater()) {

            if (!hasEgoGold(player)) {
                int interval = Math.max(
                        1,
                        Math.round(4.0F / multiplier(player))
                );

                if (player.tickCount % interval == 0) {
                    player.setAirSupply(
                            Math.max(-20, player.getAirSupply() - 1)
                    );
                }
            } else {
                player.setAirSupply(player.getMaxAirSupply());
            }

            if (
                    CocoonCoreProgress.unlocked(
                            player,
                            CocoonCoreProgress.ICE_STAGE
                    )
            ) {
                player.addEffect(
                        new MobEffectInstance(
                                MobEffects.WEAKNESS,
                                40,
                                0,
                                false,
                                false,
                                true
                        )
                );
            }
        }

        if (
                CocoonCoreProgress.unlocked(
                        player,
                        CocoonCoreProgress.ICE_STAGE
                ) &&
                        isColdBiome(player) &&
                        player.tickCount % 100 == 0
        ) {
            player.hurt(
                    player.damageSources().freeze(),
                    CocoonCoreProgress.scaleValue(player, 1.0F)
            );
        }
    }

    private static void applyThunderRestoredEffect(Player player) {
        if (
                !CocoonCoreProgress.unlocked(
                        player,
                        CocoonCoreProgress.THUNDER_STAGE
                )
        ) {
            return;
        }

        if (player.tickCount % 100 != 0) {
            return;
        }

        if (!player.level().canSeeSky(player.blockPosition())) {
            return;
        }

        float baseChance;

        if (player.level().isThundering()) {
            baseChance = 0.20F;
        } else if (player.level().isRaining()) {
            baseChance = 0.10F;
        } else {
            return;
        }

        if (RAND.nextFloat() >= chance(player, baseChance)) {
            return;
        }

        LightningBolt lightning =
                EntityType.LIGHTNING_BOLT.create(player.level());

        if (lightning != null) {
            lightning.moveTo(
                    player.getX(),
                    player.getY(),
                    player.getZ()
            );

            player.level().addFreshEntity(lightning);
        }
    }

    private static void applyFireRestoredEffect(Player player) {
        if (
                !CocoonCoreProgress.unlocked(
                        player,
                        CocoonCoreProgress.FIRE_STAGE
                )
        ) {
            return;
        }

        if (
                player.getBlockStateOn().is(Blocks.MAGMA_BLOCK) &&
                        player.tickCount % 20 == 0
        ) {
            player.hurt(
                    player.damageSources().hotFloor(),
                    CocoonCoreProgress.scaleValue(player, 1.0F)
            );
        }

        if (
                player.isOnFire() &&
                        player.tickCount % 20 == 0
        ) {
            player.hurt(
                    player.damageSources().onFire(),
                    CocoonCoreProgress.scaleValue(player, 1.0F)
            );
        }
    }

    private static void applySentienceRestoredEffect(Player player) {
        if (
                !CocoonCoreProgress.unlocked(
                        player,
                        CocoonCoreProgress.SENTIENCE_STAGE
                )
        ) {
            return;
        }

        CompoundTag data = player.getPersistentData();
        long now = player.level().getGameTime();
        long next = data.getLong(NBT_NEXT_HALLUCINATION);

        if (next == 0L) {
            data.putLong(
                    NBT_NEXT_HALLUCINATION,
                    now + 600L + RAND.nextInt(601)
            );
            return;
        }

        if (now < next) {
            return;
        }

        data.putLong(
                NBT_NEXT_HALLUCINATION,
                now + 600L + RAND.nextInt(601)
        );

        int effectDuration = duration(player, 50);

        int selection = RAND.nextInt(3);

        if (selection == 0) {
            player.addEffect(
                    new MobEffectInstance(
                            MobEffects.CONFUSION,
                            effectDuration,
                            0
                    )
            );
        } else if (selection == 1) {
            player.addEffect(
                    new MobEffectInstance(
                            MobEffects.BLINDNESS,
                            effectDuration,
                            0
                    )
            );
        } else {
            player.addEffect(
                    new MobEffectInstance(
                            MobEffects.DARKNESS,
                            effectDuration,
                            0
                    )
            );
        }

        player.level().playSound(
                null,
                player.blockPosition(),
                SoundEvents.ELDER_GUARDIAN_CURSE,
                SoundSource.PLAYERS,
                0.8F,
                0.8F + RAND.nextFloat() * 0.4F
        );
    }

    private static void applyDominanceRestoredEffect(Player player) {
        if (
                !CocoonCoreProgress.unlocked(
                        player,
                        CocoonCoreProgress.DOMINANCE_STAGE
                )
        ) {
            return;
        }

        if (player.tickCount % 400 != 0) {
            return;
        }

        if (RAND.nextFloat() >= chance(player, 0.10F)) {
            return;
        }

        int controlDuration = duration(player, 30);

        player.addEffect(
                new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        controlDuration,
                        255,
                        false,
                        false,
                        true
                )
        );

        player.addEffect(
                new MobEffectInstance(
                        MobEffects.DIG_SLOWDOWN,
                        controlDuration,
                        4,
                        false,
                        false,
                        true
                )
        );
    }

    private static void applyBindingCurse(Player player) {

        if (
                !CocoonCoreProgress.unlocked(
                        player,
                        CocoonCoreProgress.BINDING_STAGE
                )
        ) {
            if (player.tickCount % 600 == 0) {
                player.addEffect(
                        new MobEffectInstance(
                                MobEffects.DIG_SLOWDOWN,
                                duration(player, 100),
                                0,
                                false,
                                false,
                                true
                        )
                );
            }

            return;
        }

        player.addEffect(
                new MobEffectInstance(
                        MobEffects.DIG_SLOWDOWN,
                        40,
                        0,
                        false,
                        false,
                        true
                )
        );
    }

    private static void applyCorrosionRestoredEffect(Player player) {
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onVoidAttackTeleport(
            LivingAttackEvent event
    ) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (isVoidSummon(event.getEntity())) {
            tryVoidSummonTeleport(event.getEntity());
        }

        if (
                event.getSource().is(
                        VOID_SUMMON_DAMAGE_TYPE
                )
        ) {
            return;
        }

        // Skip teleport if no actual damage occurred (e.g., from armor effect removal)
        if (event.getAmount() <= 0.0F) {
            return;
        }

        if (event.getEntity() instanceof Player victim) {
            Zombie summoned =
                    getOwnVoidSummon(
                            event.getSource(),
                            victim
                    );

            if (summoned != null) {
                handleVoidSummonAttack(
                        event,
                        victim,
                        summoned
                );
                return;
            }
        }

        if (
                event.getSource().getEntity()
                        instanceof Player attacker
        ) {
            tryVoidTeleport(attacker);
        }

        if (event.getEntity() instanceof Player victim) {
            tryVoidTeleport(victim);
        }
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onRockCurseAttack(
            LivingAttackEvent event
    ) {
        if (
                event.getEntity().level().isClientSide() ||
                event.getSource().is(
                        VOID_SUMMON_DAMAGE_TYPE
                ) ||
                !(event.getEntity()
                        instanceof Player player) ||
                !hasCocoon(player)
        ) {
            return;
        }

        damageArmor(
                player,
                Math.max(
                        1,
                        Math.round(
                                0.20F * multiplier(player)
                        )
                )
        );

        if (
                CocoonCoreProgress.unlocked(
                        player,
                        CocoonCoreProgress.EARTH_STAGE
                ) &&
                RAND.nextFloat() < chance(player, 0.03F)
        ) {
            deleteRandomEquippedArmor(player);
        }
    }

    private static void handleVoidSummonAttack(
            LivingAttackEvent event,
            Player victim,
            Zombie summoned
    ) {

        event.setCanceled(true);

        victim.addEffect(
                new MobEffectInstance(
                        MobEffects.WEAKNESS,
                        VOID_SUMMON_WEAKNESS_TICKS,
                        VOID_SUMMON_WEAKNESS_AMPLIFIER,
                        false,
                        true,
                        true
                )
        );

        SignetEffects.addCorruptionByPercent(
                victim,
                VOID_SUMMON_BASE_CORRUPTION_PERCENT
        );

        float cappedDamage =
                Math.min(
                        Math.max(
                                0.0F,
                                event.getAmount()
                        ),
                        victim.getMaxHealth()
                                * VOID_SUMMON_MAX_DAMAGE_PERCENT
                );

        if (cappedDamage > 0.0F) {
            victim.hurt(
                    createVoidSummonDamageSource(
                            victim,
                            summoned
                    ),
                    cappedDamage
            );
        }

    }

    private static DamageSource
            createVoidSummonDamageSource(
            Player victim,
            Zombie summoned
    ) {
        return new DamageSource(
                victim.level()
                        .registryAccess()
                        .registryOrThrow(
                                Registries.DAMAGE_TYPE
                        )
                        .getHolderOrThrow(
                                VOID_SUMMON_DAMAGE_TYPE
                        ),
                summoned,
                summoned
        );
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onVoidSummonFinalDamage(
            LivingDamageEvent event
    ) {
        if (isVoidSummon(event.getEntity())) {
            LivingEntity summoned = event.getEntity();
            CompoundTag data = summoned.getPersistentData();
            long now = summoned.level().getGameTime();
            long windowUntil = data.getLong(
                    NBT_VOID_SUMMON_DAMAGE_WINDOW_UNTIL
            );

            if (windowUntil <= now) {
                data.putLong(
                        NBT_VOID_SUMMON_DAMAGE_WINDOW_UNTIL,
                        now + VOID_SUMMON_DAMAGE_WINDOW_TICKS
                );
                data.putFloat(
                        NBT_VOID_SUMMON_DAMAGE_TAKEN,
                        0.0F
                );
            }

            float maximumDamage =
                    summoned.getMaxHealth()
                            * VOID_SUMMON_MAX_INCOMING_DAMAGE_PERCENT;
            float damageTaken = Math.max(
                    0.0F,
                    data.getFloat(
                            NBT_VOID_SUMMON_DAMAGE_TAKEN
                    )
            );
            float remainingDamage = Math.max(
                    0.0F,
                    maximumDamage - damageTaken
            );
            float allowedDamage = Math.min(
                    Math.max(0.0F, event.getAmount()),
                    remainingDamage
            );

            event.setAmount(allowedDamage);
            data.putFloat(
                    NBT_VOID_SUMMON_DAMAGE_TAKEN,
                    Math.min(
                            maximumDamage,
                            damageTaken + allowedDamage
                    )
            );
            return;
        }

        if (
                !(event.getEntity()
                        instanceof Player player) ||
                !event.getSource().is(
                        VOID_SUMMON_DAMAGE_TYPE
                )
        ) {
            return;
        }

        event.setAmount(
                Math.min(
                        event.getAmount(),
                        player.getMaxHealth()
                                * VOID_SUMMON_MAX_DAMAGE_PERCENT
                )
        );
    }

    private static void tryVoidTeleport(Player player) {
        if (
                !hasCocoon(player) ||
                !CocoonCoreProgress.unlocked(
                        player,
                        CocoonCoreProgress.VOID_STAGE
                )
        ) {
            return;
        }

        // Check cooldown (use tickCount to avoid issues with world time)
        CompoundTag data = player.getPersistentData();
        long lastTeleportTick = data.getLong("LSE_VoidCurse_Teleport_LastTick");
        if (player.tickCount < lastTeleportTick + VOID_SUMMON_TELEPORT_COOLDOWN_TICKS) {
            return;
        }

        if (RAND.nextFloat() >= chance(player, 0.05F)) {
            return;
        }

        if (!randomTeleport(player)) {
            return;
        }

        // Set cooldown
        data.putLong("LSE_VoidCurse_Teleport_LastTick", player.tickCount);

        if (
                RAND.nextFloat()
                        < chance(player, 0.25F)
        ) {
            trySpawnVoidCatalyst(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onCocoonDamage(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (event.getSource().getEntity() instanceof Player attacker
                && isWearingCocoon(attacker)
                && CocoonCoreProgress.isReversed(attacker)
                && event.getEntity().isOnFire()) {
            event.setAmount(event.getAmount() * 1.20F);
        }

        if (event.getEntity() instanceof Player victim
                && isWearingCocoon(victim)
                && CocoonCoreProgress.isReversed(victim)) {
            if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                event.setAmount(0.0F);
                victim.clearFire();
                return;
            }
            if (event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
                event.setAmount(event.getAmount() * 0.80F);
            }
            if (victim.getHealth() <= victim.getMaxHealth() * 0.20F) {
                event.setAmount(event.getAmount() * 0.80F);
            }
            return;
        }

        boolean cocoonAttacker =
                event.getSource().getEntity() instanceof Player
                        && hasCocoon((Player) event.getSource().getEntity());

        boolean cocoonVictim =
                event.getEntity() instanceof Player
                        && hasCocoon((Player) event.getEntity());

        if (!cocoonAttacker && !cocoonVictim) {
            return;
        }

        if (event.isCanceled()) {
            event.setCanceled(false);
        }

        if (
                event.getSource().getEntity() instanceof Player attacker &&
                        hasCocoon(attacker)
        ) {

            if (
                    CocoonCoreProgress.unlocked(
                            attacker,
                            CocoonCoreProgress.FIRE_STAGE
                    ) &&
                            RAND.nextFloat() < chance(attacker, 0.10F)
            ) {
                attacker.setSecondsOnFire(
                        Math.max(
                                attacker.getRemainingFireTicks() / 20,
                                Math.round(3.0F * multiplier(attacker))
                        )
                );
            }

            if (
                    CocoonCoreProgress.unlocked(
                            attacker,
                            CocoonCoreProgress.SENTIENCE_STAGE
                    ) &&
                            RAND.nextFloat() < chance(attacker, 0.08F)
            ) {
                attacker.addEffect(
                        new MobEffectInstance(
                                MobEffects.CONFUSION,
                                duration(attacker, 60),
                                0
                        )
                );
            }

            if (
                    CocoonCoreProgress.unlocked(
                            attacker,
                            CocoonCoreProgress.REASON_STAGE
                    ) &&
                            RAND.nextFloat() < chance(attacker, 0.025F)
            ) {
                spawnCatalystMobs(attacker);
                // Remove one stackable item from victim as Reason curse penalty
                if (event.getEntity() instanceof Player victim) {
                    removeRandomStackableItem(victim);
                }
            }
        }

        if (
                event.getEntity() instanceof Player player &&
                        hasCocoon(player)
        ) {

            if (
                    player.getHealth() <=
                            player.getMaxHealth() * 0.20F
            ) {
                event.setAmount(
                        event.getAmount() *
                                (
                                        1.0F +
                                                CocoonCoreProgress.scalePercent(
                                                        player,
                                                        0.10F
                                                )
                                )
                );
            }

            if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                event.setAmount(
                        event.getAmount() *
                                (
                                        1.0F +
                                                CocoonCoreProgress.scalePercent(
                                                        player,
                                                        0.20F
                                                )
                                )
                );

                int extraSeconds = Math.max(
                        1,
                        Math.round(0.25F * multiplier(player))
                );

                player.setSecondsOnFire(
                        player.getRemainingFireTicks() / 20 +
                                extraSeconds
                );
            }

            damageHeldItem(
                    player,
                    Math.max(
                            1,
                            Math.round(0.15F * multiplier(player))
                    )
            );

            if (
                    CocoonCoreProgress.unlocked(
                            player,
                            CocoonCoreProgress.FIRE_STAGE
                    ) &&
                            RAND.nextFloat() < chance(player, 0.10F)
            ) {
                player.setSecondsOnFire(
                        Math.max(
                                player.getRemainingFireTicks() / 20,
                                Math.round(3.0F * multiplier(player))
                        )
                );
            }

            if (
                    CocoonCoreProgress.unlocked(
                            player,
                            CocoonCoreProgress.DEATH_STAGE
                    ) &&
                            player.getHealth() <=
                                    player.getMaxHealth() * 0.01F &&
                            event.getAmount() >= player.getHealth()
            ) {
                event.setAmount(
                        Math.max(
                                event.getAmount(),
                                player.getHealth() +
                                        player.getAbsorptionAmount() +
                                        1000.0F
                        )
                );
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onCocoonHeal(LivingHealEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (isWearingCocoon(player) && CocoonCoreProgress.isReversed(player)) {
            event.setAmount(event.getAmount() * 1.20F);
            return;
        }

        if (!hasCocoon(player)) {
            return;
        }

        float reduction = Mth.clamp(
                0.25F * multiplier(player),
                0.0F,
                0.90F
        );

        event.setAmount(
                event.getAmount() * (1.0F - reduction)
        );

        if (
                CocoonCoreProgress.unlocked(
                        player,
                        CocoonCoreProgress.WIND_STAGE
                ) &&
                        player.getFoodData().getFoodLevel() >= 18 &&
                        !player.hasEffect(MobEffects.REGENERATION)
        ) {
            event.setAmount(0.0F);
        }
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onKnockback(
            LivingKnockBackEvent event
    ) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (hasEgoVicissitude(player)) {
            event.setStrength(0.0F);
            event.setCanceled(true);
            return;
        }

        if (!hasCocoon(player)) {
            return;
        }

        if (event.isCanceled()) {
            event.setCanceled(false);
        }

        float bonus = CocoonCoreProgress.scalePercent(
                player,
                0.15F
        );

        event.setStrength(
                event.getStrength() * (1.0F + bonus)
        );
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEnchantAttempt(
            EnchantmentLevelSetEvent event
    ) {
        Level level = event.getLevel();

        if (level == null || level.isClientSide()) {
            return;
        }

        List<Player> reversed = level.getEntitiesOfClass(
                Player.class,
                new net.minecraft.world.phys.AABB(event.getPos()).inflate(8.0D),
                player -> player.isAlive() && isWearingCocoon(player)
                        && CocoonCoreProgress.isReversed(player)
        );
        if (!reversed.isEmpty()) {
            event.setEnchantLevel(Math.max(1, Math.round(event.getEnchantLevel() * 1.10F)));
            return;
        }

        List<Player> nearby = level.getEntitiesOfClass(
                Player.class,
                new net.minecraft.world.phys.AABB(
                        event.getPos()
                ).inflate(8.0D),
                player -> player.isAlive() && hasCocoon(player)
        );

        if (nearby.isEmpty()) {
            return;
        }

        Player player = nearby.get(0);

        if (
                RAND.nextFloat() <
                        CocoonCoreProgress.scaleChance(
                                player,
                                0.25F
                        )
        ) {
            event.setEnchantLevel(0);
        }
    }

    @SubscribeEvent
    public static void onExperienceGain(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();
        if (event.getAmount() > 0 && isWearingCocoon(player)
                && CocoonCoreProgress.isReversed(player)) {
            event.setAmount(Math.max(1, Math.round(event.getAmount() * 1.30F)));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void applyPendingCraftRollback(
            TickEvent.PlayerTickEvent event
    ) {
        if (
                event.phase != TickEvent.Phase.END ||
                event.side != LogicalSide.SERVER
        ) {
            return;
        }

        PendingCraftRollback rollback =
                PENDING_CRAFT_ROLLBACKS.remove(
                        event.player.getUUID()
                );

        if (rollback != null) {
            rollback.restore(event.player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCraftAttempt(
            PlayerEvent.ItemCraftedEvent event
    ) {
        Player player = event.getEntity();

        if (
                player.level().isClientSide() ||
                !hasCocoon(player)
        ) {
            return;
        }


        // Skip if this is not a real crafting event (e.g., curios equipping/unequipping)
        boolean foundInInventory = false;
        Inventory playerInv = player.getInventory();
        for (int i = 0; i < playerInv.getContainerSize(); i++) {
            if (playerInv.getItem(i).equals(event.getCrafting())) {
                foundInInventory = true;
                break;
            }
        }
        if (!foundInInventory) {
            return;
        }

        ItemStack crafted =
                event.getCrafting().copy();

        if (crafted.isEmpty()) {
            return;
        }

        long now = player.level().getGameTime();

        if (
                PRECHECKED_CRAFT_UNTIL.getOrDefault(
                        player.getUUID(),
                        Long.MIN_VALUE
                ) >= now
        ) {
            return;
        }

        if (
                PENDING_CRAFT_ROLLBACKS.containsKey(
                        player.getUUID()
                )
        ) {
            event.getCrafting().setCount(0);
            return;
        }

        boolean failed = applyReasonCraftAttempt(player);

        if (!failed) {
            return;
        }

        PENDING_CRAFT_ROLLBACKS.put(
                player.getUUID(),
                PendingCraftRollback.capture(
                        player,
                        event.getInventory(),
                        crafted
                )
        );

        event.getCrafting().setCount(0);

    }

    public static boolean handleCraftOutputTake(
            Player player,
            ItemStack crafted
    ) {
        if (
                player.level().isClientSide()
                        || crafted.isEmpty()
                        || !hasCocoon(player)
        ) {
            return false;
        }

        PRECHECKED_CRAFT_UNTIL.put(
                player.getUUID(),
                player.level().getGameTime() + 1L
        );

        return applyReasonCraftAttempt(player);
    }

    private static boolean applyReasonCraftAttempt(
            Player player
    ) {

        SignetEffects.addCorruptionByPercent(
                player,
                0.01F
        );

        boolean failed =
                RAND.nextFloat() <
                        CocoonCoreProgress.scaleChance(
                                player,
                                0.08F
                        );

        if (!failed) {
            return false;
        }

        SignetEffects.addCorruptionByPercent(
                player,
                0.05F
        );

        player.level().explode(
                player,
                player.getX(),
                player.getY() + 0.5D,
                player.getZ(),
                REASON_CRAFT_EXPLOSION_POWER,
                Level.ExplosionInteraction.NONE
        );

        if (
                CocoonCoreProgress.unlocked(
                        player,
                        CocoonCoreProgress.REASON_STAGE
                )
        ) {
            spawnCatalystMobs(player);
        }

        player.displayClientMessage(
                Component.literal(
                        "真理拒绝了你"
                ).withStyle(ChatFormatting.RED),
                true
        );

        return true;
    }

    private static final class PendingCraftRollback {

        private final AbstractContainerMenu originalMenu;
        private final Container craftingInventory;
        private final List<ItemStack> craftingSlots;
        private final List<ItemStack> playerInventory;
        private final ItemStack carried;

        private PendingCraftRollback(
                AbstractContainerMenu originalMenu,
                Container craftingInventory,
                List<ItemStack> craftingSlots,
                List<ItemStack> playerInventory,
                ItemStack carried
        ) {
            this.originalMenu = originalMenu;
            this.craftingInventory = craftingInventory;
            this.craftingSlots = craftingSlots;
            this.playerInventory = playerInventory;
            this.carried = carried;
        }

        private static PendingCraftRollback capture(
                Player player,
                Container craftingInventory,
                ItemStack crafted
        ) {
            List<ItemStack> craftingSlots =
                    new ArrayList<>(
                            craftingInventory.getContainerSize()
                    );

            for (
                    int slot = 0;
                    slot <
                            craftingInventory
                                    .getContainerSize();
                    slot++
            ) {
                craftingSlots.add(
                        craftingInventory
                                .getItem(slot)
                                .copy()
                );
            }

            List<ItemStack> playerInventory =
                    new ArrayList<>(
                            player.getInventory()
                                    .getContainerSize()
                    );

            for (
                    int slot = 0;
                    slot <
                            player.getInventory()
                                    .getContainerSize();
                    slot++
            ) {
                playerInventory.add(
                        player.getInventory()
                                .getItem(slot)
                                .copy()
                );
            }

            ItemStack carried =
                    player.containerMenu
                            .getCarried()
                            .copy();

            int remaining =
                    crafted.getCount();

            if (
                    remaining > 0 &&
                    !carried.isEmpty() &&
                    ItemStack.isSameItemSameTags(
                            carried,
                            crafted
                    )
            ) {
                int removed = Math.min(
                        remaining,
                        carried.getCount()
                );

                carried.shrink(removed);
                remaining -= removed;
            }

            for (
                    int slot =
                            playerInventory.size() - 1;
                    slot >= 0 && remaining > 0;
                    slot--
            ) {
                ItemStack stack =
                        playerInventory.get(slot);

                if (
                        stack.isEmpty() ||
                        !ItemStack.isSameItemSameTags(
                                stack,
                                crafted
                        )
                ) {
                    continue;
                }

                int removed = Math.min(
                        remaining,
                        stack.getCount()
                );

                stack.shrink(removed);
                remaining -= removed;
            }

            return new PendingCraftRollback(
                    player.containerMenu,
                    craftingInventory,
                    craftingSlots,
                    playerInventory,
                    carried
            );
        }

        private void restore(Player player) {

            for (
                    int slot = 0;
                    slot < playerInventory.size() &&
                            slot <
                                    player.getInventory()
                                            .getContainerSize();
                    slot++
            ) {
                player.getInventory().setItem(
                        slot,
                        playerInventory
                                .get(slot)
                                .copy()
                );
            }

            player.containerMenu.setCarried(
                    carried.copy()
            );

            if (player.containerMenu == originalMenu) {

                for (
                        int slot = 0;
                        slot < craftingSlots.size() &&
                                slot <
                                        craftingInventory
                                                .getContainerSize();
                        slot++
                ) {
                    craftingInventory.setItem(
                            slot,
                            craftingSlots
                                    .get(slot)
                                    .copy()
                    );
                }

                craftingInventory.setChanged();
            } else {

                for (ItemStack stack : craftingSlots) {
                    if (stack.isEmpty()) {
                        continue;
                    }

                    ItemStack refund =
                            stack.copy();

                    player.getInventory().add(refund);

                    if (!refund.isEmpty()) {
                        player.drop(
                                refund,
                                false
                        );
                    }
                }
            }

            player.getInventory().setChanged();
            player.containerMenu.broadcastChanges();
            player.inventoryMenu.broadcastChanges();
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(
            PlayerEvent.PlayerLoggedOutEvent event
    ) {
        UUID uuid =
                event.getEntity().getUUID();

        PENDING_CRAFT_ROLLBACKS.remove(uuid);
        PRECHECKED_CRAFT_UNTIL.remove(uuid);
        VOID_SUMMON_COOLDOWN_UNTIL.remove(uuid);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDimensionChanged(
            PlayerEvent.PlayerChangedDimensionEvent event
    ) {
        Player player = event.getEntity();

        if (player.level().isClientSide() || !isWearingCocoon(player)) {
            return;
        }

        if (CocoonCoreProgress.isReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE, 200, 0, false, false, true
            ));
            return;
        }

        if (event.getTo() == Level.OVERWORLD) {
            return;
        }

        // NOTE: Dimension change teleport disabled per user request
        // No automatic teleport when entering Elysian Realm
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();

        if (player == null || !hasCocoon(player)) {
            return;
        }

        if (
                CocoonCoreProgress.unlocked(
                        player,
                        CocoonCoreProgress.BINDING_STAGE
                )
        ) {
            int damage = Math.max(
                    1,
                    Math.round(0.50F * multiplier(player))
            );

            damageHeldItem(player, damage);
        }
    }

    private static boolean isColdBiome(Player player) {
        return player.level()
                .getBiome(player.blockPosition())
                .value()
                .getBaseTemperature() <= 0.15F;
    }

    private static void setModifier(
            AttributeInstance attribute,
            UUID uuid,
            String name,
            double amount
    ) {
        if (attribute == null) {
            return;
        }

        attribute.removeModifier(uuid);

        attribute.addTransientModifier(
                new AttributeModifier(
                        uuid,
                        name,
                        amount,
                        AttributeModifier.Operation.MULTIPLY_BASE
                )
        );
    }

    private static void setReversedModifier(
            AttributeInstance attribute,
            UUID uuid,
            String name,
            double amount
    ) {
        if (attribute == null) return;
        attribute.removeModifier(uuid);
        attribute.addTransientModifier(
                new AttributeModifier(
                        uuid,
                        name,
                        amount,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
        );
    }

    private static void removeModifier(
            AttributeInstance attribute,
            UUID uuid
    ) {
        if (attribute != null) {
            attribute.removeModifier(uuid);
        }
    }

    private static void removeModifiers(Player player) {
        removeModifier(
                player.getAttribute(Attributes.MOVEMENT_SPEED),
                THUNDER_SPEED_UUID
        );

        removeModifier(
                player.getAttribute(Attributes.ATTACK_SPEED),
                THUNDER_ATTACK_SPEED_UUID
        );

        removeModifier(
                player.getAttribute(Attributes.MAX_HEALTH),
                DEATH_HEALTH_UUID
        );

        removeModifier(
                player.getAttribute(Attributes.MOVEMENT_SPEED),
                ICE_SPEED_UUID
        );

        removeModifier(
                player.getAttribute(Attributes.ATTACK_DAMAGE),
                DOMINANCE_ATTACK_UUID
        );

        removeModifier(
                player.getAttribute(Attributes.ARMOR),
                DOMINANCE_ARMOR_UUID
        );
    }

    private static boolean randomTeleport(
            LivingEntity entity
    ) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return false;
        }

        double oldX = entity.getX();
        double oldY = entity.getY();
        double oldZ = entity.getZ();
        BlockPos origin = entity.blockPosition();

        List<BlockPos> candidates =
                new ArrayList<>(26);

        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetY = -1; offsetY <= 1; offsetY++) {
                for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
                    if (
                            offsetX == 0
                                    && offsetY == 0
                                    && offsetZ == 0
                    ) {
                        continue;
                    }

                    candidates.add(
                            origin.offset(
                                    offsetX,
                                    offsetY,
                                    offsetZ
                            )
                    );
                }
            }
        }

        Collections.shuffle(candidates, RAND);

        for (BlockPos target : candidates) {

            BlockPos head = target.above();
            BlockPos floor = target.below();

            if (
                    !level.getBlockState(target)
                            .getCollisionShape(level, target)
                            .isEmpty() ||
                    !level.getBlockState(head)
                            .getCollisionShape(level, head)
                            .isEmpty() ||
                    level.getBlockState(floor)
                            .getCollisionShape(level, floor)
                            .isEmpty()
            ) {
                continue;
            }

            entity.teleportTo(
                    target.getX() + 0.5D,
                    target.getY(),
                    target.getZ() + 0.5D
            );
            entity.fallDistance = 0.0F;

            level.sendParticles(
                    ParticleTypes.PORTAL,
                    oldX,
                    oldY + 1.0D,
                    oldZ,
                    24,
                    0.4D,
                    0.8D,
                    0.4D,
                    0.15D
            );

            level.sendParticles(
                    ParticleTypes.PORTAL,
                    entity.getX(),
                    entity.getY()
                            + entity.getBbHeight() * 0.5D,
                    entity.getZ(),
                    24,
                    0.4D,
                    0.8D,
                    0.4D,
                    0.15D
            );

            level.playSound(
                    null,
                    entity.blockPosition(),
                    SoundEvents.CHORUS_FRUIT_TELEPORT,
                    entity instanceof Player
                            ? SoundSource.PLAYERS
                            : SoundSource.HOSTILE,
                    1.0F,
                    1.0F
            );

            return true;
        }

        return false;
    }

    private static void tryVoidSummonTeleport(
            LivingEntity summoned
    ) {
        CompoundTag data = summoned.getPersistentData();
        long lastTick = data.getLong("LSE_VoidCurse_Teleport_LastTick");
        if (summoned.tickCount < lastTick + VOID_SUMMON_TELEPORT_COOLDOWN_TICKS) {
            return;
        }

        if (randomTeleport(summoned)) {
            data.putLong("LSE_VoidCurse_Teleport_LastTick", summoned.tickCount);
        }
    }

    private static void spawnCatalystMobs(
            Player player
    ) {
        spawnOneCatalyst(
                player,
                true
        );

        spawnOneCatalyst(
                player,
                true
        );
    }

    private static void trySpawnVoidCatalyst(
            Player player
    ) {
        long now =
                player.level().getGameTime();

        long cooldownUntil =
                VOID_SUMMON_COOLDOWN_UNTIL
                        .getOrDefault(
                                player.getUUID(),
                                Long.MIN_VALUE
                        );

        if (cooldownUntil > now) {
            return;
        }

        if (
                spawnOneCatalyst(
                        player,
                        true
                )
        ) {
            VOID_SUMMON_COOLDOWN_UNTIL.put(
                    player.getUUID(),
                    now
                            + VOID_SUMMON_COOLDOWN_TICKS
            );
        }
    }

    private static boolean spawnOneCatalyst(
            Player player,
            boolean voidCurseSummon
    ) {
        if (
                !(player.level()
                        instanceof ServerLevel level)
        ) {
            return false;
        }

        // Check chunk zombie limit
        String chunkKey = player.chunkPosition().toString();
        int currentZombieCount = CHUNK_ZOMBIE_COUNT.getOrDefault(chunkKey, 0);
        if (currentZombieCount >= MAX_ZOMBIES_PER_CHUNK) {
            return false;
        }

        Zombie zombie =
                EntityType.ZOMBIE.create(level);

        if (zombie == null) {
            return false;
        }

        zombie.moveTo(
                player.getX()
                        + RAND.nextInt(7) - 3,
                player.getY(),
                player.getZ()
                        + RAND.nextInt(7) - 3
        );

        if (voidCurseSummon) {
            applyVoidSummonProperties(
                    player,
                    zombie
            );
        }

        zombie.setTarget(player);

        // Store chunk key on zombie for cleanup tracking
        zombie.getPersistentData().putString("LSE_Cocoon_Chunk_Key", chunkKey);
        
        boolean spawned = level.addFreshEntity(zombie);
        if (spawned) {
            CHUNK_ZOMBIE_COUNT.put(chunkKey, currentZombieCount + 1);
        }
        
        return spawned;
    }

    private static void removeRandomStackableItem(Player player) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.isStackable() && stack.getCount() < stack.getMaxStackSize()) {
                stack.shrink(1);
                inventory.setChanged();
                break;
            }
        }
    }

    private static void applyVoidSummonProperties(
            Player owner,
            Zombie summoned
    ) {

        double healthMultiplier =
                VOID_SUMMON_BASE_HEALTH_MULTIPLIER
                        * CocoonCoreProgress
                        .getMultiplier(owner);

        AttributeInstance maxHealth =
                summoned.getAttribute(
                        Attributes.MAX_HEALTH
                );

        if (maxHealth != null) {
            maxHealth.setBaseValue(
                    maxHealth.getBaseValue()
                            * healthMultiplier
            );

            summoned.setHealth(
                    summoned.getMaxHealth()
            );
        }

        summoned.getPersistentData()
                .putBoolean(
                        NBT_VOID_SUMMONED_MOB,
                        true
                );

        summoned.getPersistentData()
                .putUUID(
                        NBT_VOID_SUMMON_OWNER,
                        owner.getUUID()
                );

        summoned.setCanPickUpLoot(false);
    }

    private static boolean isVoidSummon(
            Entity entity
    ) {
        return entity != null
                && entity.getPersistentData()
                .getBoolean(
                        NBT_VOID_SUMMONED_MOB
                );
    }

    private static Zombie getOwnVoidSummon(
            DamageSource source,
            Player damagedPlayer
    ) {
        if (
                !(source.getEntity()
                        instanceof Zombie summoned)
        ) {
            return null;
        }

        CompoundTag data =
                summoned.getPersistentData();

        if (
                !data.getBoolean(
                        NBT_VOID_SUMMONED_MOB
                ) ||
                !data.hasUUID(
                        NBT_VOID_SUMMON_OWNER
                ) ||
                !data.getUUID(
                        NBT_VOID_SUMMON_OWNER
                ).equals(
                        damagedPlayer.getUUID()
                )
        ) {
            return null;
        }

        return summoned;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onVoidSummonDrops(
            LivingDropsEvent event
    ) {
        if (!isVoidSummon(event.getEntity())) {
            return;
        }

        event.getDrops().clear();
        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onVoidSummonExperience(
            LivingExperienceDropEvent event
    ) {
        if (!isVoidSummon(event.getEntity())) {
            return;
        }

        event.setDroppedExperience(0);
    }

    private static void damageArmor(Player player, int amount) {
        if (amount <= 0) {
            return;
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) {
                continue;
            }

            ItemStack stack = player.getItemBySlot(slot);

            if (
                    stack.isEmpty() ||
                    !stack.isDamageableItem() ||
                    isUnbreakable(stack)
            ) {
                continue;
            }

            stack.hurtAndBreak(
                    amount,
                    player,
                    target -> target.broadcastBreakEvent(slot)
            );
        }
    }

    private static void damageHeldItem(Player player, int amount) {
        ItemStack stack = player.getMainHandItem();

        if (
                stack.isEmpty() ||
                        !stack.isDamageableItem() ||
                        isUnbreakable(stack) ||
                        amount <= 0
        ) {
            return;
        }

        stack.hurtAndBreak(
                amount,
                player,
                target -> target.broadcastBreakEvent(
                        InteractionHand.MAIN_HAND
                )
        );
    }

    private static void sendIntoVoid(Player player) {
        player.teleportTo(
                player.getX(),
                player.level().getMinBuildHeight() + 1,
                player.getZ()
        );

        player.displayClientMessage(
                Component.literal(
                        "坠入虚空吧，虫豸。"
                ).withStyle(ChatFormatting.DARK_PURPLE),
                false
        );
    }
    public static void deleteRandomEquippedArmor(
            Player player
    ) {
        EquipmentSlot[] armorSlots = {
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        };

        ArrayList<EquipmentSlot>
                damageableSlots =
                new ArrayList<>();

        for (EquipmentSlot slot : armorSlots) {
            ItemStack stack =
                    player.getItemBySlot(slot);

            if (
                    stack.isEmpty() ||
                    !stack.isDamageableItem() ||
                    isUnbreakable(stack)
            ) {
                continue;
            }

            int remainingDurability =
                    stack.getMaxDamage()
                            - stack.getDamageValue();

            if (remainingDurability > 1) {
                damageableSlots.add(slot);
            }
        }

        if (damageableSlots.isEmpty()) {
            return;
        }

        EquipmentSlot selectedSlot =
                damageableSlots.get(
                        RAND.nextInt(
                                damageableSlots.size()
                        )
                );

        ItemStack stack =
                player.getItemBySlot(
                        selectedSlot
                );

        int remainingDurability =
                stack.getMaxDamage()
                        - stack.getDamageValue();

        int requestedDamage =
                Math.max(
                        1,
                        Math.round(
                                remainingDurability * 0.20F
                        )
                );

        int maxSafeDamage =
                Math.max(
                        0,
                        remainingDurability - 1
                );

        int actualDamage =
                Math.min(
                        requestedDamage,
                        maxSafeDamage
                );

        if (actualDamage <= 0) {
            return;
        }

        stack.setDamageValue(
                stack.getDamageValue()
                        + actualDamage
        );

        player.level().playSound(
                null,
                player.blockPosition(),
                SoundEvents.ITEM_BREAK,
                SoundSource.PLAYERS,
                0.7F,
                0.85F
        );

        player.displayClientMessage(
                Component.literal(
                        "岩之诅咒损耗了 "
                ).withStyle(
                        ChatFormatting.GOLD
                ).append(
                        stack.getHoverName()
                                .copy()
                                .withStyle(
                                        ChatFormatting.YELLOW
                                )
                ),
                false
        );
    }

    private static boolean isUnbreakable(
            ItemStack stack
    ) {
        return stack.hasTag()
                && stack.getTag()
                        .getBoolean(
                                "Unbreakable"
                        );
    }
}
