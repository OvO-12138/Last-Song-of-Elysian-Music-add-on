package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.entity.CorrosionMirrorEntity;
import com.he.lastsongofelysian.entity.ElysianHeroNpcEntity;
import com.he.lastsongofelysian.entity.PardofelisMerchantEntity;
import com.he.lastsongofelysian.entity.QuGeEntity;
import com.he.lastsongofelysian.entity.YellowPurpleAxolotlEntity;
import com.he.lastsongofelysian.entity.YellowPurpleBabyAxolotlEntity;
import com.he.lastsongofelysian.entity.YellowPurpleFishEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.mixin.RangedAttributeAccessor;
import com.he.lastsongofelysian.registry.ModEntities;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class ModEntityEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        RangedAttribute maxHealth = (RangedAttribute) Attributes.MAX_HEALTH;
        if (maxHealth.getMaxValue() < QuGeEntity.QU_GE_MAX_HEALTH) {
            ((RangedAttributeAccessor) (Object) maxHealth)
                    .lastsongofelysian$setMaxValue(QuGeEntity.QU_GE_MAX_HEALTH);
        }

        event.put(
                ModEntities.CORROSION_MIRROR.get(),
                CorrosionMirrorEntity.createAttributes().build()
        );
        event.put(
                ModEntities.PARDOFELIS_MERCHANT.get(),
                PardofelisMerchantEntity.createAttributes().build()
        );
        event.put(
                ModEntities.APONIA_NPC.get(),
                ElysianHeroNpcEntity.createAttributes().build()
        );
        event.put(
                ModEntities.ELYSIA_NPC.get(),
                ElysianHeroNpcEntity.createAttributes().build()
        );
        event.put(
                ModEntities.GRISEO_NPC.get(),
                ElysianHeroNpcEntity.createAttributes().build()
        );
        event.put(
                ModEntities.VILL_V_NPC.get(),
                ElysianHeroNpcEntity.createAttributes().build()
        );
        event.put(
                ModEntities.EDEN_NPC.get(),
                ElysianHeroNpcEntity.createAttributes().build()
        );
        event.put(
                ModEntities.QU_GE.get(),
                QuGeEntity.createAttributes().build()
        );
        event.put(
                ModEntities.YELLOW_PURPLE_LITTLE_FISH.get(),
                YellowPurpleFishEntity.createAttributes().build()
        );
        event.put(
                ModEntities.YELLOW_PURPLE_LITTLE_AXOLOTL.get(),
                YellowPurpleAxolotlEntity.createAttributes().build()
        );
        event.put(
                ModEntities.YELLOW_PURPLE_LITTLE_AXOLOTL_BABY.get(),
                YellowPurpleBabyAxolotlEntity.createAttributes().build()
        );
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                ModEntities.YELLOW_PURPLE_LITTLE_FISH.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                YellowPurpleFishEntity::canSpawn,
                SpawnPlacementRegisterEvent.Operation.REPLACE
        );
        event.register(
                ModEntities.YELLOW_PURPLE_LITTLE_AXOLOTL.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                YellowPurpleAxolotlEntity::canSpawn,
                SpawnPlacementRegisterEvent.Operation.REPLACE
        );
        event.register(
                ModEntities.YELLOW_PURPLE_LITTLE_AXOLOTL_BABY.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                YellowPurpleAxolotlEntity::canSpawn,
                SpawnPlacementRegisterEvent.Operation.REPLACE
        );
    }
}
