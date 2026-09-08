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
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

public class SignetOfBodhiItem extends Item implements ICurioItem {

    private static final Style ICE_GREEN_BOLD  = Style.EMPTY.withColor(TextColor.fromRgb(0x40E0B0)).withBold(true);
    private static final Style LIGHT_ICE_ITALIC = Style.EMPTY.withColor(TextColor.fromRgb(0xAAF5E0)).withItalic(true);
    private static final Style ICE_GREEN        = Style.EMPTY.withColor(TextColor.fromRgb(0x40E0B0));

    public SignetOfBodhiItem(Properties properties) { super(properties); }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        boolean ego = HelixHelper.isEgoEquipped();

        tooltip.add(Component.literal("「天慧」善法之瞳 苏 VII").withStyle(ICE_GREEN_BOLD));

        if (ego) {
            tooltip.add(Component.literal("医师“觉者”的落叶，以此洞察世界，答案难寻，坠「天慧」之泡影")
                    .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("诸行无常").withStyle(LIGHT_ICE_ITALIC));
        tooltip.add(Component.literal("皆是因果轮转").withStyle(LIGHT_ICE_ITALIC));
        tooltip.add(Component.literal("刹那生灭").withStyle(LIGHT_ICE_ITALIC));
        tooltip.add(Component.literal("来吧").withStyle(LIGHT_ICE_ITALIC));
        tooltip.add(Component.literal("踏入这无量光中").withStyle(LIGHT_ICE_ITALIC));
        tooltip.add(Component.literal("此即「天慧」之铭").withStyle(LIGHT_ICE_ITALIC));

        if (Screen.hasShiftDown()) {
            if (ego) {
                tooltip.add(Component.empty());
                tooltip.add(Component.literal("愿善法之瞳爱佑其身，赐予「天慧」的智识").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("获得永久夜视效果").withStyle(LIGHT_ICE_ITALIC));
            }
            SignetUpgradeData.addSkillTooltips(
                    tooltip, stack, SignetUpgradeData.Signet.BODHI, 0x40E0B0, 0xAAF5E0);
        } else {
            tooltip.add(Component.literal("按住 [Shift] 查看效果").withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
