package com.he.lastsongofelysian.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class YellowPurpleLittleBedItemRenderer
        extends BlockEntityWithoutLevelRenderer {

    private final YellowPurpleLittleBedRenderer bedRenderer;

    public YellowPurpleLittleBedItemRenderer(
            BlockEntityRenderDispatcher dispatcher,
            EntityModelSet modelSet
    ) {
        super(dispatcher, modelSet);
        this.bedRenderer =
                new YellowPurpleLittleBedRenderer(modelSet);
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 0.5F);
        poseStack.scale(0.7F, 0.7F, 0.7F);
        bedRenderer.renderItem(
                poseStack,
                bufferSource,
                packedLight,
                packedOverlay
        );
        poseStack.popPose();
    }
}
