package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class FlawlessChargeMovementEvents {

    private static final float USE_SLOWDOWN_COMPENSATION =
            5.0F;

    private FlawlessChargeMovementEvents() {
    }

    @SubscribeEvent
    public static void onMovementInput(
            MovementInputUpdateEvent event
    ) {
        Player player = event.getEntity();

        if (!player.isUsingItem()) {
            return;
        }

        ItemStack usedItem = player.getUseItem();

        if (
                !usedItem.is(
                        ModItems
                                .FLAWLESS_BENEDICTION_LEGACY
                                .get()
                )
        ) {
            return;
        }

        event.getInput().leftImpulse *=
                USE_SLOWDOWN_COMPENSATION;

        event.getInput().forwardImpulse *=
                USE_SLOWDOWN_COMPENSATION;
    }

    @SubscribeEvent
    public static void onClientTick(
            TickEvent.ClientTickEvent event
    ) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Player player = Minecraft.getInstance().player;

        if (
                player == null
                        || !player.isUsingItem()
                        || !player.getUseItem().is(
                                ModItems.FLAWLESS_BENEDICTION_LEGACY.get()
                        )
        ) {
            return;
        }

        BlockPos fluidPos = findExposedFluidBelow(player);

        if (fluidPos == null) {
            return;
        }

        FluidState fluidState =
                player.level().getFluidState(fluidPos);

        double surfaceY =
                fluidPos.getY()
                        + fluidState.getHeight(
                                player.level(),
                                fluidPos
                        );

        if (player.getY() < surfaceY) {
            return;
        }

        Vec3 motion = player.getDeltaMovement();

        if (motion.y > 0.08D) {
            return;
        }

        player.setPos(
                player.getX(),
                surfaceY + 0.02D,
                player.getZ()
        );
        player.setDeltaMovement(
                motion.x,
                0.0D,
                motion.z
        );
        player.fallDistance = 0.0F;
        player.setOnGround(true);
    }

    private static BlockPos findExposedFluidBelow(
            Player player
    ) {
        BlockPos origin = player.blockPosition();

        for (int depth = 0; depth <= 2; depth++) {
            BlockPos candidate = origin.below(depth);
            FluidState fluid =
                    player.level().getFluidState(candidate);

            if (
                    !fluid.is(FluidTags.WATER)
                            && !fluid.is(FluidTags.LAVA)
            ) {
                continue;
            }

            FluidState above =
                    player.level().getFluidState(
                            candidate.above()
                    );

            if (
                    fluid.is(FluidTags.WATER)
                            && above.is(FluidTags.WATER)
                            || fluid.is(FluidTags.LAVA)
                            && above.is(FluidTags.LAVA)
            ) {
                continue;
            }

            double surfaceY =
                    candidate.getY()
                            + fluid.getHeight(
                                    player.level(),
                                    candidate
                            );

            double distance = player.getY() - surfaceY;

            if (distance >= 0.0D && distance <= 0.65D) {
                return candidate.immutable();
            }
        }

        return null;
    }

}
