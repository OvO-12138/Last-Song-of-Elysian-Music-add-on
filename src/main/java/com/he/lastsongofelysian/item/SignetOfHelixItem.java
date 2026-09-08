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

public class SignetOfHelixItem extends Item {
    public SignetOfHelixItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        boolean ego = HelixHelper.isEgoEquipped();

        tooltip.add(Component.literal("「螺旋」 愚戏之匣 维尔薇 V")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));

        if (ego) {
            tooltip.add(Component.literal("多面“愚人”的齿轮：以此矛盾倒错，常理难覆，扭「螺旋」之现")
                    .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("现实…")
                .withStyle(style -> style.withItalic(true).withColor(0xCCAA88)));
        tooltip.add(Component.literal("既不绝对")
                .withStyle(style -> style.withItalic(true).withColor(0xCCAA88)));
        tooltip.add(Component.literal("也不唯一")
                .withStyle(style -> style.withItalic(true).withColor(0xCCAA88)));
        tooltip.add(Component.literal("说到底")
                .withStyle(style -> style.withItalic(true).withColor(0xCCAA88)));
        tooltip.add(Component.literal("它不过只是一台精密运作的机器")
                .withStyle(style -> style.withItalic(true).withColor(0xCCAA88)));
        tooltip.add(Component.literal("既然如此")
                .withStyle(style -> style.withItalic(true).withColor(0xCCAA88)));
        tooltip.add(Component.literal("那就让我们来将它彻底解构")
                .withStyle(style -> style.withItalic(true).withColor(0xCCAA88)));
        tooltip.add(Component.literal("并以此—")
                .withStyle(style -> style.withItalic(true).withColor(0xCCAA88)));
        tooltip.add(Component.literal("创造这绝无仅有的奇迹")
                .withStyle(style -> style.withItalic(true).withColor(0xCCAA88)));

        if (Screen.hasShiftDown()) {

            if (ego) {
                tooltip.add(Component.empty());
                tooltip.add(Component.literal("愿愚戏之匣爱佑其身，赐予「螺旋」的奇迹").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("武器技能伤害提高64%")
                        .withStyle(style -> style.withItalic(true).withColor(0xCCAA88)));
            }
            SignetUpgradeData.addSkillTooltips(
                    tooltip, stack, SignetUpgradeData.Signet.HELIX, 0xFFAA00, 0xCCAA88);
        } else {
            tooltip.add(Component.literal("按住 [Shift] 查看效果")
                    .withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
