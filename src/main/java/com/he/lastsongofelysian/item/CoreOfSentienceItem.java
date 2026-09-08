package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfSentienceItem extends Item {

    public CoreOfSentienceItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("第八律者的核心，承载着“识”的权能，能够干涉意识、记忆、认知与自我，将幻象化作战场。")
                .withStyle(ChatFormatting.YELLOW));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("她诞生于记忆与身体的缝隙之中，")
                .withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("拥有符华的过去，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("却不愿只是符华。")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("核心中，")
                .withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("意识之海翻涌不息。")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("记忆、执念、幻境、自我，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("都在其中互相交叠。")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.literal("当识之权能展开，")
                .withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("真实与虚假便不再有清晰界线。")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("她拥有符华的身体，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("拥有符华的记忆，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("也拥有符华从未表现出的张扬、愤怒与骄傲。")
                .withStyle(ChatFormatting.YELLOW));

        tooltip.add(Component.literal("她想证明自己。")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("她想让所有人承认，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("自己不是替代品，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("不是错误，")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("而是独立存在的“我”。")
                .withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「时代变了，我们可以做自己了」")
                .withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("「对，真正的自己」")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
