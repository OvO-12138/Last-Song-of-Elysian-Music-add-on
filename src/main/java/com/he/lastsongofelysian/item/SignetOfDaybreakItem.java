package com.he.lastsongofelysian.item;

import com.he.lastsongofelysian.util.HelixHelper;
import com.he.lastsongofelysian.util.SignetUpgradeData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SignetOfDaybreakItem extends Item {

    private static final Style TITLE  = Style.EMPTY.withColor(TextColor.fromRgb(0x55FF55)).withBold(true);
    private static final Style HEADER = Style.EMPTY.withColor(TextColor.fromRgb(0x55FF55));
    private static final Style LORE   = Style.EMPTY.withColor(TextColor.fromRgb(0xAAFFAA)).withItalic(true);
    private static final Style PINK_ITALIC = Style.EMPTY.withColor(TextColor.fromRgb(0xFFB6C1)).withItalic(true);

    public SignetOfDaybreakItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        boolean ego = HelixHelper.isEgoEquipped();

        tooltip.add(Component.literal("「旭光」 黎明之哨 科斯魔 IX").withStyle(TITLE));

        if (ego) {
            tooltip.add(Component.literal("少年之琴音，以此伴暮色之光，无声回响，守「旭光」之明静").withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("大地也曾渴求雨露").withStyle(LORE));
        tooltip.add(Component.literal("但我们").withStyle(LORE));
        tooltip.add(Component.literal("却只能为它献上这临行前的祈祷").withStyle(LORE));
        tooltip.add(Component.literal("动身吧").withStyle(LORE));
        tooltip.add(Component.literal("身为英桀之名…").withStyle(LORE));
        tooltip.add(Component.literal("正是为了将其了结").withStyle(LORE));

        if (Screen.hasShiftDown()) {
            if (ego) {
                tooltip.add(Component.empty());
                tooltip.add(Component.literal("愿黎明之哨爱佑其身，赐予「旭光」的意志").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("撕裂上限提高18，撕裂伤害无视防御").withStyle(LORE));
            }
            SignetUpgradeData.addSkillTooltips(
                    tooltip, stack, SignetUpgradeData.Signet.DAYBREAK, 0x55FF55, 0xAAFFAA);
        } else {
            tooltip.add(Component.literal("按住 [Shift] 查看效果").withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
