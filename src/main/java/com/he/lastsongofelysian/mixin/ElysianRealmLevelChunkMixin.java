package com.he.lastsongofelysian.mixin;

import com.he.lastsongofelysian.dimension.ElysianRealmBlockMutationGuard;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public abstract class ElysianRealmLevelChunkMixin {

    @Shadow
    @Final
    private Level level;

    @Inject(
            method =
                    "setBlockState(Lnet/minecraft/core/BlockPos;"
                            + "Lnet/minecraft/world/level/block/state/"
                            + "BlockState;Z)"
                            + "Lnet/minecraft/world/level/block/state/"
                            + "BlockState;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void lastsongofelysian$lockChunkSetBlockState(
            BlockPos pos,
            BlockState state,
            boolean isMoving,
            CallbackInfoReturnable<BlockState> cir
    ) {
        if (
                ElysianRealmBlockMutationGuard
                        .shouldRejectChange(
                                level,
                                pos,
                                state
                        )
        ) {
            cir.setReturnValue(
                    level.getBlockState(pos)
            );
        }
    }
}
