package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.registry.ModEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class FlawlessBloomLayerRenderer {

    private FlawlessBloomLayerRenderer() {
    }

    @SubscribeEvent
    public static void onRenderLevel(
            RenderLevelStageEvent event
    ) {
        if (
                event.getStage() !=
                        RenderLevelStageEvent.Stage.AFTER_PARTICLES
        ) {
            return;
        }

        Minecraft minecraft =
                Minecraft.getInstance();

        if (
                minecraft.level == null ||
                minecraft.player == null
        ) {
            FlawlessBloomClientData.clear();
            return;
        }

        AABB range =
                minecraft.player
                        .getBoundingBox()
                        .inflate(96.0D);

        List<LivingEntity> entities =
                minecraft.level.getEntitiesOfClass(
                        LivingEntity.class,
                        range,
                        LivingEntity::isAlive
                );

        if (entities.isEmpty()) {
            return;
        }

        PoseStack poseStack =
                event.getPoseStack();

        MultiBufferSource.BufferSource buffers =
                minecraft.renderBuffers()
                        .bufferSource();

        Vec3 camera =
                event.getCamera()
                        .getPosition();

        float partialTick =
                event.getPartialTick();

        Font font =
                minecraft.font;

        for (LivingEntity entity : entities) {
            int stacks =
                    FlawlessBloomClientData.getStacks(
                            entity.getUUID()
                    );

            if (stacks <= 0) {
                MobEffectInstance bloom =
                        entity.getEffect(
                                ModEffects.FLAWLESS_BLOOM.get()
                        );

                if (bloom != null) {
                    stacks = Math.min(
                            3,
                            bloom.getAmplifier() + 1
                    );
                }
            }

            if (stacks <= 0) {
                continue;
            }

            double renderX =
                    Mth.lerp(
                            partialTick,
                            entity.xOld,
                            entity.getX()
                    ) - camera.x;

            double renderY =
                    Mth.lerp(
                            partialTick,
                            entity.yOld,
                            entity.getY()
                    )
                            + entity.getBbHeight()
                            + 0.75D
                            - camera.y;

            double renderZ =
                    Mth.lerp(
                            partialTick,
                            entity.zOld,
                            entity.getZ()
                    ) - camera.z;

            Component text =
                    Component.literal(
                            "真我之绽 × " + stacks
                    );

            poseStack.pushPose();

            poseStack.translate(
                    renderX,
                    renderY,
                    renderZ
            );

            poseStack.mulPose(
                    minecraft
                            .getEntityRenderDispatcher()
                            .cameraOrientation()
            );

            poseStack.scale(
                    -0.025F,
                    -0.025F,
                    0.025F
            );

            float x =
                    -font.width(text) / 2.0F;

            font.drawInBatch(
                    text,
                    x,
                    0.0F,
                    0xFFFF69B4,
                    false,
                    poseStack.last().pose(),
                    buffers,
                    Font.DisplayMode.SEE_THROUGH,
                    0x88000000,
                    15728880
            );

            font.drawInBatch(
                    text,
                    x,
                    0.0F,
                    0xFFFFB6E3,
                    false,
                    poseStack.last().pose(),
                    buffers,
                    Font.DisplayMode.NORMAL,
                    0,
                    15728880
            );

            poseStack.popPose();
        }

        buffers.endBatch();
    }
}
