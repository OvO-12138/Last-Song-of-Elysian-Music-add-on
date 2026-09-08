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

public class SignetOfStarsItem extends Item implements ICurioItem {

    private static final Style LIGHT_BLUE_BOLD = Style.EMPTY
            .withColor(TextColor.fromRgb(0x7EC8E3)).withBold(true);
    private static final Style VERY_LIGHT_BLUE_ITALIC = Style.EMPTY
            .withColor(TextColor.fromRgb(0xC8E8FF)).withItalic(true);
    private static final Style RED = Style.EMPTY
            .withColor(TextColor.fromRgb(0xFF4444));
    private static final Style RED_LIGHT = Style.EMPTY
            .withColor(TextColor.fromRgb(0xFF9999)).withItalic(true);
    private static final Style BLUE = Style.EMPTY
            .withColor(TextColor.fromRgb(0x4488FF));
    private static final Style BLUE_LIGHT = Style.EMPTY
            .withColor(TextColor.fromRgb(0x99BBFF)).withItalic(true);

    public SignetOfStarsItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        boolean ego = HelixHelper.isEgoEquipped();

        tooltip.add(Component.literal("「繁星」绘世之卷 格蕾修 XI")
                .withStyle(LIGHT_BLUE_BOLD));

        if (ego) {
            tooltip.add(Component.literal("美好“画家”的笔墨：以此孤月相伴，希望难见，观「繁星」之绘梦")
                    .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("天空…").withStyle(VERY_LIGHT_BLUE_ITALIC));
        tooltip.add(Component.literal("云朵…").withStyle(VERY_LIGHT_BLUE_ITALIC));
        tooltip.add(Component.literal("蓝色的…").withStyle(VERY_LIGHT_BLUE_ITALIC));
        tooltip.add(Component.literal("白色的...").withStyle(VERY_LIGHT_BLUE_ITALIC));
        tooltip.add(Component.literal("还没画完的部分…").withStyle(VERY_LIGHT_BLUE_ITALIC));
        tooltip.add(Component.literal("想用你喜欢的颜色").withStyle(VERY_LIGHT_BLUE_ITALIC));
        tooltip.add(Component.literal("你的颜色").withStyle(VERY_LIGHT_BLUE_ITALIC));

        if (Screen.hasShiftDown()) {

            if (ego) {
                tooltip.add(Component.empty());
                tooltip.add(Component.literal("愿绘世之卷爱佑其身，赐予「繁星」的纯真").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("粉色的，真我的").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("攻击时无视50%防御")
                        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFB6C1)).withItalic(true)));
            }
            SignetUpgradeData.addSkillTooltips(
                    tooltip, stack, SignetUpgradeData.Signet.STARS, 0x7EC8E3, 0xC8E8FF);
        } else {
            tooltip.add(Component.literal("按住 [Shift] 查看效果")
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
