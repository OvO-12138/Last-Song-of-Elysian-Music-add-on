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

public class SignetOfGoldItem extends Item {
    public SignetOfGoldItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        boolean ego = HelixHelper.isEgoEquipped();

        tooltip.add(Component.literal("「黄金」 璀耀之歌 伊甸 IV")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));

        if (ego) {
            tooltip.add(Component.literal("璀璨“歌者”的馈赠: 以此金杯遥敬，辉煌难回，奏「黄金」之终章")
                    .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("「愿时光永驻此刻，愿明日如黄金般辉煌」")
                .withStyle(style -> style.withItalic(true).withColor(0xFFD700)));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.literal("「某一日，祂从天坠落」")
                .withStyle(style -> style.withItalic(true).withColor(0xFFD700)));
        tooltip.add(Component.literal("「人们抬头仰望，于是看见了星空…」")
                .withStyle(style -> style.withItalic(true).withColor(0xFFD700)));

        if (Screen.hasShiftDown()) {

            if (ego) {
                tooltip.add(Component.empty());
                tooltip.add(Component.literal("愿璀耀之歌爱佑其身，赐予「黄金」的光辉").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("可以进行40秒的短暂飞行，获得永久水下呼吸")
                        .withStyle(style -> style.withItalic(true).withColor(0xFFD700)));
            }
            SignetUpgradeData.addSkillTooltips(
                    tooltip, stack, SignetUpgradeData.Signet.GOLD, 0xFFD700, 0xFFD700);
        } else {
            tooltip.add(Component.literal("按住 [Shift] 查看效果")
                    .withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
