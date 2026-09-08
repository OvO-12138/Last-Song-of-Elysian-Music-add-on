package com.he.lastsongofelysian.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;

public class SilverPouchItem extends Item {

    private static final String NBT_KEY = "SilverCount";

    public SilverPouchItem(Properties properties) {
        super(properties);
    }

    public static long getBalance(ItemStack stack) {
        if (!stack.hasTag()) return 0L;
        return stack.getOrCreateTag().getLong(NBT_KEY);
    }

    public static void setBalance(ItemStack stack, long amount) {
        stack.getOrCreateTag().putLong(NBT_KEY, Math.max(0, amount));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            long balance = getBalance(stack);
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                    net.minecraft.client.Minecraft.getInstance()
                            .setScreen(new com.he.lastsongofelysian.screen.SilverPouchScreen(balance)));
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("余额：" + getBalance(stack) + " 枚")
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("右键打开")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}
