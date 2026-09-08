package com.he.lastsongofelysian.client.renderer.layer;

import com.he.lastsongofelysian.client.model.ElysianHeroModel;
import com.he.lastsongofelysian.entity.ElysianHeroNpcEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class AponiaButterflyLayer
        extends RenderLayer<ElysianHeroNpcEntity, ElysianHeroModel> {

    private static final int BUTTERFLY_COUNT = 7;
    private static final float TWO_PI = Mth.PI * 2.0F;

    public AponiaButterflyLayer(
            RenderLayerParent<ElysianHeroNpcEntity, ElysianHeroModel> parent
    ) {
        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            ElysianHeroNpcEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (entity.getVariant() != ElysianHeroNpcEntity.HeroVariant.APONIA) {
            return;
        }

        VertexConsumer consumer = buffer.getBuffer(RenderType.lightning());
        float entityPhase = entity.getId() * 0.371F;

        for (int i = 0; i < BUTTERFLY_COUNT; i++) {
            float seed = entityPhase + TWO_PI * i / BUTTERFLY_COUNT;
            float speed = 0.022F + (i % 3) * 0.004F;
            float orbit = seed + ageInTicks * speed;
            float radius = 0.58F + (i % 3) * 0.13F;
            float worldHeight = 0.42F
                    + (i % 4) * 0.43F
                    + Mth.sin(ageInTicks * 0.055F + seed * 1.7F) * 0.16F;
            float x = Mth.cos(orbit) * radius;
            float z = Mth.sin(orbit) * radius * 0.78F;

            poseStack.pushPose();
            poseStack.translate(x, 1.501F - worldHeight, z);
            poseStack.mulPose(Axis.YP.rotation(-orbit + Mth.HALF_PI));
            poseStack.mulPose(Axis.XP.rotation(
                    Mth.sin(ageInTicks * 0.08F + seed) * 0.18F
            ));

            float scale = 0.82F + (i % 3) * 0.12F;
            poseStack.scale(scale, scale, scale);
            drawButterfly(
                    poseStack,
                    consumer,
                    ageInTicks * (0.66F + (i % 2) * 0.08F) + seed * 3.0F,
                    i
            );
            poseStack.popPose();
        }
    }

    private static void drawButterfly(
            PoseStack poseStack,
            VertexConsumer consumer,
            float flapPhase,
            int index
    ) {
        Matrix4f pose = poseStack.last().pose();
        float flap = Mth.sin(flapPhase);
        float lift = 0.018F + flap * 0.052F;
        int brightAlpha = 150 + index % 3 * 12;
        int softAlpha = 72 + index % 2 * 12;

        drawWingPair(
                pose,
                consumer,
                lift,
                1.18F,
                255,
                222,
                105,
                softAlpha
        );
        drawWingPair(
                pose,
                consumer,
                lift,
                0.92F,
                255,
                234,
                142,
                brightAlpha
        );
        drawBody(pose, consumer);
    }

    private static void drawWingPair(
            Matrix4f pose,
            VertexConsumer consumer,
            float lift,
            float scale,
            int red,
            int green,
            int blue,
            int alpha
    ) {
        for (int side = -1; side <= 1; side += 2) {
            quad(
                    pose,
                    consumer,
                    side * 0.010F, 0.000F, 0.020F,
                    side * 0.075F * scale, lift * 0.72F, 0.060F * scale,
                    side * 0.145F * scale, lift, -0.005F,
                    side * 0.050F * scale, lift * 0.32F, -0.018F,
                    red,
                    green,
                    blue,
                    alpha
            );
            quad(
                    pose,
                    consumer,
                    side * 0.008F, 0.000F, -0.008F,
                    side * 0.052F * scale, lift * 0.42F, -0.018F,
                    side * 0.105F * scale, lift * 0.78F, -0.082F * scale,
                    side * 0.026F * scale, lift * 0.18F, -0.072F * scale,
                    red,
                    green,
                    blue,
                    alpha
            );
        }
    }

    private static void drawBody(
            Matrix4f pose,
            VertexConsumer consumer
    ) {
        quad(
                pose,
                consumer,
                -0.010F, -0.010F, 0.052F,
                0.010F, -0.010F, 0.052F,
                0.010F, 0.010F, -0.070F,
                -0.010F, 0.010F, -0.070F,
                205,
                139,
                34,
                210
        );
        quad(
                pose,
                consumer,
                0.000F, -0.014F, 0.052F,
                0.000F, 0.014F, 0.052F,
                0.000F, 0.014F, -0.070F,
                0.000F, -0.014F, -0.070F,
                255,
                207,
                68,
                190
        );
    }

    private static void quad(
            Matrix4f pose,
            VertexConsumer consumer,
            float x1,
            float y1,
            float z1,
            float x2,
            float y2,
            float z2,
            float x3,
            float y3,
            float z3,
            float x4,
            float y4,
            float z4,
            int red,
            int green,
            int blue,
            int alpha
    ) {
        vertex(pose, consumer, x1, y1, z1, red, green, blue, alpha);
        vertex(pose, consumer, x2, y2, z2, red, green, blue, alpha);
        vertex(pose, consumer, x3, y3, z3, red, green, blue, alpha);
        vertex(pose, consumer, x4, y4, z4, red, green, blue, alpha);
    }

    private static void vertex(
            Matrix4f pose,
            VertexConsumer consumer,
            float x,
            float y,
            float z,
            int red,
            int green,
            int blue,
            int alpha
    ) {
        consumer.vertex(pose, x, y, z)
                .color(red, green, blue, alpha)
                .endVertex();
    }
}
