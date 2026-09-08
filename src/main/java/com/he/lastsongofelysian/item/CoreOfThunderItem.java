package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfThunderItem extends Item {

    public CoreOfThunderItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("第三律者的核心，承载着“雷”的权能，能够操控雷电，贯穿敌阵，撕裂束缚自身的命运。")
                .withStyle(ChatFormatting.LIGHT_PURPLE));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("征服宝石的光芒在核心中闪烁，")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.literal("紫色雷电穿过晶体，")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.literal("像是压抑已久的情感，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("终于化作斩断一切的刀锋。")
                .withStyle(ChatFormatting.LIGHT_PURPLE));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("雷光斩断过去，你我踏上歧路")
                .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("当雷鸣散尽，暴雨停歇……")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("我将坠入黑暗，换你回到光明。")
                .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("雷电芽衣，")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.literal("曾经温柔、犹豫、害怕失去。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("她珍视身边的人，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("也因此比任何人都害怕无能为力。")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.literal("但当命运一次又一次夺走她想守护的东西，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("她最终选择独自踏入黑暗。")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.literal("不是因为她背离了同伴，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("而是因为她想以自己的方式，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("将重要之人带回光明。")
                .withStyle(ChatFormatting.LIGHT_PURPLE));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「我只知道，在我心里，比起这个世界，你更重要」")
                .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「如果拯救你是一种罪，那就由我来当这个罪人」")
                .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「去告诉这个世界雷电女王的归来吧」")
                .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
