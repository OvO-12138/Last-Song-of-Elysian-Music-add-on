package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfRockItem extends Item {

    public CoreOfRockItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("第九律者的核心，承载着“岩”的权能，能够操控重力、大地与压迫性的空间场")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("它的力量不在于锋利，")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("而在于沉重。")
                .withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.literal("当重力压下，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("连抬头都会变成一种反抗。")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("金棕色晶体中，")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("崩坏能如同地脉般缓慢流动。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("它没有雷电的迅捷，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("没有火焰的狂暴，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("却拥有让一切坠落的绝对重量。")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("“枭”")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("他并非一开始就想成为灾难。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("他只是想守住重要的人，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("想在崩坏与命运之间，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("找到一个哪怕微小的出口。")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("但星辰坠落时，")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("个人的愿望被重力吞没。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("爱、执念、绝望，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("最终都被压缩成无法逃离的核心。")
                .withStyle(ChatFormatting.DARK_GRAY));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
