package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.client.overlay.ComboOverlay;
import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = lastsongofelysian.MODID,
        value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        ComboOverlay.clientTick();
        DisciplineHudData.clientTick();
    }
}
