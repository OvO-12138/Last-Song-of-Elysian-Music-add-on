package com.he.lastsongofelysian.client.renderer;

import com.he.lastsongofelysian.blockentity.YellowPurpleLittleBedBlockEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class YellowPurpleLittleBedRenderer
        implements BlockEntityRenderer<YellowPurpleLittleBedBlockEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    lastsongofelysian.MODID,
                    "textures/entity/bed/yellow_purple_little_bed.png"
            );

    private final ModelPart headRoot;
    private final ModelPart footRoot;

    public YellowPurpleLittleBedRenderer(
            BlockEntityRendererProvider.Context context
    ) {
        this.headRoot = context.bakeLayer(ModelLayers.BED_HEAD);
        this.footRoot = context.bakeLayer(ModelLayers.BED_FOOT);
    }

    public YellowPurpleLittleBedRenderer(EntityModelSet modelSet) {
        this.headRoot = modelSet.bakeLayer(ModelLayers.BED_HEAD);
        this.footRoot = modelSet.bakeLayer(ModelLayers.BED_FOOT);
    }

    @Override
    public void render(
            YellowPurpleLittleBedBlockEntity blockEntity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        BlockState state = blockEntity.getBlockState();
        ModelPart part =
                state.getValue(BedBlock.PART) == BedPart.HEAD
                        ? headRoot
                        : footRoot;

        renderPiece(
                poseStack,
                bufferSource,
                part,
                state.getValue(BedBlock.FACING),
                packedLight,
                packedOverlay,
                false
        );
    }

    public void renderItem(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        renderPiece(
                poseStack,
                bufferSource,
                headRoot,
                Direction.SOUTH,
                packedLight,
                packedOverlay,
                false
        );
        renderPiece(
                poseStack,
                bufferSource,
                footRoot,
                Direction.SOUTH,
                packedLight,
                packedOverlay,
                true
        );
    }

    private static void renderPiece(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            ModelPart part,
            Direction direction,
            int packedLight,
            int packedOverlay,
            boolean moveFoot
    ) {
        poseStack.pushPose();
        poseStack.translate(
                0.0F,
                0.5625F,
                moveFoot ? -1.0F : 0.0F
        );
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(
                Axis.ZP.rotationDegrees(
                        180.0F + direction.toYRot()
                )
        );
        poseStack.translate(-0.5F, -0.5F, -0.5F);

        VertexConsumer vertexConsumer =
                bufferSource.getBuffer(
                        RenderType.entitySolid(TEXTURE)
                );
        part.render(
                poseStack,
                vertexConsumer,
                packedLight,
                packedOverlay
        );
        poseStack.popPose();
    }
}
