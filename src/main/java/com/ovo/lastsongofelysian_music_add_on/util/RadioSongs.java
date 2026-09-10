package com.ovo.lastsongofelysian_music_add_on.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public final class RadioSongs {
    private RadioSongs() {
    }

    public static final String DA_CAPO = "da_capo";
    public static final String RUBIA = "rubia";
    public static final String BEFALL = "befall";
    public static final String DUAL_EGO = "dual_ego";
    public static final String NIGHTGLOW = "nightglow";
    public static final String STARFALL = "starfall";
    public static final String TRUE_SONG = "true";
    public static final String PHI2 = "phi2";
    public static final String HONKAI_WORLD_DIVA = "honkai_world_diva";
    public static final String MILLENNIUM_FEATHER = "millennium_feather";
    public static final String CYBERANGEL = "cyberangel";
    public static final String MOON_HALO = "moon_halo";
    public static final String REGRESSION = "regression";
    public static final String STAR_AND_DISAPPEARANCE_DAY = "star_and_disappearance_day";
    public static final String ORACLE = "oracle";
    public static final String OATHS = "oaths";

    private static final List<String> BUILTIN_SONGS = List.of(
            DA_CAPO, RUBIA, BEFALL, DUAL_EGO, NIGHTGLOW,
            STARFALL, TRUE_SONG, PHI2, HONKAI_WORLD_DIVA,
            MILLENNIUM_FEATHER, CYBERANGEL, MOON_HALO,
            REGRESSION, STAR_AND_DISAPPEARANCE_DAY, ORACLE, OATHS
    );

    public static List<String> allIds() {
        return BUILTIN_SONGS;
    }

    public static SoundEvent sound(String songId) {
        ResourceLocation location = new ResourceLocation(
                "lastsongofelysian_music_add_on", songId
        );
        return ForgeRegistries.SOUND_EVENTS.getValue(location);
    }

    public static ItemStack disc(String songId) {
        ResourceLocation location = new ResourceLocation(
                "lastsongofelysian_music_add_on", songId + "_disc"
        );
        return new ItemStack(net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(location));
    }

    public static String titleStr(String songId) {
        return "song.lastsongofelysian_music_add_on." + songId;
    }

    public static MutableComponent title(String songId) {
        return Component.translatable(titleStr(songId));
    }
}
