package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfIceItem extends Item {

    public CoreOfIceItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("第五律者的核心，承载着“冰”的权能，能够冻结物质、停滞能量，将生命活动封入极寒之中。")
                .withStyle(ChatFormatting.AQUA));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("它是沉默，")
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("是隔绝，")
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("是所有无法回应的情感，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("最终凝结成不化的霜。")
                .withStyle(ChatFormatting.AQUA));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("冰蓝色晶体之中，")
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("崩坏能化作极寒的流光。")
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("它只是安静地扩散，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("让一切逐渐失去温度。")
                .withStyle(ChatFormatting.AQUA));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("安娜·沙尼亚特，")
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("天命女武神，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("也是被崩坏选中的悲剧之人。")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.literal("她并非生来就是灾厄，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("她也曾拥有责任、感情与想要守护的人。")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("但她再也无法回到过去。")
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("冰封的不只是城市，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("也是她与人类之间最后的距离。")
                .withStyle(ChatFormatting.AQUA));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("她的寒冷并非无情。")
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("正因为仍有感情，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("那份无法传达的痛苦，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("才显得更加残酷。")
                .withStyle(ChatFormatting.AQUA));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
