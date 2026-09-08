package com.he.lastsongofelysian.mixin;

import com.he.lastsongofelysian.dimension.ElysianRealmBlockMutationGuard;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class ElysianRealmLevelMixin {

    @Inject(
            method =
                    "setBlock(Lnet/minecraft/core/BlockPos;"
                            + "Lnet/minecraft/world/level/block/state/"
                            + "BlockState;I)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void lastsongofelysian$lockSetBlock(
            BlockPos pos,
            BlockState state,
            int flags,
            CallbackInfoReturnable<Boolean> cir
    ) {
        Level level =
                (Level) (Object) this;

        if (
                ElysianRealmBlockMutationGuard
                        .shouldRejectChange(
                                level,
                                pos,
                                state
                        )
        ) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method =
                    "setBlock(Lnet/minecraft/core/BlockPos;"
                            + "Lnet/minecraft/world/level/block/state/"
                            + "BlockState;II)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void lastsongofelysian$lockSetBlockWithDepth(
            BlockPos pos,
            BlockState state,
            int flags,
            int recursionLeft,
            CallbackInfoReturnable<Boolean> cir
    ) {
        Level level =
                (Level) (Object) this;

        if (
                ElysianRealmBlockMutationGuard
                        .shouldRejectChange(
                                level,
                                pos,
                                state
                        )
        ) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method =
                    "removeBlock(Lnet/minecraft/core/BlockPos;Z)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void lastsongofelysian$lockRemoveBlock(
            BlockPos pos,
            boolean isMoving,
            CallbackInfoReturnable<Boolean> cir
    ) {
        Level level =
                (Level) (Object) this;

        if (
                ElysianRealmBlockMutationGuard
                        .shouldRejectRemoval(level)
        ) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method =
                    "destroyBlock(Lnet/minecraft/core/BlockPos;Z"
                            + "Lnet/minecraft/world/entity/Entity;I)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void lastsongofelysian$lockDestroyBlock(
            BlockPos pos,
            boolean dropBlock,
            @Nullable Entity breaker,
            int recursionLeft,
            CallbackInfoReturnable<Boolean> cir
    ) {
        Level level =
                (Level) (Object) this;

        if (
                ElysianRealmBlockMutationGuard
                        .shouldRejectRemoval(level)
        ) {
            cir.setReturnValue(false);
        }
    }
}
