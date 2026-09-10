package com.ovo.lastsongofelysian_music_add_on.item;

import com.ovo.lastsongofelysian_music_add_on.menu.RadioMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public final class RadioItem extends Item {
    public RadioItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(
                    serverPlayer,
                    new SimpleMenuProvider(
                            (containerId, inventory, ignoredPlayer) ->
                                    new RadioMenu(containerId, inventory, hand),
                            Component.translatable("item.lastsongofelysian_music_add_on.radio")
                    ),
                    buffer -> buffer.writeEnum(hand)
            );
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
