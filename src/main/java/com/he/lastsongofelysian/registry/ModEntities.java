package com.he.lastsongofelysian.registry;

import com.he.lastsongofelysian.entity.CorrosionMirrorEntity;
import com.he.lastsongofelysian.entity.ElysianHeroNpcEntity;
import com.he.lastsongofelysian.entity.PardofelisMerchantEntity;
import com.he.lastsongofelysian.entity.QuGeEntity;
import com.he.lastsongofelysian.entity.QuGeSpitEntity;
import com.he.lastsongofelysian.entity.YellowPurpleAxolotlEntity;
import com.he.lastsongofelysian.entity.YellowPurpleBabyAxolotlEntity;
import com.he.lastsongofelysian.entity.YellowPurpleFishEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, lastsongofelysian.MODID);

    public static final RegistryObject<EntityType<CorrosionMirrorEntity>> CORROSION_MIRROR =
            ENTITY_TYPES.register("corrosion_mirror",
                    () -> EntityType.Builder.of(CorrosionMirrorEntity::new, MobCategory.MISC)
                            .sized(0.6F, 1.8F)
                            .clientTrackingRange(10)
                            .updateInterval(2)
                            .build("corrosion_mirror"));

    public static final RegistryObject<EntityType<PardofelisMerchantEntity>> PARDOFELIS_MERCHANT =
            ENTITY_TYPES.register("pardofelis_merchant",
                    () -> EntityType.Builder.of(PardofelisMerchantEntity::new, MobCategory.CREATURE)
                            .sized(0.6F, 1.8F)
                            .build("pardofelis_merchant"));

    public static final RegistryObject<EntityType<ElysianHeroNpcEntity>> APONIA_NPC =
            registerHero("aponia_npc", ElysianHeroNpcEntity.HeroVariant.APONIA);

    public static final RegistryObject<EntityType<ElysianHeroNpcEntity>> ELYSIA_NPC =
            registerHero("elysia_npc", ElysianHeroNpcEntity.HeroVariant.ELYSIA);

    public static final RegistryObject<EntityType<ElysianHeroNpcEntity>> GRISEO_NPC =
            registerHero("griseo_npc", ElysianHeroNpcEntity.HeroVariant.GRISEO);

    public static final RegistryObject<EntityType<ElysianHeroNpcEntity>> VILL_V_NPC =
            registerHero("vill_v_npc", ElysianHeroNpcEntity.HeroVariant.VILL_V);

    public static final RegistryObject<EntityType<ElysianHeroNpcEntity>> EDEN_NPC =
            registerHero("eden_npc", ElysianHeroNpcEntity.HeroVariant.EDEN);

    public static final RegistryObject<EntityType<QuGeEntity>> QU_GE =
            ENTITY_TYPES.register("qu_ge",
                    () -> EntityType.Builder.of(QuGeEntity::new, MobCategory.CREATURE)
                            .sized(1.55F, 0.65F)
                            .clientTrackingRange(10)
                            .build("qu_ge"));

    public static final RegistryObject<EntityType<QuGeSpitEntity>> QU_GE_SPIT =
            ENTITY_TYPES.register("qu_ge_spit",
                    () -> EntityType.Builder.<QuGeSpitEntity>of(QuGeSpitEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(8)
                            .updateInterval(10)
                            .build("qu_ge_spit"));

    public static final RegistryObject<EntityType<YellowPurpleFishEntity>> YELLOW_PURPLE_LITTLE_FISH =
            ENTITY_TYPES.register("yellow_purple_little_fish",
                    () -> EntityType.Builder.of(
                                    YellowPurpleFishEntity::new,
                                    MobCategory.WATER_AMBIENT
                            )
                            .sized(0.7F, 0.4F)
                            .clientTrackingRange(4)
                            .updateInterval(3)
                            .build("yellow_purple_little_fish"));

    public static final RegistryObject<EntityType<YellowPurpleAxolotlEntity>> YELLOW_PURPLE_LITTLE_AXOLOTL =
            ENTITY_TYPES.register("yellow_purple_little_axolotl",
                    () -> EntityType.Builder.of(
                                    YellowPurpleAxolotlEntity::new,
                                    MobCategory.AXOLOTLS
                            )
                            .sized(0.75F, 0.42F)
                            .clientTrackingRange(8)
                            .updateInterval(3)
                            .build("yellow_purple_little_axolotl"));

    public static final RegistryObject<EntityType<YellowPurpleBabyAxolotlEntity>> YELLOW_PURPLE_LITTLE_AXOLOTL_BABY =
            ENTITY_TYPES.register("yellow_purple_little_axolotl_baby",
                    () -> EntityType.Builder.of(
                                    YellowPurpleBabyAxolotlEntity::new,
                                    MobCategory.AXOLOTLS
                            )
                            .sized(0.42F, 0.24F)
                            .clientTrackingRange(8)
                            .updateInterval(3)
                            .build("yellow_purple_little_axolotl_baby"));

    private static RegistryObject<EntityType<ElysianHeroNpcEntity>> registerHero(
            String id,
            ElysianHeroNpcEntity.HeroVariant variant
    ) {
        return ENTITY_TYPES.register(
                id,
                () -> EntityType.Builder.<ElysianHeroNpcEntity>of(
                                (type, level) ->
                                        new ElysianHeroNpcEntity(
                                                type,
                                                level,
                                                variant
                                        ),
                                MobCategory.CREATURE
                        )
                        .sized(0.6F, 1.8F)
                        .clientTrackingRange(10)
                        .updateInterval(2)
                        .build(id)
        );
    }

}
