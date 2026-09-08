package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.lastsongofelysian;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class SakuraDodgeVisual {

    private static final int DURATION = 26;
    private static int remainingTicks = 0;

    private SakuraDodgeVisual() {
    }

    public static void trigger() {
        remainingTicks = DURATION;
    }

    @SubscribeEvent
    public static void onClientTick(
            TickEvent.ClientTickEvent event
    ) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (
                remainingTicks <= 0 ||
                minecraft.level == null ||
                minecraft.player == null
        ) {
            return;
        }

        remainingTicks--;

        if (remainingTicks % 2 != 0) {
            return;
        }

        Player player = minecraft.player;
        double phase = (
                DURATION - remainingTicks
        ) * 0.42D;

        for (int i = 0; i < 2; i++) {
            double angle = phase + Math.PI * i;
            double radius = 1.05D + i * 0.28D;

            minecraft.level.addParticle(
                    ParticleTypes.CHERRY_LEAVES,
                    player.getX() + Math.cos(angle) * radius,
                    player.getY() + 0.30D + i * 0.72D,
                    player.getZ() + Math.sin(angle) * radius,
                    -Math.sin(angle) * 0.018D,
                    0.012D,
                    Math.cos(angle) * 0.018D
            );
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(
            RenderLevelStageEvent event
    ) {
        if (
                event.getStage()
                        != RenderLevelStageEvent.Stage.AFTER_PARTICLES ||
                remainingTicks <= 0
        ) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (
                minecraft.level == null ||
                minecraft.player == null
        ) {
            return;
        }

        float partialTick = event.getPartialTick();
        float progress = Mth.clamp(
                (DURATION - remainingTicks + partialTick)
                        / DURATION,
                0.0F,
                1.0F
        );
        float alpha = Mth.sin(progress * Mth.PI);
        double radius = 1.02D + progress * 0.55D;
        double rotation = progress * Math.PI * 2.8D;
        Vec3 center = minecraft.player
                .getPosition(partialTick)
                .add(0.0D, 0.16D, 0.0D);
        Vec3 camera = event.getCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffers =
                minecraft.renderBuffers().bufferSource();
        RenderType renderType = RenderType.lines();
        VertexConsumer consumer = buffers.getBuffer(renderType);

        poseStack.pushPose();
        poseStack.translate(-camera.x, -camera.y, -camera.z);

        drawRing(
                poseStack,
                consumer,
                center,
                radius,
                rotation,
                255,
                116,
                185,
                alpha * 0.92F
        );
        drawRing(
                poseStack,
                consumer,
                center.add(0.0D, 0.82D, 0.0D),
                radius * 0.82D,
                -rotation * 0.72D,
                255,
                226,
                246,
                alpha * 0.70F
        );
        drawPetals(
                poseStack,
                consumer,
                center.add(0.0D, 0.48D, 0.0D),
                radius,
                rotation,
                alpha
        );

        poseStack.popPose();
        buffers.endBatch(renderType);
    }

    private static void drawRing(
            PoseStack poseStack,
            VertexConsumer consumer,
            Vec3 center,
            double radius,
            double rotation,
            int red,
            int green,
            int blue,
            float alpha
    ) {
        int segments = 72;

        for (int i = 0; i < segments; i++) {
            double first = rotation
                    + Math.PI * 2.0D * i / segments;
            double second = rotation
                    + Math.PI * 2.0D * (i + 1) / segments;
            Vec3 from = center.add(
                    Math.cos(first) * radius,
                    Math.sin(first * 3.0D) * 0.035D,
                    Math.sin(first) * radius
            );
            Vec3 to = center.add(
                    Math.cos(second) * radius,
                    Math.sin(second * 3.0D) * 0.035D,
                    Math.sin(second) * radius
            );

            line(
                    poseStack,
                    consumer,
                    from,
                    to,
                    red,
                    green,
                    blue,
                    alpha
            );
        }
    }

    private static void drawPetals(
            PoseStack poseStack,
            VertexConsumer consumer,
            Vec3 center,
            double radius,
            double rotation,
            float alpha
    ) {
        for (int i = 0; i < 8; i++) {
            double angle = rotation
                    + Math.PI * 2.0D * i / 8.0D;
            Vec3 origin = center.add(
                    Math.cos(angle) * radius,
                    Math.sin(angle * 2.0D) * 0.30D,
                    Math.sin(angle) * radius
            );
            Vec3 tangent = new Vec3(
                    -Math.sin(angle),
                    0.0D,
                    Math.cos(angle)
            );
            Vec3 radial = new Vec3(
                    Math.cos(angle),
                    0.0D,
                    Math.sin(angle)
            );
            Vec3 tip = origin
                    .add(radial.scale(0.22D))
                    .add(0.0D, 0.14D, 0.0D);
            Vec3 left = origin
                    .add(tangent.scale(0.10D))
                    .add(0.0D, -0.05D, 0.0D);
            Vec3 right = origin
                    .subtract(tangent.scale(0.10D))
                    .add(0.0D, -0.05D, 0.0D);

            line(poseStack, consumer, left, tip, 255, 142, 202, alpha);
            line(poseStack, consumer, tip, right, 255, 226, 246, alpha);
            line(poseStack, consumer, right, left, 255, 108, 178, alpha * 0.75F);
        }
    }

    private static void line(
            PoseStack poseStack,
            VertexConsumer consumer,
            Vec3 from,
            Vec3 to,
            int red,
            int green,
            int blue,
            float alpha
    ) {
        Vec3 normal = to.subtract(from);

        if (normal.lengthSqr() < 0.000001D) {
            return;
        }

        normal = normal.normalize();
        Matrix4f pose = poseStack.last().pose();
        Matrix3f normalMatrix = poseStack.last().normal();
        int opacity = Mth.clamp(
                Math.round(alpha * 255.0F),
                0,
                255
        );

        consumer.vertex(
                        pose,
                        (float) from.x,
                        (float) from.y,
                        (float) from.z
                )
                .color(red, green, blue, opacity)
                .normal(
                        normalMatrix,
                        (float) normal.x,
                        (float) normal.y,
                        (float) normal.z
                )
                .endVertex();
        consumer.vertex(
                        pose,
                        (float) to.x,
                        (float) to.y,
                        (float) to.z
                )
                .color(red, green, blue, opacity)
                .normal(
                        normalMatrix,
                        (float) normal.x,
                        (float) normal.y,
                        (float) normal.z
                )
                .endVertex();
    }
}
