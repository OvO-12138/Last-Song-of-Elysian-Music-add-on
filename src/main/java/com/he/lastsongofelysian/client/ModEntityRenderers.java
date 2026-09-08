package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.client.model.ElysianHeroModel;
import com.he.lastsongofelysian.client.model.PardofelisMerchantModel;
import com.he.lastsongofelysian.client.model.QuGeModel;
import com.he.lastsongofelysian.client.renderer.CorrosionMirrorRenderer;
import com.he.lastsongofelysian.client.renderer.ElysianHeroRenderer;
import com.he.lastsongofelysian.client.renderer.PardofelisMerchantRenderer;
import com.he.lastsongofelysian.client.renderer.QuGeRenderer;
import com.he.lastsongofelysian.client.renderer.YellowPurpleAxolotlRenderer;
import com.he.lastsongofelysian.client.renderer.YellowPurpleBabyAxolotlRenderer;
import com.he.lastsongofelysian.client.renderer.YellowPurpleFishRenderer;
import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.registry.ModEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ModEntityRenderers {

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                ElysianHeroModel.LAYER_LOCATION,
                ElysianHeroModel::createBodyLayer
        );
        event.registerLayerDefinition(
                PardofelisMerchantModel.LAYER_LOCATION,
                PardofelisMerchantModel::createBodyLayer
        );
        event.registerLayerDefinition(
                QuGeModel.LAYER_LOCATION,
                QuGeModel::createBodyLayer
        );
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntities.CORROSION_MIRROR.get(),
                CorrosionMirrorRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.APONIA_NPC.get(),
                ElysianHeroRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.ELYSIA_NPC.get(),
                ElysianHeroRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.GRISEO_NPC.get(),
                ElysianHeroRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.VILL_V_NPC.get(),
                ElysianHeroRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.EDEN_NPC.get(),
                ElysianHeroRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.PARDOFELIS_MERCHANT.get(),
                PardofelisMerchantRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.QU_GE.get(),
                QuGeRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.QU_GE_SPIT.get(),
                ThrownItemRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.YELLOW_PURPLE_LITTLE_FISH.get(),
                YellowPurpleFishRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.YELLOW_PURPLE_LITTLE_AXOLOTL.get(),
                YellowPurpleAxolotlRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.YELLOW_PURPLE_LITTLE_AXOLOTL_BABY.get(),
                YellowPurpleBabyAxolotlRenderer::new
        );
    }
}
