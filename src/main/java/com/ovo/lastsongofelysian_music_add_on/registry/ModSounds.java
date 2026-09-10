package com.ovo.lastsongofelysian_music_add_on.registry;

import com.ovo.lastsongofelysian_music_add_on.LastSongOfElysianMusicAddOn;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, LastSongOfElysianMusicAddOn.MOD_ID);

    public static final RegistryObject<SoundEvent> DA_CAPO = sound("da_capo");
    public static final RegistryObject<SoundEvent> RUBIA = sound("rubia");
    public static final RegistryObject<SoundEvent> BEFALL = sound("befall");
    public static final RegistryObject<SoundEvent> DUAL_EGO = sound("dual_ego");
    public static final RegistryObject<SoundEvent> NIGHTGLOW = sound("nightglow");
    public static final RegistryObject<SoundEvent> STARFALL = sound("starfall");
    public static final RegistryObject<SoundEvent> TRUE_SONG = sound("true");
    public static final RegistryObject<SoundEvent> PHI2 = sound("phi2");
    public static final RegistryObject<SoundEvent> HONKAI_WORLD_DIVA = sound("honkai_world_diva");
    public static final RegistryObject<SoundEvent> MILLENNIUM_FEATHER = sound("millennium_feather");
    public static final RegistryObject<SoundEvent> CYBERANGEL = sound("cyberangel");
    public static final RegistryObject<SoundEvent> MOON_HALO = sound("moon_halo");
    public static final RegistryObject<SoundEvent> REGRESSION = sound("regression");
    public static final RegistryObject<SoundEvent> STAR_AND_DISAPPEARANCE_DAY = sound("star_and_disappearance_day");
    public static final RegistryObject<SoundEvent> ORACLE = sound("oracle");
    public static final RegistryObject<SoundEvent> OATHS = sound("oaths");

    private ModSounds() {
    }

    private static RegistryObject<SoundEvent> sound(String id) {
        return SOUNDS.register(id,
                () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(LastSongOfElysianMusicAddOn.MOD_ID, id)));
    }
}
