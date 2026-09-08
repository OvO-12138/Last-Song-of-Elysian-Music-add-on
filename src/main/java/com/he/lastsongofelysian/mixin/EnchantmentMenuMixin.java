package com.he.lastsongofelysian.mixin;

import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Map;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {

    @Inject(method = "clickMenuButton", at = @At("RETURN"))
    private void onEnchant(Player player, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || player.level().isClientSide()) return;
        if (CuriosApi.getCuriosHelper()
                .findFirstCurio(player, stack -> stack.is(ModItems.SIGNET_OF_HELIX.get()))
                .isEmpty()) return;

        ItemStack result = ((EnchantmentMenu) (Object) this).getSlot(0).getItem();
        if (result.isEmpty()) return;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(result);
        if (enchantments.isEmpty()) return;
        enchantments.replaceAll((enchantment, level) -> Math.max(level, 5 + player.getRandom().nextInt(4)));
        EnchantmentHelper.setEnchantments(enchantments, result);
    }
}
