package com.he.lastsongofelysian.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.he.lastsongofelysian.entity.PardofelisMerchantEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PardofelisMerchantModel extends PlayerModel<PardofelisMerchantEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(lastsongofelysian.MODID, "pardofelis_merchant"),
            "main"
    );

    private static final String RIGHT_EAR = "pardofelis_right_ear";
    private static final String LEFT_EAR = "pardofelis_left_ear";
    private static final String TAIL = "pardofelis_tail";
    private static final String TAIL_TIP = "pardofelis_tail_tip";
    private static final String TAIL_END = "pardofelis_tail_end";
    private static final String TAIL_SPIKE = "pardofelis_tail_spike";
    private static final String APPLE = "pardofelis_tail_apple";
    private static final String APPLE_STEM = "pardofelis_apple_stem";
    private static final String APPLE_LEAF = "pardofelis_apple_leaf";

    private final ModelPart tail;
    private final ModelPart tailTip;
    private final ModelPart tailEnd;
    private final ModelPart apple;

    public PardofelisMerchantModel(ModelPart root) {
        super(root, true);
        this.tail = this.body.getChild(TAIL);
        this.tailTip = this.tail.getChild(TAIL_TIP);
        this.tailEnd = this.tailTip.getChild(TAIL_END);
        this.apple = this.tailEnd.getChild(APPLE);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, true);
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");
        PartDefinition body = root.getChild("body");

        head.addOrReplaceChild(
                RIGHT_EAR,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -10.4F, -1.65F, 3.0F, 2.4F, 1.3F, new CubeDeformation(0.12F))
                        .texOffs(0, 0)
                        .addBox(-3.65F, -11.15F, -1.55F, 2.3F, 0.9F, 1.1F, new CubeDeformation(0.12F)),
                PartPose.rotation(0.0F, 0.0F, -0.04F)
        );

        head.addOrReplaceChild(
                LEFT_EAR,
                CubeListBuilder.create()
                        .texOffs(56, 0)
                        .addBox(1.0F, -10.4F, -1.65F, 3.0F, 2.4F, 1.3F, new CubeDeformation(0.12F))
                        .texOffs(56, 0)
                        .addBox(1.35F, -11.15F, -1.55F, 2.3F, 0.9F, 1.1F, new CubeDeformation(0.12F)),
                PartPose.rotation(0.0F, 0.0F, 0.04F)
        );

        PartDefinition tail = body.addOrReplaceChild(
                TAIL,
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(-1.25F, 0.0F, -1.25F, 2.5F, 6.0F, 2.5F, new CubeDeformation(0.10F)),
                PartPose.offsetAndRotation(0.0F, 9.4F, 2.0F, 0.92F, 0.0F, 0.0F)
        );

        PartDefinition tailTip = tail.addOrReplaceChild(
                TAIL_TIP,
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(-1.1F, 0.0F, -1.1F, 2.2F, 5.5F, 2.2F, new CubeDeformation(0.10F)),
                PartPose.offsetAndRotation(0.0F, 5.45F, 0.0F, 0.58F, 0.0F, 0.0F)
        );

        PartDefinition tailEnd = tailTip.addOrReplaceChild(
                TAIL_END,
                CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(-0.9F, 0.0F, -0.9F, 1.8F, 4.4F, 1.8F, new CubeDeformation(0.09F)),
                PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.48F, 0.0F, 0.0F)
        );

        PartDefinition apple = tailEnd.addOrReplaceChild(
                APPLE,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.65F, -1.5F, -1.65F, 3.3F, 3.0F, 3.3F, new CubeDeformation(0.08F))
                        .texOffs(0, 8)
                        .addBox(-1.35F, -1.85F, -1.35F, 2.7F, 0.45F, 2.7F, new CubeDeformation(0.04F))
                        .texOffs(0, 12)
                        .addBox(-1.35F, 1.4F, -1.35F, 2.7F, 0.45F, 2.7F, new CubeDeformation(0.04F)),
                PartPose.offsetAndRotation(0.0F, 4.05F, 0.0F, 0.04F, 0.0F, 0.10F)
        );

        apple.addOrReplaceChild(
                TAIL_SPIKE,
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-0.42F, -0.9F, -0.42F, 0.84F, 4.25F, 0.84F, new CubeDeformation(0.04F)),
                PartPose.ZERO
        );

        apple.addOrReplaceChild(
                APPLE_STEM,
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-0.3F, -2.9F, -0.3F, 0.6F, 1.4F, 0.6F, new CubeDeformation(0.03F)),
                PartPose.ZERO
        );

        apple.addOrReplaceChild(
                APPLE_LEAF,
                CubeListBuilder.create()
                        .texOffs(48, 0)
                        .addBox(0.1F, -2.7F, -0.05F, 1.5F, 0.3F, 0.9F, new CubeDeformation(0.03F)),
                PartPose.rotation(0.0F, -0.35F, -0.25F)
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        boolean wasVisible = this.apple.visible;
        this.apple.visible = false;
        super.renderToBuffer(
                poseStack,
                vertexConsumer,
                packedLight,
                packedOverlay,
                red,
                green,
                blue,
                alpha
        );
        this.apple.visible = wasVisible;
    }

    public void renderApple(
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            int packedLight,
            int packedOverlay
    ) {
        poseStack.pushPose();
        this.body.translateAndRotate(poseStack);
        this.tail.translateAndRotate(poseStack);
        this.tailTip.translateAndRotate(poseStack);
        this.tailEnd.translateAndRotate(poseStack);
        this.apple.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(
            PardofelisMerchantEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        float walkSway = Mth.cos(limbSwing * 0.6662F) * 0.14F * limbSwingAmount;
        float idleSway = Mth.sin(ageInTicks * 0.10F) * 0.12F;

        this.tail.xRot = 0.92F + walkSway * 0.45F;
        this.tail.yRot = 0.18F + idleSway * 0.45F;
        this.tail.zRot = Mth.cos(ageInTicks * 0.08F) * 0.025F;
        this.tailTip.xRot = 0.58F + Mth.sin(ageInTicks * 0.12F) * 0.050F;
        this.tailTip.yRot = 0.10F + idleSway * 0.28F;
        this.tailEnd.xRot = 0.48F + Mth.sin(ageInTicks * 0.14F) * 0.040F;
        this.tailEnd.yRot = 0.06F + idleSway * 0.18F;
    }
}
