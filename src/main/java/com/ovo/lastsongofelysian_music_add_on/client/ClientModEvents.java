package com.ovo.lastsongofelysian_music_add_on.client;

import com.ovo.lastsongofelysian_music_add_on.LastSongOfElysianMusicAddOn;
import com.ovo.lastsongofelysian_music_add_on.menu.RadioMenu;
import com.ovo.lastsongofelysian_music_add_on.registry.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = LastSongOfElysianMusicAddOn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.ScreenConstructor<RadioMenu, RadioScreen> constructor = RadioScreen::new;
            MenuScreens.register(ModMenus.RADIO_MENU.get(), constructor);
        });
    }

    @Mod.EventBusSubscriber(modid = LastSongOfElysianMusicAddOn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static final class ForgeEvents {
        private ForgeEvents() {
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) RadioPlayback.tick();
        }
    }
}
