package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfDeathItem extends Item {

    public CoreOfDeathItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("第六律者的核心，承载着“死”的权能，也触及生命、凋零、静谧与新生的边界。")
                .withStyle(ChatFormatting.DARK_GREEN));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("死亡并非单一的答案，")
                .withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add(Component.literal("它既可以让万物归于沉眠，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("也可以在沉眠之后，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("孕育另一种开始。")
                .withStyle(ChatFormatting.DARK_GREEN));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("静谧宝石的幽光在核心深处流动，")
                .withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add(Component.literal("黑与绿的光彼此交错。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("一侧是衰亡，")
                .withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add(Component.literal("一侧是生命，")
                .withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal("像是生与死之间永远无法分割的裂隙。")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("曾与西琳的命运相连，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("也曾成为琪亚娜体内无法忽视的一部分。")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("而当“死生”的概念被希儿·芙乐艾承载时，")
                .withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add(Component.literal("死亡不再只是失去。")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("她所面对的，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("是告别、守护、分离，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("以及即使害怕也要前进的勇气。")
                .withStyle(ChatFormatting.DARK_GREEN));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("她温柔且勇敢，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("却在无数次选择中证明，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("温柔本身也可以成为力量。")
                .withStyle(ChatFormatting.GREEN));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「这一次，让我来守护大家」")
                .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「真拿你没办法」")
                .withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
