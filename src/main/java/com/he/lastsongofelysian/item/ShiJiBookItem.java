package com.he.lastsongofelysian.item;

import com.he.lastsongofelysian.client.ClientScreenHooks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;

public class ShiJiBookItem extends Item {

    public ShiJiBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        entity.clearFire();
        entity.setInvulnerable(true);
        entity.setUnlimitedLifetime();
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            DistExecutor.unsafeRunWhenOn(
                    net.minecraftforge.api.distmarker.Dist.CLIENT,
                    () -> ClientScreenHooks::openShiJiScreen
            );
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("岁月史书")
                .withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));

        tooltip.add(Component.literal("by刑部尚书/钦天监首领/流光忆庭首领/无漏净子/司马迁/构史爵/浮黎/灰暗之手/遐蝶/虚构史学家")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));

        tooltip.add(Component.literal("此书不可焚，不可毁，不可存，不可遗。")
                .withStyle(ChatFormatting.DARK_RED));
        tooltip.add(Component.literal("")
                .withStyle(ChatFormatting.DARK_RED));

        tooltip.add(Component.literal("在此")
                .withStyle(ChatFormatting.DARK_RED));
        tooltip.add(Component.literal("我向他们许下承诺")
                .withStyle(ChatFormatting.DARK_RED));
        tooltip.add(Component.literal("史")
                .withStyle(ChatFormatting.DARK_RED));
        tooltip.add(Component.literal("一定会战胜人类")
                .withStyle(ChatFormatting.DARK_RED));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
