package com.he.lastsongofelysian.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.he.lastsongofelysian.entity.ElysianHeroNpcEntity;
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

public class ElysianHeroModel
        extends PlayerModel<ElysianHeroNpcEntity> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    new ResourceLocation(
                            lastsongofelysian.MODID,
                            "elysian_hero_npc"
                    ),
                    "main"
            );

    private static final String CHEST_GEAR = "vill_v_chest_gear";
    private static final String WAIST_GEAR = "vill_v_waist_gear";

    private final ModelPart chestGear;
    private final ModelPart waistGear;

    public ElysianHeroModel(
            ModelPart root
    ) {
        super(root, true);
        this.chestGear = this.body.getChild(CHEST_GEAR);
        this.waistGear = this.body.getChild(WAIST_GEAR);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh =
                PlayerModel.createMesh(
                        CubeDeformation.NONE,
                        true
                );

        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.getChild("body");

        body.addOrReplaceChild(
                CHEST_GEAR,
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(-0.88F, -0.88F, -0.28F, 1.76F, 1.76F, 0.56F, new CubeDeformation(0.03F))
                        .texOffs(16, 32)
                        .addBox(-0.32F, -1.35F, -0.31F, 0.64F, 0.47F, 0.62F, new CubeDeformation(0.03F))
                        .addBox(-0.32F, 0.88F, -0.31F, 0.64F, 0.47F, 0.62F, new CubeDeformation(0.03F))
                        .addBox(-1.35F, -0.32F, -0.31F, 0.47F, 0.64F, 0.62F, new CubeDeformation(0.03F))
                        .addBox(0.88F, -0.32F, -0.31F, 0.47F, 0.64F, 0.62F, new CubeDeformation(0.03F))
                        .texOffs(28, 32)
                        .addBox(-0.32F, -0.32F, -0.42F, 0.64F, 0.64F, 0.3F, new CubeDeformation(0.02F)),
                PartPose.offset(2.05F, 4.0F, -2.15F)
        );

        body.addOrReplaceChild(
                WAIST_GEAR,
                CubeListBuilder.create()
                        .texOffs(0, 44)
                        .addBox(-0.68F, -0.68F, -0.25F, 1.36F, 1.36F, 0.5F, new CubeDeformation(0.03F))
                        .texOffs(12, 44)
                        .addBox(-0.27F, -1.02F, -0.28F, 0.54F, 0.34F, 0.56F, new CubeDeformation(0.03F))
                        .addBox(-0.27F, 0.68F, -0.28F, 0.54F, 0.34F, 0.56F, new CubeDeformation(0.03F))
                        .addBox(-1.02F, -0.27F, -0.28F, 0.34F, 0.54F, 0.56F, new CubeDeformation(0.03F))
                        .addBox(0.68F, -0.27F, -0.28F, 0.34F, 0.54F, 0.56F, new CubeDeformation(0.03F)),
                PartPose.offset(-2.55F, 9.55F, -2.1F)
        );

        return LayerDefinition.create(
                mesh,
                64,
                64
        );
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
        boolean chestVisible = this.chestGear.visible;
        boolean waistVisible = this.waistGear.visible;
        this.chestGear.visible = false;
        this.waistGear.visible = false;
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
        this.chestGear.visible = chestVisible;
        this.waistGear.visible = waistVisible;
    }

    public void renderVillVAccessories(
            PoseStack poseStack,
            VertexConsumer gearConsumer,
            int packedLight,
            int packedOverlay
    ) {
        poseStack.pushPose();
        this.body.translateAndRotate(poseStack);
        this.chestGear.render(
                poseStack,
                gearConsumer,
                packedLight,
                packedOverlay,
                0.72F,
                0.62F,
                0.46F,
                0.62F
        );
        this.waistGear.render(
                poseStack,
                gearConsumer,
                packedLight,
                packedOverlay,
                0.72F,
                0.62F,
                0.46F,
                0.58F
        );
        poseStack.popPose();
    }

    @Override
    public void setupAnim(
            ElysianHeroNpcEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        super.setupAnim(
                entity,
                limbSwing,
                limbSwingAmount,
                ageInTicks,
                netHeadYaw,
                headPitch
        );

        float idleSlow =
                Mth.sin(
                        ageInTicks * 0.075F
                );

        float idleFast =
                Mth.sin(
                        ageInTicks * 0.13F
                );

        float movement =
                Mth.clamp(
                        limbSwingAmount,
                        0.0F,
                        1.0F
                );

        boolean villV = entity.getVariant() == ElysianHeroNpcEntity.HeroVariant.VILL_V;
        this.chestGear.visible = villV;
        this.waistGear.visible = villV;
        if (villV) {
            this.chestGear.zRot = ageInTicks * 0.012F;
            this.waistGear.zRot = -ageInTicks * 0.014F;
        }

        this.head.yRot =
                Mth.clamp(
                        this.head.yRot,
                        -0.62F,
                        0.62F
                );

        this.head.xRot =
                Mth.clamp(
                        this.head.xRot,
                        -0.35F,
                        0.45F
                );

        this.head.zRot = 0.0F;
        this.body.xRot = idleSlow * 0.004F;

        switch (entity.getVariant()) {
            case APONIA -> {
                this.leftArm.zRot =
                        -0.035F
                                - idleSlow * 0.006F;

                this.rightArm.zRot =
                        0.035F
                                + idleSlow * 0.006F;
            }

            case ELYSIA -> {
                this.leftArm.zRot =
                        -0.025F
                                - idleFast * 0.010F;

                this.rightArm.zRot =
                        0.025F
                                + idleFast * 0.010F;
            }

            case GRISEO -> {
                this.leftArm.zRot =
                        -0.012F
                                - idleSlow * 0.004F;

                this.rightArm.zRot =
                        0.012F
                                + idleSlow * 0.004F;
            }

            case VILL_V -> {
                this.leftArm.zRot =
                        -0.030F
                                - movement * 0.015F;

                this.rightArm.zRot =
                        0.030F
                                + movement * 0.015F;
            }

            case EDEN -> {
                this.leftArm.zRot =
                        -0.045F
                                - idleSlow * 0.005F;

                this.rightArm.zRot =
                        0.045F
                                + idleSlow * 0.005F;
            }
        }

        this.hat.copyFrom(
                this.head
        );

        this.jacket.copyFrom(
                this.body
        );

        this.leftSleeve.copyFrom(
                this.leftArm
        );

        this.rightSleeve.copyFrom(
                this.rightArm
        );

        this.leftPants.copyFrom(
                this.leftLeg
        );

        this.rightPants.copyFrom(
                this.rightLeg
        );
    }
}
