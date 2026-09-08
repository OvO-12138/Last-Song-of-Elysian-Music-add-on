package com.he.lastsongofelysian.client.renderer;

import com.he.lastsongofelysian.entity.YellowPurpleFishEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.SalmonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class YellowPurpleFishRenderer
        extends MobRenderer<YellowPurpleFishEntity, SalmonModel<YellowPurpleFishEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            lastsongofelysian.MODID,
            "textures/entity/yellow_purple_little_fish.png"
    );

    public YellowPurpleFishRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new SalmonModel<>(context.bakeLayer(ModelLayers.SALMON)),
                0.3F
        );
    }

    @Override
    protected void setupRotations(
            YellowPurpleFishEntity fish,
            PoseStack poseStack,
            float ageInTicks,
            float rotationYaw,
            float partialTicks
    ) {
        super.setupRotations(
                fish,
                poseStack,
                ageInTicks,
                rotationYaw,
                partialTicks
        );

        float turnStrength = fish.isInWater() ? 1.0F : 1.3F;
        float turnSpeed = fish.isInWater() ? 1.0F : 1.7F;

        poseStack.mulPose(
                Axis.YP.rotationDegrees(
                        turnStrength * 4.3F
                                * Mth.sin(turnSpeed * 0.6F * ageInTicks)
                )
        );

        if (!fish.isInWater()) {
            poseStack.translate(0.2F, 0.1F, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(YellowPurpleFishEntity fish) {
        return TEXTURE;
    }
}
