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

public class SignetOfInfinityItem extends Item implements ICurioItem {

    private static final Style DARK_GREEN_BOLD  = Style.EMPTY.withColor(TextColor.fromRgb(0x2E8B57)).withBold(true);
    private static final Style CYAN_ITALIC       = Style.EMPTY.withColor(TextColor.fromRgb(0x40CFA0)).withItalic(true);
    private static final Style DARK_GREEN        = Style.EMPTY.withColor(TextColor.fromRgb(0x2E8B57));

    public SignetOfInfinityItem(Properties properties) { super(properties); }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        boolean ego = HelixHelper.isEgoEquipped();

        tooltip.add(Component.literal("「无限」噬界之蛇 梅比乌斯 X").withStyle(DARK_GREEN_BOLD));

        if (ego) {
            tooltip.add(Component.literal("渴望“蛇主”的未来，以此永恒轮回，生死难忘，掀「∞」之禁忌")
                    .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("死亡并不是生命的终点").withStyle(CYAN_ITALIC));
        tooltip.add(Component.literal("生命将因死亡得到进化").withStyle(CYAN_ITALIC));
        tooltip.add(Component.literal("并由此重获新生").withStyle(CYAN_ITALIC));

        if (Screen.hasShiftDown()) {

            if (ego) {
                tooltip.add(Component.empty());
                tooltip.add(Component.literal("愿噬界之蛇爱佑其身，赐予「无限」的渴求").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("召唤物与协同者造成的伤害提高31.415926%")
                        .withStyle(CYAN_ITALIC));
            }
            SignetUpgradeData.addSkillTooltips(
                    tooltip, stack, SignetUpgradeData.Signet.INFINITY, 0x2E8B57, 0x40CFA0);
        } else {
            tooltip.add(Component.literal("按住 [Shift] 查看效果")
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
