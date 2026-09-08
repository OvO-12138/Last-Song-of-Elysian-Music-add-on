package com.he.lastsongofelysian.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PlainNamedItem extends Item {
    private final String displayName;

    public PlainNamedItem(Properties properties, String displayName) {
        super(properties);
        this.displayName = displayName;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal(displayName);
    }
}
