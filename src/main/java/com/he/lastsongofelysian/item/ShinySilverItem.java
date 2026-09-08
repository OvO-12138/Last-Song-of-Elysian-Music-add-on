package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ShinySilverItem extends Item {

    public ShinySilverItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("可以在银币商店购买和升级刻印")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.BOLD));

        tooltip.add(Component.literal("闪闪发亮的银质硬币")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("印有形似猫爪的图案。")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("显然，这并不是一种曾在世界上的某处流通过的货币。")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("但在这里，它却的确作为一种特殊的货币在被使用着。")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("因为它永远都亮闪闪的，而她，则刚好喜欢一切亮闪闪的东西。")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}
