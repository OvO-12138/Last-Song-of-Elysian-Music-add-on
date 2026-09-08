package com.he.lastsongofelysian.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.he.lastsongofelysian.entity.CorrosionMirrorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class CorrosionMirrorRenderer extends
        MobRenderer<CorrosionMirrorEntity, PlayerModel<CorrosionMirrorEntity>> {

    public CorrosionMirrorRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new PlayerModel<>(
                        context.bakeLayer(ModelLayers.PLAYER),
                        false
                ),
                0.50F
        );

        this.addLayer(
                new HumanoidArmorLayer<>(
                        this,
                        new HumanoidModel<>(
                                context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)
                        ),
                        new HumanoidModel<>(
                                context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)
                        ),
                        context.getModelManager()
                )
        );

        this.addLayer(
                new ItemInHandLayer<>(
                        this,
                        context.getItemInHandRenderer()
                )
        );
    }

    @Override
    public void render(
            CorrosionMirrorEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        HumanoidModel.ArmPose mainPose = getMainHandPose(entity);
        HumanoidModel.ArmPose offPose = entity.getOffhandItem().isEmpty()
                ? HumanoidModel.ArmPose.EMPTY
                : HumanoidModel.ArmPose.ITEM;

        if (entity.getMainArm() == HumanoidArm.RIGHT) {
            this.model.rightArmPose = mainPose;
            this.model.leftArmPose = offPose;
        } else {
            this.model.leftArmPose = mainPose;
            this.model.rightArmPose = offPose;
        }

        this.model.crouching = entity.isCrouching();

        super.render(
                entity,
                entityYaw,
                partialTick,
                poseStack,
                buffer,
                packedLight
        );
    }

    private HumanoidModel.ArmPose getMainHandPose(
            CorrosionMirrorEntity entity
    ) {
        ItemStack mainHand = entity.getMainHandItem();
        if (mainHand.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        }

        if (entity.isUsingItem()) {
            ItemStack useItem = entity.getUseItem();

            if (useItem.getItem() instanceof BowItem) {
                return HumanoidModel.ArmPose.BOW_AND_ARROW;
            }

            if (useItem.getItem() instanceof CrossbowItem) {
                return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
            }
        }

        if (mainHand.getItem() instanceof CrossbowItem) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        }

        return HumanoidModel.ArmPose.ITEM;
    }

    @Override
    public ResourceLocation getTextureLocation(CorrosionMirrorEntity entity) {
        UUID ownerUUID = entity.getOwnerUUID();
        UUID skinUUID = ownerUUID != null ? ownerUUID : entity.getUUID();

        if (ownerUUID != null && Minecraft.getInstance().getConnection() != null) {
            PlayerInfo info = Minecraft.getInstance()
                    .getConnection()
                    .getPlayerInfo(ownerUUID);

            if (info != null) {
                return info.getSkinLocation();
            }
        }

        return DefaultPlayerSkin.getDefaultSkin(skinUUID);
    }
}
