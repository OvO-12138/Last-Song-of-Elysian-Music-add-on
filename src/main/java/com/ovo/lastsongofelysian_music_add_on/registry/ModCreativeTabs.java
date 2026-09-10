package com.ovo.lastsongofelysian_music_add_on.registry;

import com.ovo.lastsongofelysian_music_add_on.LastSongOfElysianMusicAddOn;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LastSongOfElysianMusicAddOn.MOD_ID);

    public static final RegistryObject<CreativeModeTab> RADIO_TAB = TABS.register("radio_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.lastsongofelysian_music_add_on.radio"))
                    .icon(() -> new ItemStack(ModItems.TRUE_DISC.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.RADIO.get());
                        ModItems.ALL_DISCS.forEach(item -> output.accept(item.get()));
                    })
                    .build());

    private ModCreativeTabs() {
    }
}
