package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfDominationItem extends Item {

    public CoreOfDominationItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("第十律者的核心，承载着“支配”的权能，能够操纵意志、制造傀儡，将无数破碎的声音汇聚成剧场。")
                .withStyle(ChatFormatting.YELLOW));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("它是无数人的怨恨，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("无数人的软弱，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("无数人的不甘，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("在崩坏中拼接而成的集合。")
                .withStyle(ChatFormatting.YELLOW));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("每一道裂面都像是一张面具。")
                .withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("每一块碎片，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("都藏着一个声音。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("它们哭喊、嘲笑、诱导、操纵，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("最终让人忘记自己的选择究竟属于谁。")
                .withStyle(ChatFormatting.DARK_GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("千人律者没有单一完整的人格，")
                .withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("而是以剧场、傀儡与群体意识的形式出现")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("支配剧场之中，")
                .withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("人性最脆弱的部分被无限放大。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("恐惧会成为绳索，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("愤怒会成为刀刃，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("自卑与嫉妒会成为操纵他人的线。")
                .withStyle(ChatFormatting.DARK_GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「灾难没有压垮我们，但人类却做到了」")
                .withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「你所谓的希望，又能坚持多久？」")
                .withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
