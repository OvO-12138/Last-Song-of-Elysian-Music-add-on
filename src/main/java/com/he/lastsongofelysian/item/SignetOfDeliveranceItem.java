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

public class SignetOfDeliveranceItem extends Item {
    public SignetOfDeliveranceItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        boolean ego = HelixHelper.isEgoEquipped();

        tooltip.add(Component.literal("「救世」 无烬之剑 凯文 I")
                .withStyle(ChatFormatting.DARK_BLUE, ChatFormatting.BOLD));

        if (ego) {
            tooltip.add(Component.literal("孤独“英雄”的长路：以此冰焰祭奠，孤身难行，登「救世」之阶梯")
                    .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("“凯文,不是只有英雄才能拯救世界”")
                .withStyle(style -> style.withItalic(true).withColor(0xFFB6C1)));
        tooltip.add(Component.literal("“而是只有拯救了世界，所以才会被称为英雄”")
                .withStyle(style -> style.withItalic(true).withColor(0xFFB6C1)));

        tooltip.add(Component.literal("但我们都知道")
                .withStyle(style -> style.withItalic(true).withColor(0x4444FF)));
        tooltip.add(Component.literal("这世界早已无法拯救")
                .withStyle(style -> style.withItalic(true).withColor(0x4444FF)));
        tooltip.add(Component.literal("可我们")
                .withStyle(style -> style.withItalic(true).withColor(0x4444FF)));
        tooltip.add(Component.literal("还是必须成为英雄")
                .withStyle(style -> style.withItalic(true).withColor(0x4444FF)));

        if (Screen.hasShiftDown()) {

            if (ego) {
                tooltip.add(Component.empty());
                tooltip.add(Component.literal("愿无烬之剑爱佑其身，赐予「救世」的理想").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("基础攻击力提高100%")
                        .withStyle(style -> style.withItalic(true).withColor(0x4444FF)));
            }
            SignetUpgradeData.addSkillTooltips(
                    tooltip, stack, SignetUpgradeData.Signet.DELIVERANCE, 0x0000AA, 0x4444FF);
            SignetUpgradeData.addUnlockedDeliveranceSecondaryTooltips(
                    tooltip, stack, 0x0000AA, 0x4444FF);
            SignetUpgradeData.addDeliveranceCoreTooltips(
                    tooltip, stack, 0x0000AA, 0x4444FF);
        } else {
            tooltip.add(Component.literal("按住 [Shift] 查看效果")
                    .withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
