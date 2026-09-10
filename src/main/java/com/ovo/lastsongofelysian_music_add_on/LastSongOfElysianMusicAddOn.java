package com.ovo.lastsongofelysian_music_add_on;

import com.ovo.lastsongofelysian_music_add_on.registry.ModCreativeTabs;
import com.ovo.lastsongofelysian_music_add_on.registry.ModItems;
import com.ovo.lastsongofelysian_music_add_on.registry.ModMenus;
import com.ovo.lastsongofelysian_music_add_on.registry.ModSounds;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LastSongOfElysianMusicAddOn.MOD_ID)
public final class LastSongOfElysianMusicAddOn {
    public static final String MOD_ID = "lastsongofelysian_music_add_on";

    public LastSongOfElysianMusicAddOn() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modBus);
        ModMenus.MENUS.register(modBus);
        ModSounds.SOUNDS.register(modBus);
        ModCreativeTabs.TABS.register(modBus);
    }
}
