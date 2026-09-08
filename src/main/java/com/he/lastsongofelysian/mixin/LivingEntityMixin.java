package com.he.lastsongofelysian.mixin;

import com.he.lastsongofelysian.registry.ModItems;
import com.he.lastsongofelysian.util.SignetUpgradeData;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "addEffect", at = @At("HEAD"))
    private void onAddEffect(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (!((Object) this instanceof Player player)) return;

        boolean hasDeliverance = CuriosApi.getCuriosHelper()
                .findFirstCurio(player, stack -> stack.is(ModItems.SIGNET_OF_DELIVERANCE.get()))
                .isPresent();
        if (hasDeliverance && effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
            float reduction = Math.min(0.95F, SignetUpgradeData.scaleBonus(
                    player,
                    SignetUpgradeData.Skill.DELIVERANCE_EFFECT,
                    0.30F
            ));
            int newDuration = Math.max(1, (int) (effect.getDuration() * (1.0F - reduction)));
            ((MobEffectInstanceAccessor) effect).setDuration(newDuration);
            return;
        }

        boolean hasGold = CuriosApi.getCuriosHelper()
                .findFirstCurio(player, stack -> stack.is(ModItems.SIGNET_OF_GOLD.get()))
                .isPresent();
        if (hasGold && effect.getEffect().getCategory() == MobEffectCategory.BENEFICIAL) {
            float bonus = SignetUpgradeData.scaleBonus(
                    player,
                    SignetUpgradeData.Skill.GOLD_STREAM,
                    0.30F
            );
            int newDuration = (int) (effect.getDuration() * (1.0F + bonus));
            ((MobEffectInstanceAccessor) effect).setDuration(newDuration);
        }
    }
}
