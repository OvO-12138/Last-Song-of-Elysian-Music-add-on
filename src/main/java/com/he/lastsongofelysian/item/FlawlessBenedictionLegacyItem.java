package com.he.lastsongofelysian.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.he.lastsongofelysian.event.FlawlessBenedictionLegacyEvents;
import com.he.lastsongofelysian.event.FlawlessVisualEffects;
import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class FlawlessBenedictionLegacyItem extends BowItem {

    public static final double BASE_ATTACK_DAMAGE = 100.0D;

    private static final UUID BASE_ATTACK_DAMAGE_MODIFIER_UUID =
            UUID.fromString(
                    "18ab0714-8e5f-4e3b-98d7-29c93a1b0d14"
            );

    private static final Multimap<Attribute, AttributeModifier>
            DEFAULT_ATTRIBUTE_MODIFIERS;

    static {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder =
                ImmutableMultimap.builder();

        builder.put(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        BASE_ATTACK_DAMAGE_MODIFIER_UUID,
                        "flawless_benediction_legacy_base_attack",
                        BASE_ATTACK_DAMAGE - 1.0D,
                        AttributeModifier.Operation.ADDITION
                )
        );

        DEFAULT_ATTRIBUTE_MODIFIERS = builder.build();
    }

    public static final String NBT_DOMAIN_UNTIL =
            "FlawlessDomainUntil";

    public static final String NBT_ARROW_MARKER =
            "FlawlessChargedArrow";

    private static final int NORMAL_FULL_CHARGE_TICKS = 20;

    private static final Style PINK_STYLE =
            Style.EMPTY.withColor(
                    TextColor.fromRgb(0xFF69B4)
            );

    private static final Style LIGHT_PINK_STYLE =
            Style.EMPTY.withColor(
                    TextColor.fromRgb(0xFFB6C1)
            );

    private static final Style PINK_BOLD_STYLE =
            PINK_STYLE.withBold(true);

    public FlawlessBenedictionLegacyItem(Properties properties) {
        super(properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier>
            getDefaultAttributeModifiers(
            EquipmentSlot slot
    ) {
        if (slot == EquipmentSlot.MAINHAND) {
            return DEFAULT_ATTRIBUTE_MODIFIERS;
        }

        return super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);

        player.startUsingItem(hand);

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(
            ItemStack stack,
            Level level,
            LivingEntity livingEntity,
            int timeLeft
    ) {
        if (!(livingEntity instanceof Player player)) {
            return;
        }

        int usedTicks = getUseDuration(stack) - timeLeft;
        float power = getChargePower(
                stack,
                level,
                player,
                usedTicks
        );

        if (power < 0.10F) {
            return;
        }

        if (!level.isClientSide()) {
            Arrow arrow = new Arrow(level, player);

            arrow.setPos(
                    player.getX(),
                    player.getEyeY() - 0.10D,
                    player.getZ()
            );

            arrow.shootFromRotation(
                    player,
                    player.getXRot(),
                    player.getYRot(),
                    0.0F,
                    power * 3.25F,
                    0.25F
            );

            double baseAttack = Math.max(
                    1.0D,
                    player.getAttributeValue(
                            Attributes.ATTACK_DAMAGE
                    )
            );

            arrow.setBaseDamage(
                    4.0D + baseAttack * power
            );

            int powerLevel =
                    EnchantmentHelper.getItemEnchantmentLevel(
                            Enchantments.POWER_ARROWS,
                            stack
                    );

            if (powerLevel > 0) {
                arrow.setBaseDamage(
                        arrow.getBaseDamage()
                                + powerLevel * 0.5D
                                + 0.5D
                );
            }

            int punchLevel =
                    EnchantmentHelper.getItemEnchantmentLevel(
                            Enchantments.PUNCH_ARROWS,
                            stack
                    );

            if (punchLevel > 0) {
                arrow.setKnockback(punchLevel);
            }

            if (
                    EnchantmentHelper.getItemEnchantmentLevel(
                            Enchantments.FLAMING_ARROWS,
                            stack
                    ) > 0
            ) {
                arrow.setSecondsOnFire(100);
            }

            arrow.setCritArrow(power >= 1.0F);
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;

            arrow.getPersistentData().putBoolean(
                    NBT_ARROW_MARKER,
                    true
            );

            level.addFreshEntity(arrow);
            FlawlessVisualEffects.spawnArrowTrail(
                    arrow,
                    false
            );

            level.playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.ARROW_SHOOT,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F / (
                            level.getRandom().nextFloat() * 0.4F
                                    + 1.2F
                    ) + power * 0.4F
            );
        }

        player.awardStat(Stats.ITEM_USED.get(this));
    }

    public static float getChargePower(
            ItemStack stack,
            @Nullable Level level,
            @Nullable LivingEntity entity,
            int usedTicks
    ) {
        float effectiveTicks = usedTicks;

        if (isDomainActiveOnStack(stack, level)) {
            effectiveTicks *= 3.0F;
        }

        float progress =
                effectiveTicks / NORMAL_FULL_CHARGE_TICKS;

        progress =
                (progress * progress + progress * 2.0F)
                        / 3.0F;

        return Math.min(progress, 1.0F);
    }

    public static float getVisualPull(
            ItemStack stack,
            @Nullable Level level,
            @Nullable LivingEntity entity,
            int usedTicks
    ) {
        float effectiveTicks = usedTicks;

        if (isDomainActiveOnStack(stack, level)) {
            effectiveTicks *= 3.0F;
        }

        return effectiveTicks / 20.0F;
    }

    public static boolean isDomainActiveOnStack(
            ItemStack stack,
            @Nullable Level level
    ) {
        if (level == null || !stack.hasTag()) {
            return false;
        }

        return stack.getTag()
                .getLong(NBT_DOMAIN_UNTIL)
                > level.getGameTime();
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 30;
    }

    @Override
    public void inventoryTick(
            ItemStack stack,
            Level level,
            net.minecraft.world.entity.Entity entity,
            int slotId,
            boolean isSelected
    ) {
        super.inventoryTick(
                stack,
                level,
                entity,
                slotId,
                isSelected
        );

        if (level.isClientSide()) {
            return;
        }

        ensureMaxEnchantments(stack);

        if (
                stack.hasTag() &&
                stack.getTag()
                        .getLong(NBT_DOMAIN_UNTIL)
                        <= level.getGameTime()
        ) {
            stack.getTag().remove(NBT_DOMAIN_UNTIL);
        }
    }

    private static void ensureMaxEnchantments(ItemStack stack) {
        addMaxEnchantment(
                stack,
                Enchantments.POWER_ARROWS
        );
        addMaxEnchantment(
                stack,
                Enchantments.PUNCH_ARROWS
        );
        addMaxEnchantment(
                stack,
                Enchantments.FLAMING_ARROWS
        );
        addMaxEnchantment(
                stack,
                Enchantments.INFINITY_ARROWS
        );
        addMaxEnchantment(
                stack,
                Enchantments.UNBREAKING
        );
    }

    private static void addMaxEnchantment(
            ItemStack stack,
            net.minecraft.world.item.enchantment.Enchantment enchantment
    ) {
        int current =
                EnchantmentHelper.getItemEnchantmentLevel(
                        enchantment,
                        stack
                );

        int max = enchantment.getMaxLevel();

        if (current < max) {
            stack.enchant(enchantment, max);
        }
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level level,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        if (!Screen.hasShiftDown()) {
            addIntroduction(tooltip);
            tooltip.add(Component.empty());
            addLightPinkLine(tooltip, "Shift查看");
            return;
        }

        addAbilityTooltips(tooltip);
    }

    private static void addIntroduction(
            List<Component> tooltip
    ) {
        addLightPinkLine(
                tooltip,
                "少女是如此深爱着这个世界——跨越终局，开辟未来"
        );

        addLightPinkLine(
                tooltip,
                "她将她的善，她的美，她的光，赠予那些新生的人类，为陌生的旅人带来了这份真我的希望"
        );

        tooltip.add(Component.empty());

        addPinkLine(tooltip, "「而今，归去之时已至」");
        addPinkLine(tooltip, "「就此告别吧，美丽的世界」");

        tooltip.add(Component.empty());

        addPinkLine(tooltip, "「此后，将有群星闪耀」");
        addPinkLine(tooltip, "「因为我如今来过」");
        addPinkLine(tooltip, "「此后，将有百花绽放」");
        addPinkLine(tooltip, "「因为我从未离去」");
        addPinkLine(
                tooltip,
                "「请将我的箭，我的花，与我的爱，织成新生的种子，带向那枯萎的大地」"
        );
        addPinkLine(
                tooltip,
                "「然后…便让它开出永恒而无暇的」"
        );
        addPinkLine(tooltip, "「人性之华吧」");
    }

    private static void addAbilityTooltips(
            List<Component> tooltip
    ) {
        addAbility(
                tooltip,
                "绚丽，如繁花初绽",
                "每5秒在周围草地上催生花朵"

        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "青春，如萌芽新生",
                "周围的农作物生长速度提升1314%"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "圣洁，如白鸟齐舞",
                "黄金·真我之祝福飞行时间提升至永久"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "甘洌，如清泉落花",
                "真我之境·无暇回归蓄力期间，可以使角色在水上行走并获得520%加速效果"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "嫣然，如少女回眸",
                "真我之境·无暇回归蓄力期间，可以使角色在岩浆上行走并获得131.4%加速效果"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "沉醉，如烛光余香",
                "删除真我之境·无暇回归的耐久值"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "幽雅，如淡云蔽月",
                "携带真我之境·无暇回归时，生物不会主动远离玩家"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "恬静，如夜露微明",
                "主手持有真我之境·无暇回归时，敌对生物不会主动攻击玩家"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "缱绻，如薰风卷雪",
                "武器技—天气之子：处于天气之子形态下，使用武器技可切换天气"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "光辉，如朝阳初升",
                "武器技—日月的女儿：处于日月的女儿形态下，使用武器技可切换时间"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "闪耀，如明星天坠",
                "真我之境·无暇回归获得全部满级附魔"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "隽永，如牧歌长鸣",
                "实体单位受到来自真我之境·无暇回归的蓄力攻击时，获得一层真我之绽，上限三层，获得5点能量",
                "每层真我之绽增加1733.3%易伤"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "永恒，如往世折枝",
                "真我之绽三层时，可以释放武器技—无暇绽放：处于无暇形态下，清空所有真我之绽，造成13140%基础攻击力的扩散伤害，并冻结该单位，获得25点能量"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "无暇，如神佑乐土",
                "终结技—无暇乐土：展开可跟随的领域，持续20秒，领域中其他的敌方单位造成的伤害降低52%，每秒损失10000血量，领域内有花瓣从天而降，每3秒冻结1秒并增加一层真我之绽，真我之境·无暇回归蓄力时间减少200%，可提前关闭，造成200000*剩余tick伤害，能量60，冷却20秒"
        );
        tooltip.add(Component.empty());

        addAbility(
                tooltip,
                "纯美，如爱莉希雅",
                "主手持有真我之境·无暇回归时，获得速度V，力量V，抗性III，火焰抵抗V，生命回复V，跳跃提升III，村庄英雄V，饱和V，幸运V，生命提升V"
        );
    }

    private static void addAbility(
            List<Component> tooltip,
            String name,
            String... effects
    ) {
        tooltip.add(
                Component.literal(name)
                        .setStyle(PINK_BOLD_STYLE)
        );

        for (String effect : effects) {
            addLightPinkLine(tooltip, effect);
        }
    }

    private static void addPinkLine(
            List<Component> tooltip,
            String text
    ) {
        tooltip.add(
                Component.literal(text)
                        .setStyle(PINK_STYLE)
        );
    }

    private static void addLightPinkLine(
            List<Component> tooltip,
            String text
    ) {
        tooltip.add(
                Component.literal(text)
                        .setStyle(LIGHT_PINK_STYLE)
        );
    }

}
