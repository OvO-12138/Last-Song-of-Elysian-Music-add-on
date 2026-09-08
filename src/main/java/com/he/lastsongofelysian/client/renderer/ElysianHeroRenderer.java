package com.he.lastsongofelysian.client.renderer;

import com.he.lastsongofelysian.client.model.ElysianHeroModel;
import com.he.lastsongofelysian.client.renderer.layer.AponiaButterflyLayer;
import com.he.lastsongofelysian.client.renderer.layer.VillVMechanicalLayer;
import com.he.lastsongofelysian.entity.ElysianHeroNpcEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ElysianHeroRenderer
        extends HumanoidMobRenderer<ElysianHeroNpcEntity, ElysianHeroModel> {

    private static final ResourceLocation APONIA = texture("aponia");
    private static final ResourceLocation ELYSIA = texture("elysia");
    private static final ResourceLocation GRISEO = texture("griseo");
    private static final ResourceLocation VILL_V = texture("vill_v");
    private static final ResourceLocation EDEN = texture("eden");

    public ElysianHeroRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ElysianHeroModel(
                        context.bakeLayer(ElysianHeroModel.LAYER_LOCATION)
                ),
                0.5F
        );
        this.addLayer(new AponiaButterflyLayer(this));
        this.addLayer(new VillVMechanicalLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ElysianHeroNpcEntity entity) {
        return switch (entity.getVariant()) {
            case APONIA -> APONIA;
            case ELYSIA -> ELYSIA;
            case GRISEO -> GRISEO;
            case VILL_V -> VILL_V;
            case EDEN -> EDEN;
        };
    }

    @Override
    protected void scale(
            ElysianHeroNpcEntity entity,
            PoseStack poseStack,
            float partialTickTime
    ) {
        super.scale(entity, poseStack, partialTickTime);
        if (entity.getVariant() == ElysianHeroNpcEntity.HeroVariant.GRISEO) {
            poseStack.scale(0.86F, 0.86F, 0.86F);
        }
    }

    private static ResourceLocation texture(String name) {
        return new ResourceLocation(
                lastsongofelysian.MODID,
                "textures/entity/elysian_heroes/" + name + ".png"
        );
    }
}
