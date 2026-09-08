package com.he.lastsongofelysian.client.renderer;

import com.he.lastsongofelysian.client.model.PardofelisMerchantModel;
import com.he.lastsongofelysian.client.renderer.layer.PardofelisAppleLayer;
import com.he.lastsongofelysian.entity.PardofelisMerchantEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PardofelisMerchantRenderer extends HumanoidMobRenderer<PardofelisMerchantEntity, PardofelisMerchantModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(lastsongofelysian.MODID, "textures/entity/pardofelis_merchant.png");

    public PardofelisMerchantRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new PardofelisMerchantModel(context.bakeLayer(PardofelisMerchantModel.LAYER_LOCATION)),
                0.5F
        );
        this.addLayer(new PardofelisAppleLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(PardofelisMerchantEntity entity) {
        return TEXTURE;
    }
}
