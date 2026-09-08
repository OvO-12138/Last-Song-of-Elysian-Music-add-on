package com.he.lastsongofelysian.mixin;

import com.he.lastsongofelysian.dimension.ElysianRealmBlockMutationGuard;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class ElysianRealmServerPlayerGameModeMixin {

    @Shadow
    @Final
    protected ServerPlayer player;

    @Inject(
            method = "destroyBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void lastsongofelysian$lockPlayerDestroyBlock(
            BlockPos pos,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (
                ElysianRealmBlockMutationGuard
                        .isProtectedLevel(
                                player.level()
                        )
        ) {
            cir.setReturnValue(false);
        }
    }
}
