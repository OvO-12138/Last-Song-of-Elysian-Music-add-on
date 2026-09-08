package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfVoidItem extends Item {

    public CoreOfVoidItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("第二律者的核心，承载着“空”的权能，能够支配空间，撕裂现实与虚数之间的边界。")
                .withStyle(ChatFormatting.LIGHT_PURPLE));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("空之律者，从来都不只是灾厄的名字。")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.literal("她诞生于仇恨、孤独与实验的深渊，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("也在另一个少女的身体中，成为必须被面对的命运。")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("虚数空间展开之时，")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.literal("距离失去意义，")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.literal("现实被洞穿，")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.literal("世界仿佛只剩下女王俯视众生的王座。")
                .withStyle(ChatFormatting.DARK_PURPLE));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("西琳,")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.literal("被世界伤害的少女，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("在崩坏的回应中获得了力量。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("她憎恨人类，憎恨命运，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("也将自己的痛苦化作降临世界的灾厄。")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「孱弱渺小，不自量力，垂死挣扎」")
                .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「不过是加速死亡的到来」")
                .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「亲人，朋友，幸福 ，梦想，未来，希望，她不该拥有，不配拥有，不将拥有」")
                .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("琪亚娜·卡斯兰娜体内的阴影")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.literal("曾一度夺走她的身体与意志。")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("但她没有永远沉入虚空。")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.literal("她从女王的阴影中夺回自我，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("从被命运支配的人，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("成为了选择守护世界的人。")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「Ich Iiebe dich」")
                .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
