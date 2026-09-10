package com.ovo.lastsongofelysian_music_add_on.registry;

import com.ovo.lastsongofelysian_music_add_on.LastSongOfElysianMusicAddOn;
import com.ovo.lastsongofelysian_music_add_on.menu.RadioMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, LastSongOfElysianMusicAddOn.MOD_ID);

    public static final RegistryObject<MenuType<RadioMenu>> RADIO_MENU =
            MENUS.register("radio_menu", () -> IForgeMenuType.create(RadioMenu::new));

    private ModMenus() {
    }
}
