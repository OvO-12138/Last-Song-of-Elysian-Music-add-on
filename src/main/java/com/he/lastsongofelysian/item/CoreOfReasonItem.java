package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfReasonItem extends Item {

    public CoreOfReasonItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("第一律者的核心，承载着“理”的权能，能够解构万物，创造不存在之物，再造奇迹。")
                .withStyle(ChatFormatting.BLUE));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("理之律者，从来都不只是一段历史")
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("以三十万分之一为起点")
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("它的『全部』活在了当下")
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("并远胜过去的所有")
                .withStyle(ChatFormatting.AQUA));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「不要向崩坏屈服」")
                .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「不要向神明低头」")
                .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「这就是我们一致的思想」")
                .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("第一任理之律者")
                .withStyle(ChatFormatting.DARK_BLUE, ChatFormatting.BOLD));
        tooltip.add(Component.literal("瓦尔特·乔伊斯")
                .withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.literal("原生的律者，却选择为人类而战")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("阻挡了星辰的陨落，守护了一座城市")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("第二任理之律者")
                .withStyle(ChatFormatting.DARK_BLUE, ChatFormatting.BOLD));
        tooltip.add(Component.literal("约阿希姆·杨")
                .withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.literal("他继承了瓦尔特之名")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("奉献了自己的一生")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("一次又一次的拯救人类于危机中")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("第三任理之律者")
                .withStyle(ChatFormatting.DARK_BLUE, ChatFormatting.BOLD));
        tooltip.add(Component.literal("布洛妮娅·扎伊切克")
                .withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.literal("以自己的意志，选择了与他们相同的道路")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("选择这个世界")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("以“世界之名”")
                .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「瓦尔特…吗……」")
                .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「这个名字…很棒吧？」")
                .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「那么……」")
                .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「从现在开始，你就是瓦尔特了。」")
                .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「律者的核心…还有守护这个世界的使命……」")
                .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「就拜托你了，瓦尔特。」")
                .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
