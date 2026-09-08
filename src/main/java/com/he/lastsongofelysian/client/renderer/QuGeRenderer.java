package com.he.lastsongofelysian.client.renderer;

import com.he.lastsongofelysian.client.model.QuGeModel;
import com.he.lastsongofelysian.entity.QuGeEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class QuGeRenderer extends MobRenderer<QuGeEntity, QuGeModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(lastsongofelysian.MODID, "textures/entity/qu_ge.png");

    public QuGeRenderer(EntityRendererProvider.Context context) {
        super(context, new QuGeModel(context.bakeLayer(QuGeModel.LAYER_LOCATION)), 0.55F);
    }

    @Override
    protected void scale(QuGeEntity entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1.05F, 1.05F, 1.05F);
    }

    @Override
    public ResourceLocation getTextureLocation(QuGeEntity entity) {
        return TEXTURE;
    }
}
