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

public class SignetOfDecimationItem extends Item {

    public SignetOfDecimationItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        boolean ego = HelixHelper.isEgoEquipped();

        tooltip.add(Component.literal("「鏖灭」 坏劫之焱 千劫 VI")
                .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));

        if (ego) {
            tooltip.add(Component.literal("非天“狂王”的面具：以此湮灭一切，怒火难熄，见「鏖灭」之杀戮")
                    .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("厮杀本就没有意义")
                .withStyle(style -> style.withItalic(true).withColor(0xFF9999)));
        tooltip.add(Component.literal("但他们的死")
                .withStyle(style -> style.withItalic(true).withColor(0xFF9999)));
        tooltip.add(Component.literal("不能没有意义")
                .withStyle(style -> style.withItalic(true).withColor(0xFF9999)));

        if (Screen.hasShiftDown()) {

            if (ego) {
                tooltip.add(Component.literal(""));
                tooltip.add(Component.literal("愿坏劫之焱爱佑其身，赐予「鏖灭」的赤诚").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("血量上限提升60%")
                        .withStyle(style -> style.withItalic(true).withColor(0xFF9999)));
            }
            SignetUpgradeData.addSkillTooltips(
                    tooltip, stack, SignetUpgradeData.Signet.DECIMATION, 0xFF5555, 0xFF9999);
        } else {
            tooltip.add(Component.literal("按住 [Shift] 查看效果")
                    .withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
