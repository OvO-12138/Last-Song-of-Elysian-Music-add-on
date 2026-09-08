package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.dimension.ModDimensions;
import com.he.lastsongofelysian.entity.CorrosionMirrorEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.network.CorrosionCurseSyncPacket;
import com.he.lastsongofelysian.network.ModNetwork;
import com.he.lastsongofelysian.registry.ModEntities;
import com.he.lastsongofelysian.registry.ModItems;
import com.he.lastsongofelysian.util.SignetUpgradeData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class CorrosionCurseEvents {

    private static final String NBT_PLAYER_PERSISTED = "PlayerPersisted";
    private static final String NBT_ACQUIRED = "LSECorrosionCurseAcquired";
    private static final String NBT_CLEARED_BY_ORIGIN = "LSECorrosionCurseClearedByOrigin";
    private static final String NBT_NEXT_WHISPER = "LSECorrosionNextWhisper";
    private static final String NBT_NEXT_ECHO = "LSECorrosionNextEcho";
    private static final String NBT_NEXT_MIRROR_CHECK = "LSECorrosionNextMirrorCheck";
    private static final String NBT_MIRROR_FAILED_CHECKS = "LSECorrosionMirrorFailedChecks";
    private static final String NBT_FRIENDLY_PROJECTION_ENABLED = "LSECorrosionFriendlyProjectionEnabled";
    private static final String NBT_NEXT_PULSE = "LSECorrosionNextPulse";
    private static final String NBT_PULSE_WARNED = "LSECorrosionPulseWarned";
    private static final String NBT_WOUND_STACKS = "LSECorrosionWoundStacks";
    private static final String NBT_WOUND_EXPIRE = "LSECorrosionWoundExpire";
    private static final String NBT_LAST_COMBAT = "LSECorrosionLastCombat";
    private static final String NBT_COLLAPSE_TICKS = "LSECorrosionCollapseTicks";
    private static final String NBT_TRANSCEND_TICKS = "LSECorrosionTranscendTicks";
    private static final String NBT_ACQUIRED_SIGNETS = "LSECorrosionAcquiredSignets";
    private static final String NBT_NEXT_SIGNET_SCAN = "LSECorrosionNextSignetScan";
    private static final String NBT_NEXT_HIT_CORRUPTION = "LSECorrosionNextHitCorruption";
    private static final String NBT_PULSE_HEAL_SEAL = "LSECorrosionPulseHealSeal";

    private static final UUID COLLAPSE_HEALTH_UUID =
            UUID.fromString("649e4f11-2f3a-4fa3-8bd9-d3c3c7782501");

    private static final Map<UUID, EnumMap<EquipmentSlot, DurabilitySnapshot>> DURABILITY =
            new HashMap<>();

    private CorrosionCurseEvents() {
    }

    private static CompoundTag getData(Player player) {
        CompoundTag root = player.getPersistentData();
        if (!root.contains(NBT_PLAYER_PERSISTED, Tag.TAG_COMPOUND)) {
            root.put(NBT_PLAYER_PERSISTED, new CompoundTag());
        }
        return root.getCompound(NBT_PLAYER_PERSISTED);
    }

    public static boolean hasCurse(Player player) {
        CompoundTag data = getData(player);
        return data.getBoolean(NBT_ACQUIRED)
                && !data.getBoolean(NBT_CLEARED_BY_ORIGIN);
    }

    public static boolean isReversed(Player player) {
        return false;
    }

    public static boolean isFriendlyProjectionEnabled(Player player) {
        return false;
    }

    public static void toggleFriendlyProjection(Player player) {
    }

    public static float getCurseStrengthMultiplier(Player player) {
        if (player == null || !hasCurse(player)) return 1.0F;
        return 1.0F + updateAcquiredSignets(player) * 0.10F;
    }

    public static float getCombatSignetMultiplier(Player player) {
        if (player == null || !hasCurse(player)) return 1.0F;
        float ratio = SignetEffects.getCorruptionRatio(player);
        float baseMultiplier;
        if (ratio < 0.50F) {
            baseMultiplier = 1.0F;
        } else if (ratio < 0.75F) {
            baseMultiplier = 0.90F;
        } else if (ratio < 0.90F) {
            baseMultiplier = 0.75F;
        } else {
            baseMultiplier = 0.55F;
        }
        float reduction = (1.0F - baseMultiplier) * getCurseStrengthMultiplier(player);
        return Mth.clamp(1.0F - reduction, 0.10F, 1.0F);
    }

    public static void onWeaponUltimate(Player player) {
        if (player == null || player.level().isClientSide() || !hasCurse(player)) return;
        addCurseCorruption(player, 0.01F);
    }

    private static int updateAcquiredSignets(Player player) {
        CompoundTag data = getData(player);
        int mask = data.getInt(NBT_ACQUIRED_SIGNETS);
        long now = player.level().getGameTime();
        if (now < data.getLong(NBT_NEXT_SIGNET_SCAN)) {
            return Integer.bitCount(mask);
        }
        data.putLong(NBT_NEXT_SIGNET_SCAN, now + 20L);
        SignetUpgradeData.Signet[] signets = SignetUpgradeData.Signet.values();
        for (int i = 0; i < signets.length; i++) {
            if (ownsItem(player, signets[i].item())) {
                mask |= 1 << i;
            }
        }
        if (ownsItem(player, ModItems.SIGNET_OF_EGO.get())) {
            mask |= 1 << signets.length;
        }
        data.putInt(NBT_ACQUIRED_SIGNETS, mask);
        return Integer.bitCount(mask);
    }

    private static boolean ownsItem(Player player, net.minecraft.world.item.Item item) {
        if (CuriosApi.getCuriosHelper()
                .findFirstCurio(player, stack -> stack.is(item))
                .isPresent()) {
            return true;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (player.getInventory().getItem(i).is(item)) return true;
        }
        return false;
    }

    private static void addCurseCorruption(Player player, float percent) {
        SignetEffects.addCorruptionByPercent(
                player,
                percent * getCurseStrengthMultiplier(player)
        );
    }

    public static void syncToClient(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        ModNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> serverPlayer),
                new CorrosionCurseSyncPacket(hasCurse(player), isReversed(player))
        );
    }

    @SubscribeEvent
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        if (event.getTo().equals(ModDimensions.ELYSIAN_REALM)
                && !hasCurse(player)
                && !getData(player).getBoolean(NBT_CLEARED_BY_ORIGIN)) {
            CompoundTag data = getData(player);
            long now = player.level().getGameTime();

            data.putBoolean(NBT_ACQUIRED, true);
            data.putLong(NBT_NEXT_WHISPER, now + 40L);
            data.putLong(NBT_NEXT_ECHO, now + 20L);
            data.putLong(NBT_NEXT_MIRROR_CHECK, now + 40L);
            data.putInt(NBT_MIRROR_FAILED_CHECKS, 0);

            syncToClient(player);
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        syncToClient(event.getEntity());
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncToClient(event.getEntity());
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        CompoundTag oldData = getData(event.getOriginal());
        CompoundTag newData = getData(event.getEntity());

        if (oldData.getBoolean(NBT_ACQUIRED)) {
            newData.putBoolean(NBT_ACQUIRED, true);
        }
        if (oldData.getBoolean(NBT_CLEARED_BY_ORIGIN)) {
            newData.putBoolean(NBT_CLEARED_BY_ORIGIN, true);
        }
        if (oldData.contains(NBT_ACQUIRED_SIGNETS, Tag.TAG_INT)) {
            newData.putInt(NBT_ACQUIRED_SIGNETS, oldData.getInt(NBT_ACQUIRED_SIGNETS));
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            removeOwnedMirrors(player, true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;

        Player player = event.player;
        if (ownsItem(player, ModItems.CORE_OF_ORIGIN.get())) {
            clearCurseWithOrigin(player);
        }
        if (!hasCurse(player)) return;

        long now = player.level().getGameTime();
        float ratio = SignetEffects.getCorruptionRatio(player);
        boolean reversed = isReversed(player);

        if (player.tickCount % 20 == 0
                && getData(player).getLong(NBT_LAST_COMBAT) > 0L
                && now - getData(player).getLong(NBT_LAST_COMBAT) <= 300L) {
            addCurseCorruption(player, 0.0012F);
        }

        tickWhispers(player, now, ratio, reversed);
        tickClearVision(player, reversed);
        tickBodyBalance(player, ratio, reversed);
        tickWounds(player, now, reversed);
        tickEnemyResonance(player, ratio, reversed);
        tickMirrors(player, now, ratio, reversed);
        tickDurability(player, ratio, reversed);
        tickPulse(player, now, ratio, reversed);
        tickOverflowStates(player, ratio, reversed);

        if (player.tickCount % 20 == 0) {
            syncToClient(player);
        }
    }

    private static void clearCurseWithOrigin(Player player) {
        CompoundTag data = getData(player);
        if (data.getBoolean(NBT_CLEARED_BY_ORIGIN)) return;

        data.putBoolean(NBT_CLEARED_BY_ORIGIN, true);
        data.remove(NBT_ACQUIRED);
        data.remove(NBT_NEXT_WHISPER);
        data.remove(NBT_NEXT_ECHO);
        data.remove(NBT_NEXT_MIRROR_CHECK);
        data.remove(NBT_MIRROR_FAILED_CHECKS);
        data.remove(NBT_FRIENDLY_PROJECTION_ENABLED);
        data.remove(NBT_NEXT_PULSE);
        data.remove(NBT_PULSE_WARNED);
        data.remove(NBT_WOUND_STACKS);
        data.remove(NBT_WOUND_EXPIRE);
        data.remove(NBT_LAST_COMBAT);
        data.remove(NBT_COLLAPSE_TICKS);
        data.remove(NBT_TRANSCEND_TICKS);
        data.remove(NBT_NEXT_SIGNET_SCAN);
        data.remove(NBT_NEXT_HIT_CORRUPTION);
        data.remove(NBT_PULSE_HEAL_SEAL);
        DURABILITY.remove(player.getUUID());
        removeOwnedMirrors(player, false);
        removeCollapseHealthModifier(player);
        syncToClient(player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHeal(LivingHealEvent event) {
        if (!(event.getEntity() instanceof Player player) || !hasCurse(player)) return;

        float ratio = SignetEffects.getCorruptionRatio(player);
        int wounds = getData(player).getInt(NBT_WOUND_STACKS);

        float strength = getCurseStrengthMultiplier(player);
        float reduction = (0.10F + ratio * 0.60F + wounds * 0.05F) * strength;
        if (player.level().getGameTime() < getData(player).getLong(NBT_PULSE_HEAL_SEAL)) {
            reduction += 0.50F * strength;
        }
        event.setAmount(event.getAmount() * Math.max(0.05F, 1.0F - reduction));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        if (event.getSource().getEntity() instanceof Player attacker && hasCurse(attacker)) {
            applyBackflowAndSkillEffect(attacker, event);
            if (event.getEntity() instanceof Enemy) {
                applyEnemyResistance(attacker, event);
            }
        }

        if (event.getEntity() instanceof Player victim && hasCurse(victim)) {
            applyCorruptWound(victim);
            getData(victim).putLong(NBT_LAST_COMBAT, victim.level().getGameTime());
            if (event.getSource().getEntity() instanceof Enemy) {
                applyEnemyDamageBonus(victim, event);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamage(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player attacker && hasCurse(attacker)) {
            CompoundTag data = getData(attacker);
            long now = attacker.level().getGameTime();
            data.putLong(NBT_LAST_COMBAT, now);
            if (now >= data.getLong(NBT_NEXT_HIT_CORRUPTION)) {
                data.putLong(NBT_NEXT_HIT_CORRUPTION, now + 5L);
                addCurseCorruption(attacker, 0.0005F);
            }
        }
    }

    @SubscribeEvent
    public static void onItemUseFinished(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)
                || player.level().isClientSide()
                || !hasCurse(player)) {
            return;
        }

        float ratio = SignetEffects.getCorruptionRatio(player);
        float chance = Math.min(
                1.0F,
                (0.05F + ratio * 0.10F) * getCurseStrengthMultiplier(player)
        );

        if (player.getRandom().nextFloat() >= chance) return;

        addCurseCorruption(player, 0.01F);
        if (!event.getItem().isEmpty()) {
            player.getCooldowns().addCooldown(event.getItem().getItem(), 40);
        }
    }

    @SubscribeEvent
    public static void onMirrorDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof CorrosionMirrorEntity mirror)) return;
        if (mirror.isFriendly()) return;
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if (!mirror.belongsTo(player)) return;

        SignetEffects.reduceCorruptionByPercent(player, 0.05F);
    }

    public static boolean handleFullCorruption(Player player) {
        if (!hasCurse(player)) return false;

        CompoundTag data = getData(player);

        int ticks = data.getInt(NBT_COLLAPSE_TICKS) + 1;
        data.putInt(NBT_COLLAPSE_TICKS, ticks);
        float strength = getCurseStrengthMultiplier(player);

        if (ticks % 20 == 0) {
            float reduction = Math.min(0.50F, ticks / 800.0F * 0.50F * strength);
            setCollapseHealthModifier(player, reduction);
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1, false, true, true));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, false, true, true));
        }

        if (ticks % 40 == 0) {
            player.hurt(player.damageSources().magic(), 4.0F * strength);
        }

        return true;
    }

    private static void applyBackflowAndSkillEffect(Player player, LivingHurtEvent event) {
        float ratio = SignetEffects.getCorruptionRatio(player);
        float strength = getCurseStrengthMultiplier(player);

        float backflowChance = Math.min(1.0F, (0.05F + ratio * 0.08F) * strength);
        if (player.getRandom().nextFloat() < backflowChance) {
            addCurseCorruption(player, 0.01F);
        }

        float confusionChance = Math.min(1.0F, (0.05F + ratio * 0.10F) * strength);
        if (player.getRandom().nextFloat() < confusionChance) {
            ItemStack weapon = player.getMainHandItem();

            event.setAmount(event.getAmount() * Math.max(0.10F, 1.0F - 0.30F * strength));
            if (!weapon.isEmpty()) {
                player.getCooldowns().addCooldown(weapon.getItem(), Math.round(50 * strength));
                if (!weapon.isDamageableItem()
                        && player.getRandom().nextFloat() < Math.min(1.0F, 0.25F * strength)) {
                    addCurseCorruption(player, 0.005F);
                }
            }
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, Math.round(50 * strength), 0, false, true, true));
        }
    }

    private static void applyEnemyResistance(Player player, LivingHurtEvent event) {
        float ratio = SignetEffects.getCorruptionRatio(player);
        if (ratio < 0.75F) return;
        float reduction = (ratio >= 0.90F ? 0.30F : 0.20F)
                * getCurseStrengthMultiplier(player);
        event.setAmount(event.getAmount() * Math.max(0.05F, 1.0F - reduction));
    }

    private static void applyEnemyDamageBonus(Player player, LivingHurtEvent event) {
        float ratio = SignetEffects.getCorruptionRatio(player);
        if (ratio < 0.75F) return;
        float bonus = (ratio >= 0.90F ? 0.25F : 0.15F)
                * getCurseStrengthMultiplier(player);
        event.setAmount(event.getAmount() * (1.0F + bonus));
    }

    private static void applyCorruptWound(Player player) {
        CompoundTag data = getData(player);
        long now = player.level().getGameTime();
        int stacks = Math.min(5, data.getInt(NBT_WOUND_STACKS) + 1);

        data.putInt(NBT_WOUND_STACKS, stacks);
        data.putLong(
                NBT_WOUND_EXPIRE,
                now + Math.round(200.0F * getCurseStrengthMultiplier(player))
        );

    }

    private static void tickWhispers(Player player, long now, float ratio, boolean reversed) {
        CompoundTag data = getData(player);

        if (reversed) {
            long nextEcho = data.getLong(NBT_NEXT_ECHO);
            if (nextEcho <= 0L) {
                data.putLong(NBT_NEXT_ECHO, now + 20L);
                return;
            }
            if (now < nextEcho) return;

            List<LivingEntity> dangers = player.level().getEntitiesOfClass(
                    LivingEntity.class,
                    player.getBoundingBox().inflate(20.0D),
                    entity -> entity instanceof Enemy && entity.isAlive()
            );

            if (dangers.isEmpty()) {
                data.putLong(NBT_NEXT_ECHO, now + 20L);
                return;
            }

            data.putLong(NBT_NEXT_ECHO, now + 40L);
            for (LivingEntity danger : dangers) {
                danger.addEffect(new MobEffectInstance(
                        MobEffects.GLOWING,
                        50,
                        0,
                        false,
                        false,
                        true
                ));
            }

            player.displayClientMessage(
                    Component.literal("附近检测到 " + dangers.size() + " 个敌对目标")
                            .withStyle(ChatFormatting.AQUA),
                    true
            );
            return;
        }

        long nextWhisper = data.getLong(NBT_NEXT_WHISPER);
        if (nextWhisper <= 0L) {
            data.putLong(NBT_NEXT_WHISPER, now + 40L + player.getRandom().nextInt(61));
            return;
        }
        if (now < nextWhisper) return;

        int minimum = Math.max(80, Math.round(180.0F - ratio * 100.0F));
        data.putLong(NBT_NEXT_WHISPER, now + 200L + player.getRandom().nextInt(21));

        SoundEvent sound = switch (player.getRandom().nextInt(4)) {
            case 0 -> SoundEvents.ZOMBIE_AMBIENT;
            case 1 -> SoundEvents.CREEPER_PRIMED;
            case 2 -> SoundEvents.GENERIC_EXPLODE;
            default -> SoundEvents.STONE_STEP;
        };

        player.playNotifySound(
                sound,
                SoundSource.AMBIENT,
                0.75F + ratio * 0.25F,
                0.65F + player.getRandom().nextFloat() * 0.70F
        );
        player.displayClientMessage(
                Component.literal("……")
                        .withStyle(ChatFormatting.DARK_PURPLE),
                true
        );
    }

    private static void tickClearVision(Player player, boolean reversed) {
        if (!reversed || player.tickCount % 20 != 0) return;

        player.removeEffect(MobEffects.BLINDNESS);
        player.removeEffect(MobEffects.DARKNESS);
        player.removeEffect(MobEffects.CONFUSION);
    }

    private static void tickBodyBalance(Player player, float ratio, boolean reversed) {
        if (reversed) {
            if (player.tickCount % 40 == 0) {
                int amplifier = ratio >= 0.75F ? 1 : 0;
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, amplifier, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 80, amplifier, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.JUMP, 80, amplifier, false, false, true));
                player.removeEffect(MobEffects.BLINDNESS);
                player.removeEffect(MobEffects.DARKNESS);
                player.removeEffect(MobEffects.CONFUSION);
            }
            return;
        }

        if (player.tickCount % 400 != 0) return;

        int duration = Math.round(
                (100.0F + ratio * 100.0F) * getCurseStrengthMultiplier(player)
        );
        switch (player.getRandom().nextInt(3)) {
            case 0 -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 1, false, true, true));
            case 1 -> player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, duration, 1, false, true, true));
            default -> player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, 0, false, true, true));
        }
    }

    private static void tickWounds(Player player, long now, boolean reversed) {
        CompoundTag data = getData(player);
        int stacks = data.getInt(NBT_WOUND_STACKS);
        if (stacks <= 0) return;

        long expire = data.getLong(NBT_WOUND_EXPIRE);
        if (now < expire) return;

        stacks--;
        data.putInt(NBT_WOUND_STACKS, stacks);
        data.putLong(
                NBT_WOUND_EXPIRE,
                now + Math.round(100.0F * getCurseStrengthMultiplier(player))
        );
    }

    private static void tickEnemyResonance(Player player, float ratio, boolean reversed) {
        if (player.tickCount % 40 != 0) return;

        float strength = getCurseStrengthMultiplier(player);
        int amplifier = ratio >= 0.75F ? 1 : 0;
        int duration = Math.round(60.0F * strength);
        List<LivingEntity> enemies = player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(24.0D),
                entity -> entity instanceof Enemy && entity.isAlive()
        );

        for (LivingEntity enemy : enemies) {
            if (reversed) {
                enemy.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, amplifier, false, true, true));
                enemy.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, amplifier, false, true, true));
                enemy.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, amplifier, false, true, true));
            } else {
                enemy.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, amplifier, false, true, true));
                enemy.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, amplifier, false, true, true));
                enemy.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, amplifier, false, true, true));
            }
        }
    }

    private static void tickMirrors(
            Player player,
            long now,
            float ratio,
            boolean reversed
    ) {
        if (player.tickCount % 20 == 0) {
            removeLegacyPlaceholderMirrors(player);

            List<CorrosionMirrorEntity> mirrors =
                    player.level().getEntitiesOfClass(
                            CorrosionMirrorEntity.class,
                            player.getBoundingBox().inflate(128.0D),
                            mirror -> mirror.belongsTo(player)
                    );

            for (CorrosionMirrorEntity mirror : mirrors) {
                if (mirror.isExpired(now)) {
                    mirror.discard();
                }
            }
        }

        CompoundTag data = getData(player);

        if (reversed) {
            if (player.tickCount % 20 == 0) {
                removeOwnedMirrorsOfType(player, false);
            }

            boolean enabled = data.getBoolean(NBT_FRIENDLY_PROJECTION_ENABLED);

            if (!enabled) {
                if (player.tickCount % 20 == 0) {
                    removeOwnedMirrors(player, true);
                }
                return;
            }

            if (!hasOwnedMirrorNearby(player, true)) {
                spawnMirror(player, now, true, true);
            }
            return;
        }

        if (data.getBoolean(NBT_FRIENDLY_PROJECTION_ENABLED)) {
            data.putBoolean(NBT_FRIENDLY_PROJECTION_ENABLED, false);
        }
        if (player.tickCount % 20 == 0) {
            removeOwnedMirrors(player, true);
        }

        long lastCombat = data.getLong(NBT_LAST_COMBAT);
        long nextCheck = data.getLong(NBT_NEXT_MIRROR_CHECK);

        if (lastCombat <= 0L || now - lastCombat > 300L) {
            data.putInt(NBT_MIRROR_FAILED_CHECKS, 0);
            if (nextCheck <= now) {
                data.putLong(NBT_NEXT_MIRROR_CHECK, now + 40L);
            }
            return;
        }

        if (hasOwnedMirrorNearby(player, false)) {
            data.putInt(NBT_MIRROR_FAILED_CHECKS, 0);
            data.putLong(NBT_NEXT_MIRROR_CHECK, now + 40L);
            return;
        }

        if (nextCheck <= 0L) {
            data.putLong(NBT_NEXT_MIRROR_CHECK, now + 40L);
            return;
        }
        if (now < nextCheck) return;

        data.putLong(NBT_NEXT_MIRROR_CHECK, now + 100L);
        int failedChecks = data.getInt(NBT_MIRROR_FAILED_CHECKS);
        float chance = Math.min(
                1.0F,
                (0.45F + ratio * 0.45F) * getCurseStrengthMultiplier(player)
        );
        boolean shouldSpawn = failedChecks >= 2
                || player.getRandom().nextFloat() < chance;

        if (!shouldSpawn) {
            data.putInt(NBT_MIRROR_FAILED_CHECKS, failedChecks + 1);
            return;
        }

        data.putInt(NBT_MIRROR_FAILED_CHECKS, 0);
        data.putLong(NBT_NEXT_MIRROR_CHECK, now + 6000L);
        spawnMirror(player, now, false, false);
    }

    private static void removeLegacyPlaceholderMirrors(Player player) {
        List<Mob> legacyMirrors = player.level().getEntitiesOfClass(
                Mob.class,
                player.getBoundingBox().inflate(96.0D),
                mob -> !(mob instanceof CorrosionMirrorEntity)
                        && mob.getPersistentData().getBoolean(
                        CorrosionMirrorEntity.NBT_MIRROR_MARKER
                )
                        && mob.getPersistentData().hasUUID(
                        CorrosionMirrorEntity.NBT_MIRROR_OWNER
                )
                        && player.getUUID().equals(
                        mob.getPersistentData().getUUID(
                                CorrosionMirrorEntity.NBT_MIRROR_OWNER
                        )
                )
        );

        for (Mob legacyMirror : legacyMirrors) {
            legacyMirror.discard();
        }
    }

    private static boolean hasOwnedMirrorNearby(
            Player player,
            boolean friendly
    ) {
        return !player.level().getEntitiesOfClass(
                CorrosionMirrorEntity.class,
                player.getBoundingBox().inflate(128.0D),
                mirror -> mirror.belongsTo(player)
                        && mirror.isFriendly() == friendly
                        && mirror.isAlive()
        ).isEmpty();
    }

    private static void removeOwnedMirrors(
            Player player,
            boolean friendlyOnly
    ) {
        List<CorrosionMirrorEntity> mirrors = player.level().getEntitiesOfClass(
                CorrosionMirrorEntity.class,
                player.getBoundingBox().inflate(256.0D),
                mirror -> mirror.belongsTo(player)
                        && (!friendlyOnly || mirror.isFriendly())
        );

        for (CorrosionMirrorEntity mirror : mirrors) {
            mirror.discard();
        }
    }

    private static void removeOwnedMirrorsOfType(
            Player player,
            boolean friendly
    ) {
        List<CorrosionMirrorEntity> mirrors = player.level().getEntitiesOfClass(
                CorrosionMirrorEntity.class,
                player.getBoundingBox().inflate(256.0D),
                mirror -> mirror.belongsTo(player)
                        && mirror.isFriendly() == friendly
        );

        for (CorrosionMirrorEntity mirror : mirrors) {
            mirror.discard();
        }
    }

    private static void spawnMirror(
            Player player,
            long now,
            boolean friendly,
            boolean persistentProjection
    ) {
        CorrosionMirrorEntity mirror =
                ModEntities.CORROSION_MIRROR.get().create(player.level());

        if (mirror == null) return;

        double angle = player.getRandom().nextDouble() * Math.PI * 2.0D;
        double radius = friendly ? 1.75D : 2.50D;

        mirror.moveTo(
                player.getX() + Math.cos(angle) * radius,
                player.getY(),
                player.getZ() + Math.sin(angle) * radius,
                player.getYRot(),
                0.0F
        );
        mirror.configureFromPlayer(
                player,
                friendly,
                persistentProjection ? 0L : now + 600L
        );

        player.level().addFreshEntity(mirror);

        if (!friendly) {
            mirror.setTarget(player);
        }

        if (!persistentProjection) {
            announceMirrorSpawn(player, friendly);
        }
    }

    private static void announceMirrorSpawn(Player player, boolean friendly) {
        player.playNotifySound(
                SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS,
                1.0F,
                friendly ? 1.35F : 0.70F
        );
        player.displayClientMessage(
                Component.literal(friendly
                                ? "镜像已出现"
                                : "侵蚀镜像已出现")
                        .withStyle(friendly ? ChatFormatting.AQUA : ChatFormatting.DARK_PURPLE),
                true
        );

        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    friendly ? ParticleTypes.END_ROD : ParticleTypes.REVERSE_PORTAL,
                    player.getX(),
                    player.getY() + 1.0D,
                    player.getZ(),
                    40,
                    1.5D,
                    1.0D,
                    1.5D,
                    0.05D
            );
        }
    }

    private static void tickDurability(Player player, float ratio, boolean reversed) {
        EnumMap<EquipmentSlot, DurabilitySnapshot> snapshots = DURABILITY.computeIfAbsent(
                player.getUUID(),
                ignored -> new EnumMap<>(EquipmentSlot.class)
        );

        checkDurability(player, EquipmentSlot.MAINHAND, player.getMainHandItem(), ratio, reversed, snapshots);
        checkDurability(player, EquipmentSlot.OFFHAND, player.getOffhandItem(), ratio, reversed, snapshots);
        checkDurability(player, EquipmentSlot.HEAD, player.getItemBySlot(EquipmentSlot.HEAD), ratio, reversed, snapshots);
        checkDurability(player, EquipmentSlot.CHEST, player.getItemBySlot(EquipmentSlot.CHEST), ratio, reversed, snapshots);
        checkDurability(player, EquipmentSlot.LEGS, player.getItemBySlot(EquipmentSlot.LEGS), ratio, reversed, snapshots);
        checkDurability(player, EquipmentSlot.FEET, player.getItemBySlot(EquipmentSlot.FEET), ratio, reversed, snapshots);
    }

    private static void checkDurability(
            Player player,
            EquipmentSlot slot,
            ItemStack stack,
            float ratio,
            boolean reversed,
            EnumMap<EquipmentSlot, DurabilitySnapshot> snapshots
    ) {
        ResourceLocation id = stack.isEmpty() ? null : ForgeRegistries.ITEMS.getKey(stack.getItem());
        int currentDamage = stack.isDamageableItem() ? stack.getDamageValue() : 0;
        DurabilitySnapshot previous = snapshots.get(slot);

        if (previous != null
                && id != null
                && id.equals(previous.itemId)
                && currentDamage > previous.damage
                && stack.isDamageableItem()) {
            int delta = currentDamage - previous.damage;
            float chance = Math.min(
                    1.0F,
                    (0.05F + ratio * 0.20F) * getCurseStrengthMultiplier(player)
            );

            if (player.getRandom().nextFloat() < chance) {
                stack.hurtAndBreak(delta, player, living -> living.broadcastBreakEvent(slot));
            }
        }

        snapshots.put(slot, new DurabilitySnapshot(id, stack.isDamageableItem() ? stack.getDamageValue() : 0));
    }

    private static void tickPulse(Player player, long now, float ratio, boolean reversed) {
        CompoundTag data = getData(player);
        long next = data.getLong(NBT_NEXT_PULSE);
        boolean inCombat = data.getLong(NBT_LAST_COMBAT) > 0L
                && now - data.getLong(NBT_LAST_COMBAT) <= 300L;
        long interval = inCombat ? 900L : 1200L;

        if (next <= 0L) {
            data.putLong(NBT_NEXT_PULSE, now + interval);
            data.putBoolean(NBT_PULSE_WARNED, false);
            return;
        }

        if (!data.getBoolean(NBT_PULSE_WARNED) && now >= next - 60L) {
            data.putBoolean(NBT_PULSE_WARNED, true);
            player.displayClientMessage(
                    Component.literal(reversed ? "净化脉冲正在聚集" : "终末脉冲正在聚集")
                            .withStyle(reversed ? ChatFormatting.AQUA : ChatFormatting.DARK_PURPLE),
                    true
            );
            player.playNotifySound(SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 0.8F, reversed ? 1.4F : 0.6F);
        }

        if (now < next) return;

        data.putLong(NBT_NEXT_PULSE, now + interval);
        data.putBoolean(NBT_PULSE_WARNED, false);
        triggerTerminalPulse(player, ratio);
    }

    private static void triggerTerminalPulse(Player player, float ratio) {
        float strength = getCurseStrengthMultiplier(player);
        player.hurt(
                player.damageSources().magic(),
                (player.getMaxHealth() * 0.06F + 2.0F) * strength
        );
        getData(player).putLong(NBT_PULSE_HEAL_SEAL, player.level().getGameTime() + 100L);

        switch (player.getRandom().nextInt(3)) {
            case 0 -> player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, Math.round(100 * strength), 0));
            case 1 -> player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, Math.round(100 * strength), 1));
            default -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Math.round(100 * strength), 1));
        }

        for (LivingEntity enemy : player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(16.0D),
                entity -> entity instanceof Enemy && entity.isAlive()
        )) {
            int duration = Math.round(200.0F * strength);
            enemy.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, ratio >= 0.75F ? 1 : 0));
            enemy.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, ratio >= 0.75F ? 1 : 0));
        }

        playBurst(player, false);
    }

    private static void triggerPurificationPulse(Player player, float ratio) {
        for (LivingEntity enemy : player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(16.0D),
                entity -> entity instanceof Enemy && entity.isAlive()
        )) {
            enemy.hurt(player.damageSources().playerAttack(player), 6.0F + ratio * 8.0F);
        }

        List<MobEffect> harmful = new ArrayList<>();
        for (MobEffectInstance instance : player.getActiveEffects()) {
            if (instance.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                harmful.add(instance.getEffect());
            }
        }
        for (MobEffect effect : harmful) {
            player.removeEffect(effect);
        }

        SignetEffects.reduceCorruptionByPercent(player, 0.05F);
        playBurst(player, true);
    }

    private static void playBurst(Player player, boolean reversed) {
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        serverLevel.sendParticles(
                reversed ? ParticleTypes.END_ROD : ParticleTypes.REVERSE_PORTAL,
                player.getX(),
                player.getY() + 1.0D,
                player.getZ(),
                80,
                2.0D,
                1.5D,
                2.0D,
                0.08D
        );
        player.playNotifySound(
                reversed ? SoundEvents.BEACON_ACTIVATE : SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(),
                SoundSource.PLAYERS,
                1.0F,
                reversed ? 1.3F : 0.7F
        );
    }

    private static void tickOverflowStates(Player player, float ratio, boolean reversed) {
        CompoundTag data = getData(player);

        int transcend = data.getInt(NBT_TRANSCEND_TICKS);
        if (transcend > 0) {
            transcend--;
            data.putInt(NBT_TRANSCEND_TICKS, transcend);
            if (transcend <= 0) {
                SignetEffects.reduceCorruptionByPercent(player, 0.40F);
            }
        }

        if (ratio < 0.999F || reversed) {
            data.putInt(NBT_COLLAPSE_TICKS, 0);
            removeCollapseHealthModifier(player);
        }
    }

    private static void setCollapseHealthModifier(Player player, float reduction) {
        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
        if (health == null) return;

        health.removeModifier(COLLAPSE_HEALTH_UUID);
        if (reduction > 0.0F) {
            health.addTransientModifier(new AttributeModifier(
                    COLLAPSE_HEALTH_UUID,
                    "corrosion_self_collapse",
                    -Mth.clamp(reduction, 0.0F, 0.50F),
                    AttributeModifier.Operation.MULTIPLY_TOTAL
            ));
        }
        player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));
    }

    private static void removeCollapseHealthModifier(Player player) {
        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
        if (health != null) {
            health.removeModifier(COLLAPSE_HEALTH_UUID);
        }
        player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));
    }

    private static final class DurabilitySnapshot {
        private final ResourceLocation itemId;
        private final int damage;

        private DurabilitySnapshot(ResourceLocation itemId, int damage) {
            this.itemId = itemId;
            this.damage = damage;
        }
    }
}
