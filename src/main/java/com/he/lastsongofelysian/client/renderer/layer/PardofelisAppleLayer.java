package com.he.lastsongofelysian.client.renderer.layer;

import com.he.lastsongofelysian.client.model.PardofelisMerchantModel;
import com.he.lastsongofelysian.entity.PardofelisMerchantEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class PardofelisAppleLayer
        extends RenderLayer<PardofelisMerchantEntity, PardofelisMerchantModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            lastsongofelysian.MODID,
            "textures/entity/accessories/pardofelis_tail_apple.png"
    );

    public PardofelisAppleLayer(
            RenderLayerParent<PardofelisMerchantEntity, PardofelisMerchantModel> parent
    ) {
        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            PardofelisMerchantEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.getParentModel().renderApple(
                poseStack,
                vertexConsumer,
                packedLight,
                LivingEntityRenderer.getOverlayCoords(entity, 0.0F)
        );
    }
}
