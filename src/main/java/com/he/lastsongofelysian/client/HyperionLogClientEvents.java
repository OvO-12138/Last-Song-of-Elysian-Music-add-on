package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class HyperionLogClientEvents {

    private HyperionLogClientEvents() {
    }

    @SubscribeEvent
    public static void onRightClickItem(
            PlayerInteractEvent.RightClickItem event
    ) {
        if (
                !event.getLevel().isClientSide() ||
                !event.getItemStack().is(
                        ModItems.HYPERION_LOG.get()
                )
        ) {
            return;
        }

        Minecraft.getInstance().setScreen(
                new HyperionLogScreen()
        );

        event.setCancellationResult(
                InteractionResult.SUCCESS
        );

        event.setCanceled(true);
    }
}
