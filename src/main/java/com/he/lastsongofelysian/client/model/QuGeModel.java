package com.he.lastsongofelysian.client.model;

import com.he.lastsongofelysian.entity.QuGeEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.client.model.HierarchicalModel;
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

public class QuGeModel extends HierarchicalModel<QuGeEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(lastsongofelysian.MODID, "qu_ge"),
            "main"
    );

    private final ModelPart root;
    private final ModelPart segment0;
    private final ModelPart segment1;
    private final ModelPart segment2;
    private final ModelPart segment3;
    private final ModelPart segment4;
    private final ModelPart segment5;

    public QuGeModel(ModelPart root) {
        this.root = root;
        this.segment0 = root.getChild("segment0");
        this.segment1 = this.segment0.getChild("segment1");
        this.segment2 = this.segment1.getChild("segment2");
        this.segment3 = this.segment2.getChild("segment3");
        this.segment4 = this.segment3.getChild("segment4");
        this.segment5 = this.segment4.getChild("segment5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition segment0 = root.addOrReplaceChild(
                "segment0",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.5F, -3.5F, -3.0F, 7.0F, 7.0F, 6.0F, new CubeDeformation(0.20F)),
                PartPose.offset(0.0F, 20.0F, -12.0F)
        );

        PartDefinition segment1 = segment0.addOrReplaceChild(
                "segment1",
                CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 6.0F, new CubeDeformation(0.22F)),
                PartPose.offset(0.0F, 0.0F, 3.0F)
        );

        PartDefinition segment2 = segment1.addOrReplaceChild(
                "segment2",
                CubeListBuilder.create()
                        .texOffs(28, 0)
                        .addBox(-4.5F, -4.5F, 0.0F, 9.0F, 9.0F, 6.0F, new CubeDeformation(0.24F)),
                PartPose.offset(0.0F, 0.0F, 5.0F)
        );

        PartDefinition segment3 = segment2.addOrReplaceChild(
                "segment3",
                CubeListBuilder.create()
                        .texOffs(28, 16)
                        .addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 6.0F, new CubeDeformation(0.22F)),
                PartPose.offset(0.0F, 0.0F, 5.0F)
        );

        PartDefinition segment4 = segment3.addOrReplaceChild(
                "segment4",
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(-3.5F, -3.5F, 0.0F, 7.0F, 7.0F, 5.0F, new CubeDeformation(0.18F)),
                PartPose.offset(0.0F, 0.0F, 5.0F)
        );

        segment4.addOrReplaceChild(
                "segment5",
                CubeListBuilder.create()
                        .texOffs(24, 34)
                        .addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.12F)),
                PartPose.offset(0.0F, 0.0F, 4.0F)
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            QuGeEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        float moving = Mth.clamp(limbSwingAmount * 2.2F, 0.0F, 1.0F);
        float phase = ageInTicks * (0.16F + moving * 0.34F) + limbSwing * 0.45F;
        float sideStrength = 0.055F + moving * 0.15F;
        float liftStrength = 0.025F + moving * 0.075F;

        this.segment0.yRot = Mth.sin(phase) * sideStrength * 0.45F;
        this.segment1.yRot = Mth.sin(phase - 0.72F) * sideStrength;
        this.segment2.yRot = Mth.sin(phase - 1.44F) * sideStrength;
        this.segment3.yRot = Mth.sin(phase - 2.16F) * sideStrength;
        this.segment4.yRot = Mth.sin(phase - 2.88F) * sideStrength * 1.15F;
        this.segment5.yRot = Mth.sin(phase - 3.60F) * sideStrength * 1.35F;

        this.segment0.xRot = Mth.cos(phase) * liftStrength * 0.40F;
        this.segment1.xRot = Mth.cos(phase - 0.72F) * liftStrength;
        this.segment2.xRot = Mth.cos(phase - 1.44F) * liftStrength;
        this.segment3.xRot = Mth.cos(phase - 2.16F) * liftStrength;
        this.segment4.xRot = Mth.cos(phase - 2.88F) * liftStrength;
        this.segment5.xRot = Mth.cos(phase - 3.60F) * liftStrength * 1.20F;

        this.segment0.y = 20.0F - Mth.abs(Mth.sin(phase * 0.78F)) * (0.22F + moving * 0.32F);
        this.segment0.zRot = Mth.sin(phase * 0.52F) * 0.025F;
    }
}
