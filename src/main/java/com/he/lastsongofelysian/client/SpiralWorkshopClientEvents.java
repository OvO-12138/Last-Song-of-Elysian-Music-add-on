package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.client.screen.SpiralWorkshopScreen;
import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.registry.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public final class SpiralWorkshopClientEvents {

    private SpiralWorkshopClientEvents() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(
                ModMenus.SPIRAL_WORKSHOP_MENU.get(),
                SpiralWorkshopScreen::new
        ));
    }
}
