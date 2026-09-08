package com.he.lastsongofelysian.mixin;

import com.he.lastsongofelysian.registry.ModItems;
import com.he.lastsongofelysian.util.SignetUpgradeData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {

    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void onServerTick(Level level, BlockPos pos, BlockState state,
                                     AbstractFurnaceBlockEntity furnace, CallbackInfo ci) {
        AABB range = new AABB(pos).inflate(8.0);
        for (Player player : level.getEntitiesOfClass(Player.class, range)) {
            if (CuriosApi.getCuriosHelper()
                    .findFirstCurio(player, stack -> stack.is(ModItems.SIGNET_OF_GOLD.get()))
                    .isPresent()) {
                AbstractFurnaceBlockEntityAccessor accessor = (AbstractFurnaceBlockEntityAccessor) furnace;
                if (!accessor.invokeIsLit()) return;

                int progress = accessor.getCookingProgress();
                int total = accessor.getCookingTotalTime();

                progress += Math.max(1, Math.round(SignetUpgradeData.scaleBonus(
                        player,
                        SignetUpgradeData.Skill.GOLD_STREAM,
                        2.0F
                )));
                if (progress >= total) {
                    progress = total - 1;
                }
                accessor.setCookingProgress(progress);
                break;
            }
        }
    }
}
