package com.he.lastsongofelysian.registry;

import com.he.lastsongofelysian.item.*;
import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.util.SignetUpgradeData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.he.lastsongofelysian.item.NamelessTownChestplateItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import com.he.lastsongofelysian.item.WhisperOfThePastItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, lastsongofelysian.MODID);

    public static final RegistryObject<Item> SIGNET_OF_REVERIE = ITEMS.register("signet_of_reverie",
            () -> new SignetOfReverieItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHINY_SILVER = ITEMS.register("shiny_silver",
            () -> new ShinySilverItem(new Item.Properties().stacksTo(127)));
    public static final RegistryObject<Item> SIGNET_OF_VICISSITUDE = ITEMS.register("signet_of_vicissitude",
            () -> new SignetOfVicissitudeItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SILVER_POUCH = ITEMS.register("silver_pouch",
            () -> new SilverPouchItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIGNET_OF_STARS = ITEMS.register("signet_of_stars",
            () -> new SignetOfStarsItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PAINTBRUSH = ITEMS.register("paintbrush",
            () -> new PaintbrushItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIGNET_OF_INFINITY = ITEMS.register("signet_of_infinity",
            () -> new SignetOfInfinityItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIGNET_OF_DAYBREAK = ITEMS.register("signet_of_daybreak",
            () -> new SignetOfDaybreakItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIGNET_OF_SETSURA = ITEMS.register("signet_of_setsura",
            () -> new SignetOfSetsuraItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIGNET_OF_BODHI = ITEMS.register("signet_of_bodhi",
            () -> new SignetOfBodhiItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIGNET_OF_DECIMATION = ITEMS.register("signet_of_decimation",
            () -> new SignetOfDecimationItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIGNET_OF_HELIX = ITEMS.register("signet_of_helix",
            () -> new SignetOfHelixItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIGNET_OF_GOLD = ITEMS.register("signet_of_gold",
            () -> new SignetOfGoldItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIGNET_OF_DISCIPLINE = ITEMS.register("signet_of_discipline",
            () -> new SignetOfDisciplineItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIGNET_OF_DELIVERANCE = ITEMS.register("signet_of_deliverance",
            () -> new SignetOfDeliveranceItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DELIVERANCE_HUNTERS_MASK = ITEMS.register(
            "deliverance_hunters_mask",
            () -> new DeliveranceSecondarySignetItem(
                    new Item.Properties().stacksTo(1),
                    SignetUpgradeData.Skill.DELIVERANCE_HUNTERS_MASK
            ));
    public static final RegistryObject<Item> DELIVERANCE_RESTRAINERS_RELIC = ITEMS.register(
            "deliverance_restrainers_relic",
            () -> new DeliveranceSecondarySignetItem(
                    new Item.Properties().stacksTo(1),
                    SignetUpgradeData.Skill.DELIVERANCE_RESTRAINERS_RELIC
            ));
    public static final RegistryObject<Item> DELIVERANCE_SEEKERS_ROBE = ITEMS.register(
            "deliverance_seekers_robe",
            () -> new DeliveranceSecondarySignetItem(
                    new Item.Properties().stacksTo(1),
                    SignetUpgradeData.Skill.DELIVERANCE_SEEKERS_ROBE
            ));
    public static final RegistryObject<Item> DELIVERANCE_KINGS_SWORD = ITEMS.register(
            "deliverance_kings_sword",
            () -> new DeliveranceCoreSignetItem(
                    new Item.Properties().stacksTo(1),
                    SignetUpgradeData.DeliveranceCore.KINGS_SWORD
            ));
    public static final RegistryObject<Item> DELIVERANCE_LONE_SHADOW = ITEMS.register(
            "deliverance_lone_shadow",
            () -> new DeliveranceCoreSignetItem(
                    new Item.Properties().stacksTo(1),
                    SignetUpgradeData.DeliveranceCore.LONE_SHADOW
            ));
    public static final RegistryObject<Item> DELIVERANCE_KINGS_MUSTER = ITEMS.register(
            "deliverance_kings_muster",
            () -> new DeliveranceSecondarySignetItem(
                    new Item.Properties().stacksTo(1),
                    SignetUpgradeData.Skill.DELIVERANCE_KINGS_MUSTER
            ));
    public static final RegistryObject<Item> DELIVERANCE_KINGS_EXPEDITION = ITEMS.register(
            "deliverance_kings_expedition",
            () -> new DeliveranceSecondarySignetItem(
                    new Item.Properties().stacksTo(1),
                    SignetUpgradeData.Skill.DELIVERANCE_KINGS_EXPEDITION
            ));
    public static final RegistryObject<Item> DELIVERANCE_KINGS_ECHO = ITEMS.register(
            "deliverance_kings_echo",
            () -> new DeliveranceSecondarySignetItem(
                    new Item.Properties().stacksTo(1),
                    SignetUpgradeData.Skill.DELIVERANCE_KINGS_ECHO
            ));
    public static final RegistryObject<Item> DELIVERANCE_LONE_RESIDUAL_DREAM = ITEMS.register(
            "deliverance_lone_residual_dream",
            () -> new DeliveranceSecondarySignetItem(
                    new Item.Properties().stacksTo(1),
                    SignetUpgradeData.Skill.DELIVERANCE_LONE_RESIDUAL_DREAM
            ));
    public static final RegistryObject<Item> DELIVERANCE_LONE_DECISION = ITEMS.register(
            "deliverance_lone_decision",
            () -> new DeliveranceSecondarySignetItem(
                    new Item.Properties().stacksTo(1),
                    SignetUpgradeData.Skill.DELIVERANCE_LONE_DECISION
            ));
    public static final RegistryObject<Item> DELIVERANCE_LONE_TRIUMPH = ITEMS.register(
            "deliverance_lone_triumph",
            () -> new DeliveranceSecondarySignetItem(
                    new Item.Properties().stacksTo(1),
                    SignetUpgradeData.Skill.DELIVERANCE_LONE_TRIUMPH
            ));
    public static final RegistryObject<Item> REVERIE_SIGNET_UPGRADE = registerSignetUpgrade(
            "reverie_signet_upgrade", SignetUpgradeData.Signet.REVERIE);
    public static final RegistryObject<Item> VICISSITUDE_SIGNET_UPGRADE = registerSignetUpgrade(
            "vicissitude_signet_upgrade", SignetUpgradeData.Signet.VICISSITUDE);
    public static final RegistryObject<Item> STARS_SIGNET_UPGRADE = registerSignetUpgrade(
            "stars_signet_upgrade", SignetUpgradeData.Signet.STARS);
    public static final RegistryObject<Item> INFINITY_SIGNET_UPGRADE = registerSignetUpgrade(
            "infinity_signet_upgrade", SignetUpgradeData.Signet.INFINITY);
    public static final RegistryObject<Item> DAYBREAK_SIGNET_UPGRADE = registerSignetUpgrade(
            "daybreak_signet_upgrade", SignetUpgradeData.Signet.DAYBREAK);
    public static final RegistryObject<Item> SETSURA_SIGNET_UPGRADE = registerSignetUpgrade(
            "setsura_signet_upgrade", SignetUpgradeData.Signet.SETSURA);
    public static final RegistryObject<Item> BODHI_SIGNET_UPGRADE = registerSignetUpgrade(
            "bodhi_signet_upgrade", SignetUpgradeData.Signet.BODHI);
    public static final RegistryObject<Item> DECIMATION_SIGNET_UPGRADE = registerSignetUpgrade(
            "decimation_signet_upgrade", SignetUpgradeData.Signet.DECIMATION);
    public static final RegistryObject<Item> HELIX_SIGNET_UPGRADE = registerSignetUpgrade(
            "helix_signet_upgrade", SignetUpgradeData.Signet.HELIX);
    public static final RegistryObject<Item> GOLD_SIGNET_UPGRADE = registerSignetUpgrade(
            "gold_signet_upgrade", SignetUpgradeData.Signet.GOLD);
    public static final RegistryObject<Item> DISCIPLINE_SIGNET_UPGRADE = registerSignetUpgrade(
            "discipline_signet_upgrade", SignetUpgradeData.Signet.DISCIPLINE);
    public static final RegistryObject<Item> DELIVERANCE_SIGNET_UPGRADE = registerSignetUpgrade(
            "deliverance_signet_upgrade", SignetUpgradeData.Signet.DELIVERANCE);
    public static final RegistryObject<Item> SIGNET_OF_EGO = ITEMS.register("signet_of_ego",
            () -> new SignetOfEgoItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> AWAKENING =
            registerRemembranceSigil("awakening", 4);
    public static final RegistryObject<Item> NINE_LIVES =
            registerRemembranceSigil("nine_lives", 3);

    public static final RegistryObject<Item> FEAST_OF_EMPTINESS =
            registerRemembranceSigil("feast_of_emptiness", 2);

    public static final RegistryObject<Item> HOMETOWN =
            registerRemembranceSigil("hometown", 4);

    public static final RegistryObject<Item> RESOLVE =
            registerRemembranceSigil("resolve", 3);

    public static final RegistryObject<Item> MEMORY =
            registerRemembranceSigil("memory", 2);

    public static final RegistryObject<Item> HOME_LOST =
            registerRemembranceSigil("home_lost", 1);

    public static final RegistryObject<Item> THE_LONELY_MOON =
            registerRemembranceSigil("the_lonely_moon", 4);

    public static final RegistryObject<Item> GREY_SCALE_RAINBOW =
            registerRemembranceSigil("grey_scale_rainbow", 3);

    public static final RegistryObject<Item> RAINBOW_OF_ABSENCE =
            registerRemembranceSigil("rainbow_of_absence", 2);

    public static final RegistryObject<Item> BOUNDLESS_LOGOS =
            registerRemembranceSigil("boundless_logos", 4);

    public static final RegistryObject<Item> THE_FIRST_SCALE =
            registerRemembranceSigil("the_first_scale", 3);

    public static final RegistryObject<Item> FORBIDDEN_SEED =
            registerRemembranceSigil("forbidden_seed", 2);

    public static final RegistryObject<Item> FALSE_HOPE =
            registerRemembranceSigil("false_hope", 1);

    public static final RegistryObject<Item> OUT_OF_REACH =
            registerRemembranceSigil("out_of_reach", 4);

    public static final RegistryObject<Item> RAVENOUS_GULLY =
            registerRemembranceSigil("ravenous_gully", 3);

    public static final RegistryObject<Item> FRAGILE_FRIEND =
            registerRemembranceSigil("fragile_friend", 2);

    public static final RegistryObject<Item> TSUKIMI_HIMIKO =
            registerRemembranceSigil("tsukimi_himiko", 4);

    public static final RegistryObject<Item> STAINED_SAKURA =
            registerRemembranceSigil("stained_sakura", 3);

    public static final RegistryObject<Item> FORGET_ME_NOT =
            registerRemembranceSigil("forget_me_not", 2);

    public static final RegistryObject<Item> EMPTY_LIKE_SHALA =
            registerRemembranceSigil("empty_like_shala", 4);

    public static final RegistryObject<Item> HEAVY_AS_A_MILLION_LIVES =
            registerRemembranceSigil("heavy_as_a_million_lives", 3);

    public static final RegistryObject<Item> LIGHT_AS_A_BODHI_LEAF =
            registerRemembranceSigil("light_as_a_bodhi_leaf", 2);

    public static final RegistryObject<Item> AN_OLD_PALS_LEGACY =
            registerRemembranceSigil("an_old_pals_legacy", 4);

    public static final RegistryObject<Item> SHATTERED_SHACKLES =
            registerRemembranceSigil("shattered_shackles", 3);

    public static final RegistryObject<Item> MAD_KINGS_MASK =
            registerRemembranceSigil("mad_kings_mask", 2);

    public static final RegistryObject<Item> FALLING_IN_PAST_LIGHT =
            registerRemembranceSigil("falling_in_past_light", 4);

    public static final RegistryObject<Item> FARAWAY_SHIP =
            registerRemembranceSigil("faraway_ship", 3);

    public static final RegistryObject<Item> PSEUDO_MIRACLE =
            registerRemembranceSigil("pseudo_miracle", 2);

    public static final RegistryObject<Item> RUINED_LEGACY =
            registerRemembranceSigil("ruined_legacy", 1);

    public static final RegistryObject<Item> DREAMFUL_GOLD =
            registerRemembranceSigil("dreamful_gold", 4);

    public static final RegistryObject<Item> GOOD_OLD_DAYS =
            registerRemembranceSigil("good_old_days", 3);

    public static final RegistryObject<Item> GOLD_GOBLET =
            registerRemembranceSigil("gold_goblet", 2);

    public static final RegistryObject<Item> TIN_FLASK =
            registerRemembranceSigil("tin_flask", 1);

    public static final RegistryObject<Item> BOUNDLESS_FEELING =
            registerRemembranceSigil("boundless_feeling", 4);

    public static final RegistryObject<Item> PROOF_OF_GOOD_AND_EVIL =
            registerRemembranceSigil("proof_of_good_and_evil", 3);

    public static final RegistryObject<Item> VEIL_OF_TEARS =
            registerRemembranceSigil("veil_of_tears", 2);

    public static final RegistryObject<Item> KEY_TO_THE_DEEP =
            registerRemembranceSigil("key_to_the_deep", 1);

    public static final RegistryObject<Item> IT_WILL_BE_WRITTEN =
            registerRemembranceSigil("it_will_be_written", 4);

    public static final RegistryObject<Item> ABANDONED =
            registerRemembranceSigil("abandoned", 3);

    public static final RegistryObject<Item> BURDEN =
            registerRemembranceSigil("burden", 2);

    public static final RegistryObject<Item> THE_MOTH_INSIGNIA =
            registerRemembranceSigil("the_moth_insignia", 1);

    public static final RegistryObject<Item> THORNY_CROWN =
            registerRemembranceSigil("thorny_crown", 5);

    public static final RegistryObject<Item> BECAUSE_OF_YOU =
            registerRemembranceSigil("because_of_you", 4);

    public static final RegistryObject<Item> CRYSTAL_ROSE =
            registerRemembranceSigil("crystal_rose", 2);

    public static final RegistryObject<Item> FLAWLESS_KEY =
            ITEMS.register("flawless_key",
                    () -> new ElysianMaterialItem(
                            new Item.Properties().stacksTo(64),
                            "每周进入装甲强化室可领取1枚的神秘钥匙，该钥匙可用于解锁出战角色。",
                            "",
                            "一柄仿若艺术品般的水晶钥匙。美丽，纯净，永恒。",
                            "但钥匙并不会只作为艺术品而存在，它注定要去打开某处的一道枷锁。",
                            "而这把无瑕的水晶之钥，它所存在的意义，也同样如此。"
                    ));

    public static final RegistryObject<Item> STONE_OF_CHOICE =
            ITEMS.register("stone_of_choice",
                    () -> new ElysianMaterialItem(
                            new Item.Properties().stacksTo(64),
                            "战斗中获取的天赋技能升级材料，可以前往「命定的歧路」解锁或升级天赋技能。",
                            "",
                            "没有什么重量，仿佛并不真实存在一般的石块。",
                            "石块的色彩并不唯一，它们无时无刻不在流动、变化、融合。",
                            "但据说在某些时候，这种石块中的色彩又会完全固定下来。",
                            "而在那时，这石块也将变得异常沉重，远非常人所能持握。"
                    ));

    public static final RegistryObject<Item> DYING_KINDLING =
            ITEMS.register("dying_kindling",
                    () -> new ElysianMaterialItem(
                            new Item.Properties().stacksTo(64),
                            "战斗中获取的神秘材料，可以前往「追忆之皿」提升追忆之皿的等级。",
                            "",
                            "保存在形似追忆之皿的容器中，即将熄灭的火种。其本质乃是英桀们记忆的碎片。",
                            "将其注入追忆之皿的话，英桀们或许会愿意和来访者讲述更多关于旧世代的记忆。",
                            "那个世代虽已逝去，但它所留下的火种却并未消亡。",
                            "逐火者们仍将追逐着这流火前行，并最终，抵达前人所未能至的未来。"
                    ));
    public static final RegistryObject<Item> COCOON_OF_FINALITY = ITEMS.register("cocoon_of_finality",
            () -> new CocoonOfFinalityItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HYPERION_LOG =
            ITEMS.register("hyperion_log",
                    () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CYBER_CHARACTER_BIOGRAPHY =
            ITEMS.register("cyber_character_biography",
                    () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> IVTV_BOOK = ITEMS.register("ivtv_book",
            () -> new IVTVBookItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHI_JI =
            ITEMS.register("shi_ji",
                    () -> new ShiJiBookItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> CORE_OF_REASON =
            ITEMS.register("core_of_reason",
                    () -> new CoreOfReasonItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_OF_VOID =
            ITEMS.register("core_of_void",
                    () -> new CoreOfVoidItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_OF_THUNDER     = ITEMS.register("core_of_thunder",
            () -> new CoreOfThunderItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_OF_WIND        = ITEMS.register("core_of_wind",
            () -> new CoreOfWindItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_OF_ICE         = ITEMS.register("core_of_ice",
            () -> new CoreOfIceItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_OF_DEATH       = ITEMS.register("core_of_death",
            () -> new CoreOfDeathItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_OF_FIRE        = ITEMS.register("core_of_fire",
            () -> new CoreOfFlameItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_OF_SENTIENCE   = ITEMS.register("core_of_sentience",
            () -> new CoreOfSentienceItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_OF_EARTH       = ITEMS.register("core_of_earth",
            () -> new CoreOfRockItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_OF_DOMINANCE   = ITEMS.register("core_of_dominance",
            () -> new CoreOfDominationItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_OF_BINDING     = ITEMS.register("core_of_binding",
            () -> new CoreOfBindingItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_OF_CORROSION   = ITEMS.register("core_of_corrosion",
            () -> new CoreOfCorruptionItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORE_OF_ORIGIN = ITEMS.register("core_of_origin",
            () -> new CoreOfOriginItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BIOLOGICAL_RESIDUE =
            ITEMS.register("biological_residue",
                    () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> COMPRESSED_BIOLOGICAL_RESIDUE =
            ITEMS.register("compressed_biological_residue",
                    () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> DOUBLE_COMPRESSED_BIOLOGICAL_RESIDUE =
            ITEMS.register("double_compressed_biological_residue",
                    () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> LOW_HONKAI_ENERGY_INHIBITOR =
            ITEMS.register("low_honkai_energy_inhibitor",
                    () -> new HonkaiEnergyInhibitorItem(
                            new Item.Properties().stacksTo(16),
                            "低等",
                            5.0F,
                            0.05F
                    ));

    public static final RegistryObject<Item> HONKAI_ENERGY_INHIBITOR =
            ITEMS.register("honkai_energy_inhibitor",
                    () -> new HonkaiEnergyInhibitorItem(
                            new Item.Properties().stacksTo(16),
                            "中等",
                            2.0F,
                            0.10F
                    ));

    public static final RegistryObject<Item> HIGH_HONKAI_ENERGY_INHIBITOR =
            ITEMS.register("high_honkai_energy_inhibitor",
                    () -> new HonkaiEnergyInhibitorItem(
                            new Item.Properties().stacksTo(16),
                            "高等",
                            0.0F,
                            0.20F
                    ));
    public static final RegistryObject<Item> NAMELESS_TOWN_CHESTPLATE =
            ITEMS.register("nameless_town_chestplate",
                    () -> new NamelessTownChestplateItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PARDOFELIS_MERCHANT_SPAWN_EGG =
            ITEMS.register("pardofelis_merchant_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.PARDOFELIS_MERCHANT,
                            0xD9B17D,
                            0x6B4A2E,
                            new Item.Properties()
                    ));
    public static final RegistryObject<Item> QU_GE_SPAWN_EGG =
            ITEMS.register("qu_ge_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.QU_GE,
                            0x7020A8,
                            0xD796FF,
                            new Item.Properties()
                    ));
    public static final RegistryObject<Item> APONIA_NPC_SPAWN_EGG =
            ITEMS.register(
                    "aponia_npc_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.APONIA_NPC,
                            0xF4F1E8,
                            0x6E4D7B,
                            new Item.Properties()
                    )
            );

    public static final RegistryObject<Item> ELYSIA_NPC_SPAWN_EGG =
            ITEMS.register(
                    "elysia_npc_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.ELYSIA_NPC,
                            0xFFB6D9,
                            0xFF5FA2,
                            new Item.Properties()
                    )
            );

    public static final RegistryObject<Item> GRISEO_NPC_SPAWN_EGG =
            ITEMS.register(
                    "griseo_npc_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.GRISEO_NPC,
                            0xDDF7FF,
                            0x63B8FF,
                            new Item.Properties()
                    )
            );

    public static final RegistryObject<Item> VILL_V_NPC_SPAWN_EGG =
            ITEMS.register(
                    "vill_v_npc_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.VILL_V_NPC,
                            0x231A1A,
                            0xD6A56D,
                            new Item.Properties()
                    )
            );

    public static final RegistryObject<Item> EDEN_NPC_SPAWN_EGG =
            ITEMS.register(
                    "eden_npc_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.EDEN_NPC,
                            0xF3C46F,
                            0x8B3159,
                            new Item.Properties()
                    )
            );
    public static final RegistryObject<Item> YELLOW_PURPLE_LITTLE_FISH_SPAWN_EGG =
            ITEMS.register(
                    "yellow_purple_little_fish_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.YELLOW_PURPLE_LITTLE_FISH,
                            0xF0EA19,
                            0x8A13D4,
                            new Item.Properties()
                    )
            );
    public static final RegistryObject<Item> YELLOW_PURPLE_LITTLE_AXOLOTL_SPAWN_EGG =
            ITEMS.register(
                    "yellow_purple_little_axolotl_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.YELLOW_PURPLE_LITTLE_AXOLOTL,
                            0xF0EA19,
                            0x8A13D4,
                            new Item.Properties()
                    )
            );
    public static final RegistryObject<Item> YELLOW_PURPLE_LITTLE_AXOLOTL_BABY_SPAWN_EGG =
            ITEMS.register(
                    "yellow_purple_little_axolotl_baby_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.YELLOW_PURPLE_LITTLE_AXOLOTL_BABY,
                            0x8A13D4,
                            0xF0EA19,
                            new Item.Properties()
                    )
            );
    public static final RegistryObject<Item> YELLOW_PURPLE_DYE =
            ITEMS.register(
                    "yellow_purple_dye",
                    () -> new Item(new Item.Properties().stacksTo(64))
            );
    public static final RegistryObject<Item> YELLOW_PURPLE_WOOL =
            ITEMS.register(
                    "yellow_purple_wool",
                    () -> new BlockItem(
                            ModBlock.YELLOW_PURPLE_WOOL.get(),
                            new Item.Properties()
                    )
            );
    public static final RegistryObject<Item> YELLOW_PURPLE_LITTLE_BED =
            ITEMS.register(
                    "yellow_purple_little_bed",
                    () -> new YellowPurpleLittleBedItem(
                            ModBlock.YELLOW_PURPLE_LITTLE_BED.get(),
                            new Item.Properties().stacksTo(1)
                    )
            );
    public static final RegistryObject<Item> QU_GE_SPIT =
            ITEMS.register("qu_ge_spit",
                    () -> new Item(
                            new Item.Properties()
                                    .stacksTo(1)
                    ));
    public static final RegistryObject<Item> WHISPER_OF_THE_PAST =
            ITEMS.register("whisper_of_the_past",
                    () -> new WhisperOfThePastItem(new Item.Properties().durability(768)));
    public static final RegistryObject<Item> FLAWLESS_BENEDICTION_LEGACY =
            ITEMS.register(
                    "flawless_benediction_legacy",
                    () -> new FlawlessBenedictionLegacyItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .fireResistant()
                    )
            );

    public static final RegistryObject<Item> TAB_ICON = ITEMS.register("tab_icon",
            () -> new Item(new Item.Properties()));

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, lastsongofelysian.MODID);

    public static final RegistryObject<CreativeModeTab> LSE_TAB = CREATIVE_MODE_TABS.register("lse_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(TAB_ICON.get()))
                    .title(Component.translatable("itemGroup.lastsongofelysian"))
                    .displayItems((params, output) -> {
                        output.accept(COCOON_OF_FINALITY.get());
                        output.accept(SIGNET_OF_REVERIE.get());
                        output.accept(SIGNET_OF_VICISSITUDE.get());
                        output.accept(SIGNET_OF_STARS.get());
                        output.accept(SIGNET_OF_INFINITY.get());
                        output.accept(SIGNET_OF_DAYBREAK.get());
                        output.accept(SIGNET_OF_SETSURA.get());
                        output.accept(SIGNET_OF_BODHI.get());
                        output.accept(SIGNET_OF_DECIMATION.get());
                        output.accept(SIGNET_OF_HELIX.get());
                        output.accept(SIGNET_OF_GOLD.get());
                        output.accept(SIGNET_OF_DISCIPLINE.get());
                        output.accept(SIGNET_OF_DELIVERANCE.get());
                        output.accept(DELIVERANCE_HUNTERS_MASK.get());
                        output.accept(DELIVERANCE_RESTRAINERS_RELIC.get());
                        output.accept(DELIVERANCE_SEEKERS_ROBE.get());
                        output.accept(DELIVERANCE_KINGS_SWORD.get());
                        output.accept(DELIVERANCE_KINGS_MUSTER.get());
                        output.accept(DELIVERANCE_KINGS_EXPEDITION.get());
                        output.accept(DELIVERANCE_KINGS_ECHO.get());
                        output.accept(DELIVERANCE_LONE_SHADOW.get());
                        output.accept(DELIVERANCE_LONE_RESIDUAL_DREAM.get());
                        output.accept(DELIVERANCE_LONE_DECISION.get());
                        output.accept(DELIVERANCE_LONE_TRIUMPH.get());
                        output.accept(REVERIE_SIGNET_UPGRADE.get());
                        output.accept(VICISSITUDE_SIGNET_UPGRADE.get());
                        output.accept(STARS_SIGNET_UPGRADE.get());
                        output.accept(INFINITY_SIGNET_UPGRADE.get());
                        output.accept(DAYBREAK_SIGNET_UPGRADE.get());
                        output.accept(SETSURA_SIGNET_UPGRADE.get());
                        output.accept(BODHI_SIGNET_UPGRADE.get());
                        output.accept(DECIMATION_SIGNET_UPGRADE.get());
                        output.accept(HELIX_SIGNET_UPGRADE.get());
                        output.accept(GOLD_SIGNET_UPGRADE.get());
                        output.accept(DISCIPLINE_SIGNET_UPGRADE.get());
                        output.accept(DELIVERANCE_SIGNET_UPGRADE.get());
                        output.accept(SIGNET_OF_EGO.get());
                        output.accept(AWAKENING.get());
                        output.accept(NINE_LIVES.get());
                        output.accept(FEAST_OF_EMPTINESS.get());

                        output.accept(HOMETOWN.get());
                        output.accept(RESOLVE.get());
                        output.accept(MEMORY.get());
                        output.accept(HOME_LOST.get());

                        output.accept(THE_LONELY_MOON.get());
                        output.accept(GREY_SCALE_RAINBOW.get());
                        output.accept(RAINBOW_OF_ABSENCE.get());

                        output.accept(BOUNDLESS_LOGOS.get());
                        output.accept(THE_FIRST_SCALE.get());
                        output.accept(FORBIDDEN_SEED.get());
                        output.accept(FALSE_HOPE.get());

                        output.accept(OUT_OF_REACH.get());
                        output.accept(RAVENOUS_GULLY.get());
                        output.accept(FRAGILE_FRIEND.get());

                        output.accept(TSUKIMI_HIMIKO.get());
                        output.accept(STAINED_SAKURA.get());
                        output.accept(FORGET_ME_NOT.get());

                        output.accept(EMPTY_LIKE_SHALA.get());
                        output.accept(HEAVY_AS_A_MILLION_LIVES.get());
                        output.accept(LIGHT_AS_A_BODHI_LEAF.get());

                        output.accept(AN_OLD_PALS_LEGACY.get());
                        output.accept(SHATTERED_SHACKLES.get());
                        output.accept(MAD_KINGS_MASK.get());

                        output.accept(FALLING_IN_PAST_LIGHT.get());
                        output.accept(FARAWAY_SHIP.get());
                        output.accept(PSEUDO_MIRACLE.get());
                        output.accept(RUINED_LEGACY.get());

                        output.accept(DREAMFUL_GOLD.get());
                        output.accept(GOOD_OLD_DAYS.get());
                        output.accept(GOLD_GOBLET.get());
                        output.accept(TIN_FLASK.get());

                        output.accept(BOUNDLESS_FEELING.get());
                        output.accept(PROOF_OF_GOOD_AND_EVIL.get());
                        output.accept(VEIL_OF_TEARS.get());
                        output.accept(KEY_TO_THE_DEEP.get());

                        output.accept(IT_WILL_BE_WRITTEN.get());
                        output.accept(ABANDONED.get());
                        output.accept(BURDEN.get());
                        output.accept(THE_MOTH_INSIGNIA.get());

                        output.accept(THORNY_CROWN.get());
                        output.accept(BECAUSE_OF_YOU.get());
                        output.accept(CRYSTAL_ROSE.get());
                        output.accept(FLAWLESS_KEY.get());
                        output.accept(STONE_OF_CHOICE.get());
                        output.accept(DYING_KINDLING.get());
                        output.accept(CORE_OF_REASON.get());
                        output.accept(CORE_OF_VOID.get());
                        output.accept(CORE_OF_THUNDER.get());
                        output.accept(CORE_OF_WIND.get());
                        output.accept(CORE_OF_ICE.get());
                        output.accept(CORE_OF_DEATH.get());
                        output.accept(CORE_OF_FIRE.get());
                        output.accept(CORE_OF_SENTIENCE.get());
                        output.accept(CORE_OF_EARTH.get());
                        output.accept(CORE_OF_DOMINANCE.get());
                        output.accept(CORE_OF_BINDING.get());
                        output.accept(CORE_OF_CORROSION.get());
                        output.accept(CORE_OF_ORIGIN.get());
                        output.accept(WHISPER_OF_THE_PAST.get());
                        output.accept(FLAWLESS_BENEDICTION_LEGACY.get());
                        output.accept(PAINTBRUSH.get());
                        output.accept(SHINY_SILVER.get());
                        output.accept(SILVER_POUCH.get());
                        output.accept(BIOLOGICAL_RESIDUE.get());
                        output.accept(COMPRESSED_BIOLOGICAL_RESIDUE.get());
                        output.accept(DOUBLE_COMPRESSED_BIOLOGICAL_RESIDUE.get());
                        output.accept(LOW_HONKAI_ENERGY_INHIBITOR.get());
                        output.accept(HONKAI_ENERGY_INHIBITOR.get());
                        output.accept(HIGH_HONKAI_ENERGY_INHIBITOR.get());
                        output.accept(HYPERION_LOG.get());
                        output.accept(CYBER_CHARACTER_BIOGRAPHY.get());
                        output.accept(IVTV_BOOK.get());
                        output.accept(SHI_JI.get());
                        output.accept(PARDOFELIS_MERCHANT_SPAWN_EGG.get());
                        output.accept(QU_GE_SPAWN_EGG.get());
                        output.accept(APONIA_NPC_SPAWN_EGG.get());
                        output.accept(ELYSIA_NPC_SPAWN_EGG.get());
                        output.accept(GRISEO_NPC_SPAWN_EGG.get());
                        output.accept(VILL_V_NPC_SPAWN_EGG.get());
                        output.accept(EDEN_NPC_SPAWN_EGG.get());
                        output.accept(YELLOW_PURPLE_LITTLE_FISH_SPAWN_EGG.get());
                        output.accept(YELLOW_PURPLE_LITTLE_AXOLOTL_SPAWN_EGG.get());
                        output.accept(YELLOW_PURPLE_LITTLE_AXOLOTL_BABY_SPAWN_EGG.get());
                        output.accept(YELLOW_PURPLE_DYE.get());
                        output.accept(YELLOW_PURPLE_WOOL.get());
                        output.accept(YELLOW_PURPLE_LITTLE_BED.get());
                        output.accept(NAMELESS_TOWN_CHESTPLATE.get());
                    })
                    .build()
    );

    private static RegistryObject<Item> registerSignetUpgrade(
            String id,
            SignetUpgradeData.Signet signet
    ) {
        return ITEMS.register(
                id,
                () -> new SignetUpgradeItem(
                        new Item.Properties().stacksTo(64),
                        signet
                )
        );
    }

    private static RegistryObject<Item> registerRemembranceSigil(
            String id,
            int stars
    ) {
        return ITEMS.register(
                id,
                () -> new RemembranceSigilItem(
                        new Item.Properties().stacksTo(1),
                        stars
                )
        );
    }
}
