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

public class SignetOfVicissitudeItem extends Item implements ICurioItem {

    private static final Style DARK_ORANGE = Style.EMPTY
            .withColor(TextColor.fromRgb(0xD4680A));
    private static final Style DARK_ORANGE_BOLD = Style.EMPTY
            .withColor(TextColor.fromRgb(0xD4680A))
            .withBold(true);

    private static final Style LIGHT_ORANGE_ITALIC = Style.EMPTY
            .withColor(TextColor.fromRgb(0xFFA040))
            .withItalic(true);

    public SignetOfVicissitudeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        boolean ego = HelixHelper.isEgoEquipped();

        tooltip.add(Component.literal("「浮生」渡尘之羽 华 XII")
                .withStyle(DARK_ORANGE_BOLD));

        if (ego) {
            tooltip.add(Component.literal("迷茫“战士”的决意: 以此落羽铭记，凡尘难渡，历「浮生」之坚守")
                    .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("世界之纷扰，早已麻木；")
                .withStyle(LIGHT_ORANGE_ITALIC));
        tooltip.add(Component.literal("珍重之羁绊，尽皆离散；")
                .withStyle(LIGHT_ORANGE_ITALIC));
        tooltip.add(Component.literal("千年之坚守，只为拯救苍生。")
                .withStyle(LIGHT_ORANGE_ITALIC));
        tooltip.add(Component.literal("醒时恐为梦一场，身世俱忘，何处是吾乡")
                .withStyle(LIGHT_ORANGE_ITALIC));

        tooltip.add(Component.empty());
        tooltip.add(Component.literal("她是华，逐火战士")
                .withStyle(LIGHT_ORANGE_ITALIC));
        tooltip.add(Component.literal("她是赤鸢仙人，太虚山的守护者")
                .withStyle(LIGHT_ORANGE_ITALIC));
        tooltip.add(Component.literal("她是符华，圣芙蕾雅的班长")
                .withStyle(LIGHT_ORANGE_ITALIC));

        if (Screen.hasShiftDown()) {

            if (ego) {
                tooltip.add(Component.empty());
                tooltip.add(Component.literal("愿渡尘之羽爱佑其身，赐予「浮生」的坚忍").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("免疫击退，获得48%伤害免疫")
                        .withStyle(LIGHT_ORANGE_ITALIC));
            }
            SignetUpgradeData.addSkillTooltips(
                    tooltip, stack, SignetUpgradeData.Signet.VICISSITUDE, 0xD4680A, 0xFFA040);
        } else {
            tooltip.add(Component.literal("按住 [Shift] 查看效果")
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
