package com.he.lastsongofelysian.mixin;

import com.he.lastsongofelysian.registry.ModItems;
import com.he.lastsongofelysian.util.SignetUpgradeData;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {

    @Unique
    private Player lastsongofelysian$player;

    @Shadow
    public abstract void setMaximumCost(int cost);

    @Shadow
    public int repairItemCountCost;

    @Inject(
            method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
            at = @At("TAIL")
    )
    private void onInit(int containerId, Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        this.lastsongofelysian$player = inventory.player;
    }

    @Inject(method = "createResult", at = @At("TAIL"))
    private void onCreateResultTail(CallbackInfo ci) {
        if (lastsongofelysian$player == null) return;

        boolean hasHelix = CuriosApi.getCuriosHelper()
                .findFirstCurio(lastsongofelysian$player, stack -> stack.is(ModItems.SIGNET_OF_HELIX.get()))
                .isPresent();

        if (hasHelix) {
            this.setMaximumCost(1);
            this.repairItemCountCost = 1;
            ItemStack result = ((AnvilMenu) (Object) this).getSlot(2).getItem();
            float bonus = SignetUpgradeData.helixRepairBonus(lastsongofelysian$player);
            if (!result.isEmpty() && result.isDamageableItem() && bonus > 0.0F) {
                int extraRepair = Math.round(result.getMaxDamage() * bonus);
                result.setDamageValue(Math.max(0, result.getDamageValue() - extraRepair));
            }
        }
    }
}
