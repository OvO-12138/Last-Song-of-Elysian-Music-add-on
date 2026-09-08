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
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

public class SignetOfReverieItem extends Item implements ICurioItem {

    public SignetOfReverieItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        boolean ego = HelixHelper.isEgoEquipped();

        tooltip.add(Component.literal("「空梦」掠集之兽 帕朵菲莉丝 XIII")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.BOLD));

        if (ego) {
            tooltip.add(Component.literal("快活“凡人”的悲鸣：以此只身奔赴，九命难转，唯「空梦」之幻灭")
                    .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("英雄有英雄的活法，凡人有凡人的活法")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("非得活成别人的样子......")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("倒也不是不行")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("但...真的有必要吗")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));

        if (Screen.hasShiftDown()) {

            if (ego) {
                tooltip.add(Component.empty());
                tooltip.add(Component.literal("愿掠集之兽爱佑其身，赐予「空梦」的自由").withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltip.add(Component.literal("免疫摔落伤害，移动速度提高39%")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
            SignetUpgradeData.addSkillTooltips(
                    tooltip, stack, SignetUpgradeData.Signet.REVERIE, 0x555555, 0xAAAAAA);
        } else {
            tooltip.add(Component.literal("按住 [Shift] 查看效果")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
