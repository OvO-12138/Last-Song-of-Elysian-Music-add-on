package com.he.lastsongofelysian.mixin;

import com.he.lastsongofelysian.event.CocoonCurseEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class CraftingOutputSlotMixin {

    @Inject(
            method = "doClick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void lastsongofelysian$checkReasonCurse(
            int slotId,
            int button,
            ClickType clickType,
            Player player,
            CallbackInfo callback
    ) {
        if (player.level().isClientSide()) {
            return;
        }

        AbstractContainerMenu menu =
                (AbstractContainerMenu) (Object) this;

        if (
                slotId < 0
                        || slotId >= menu.slots.size()
                        || menu instanceof MerchantMenu
                        || !isOutputTakeClick(clickType)
        ) {
            return;
        }

        Slot slot = menu.getSlot(slotId);
        ItemStack result = slot.getItem();

        if (
                result.isEmpty()
                        || !slot.mayPickup(player)
                        || slot.container == player.getInventory()
                        || slot.mayPlace(result)
        ) {
            return;
        }

        if (
                CocoonCurseEvents.handleCraftOutputTake(
                        player,
                        result.copy()
                )
        ) {

            callback.cancel();
        }
    }

    private static boolean isOutputTakeClick(
            ClickType clickType
    ) {
        return clickType == ClickType.PICKUP
                || clickType == ClickType.QUICK_MOVE
                || clickType == ClickType.SWAP
                || clickType == ClickType.THROW;
    }
}
