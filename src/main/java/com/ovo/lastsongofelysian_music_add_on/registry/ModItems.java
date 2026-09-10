package com.ovo.lastsongofelysian_music_add_on.registry;

import com.ovo.lastsongofelysian_music_add_on.LastSongOfElysianMusicAddOn;
import com.ovo.lastsongofelysian_music_add_on.item.RadioItem;
import com.ovo.lastsongofelysian_music_add_on.item.RadioSongDiscItem;
import com.ovo.lastsongofelysian_music_add_on.util.RadioSongs;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LastSongOfElysianMusicAddOn.MOD_ID);

    public static final RegistryObject<Item> RADIO = ITEMS.register("radio",
            () -> new RadioItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DA_CAPO_DISC = disc("da_capo_disc", RadioSongs.DA_CAPO);
    public static final RegistryObject<Item> RUBIA_DISC = disc("rubia_disc", RadioSongs.RUBIA);
    public static final RegistryObject<Item> BEFALL_DISC = disc("befall_disc", RadioSongs.BEFALL);
    public static final RegistryObject<Item> DUAL_EGO_DISC = disc("dual_ego_disc", RadioSongs.DUAL_EGO);
    public static final RegistryObject<Item> NIGHTGLOW_DISC = disc("nightglow_disc", RadioSongs.NIGHTGLOW);
    public static final RegistryObject<Item> STARFALL_DISC = disc("starfall_disc", RadioSongs.STARFALL);
    public static final RegistryObject<Item> TRUE_DISC = disc("true_disc", RadioSongs.TRUE_SONG);
    public static final RegistryObject<Item> PHI2_DISC = disc("phi2_disc", RadioSongs.PHI2);
    public static final RegistryObject<Item> HONKAI_WORLD_DIVA_DISC = disc("honkai_world_diva_disc", RadioSongs.HONKAI_WORLD_DIVA);
    public static final RegistryObject<Item> MILLENNIUM_FEATHER_DISC = disc("millennium_feather_disc", RadioSongs.MILLENNIUM_FEATHER);
    public static final RegistryObject<Item> CYBERANGEL_DISC = disc("cyberangel_disc", RadioSongs.CYBERANGEL);
    public static final RegistryObject<Item> MOON_HALO_DISC = disc("moon_halo_disc", RadioSongs.MOON_HALO);
    public static final RegistryObject<Item> REGRESSION_DISC = disc("regression_disc", RadioSongs.REGRESSION);
    public static final RegistryObject<Item> STAR_AND_DISAPPEARANCE_DAY_DISC =
            disc("star_and_disappearance_day_disc", RadioSongs.STAR_AND_DISAPPEARANCE_DAY);
    public static final RegistryObject<Item> ORACLE_DISC = disc("oracle_disc", RadioSongs.ORACLE);
    public static final RegistryObject<Item> OATHS_DISC = disc("oaths_disc", RadioSongs.OATHS);

    public static final List<RegistryObject<Item>> ALL_DISCS = List.of(
            DA_CAPO_DISC,
            RUBIA_DISC,
            BEFALL_DISC,
            DUAL_EGO_DISC,
            NIGHTGLOW_DISC,
            STARFALL_DISC,
            TRUE_DISC,
            PHI2_DISC,
            HONKAI_WORLD_DIVA_DISC,
            MILLENNIUM_FEATHER_DISC,
            CYBERANGEL_DISC,
            MOON_HALO_DISC,
            REGRESSION_DISC,
            STAR_AND_DISAPPEARANCE_DAY_DISC,
            ORACLE_DISC,
            OATHS_DISC
    );

    private ModItems() {
    }

    private static RegistryObject<Item> disc(String itemId, String songId) {
        return ITEMS.register(itemId,
                () -> new RadioSongDiscItem(songId, new Item.Properties().stacksTo(1)));
    }
}
