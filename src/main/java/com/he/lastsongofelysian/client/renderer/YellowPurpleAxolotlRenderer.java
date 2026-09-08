package com.he.lastsongofelysian.client.renderer;

import com.he.lastsongofelysian.entity.YellowPurpleAxolotlEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class YellowPurpleAxolotlRenderer
        extends MobRenderer<YellowPurpleAxolotlEntity, AxolotlModel<YellowPurpleAxolotlEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            lastsongofelysian.MODID,
            "textures/entity/yellow_purple_little_axolotl.png"
    );

    public YellowPurpleAxolotlRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new AxolotlModel<>(context.bakeLayer(ModelLayers.AXOLOTL)),
                0.35F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(YellowPurpleAxolotlEntity axolotl) {
        return TEXTURE;
    }
}
