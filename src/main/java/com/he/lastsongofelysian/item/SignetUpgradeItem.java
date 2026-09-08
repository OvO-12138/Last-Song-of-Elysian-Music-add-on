package com.he.lastsongofelysian.item;

import com.he.lastsongofelysian.util.SignetUpgradeData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SignetUpgradeItem extends Item {

    private final SignetUpgradeData.Signet signet;

    public SignetUpgradeItem(Properties properties, SignetUpgradeData.Signet signet) {
        super(properties);
        this.signet = signet;
    }

    public SignetUpgradeData.Signet signet() {
        return this.signet;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal(this.signet.displayName() + "刻印升级");
    }
}
