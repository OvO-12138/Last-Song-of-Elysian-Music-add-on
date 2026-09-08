package com.he.lastsongofelysian.item;

import com.he.lastsongofelysian.registry.ModEffects;
import com.he.lastsongofelysian.registry.ModItems;
import com.he.lastsongofelysian.util.SignetUpgradeData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.util.List;

public class PaintbrushItem extends Item {

    private static final String NBT_MODE = "InkMode";
    public static final String MODE_RED  = "RED";
    public static final String MODE_BLUE = "BLUE";
    public static final String MODE_PINK = "PINK";

    private static final Style RED        = Style.EMPTY.withColor(TextColor.fromRgb(0xFF4444));
    private static final Style RED_LIGHT  = Style.EMPTY.withColor(TextColor.fromRgb(0xFF9999)).withItalic(true);
    private static final Style BLUE       = Style.EMPTY.withColor(TextColor.fromRgb(0x4488FF));
    private static final Style BLUE_LIGHT = Style.EMPTY.withColor(TextColor.fromRgb(0x99BBFF)).withItalic(true);
    private static final Style PINK       = Style.EMPTY.withColor(TextColor.fromRgb(0xFF69B4));
    private static final Style PINK_LIGHT = Style.EMPTY.withColor(TextColor.fromRgb(0xFFB6C1)).withItalic(true);
    private static final Style GRAY_ITALIC = Style.EMPTY.withColor(TextColor.fromRgb(0xAAAAAA)).withItalic(true);

    public PaintbrushItem(Properties properties) {
        super(properties);
    }

    public static String getMode(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(NBT_MODE)) return MODE_RED;
        return stack.getTag().getString(NBT_MODE);
    }

    public static void setMode(ItemStack stack, String mode) {
        stack.getOrCreateTag().putString(NBT_MODE, mode);
    }

    private static boolean hasSignet(Player player, Item signetItem) {
        return CuriosApi.getCuriosHelper()
                .findFirstCurio(player, stack -> stack.is(signetItem))
                .isPresent();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                String current = getMode(stack);
                String next;

                if (MODE_RED.equals(current)) {

                    if (hasSignet(player, ModItems.SIGNET_OF_STARS.get())) {
                        next = MODE_BLUE;
                    } else {
                        player.displayClientMessage(
                                Component.literal("需要佩戴繁星刻印才能使用颜料喵～").withStyle(ChatFormatting.RED),
                                true);
                        return InteractionResultHolder.success(stack);
                    }
                } else if (MODE_BLUE.equals(current)) {

                    if (hasSignet(player, ModItems.SIGNET_OF_STARS.get()) &&
                            hasSignet(player, ModItems.SIGNET_OF_EGO.get())) {
                        next = MODE_PINK;
                    } else if (!hasSignet(player, ModItems.SIGNET_OF_STARS.get())) {
                        player.displayClientMessage(
                                Component.literal("需要佩戴繁星刻印才能使用颜料喵～").withStyle(ChatFormatting.RED),
                                true);
                        return InteractionResultHolder.success(stack);
                    } else {
                        player.displayClientMessage(
                                Component.literal("需要同时佩戴繁星和真我刻印才能切换粉色颜料喵～").withStyle(ChatFormatting.LIGHT_PURPLE),
                                true);
                        return InteractionResultHolder.success(stack);
                    }
                } else {
                    next = MODE_RED;
                }

                setMode(stack, next);
                player.displayClientMessage(
                        Component.literal(
                                MODE_RED.equals(next)  ? "✦ 红色颜料" :
                                        MODE_BLUE.equals(next) ? "✦ 蓝色颜料" : "✦ 粉色颜料"
                        ).withStyle(
                                MODE_RED.equals(next)  ? RED :
                                        MODE_BLUE.equals(next) ? BLUE : PINK
                        ), true);
            }
            return InteractionResultHolder.success(stack);
        }

        if (!level.isClientSide()) {
            String mode = getMode(stack);
            MobEffect effect;
            switch (mode) {
                case MODE_BLUE: effect = ModEffects.BLUE_INK.get(); break;
                case MODE_PINK: effect = ModEffects.PINK_INK.get(); break;
                default:        effect = ModEffects.RED_INK.get();
            }

            List<LivingEntity> targets = level.getEntitiesOfClass(
                    LivingEntity.class,
                    player.getBoundingBox().inflate(8.0),
                    e -> e instanceof Enemy && e.isAlive());

            for (LivingEntity target : targets) {
                int amplifier = MODE_RED.equals(mode)
                        ? SignetUpgradeData.getEquippedLevel(player, SignetUpgradeData.Skill.STARS_RED)
                        : MODE_BLUE.equals(mode)
                        ? SignetUpgradeData.getEquippedLevel(player, SignetUpgradeData.Skill.STARS_BLUE)
                        : 0;
                target.addEffect(new MobEffectInstance(effect, 100, amplifier, false, true));
            }

            if (!targets.isEmpty()) {
                Style s = mode.equals(MODE_RED) ? RED_LIGHT :
                        mode.equals(MODE_BLUE) ? BLUE_LIGHT : PINK_LIGHT;
                player.displayClientMessage(
                        Component.literal("已涂抹 " + targets.size() + " 个敌人").withStyle(s), true);
            }
        }

        player.getCooldowns().addCooldown(this, 20);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        String mode = getMode(stack);
        boolean isRed = MODE_RED.equals(mode);
        boolean isBlue = MODE_BLUE.equals(mode);
        String colorName = isRed ? "■ 红色" : (isBlue ? "■ 蓝色" : "■ 粉色");
        Style colorStyle = isRed ? RED : (isBlue ? BLUE : PINK);
        tooltip.add(Component.literal("当前颜料：" + colorName).withStyle(colorStyle));
        tooltip.add(Component.literal("Shift+右键 切换颜料  │  右键 涂抹周围敌人")
                .withStyle(GRAY_ITALIC));
    }
}
