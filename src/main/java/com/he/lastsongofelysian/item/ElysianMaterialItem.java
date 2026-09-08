package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ElysianMaterialItem extends Item {
    private final String[] lore;

    public ElysianMaterialItem(Properties properties, String... lore) {
        super(properties);
        this.lore = lore;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        for (String line : lore) {
            tooltip.add(line.isEmpty()
                    ? Component.empty()
                    : Component.literal(line).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
