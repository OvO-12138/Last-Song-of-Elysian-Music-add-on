package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfBindingItem extends Item {

    public CoreOfBindingItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("第十一律者的核心，承载着“约束”的权能，能够压制崩坏能，封锁能量流动，使力量本身失去意义。")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("它让敌人的力量无法被使用。")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("在它的领域之内，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("反抗会被削弱，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("奇迹会被封禁，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("连希望都会变得沉重。")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("光环如同无形的戒律。")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("那不是装饰，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("而是规则。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("当约束展开，")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("一切能量都会被迫低头。")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("在前文明中留下了极其惨烈的记忆。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("战士、融合战士、律者权能，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("甚至人类最依赖的力量体系，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("都在同一片领域中失效。")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("约束，")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("锁住了“可能性”。")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("在那样的权能面前，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("牺牲也未必能换来结果。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("它像一道冰冷的规则，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("安静地宣告所有反抗无效。")
                .withStyle(ChatFormatting.GOLD));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
