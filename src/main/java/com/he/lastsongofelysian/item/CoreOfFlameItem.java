package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfFlameItem extends Item {

    public CoreOfFlameItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("第七律者的核心，承载着“炎”的权能，能够释放高温、燃烧崩坏能，将一切阻碍化为灰烬。")
                .withStyle(ChatFormatting.RED));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("火焰会吞噬，")
                .withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("也会照亮。")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("它可以焚尽城市，")
                .withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("也可以成为黑暗中唯一的路标。")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("疾疫宝石的赤色光芒在核心中燃烧，")
                .withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("金橙色的热流不断翻涌。")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("它的力量太过炽烈，")
                .withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("以至于每一次燃烧，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("都是在燃尽自身。")
                .withStyle(ChatFormatting.RED));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("无量塔姬子，")
                .withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("她并非炎之律者，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("却用自己的生命，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("将火焰的意义留给了她的学生。")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.literal("那一剑之后，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("火焰不再只是律者的灾厄。")
                .withStyle(ChatFormatting.RED));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「纵使黑云蔽日，我也要燃烧天空，带你找到回家的路」")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
