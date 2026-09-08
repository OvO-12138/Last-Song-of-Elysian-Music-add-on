package com.he.lastsongofelysian.item;

import com.he.lastsongofelysian.util.HelixHelper;
import com.he.lastsongofelysian.util.SignetUpgradeData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SignetOfSetsuraItem extends Item {
    public SignetOfSetsuraItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        boolean ego = HelixHelper.isEgoEquipped();

        tooltip.add(Component.literal("「刹那」 寸断之刃 樱 VIII")
                .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));

        if (ego) {
            tooltip.add(Component.literal("冻结“樱花”的时间: 以此转瞬即逝，至亲难挽，现「刹那」之奇迹")
                    .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("做出你的选择")
                .withStyle(style -> style.withItalic(true).withColor(0xCCFFFF)));
        tooltip.add(Component.literal("然后前进吧")
                .withStyle(style -> style.withItalic(true).withColor(0xCCFFFF)));
        tooltip.add(Component.literal("不要回头")
                .withStyle(style -> style.withItalic(true).withColor(0xCCFFFF)));
        tooltip.add(Component.literal("哪怕只是一瞬的犹豫")
                .withStyle(style -> style.withItalic(true).withColor(0xCCFFFF)));
        tooltip.add(Component.literal("都会让一切前功尽弃")
                .withStyle(style -> style.withItalic(true).withColor(0xCCFFFF)));

        if (Screen.hasShiftDown()) {

            if (ego) {
                tooltip.add(Component.literal(""));
                tooltip.add(Component.literal("愿寸断之刃爱佑其身，赐予「刹那」的决绝").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("闪避冷却减少32%")
                        .withStyle(style -> style.withItalic(true).withColor(0xCCFFFF)));
            }
            SignetUpgradeData.addSkillTooltips(
                    tooltip, stack, SignetUpgradeData.Signet.SETSURA, 0x55FFFF, 0xCCFFFF);
        } else {
            tooltip.add(Component.literal("按住 [Shift] 查看效果").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
