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

public class SignetOfDisciplineItem extends Item {
    public SignetOfDisciplineItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        boolean ego = HelixHelper.isEgoEquipped();

        tooltip.add(Component.literal("「戒律」 深罪之槛 阿波尼亚 III")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));

        if (ego) {
            tooltip.add(Component.literal("避世“苦修”的悲悯: 以此锁链自缚，大命难违，筑「戒律」之围护")
                    .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("「由我踏入哀愁之城，由我堕入永劫之苦」")
                .withStyle(style -> style.withItalic(true).withColor(0xFFFFAA)));
        tooltip.add(Component.literal("「请」回头吧，迷途的旅人。")
                .withStyle(style -> style.withItalic(true).withColor(0xFFFFAA)));
        tooltip.add(Component.literal("纠缠于你我之间的丝线，我还不想……让它就此断绝。")
                .withStyle(style -> style.withItalic(true).withColor(0xFFFFAA)));

        if (Screen.hasShiftDown()) {

            if (ego) {
                tooltip.add(Component.empty());
                tooltip.add(Component.literal("愿深罪之槛爱佑其身，赐予「戒律」的慈爱").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("规诫计数提升30，清除数量将为25%")
                        .withStyle(style -> style.withItalic(true).withColor(0xFFFFAA)));
            }
            SignetUpgradeData.addSkillTooltips(
                    tooltip, stack, SignetUpgradeData.Signet.DISCIPLINE, 0xFFAA00, 0xFFFFAA);
        } else {
            tooltip.add(Component.literal("按住 [Shift] 查看效果")
                    .withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
