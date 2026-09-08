package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfCorruptionItem extends Item {

    public CoreOfCorruptionItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("第十二律者的核心，承载着“侵蚀”的权能，能够感染意识、数据与系统，从内部改写目标的存在形式。")
                .withStyle(ChatFormatting.LIGHT_PURPLE));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("它像一道错误，")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.literal("一段污染，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("一种悄无声息进入世界内部的崩坏。")
                .withStyle(ChatFormatting.DARK_PURPLE));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("光芒像损坏的数据般闪烁。")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.literal("晶体边缘破碎不定，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("仿佛连自身形态都在被侵蚀不断改写。")
                .withStyle(ChatFormatting.DARK_PURPLE));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("它让被污染之物看似仍然完整。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("外壳没有破碎，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("声音没有改变，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("但内部的逻辑、记忆与意志，")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.literal("已经不再属于原本的自己。")
                .withStyle(ChatFormatting.DARK_PURPLE));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「嗨，感觉如何♬」")
                .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
