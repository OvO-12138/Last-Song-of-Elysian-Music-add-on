package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfWindItem extends Item {

    public CoreOfWindItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("第四律者的核心，承载着“风”的权能，能够操控气流、风压与风暴，将天空化作自己的领域。")
                .withStyle(ChatFormatting.GREEN));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("风本应无形无束，")
                .withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal("可当它被囚禁、被压抑、被利用，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("自由便会变成失控的渴望。")
                .withStyle(ChatFormatting.DARK_GREEN));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("渴望宝石沉入少女的身体，")
                .withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal("青绿色的光在核心中旋转，")
                .withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add(Component.literal("像是一场永远无法平息的风暴。")
                .withStyle(ChatFormatting.GREEN));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("它呼唤天空，")
                .withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal("也撕碎了她原本能够拥有的人生。")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("她本可以拥有属于自己的未来，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("却被宝石、实验与命运一步步推向崩坏。")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.literal("她想站起来。")
                .withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal("她想被看见。")
                .withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal("她想重新拥有自己的身体、自己的道路、自己的天空。")
                .withStyle(ChatFormatting.GREEN));

        tooltip.add(Component.literal("但风没有停下。")
                .withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add(Component.literal("当渴望被崩坏放大，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("温柔的愿望也会成为席卷一切的灾难。")
                .withStyle(ChatFormatting.DARK_GREEN));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
