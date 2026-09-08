package com.he.lastsongofelysian.client.renderer.layer;

import com.he.lastsongofelysian.client.model.ElysianHeroModel;
import com.he.lastsongofelysian.entity.ElysianHeroNpcEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class VillVMechanicalLayer
        extends RenderLayer<ElysianHeroNpcEntity, ElysianHeroModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            lastsongofelysian.MODID,
            "textures/entity/accessories/vill_v_mechanical_accessories.png"
    );

    public VillVMechanicalLayer(
            RenderLayerParent<ElysianHeroNpcEntity, ElysianHeroModel> parent
    ) {
        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            ElysianHeroNpcEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (entity.getVariant() != ElysianHeroNpcEntity.HeroVariant.VILL_V) {
            return;
        }

        VertexConsumer gearConsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
        this.getParentModel().renderVillVAccessories(
                poseStack,
                gearConsumer,
                packedLight,
                LivingEntityRenderer.getOverlayCoords(entity, 0.0F)
        );
    }
}
