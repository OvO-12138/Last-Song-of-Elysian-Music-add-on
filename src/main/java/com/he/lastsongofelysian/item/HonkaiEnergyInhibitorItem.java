package com.he.lastsongofelysian.item;

import com.he.lastsongofelysian.event.SignetEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class HonkaiEnergyInhibitorItem extends Item {

    private final String tierName;
    private final float healthCost;
    private final float reducePercent;

    public HonkaiEnergyInhibitorItem(Properties properties, String tierName, float healthCost, float reducePercent) {
        super(properties);
        this.tierName = tierName;
        this.healthCost = healthCost;
        this.reducePercent = reducePercent;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (healthCost > 0.0F && player.getHealth() <= healthCost) {
                player.displayClientMessage(
                        Component.literal("生命值不足，无法使用" + tierName + "崩坏能抑制剂。")
                                .withStyle(ChatFormatting.RED),
                        true
                );
                return InteractionResultHolder.fail(stack);
            }

            boolean success = SignetEffects.reduceCorruptionByPercent(player, reducePercent);

            if (!success) {
                player.displayClientMessage(
                        Component.literal("当前没有可抑制的崩坏能侵蚀。")
                                .withStyle(ChatFormatting.GRAY),
                        true
                );
                return InteractionResultHolder.fail(stack);
            }

            if (healthCost > 0.0F) {
                player.hurt(player.damageSources().magic(), healthCost);
            }

            level.playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.BREWING_STAND_BREW,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );

            player.displayClientMessage(
                    Component.literal(tierName + "崩坏能抑制剂生效，崩坏能侵蚀降低 "
                                    + Math.round(reducePercent * 100.0F) + "%。")
                            .withStyle(getTierColor()),
                    true
            );

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("使用后降低 " + Math.round(reducePercent * 100.0F) + "% 崩坏能侵蚀。")
                .withStyle(ChatFormatting.AQUA));

        if (healthCost > 0.0F) {
            tooltip.add(Component.literal("代价：消耗 " + Math.round(healthCost) + " 点生命值。")
                    .withStyle(ChatFormatting.RED));
        } else {
            tooltip.add(Component.literal("代价：无生命消耗。")
                    .withStyle(ChatFormatting.LIGHT_PURPLE));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }

    private ChatFormatting getTierColor() {
        return switch (tierName) {
            case "低等" -> ChatFormatting.GRAY;
            case "中等" -> ChatFormatting.AQUA;
            case "高等" -> ChatFormatting.LIGHT_PURPLE;
            default -> ChatFormatting.WHITE;
        };
    }
}
