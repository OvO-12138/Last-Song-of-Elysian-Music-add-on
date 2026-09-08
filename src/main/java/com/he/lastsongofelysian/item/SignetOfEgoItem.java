package com.he.lastsongofelysian.item;

import com.he.lastsongofelysian.screen.SignetOfEgoScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SignetOfEgoItem extends Item {

    public static final Style PINK         = Style.EMPTY.withColor(TextColor.fromRgb(0xFF69B4));
    public static final Style LIGHT_PINK_I = Style.EMPTY.withColor(TextColor.fromRgb(0xFFB6C1)).withItalic(true);

    public static final Style GRAY         = Style.EMPTY.withColor(TextColor.fromRgb(0xAAAAAA));
    public static final Style LIGHT_GRAY_I = Style.EMPTY.withColor(TextColor.fromRgb(0xD3D3D3)).withItalic(true);

    public static final Style ORANGE         = Style.EMPTY.withColor(TextColor.fromRgb(0xFF8C00));
    public static final Style LIGHT_ORANGE_I = Style.EMPTY.withColor(TextColor.fromRgb(0xFFC080)).withItalic(true);

    public static final Style LIGHT_BLUE = Style.EMPTY.withColor(TextColor.fromRgb(0x87CEFA));

    public static final Style DARK_GREEN = Style.EMPTY.withColor(TextColor.fromRgb(0x1B5E20));
    public static final Style TEAL_I     = Style.EMPTY.withColor(TextColor.fromRgb(0x26A69A)).withItalic(true);

    public static final Style GREEN         = Style.EMPTY.withColor(TextColor.fromRgb(0x4CAF50));
    public static final Style LIGHT_GREEN_I = Style.EMPTY.withColor(TextColor.fromRgb(0xA5D6A7)).withItalic(true);

    public static final Style LAKE_BLUE         = Style.EMPTY.withColor(TextColor.fromRgb(0x1CA9C9));
    public static final Style LIGHT_LAKE_BLUE_I = Style.EMPTY.withColor(TextColor.fromRgb(0x8FD9E8)).withItalic(true);

    public static final Style ICE_GREEN         = Style.EMPTY.withColor(TextColor.fromRgb(0x40E0B0));
    public static final Style LIGHT_ICE_GREEN_I = Style.EMPTY.withColor(TextColor.fromRgb(0xAAF5E0)).withItalic(true);

    public static final Style RED         = Style.EMPTY.withColor(TextColor.fromRgb(0xE53935));
    public static final Style LIGHT_RED_I = Style.EMPTY.withColor(TextColor.fromRgb(0xFF9E9E)).withItalic(true);

    public static final Style BROWN  = Style.EMPTY.withColor(TextColor.fromRgb(0x8B5A2B));
    public static final Style GRAY_I = Style.EMPTY.withColor(TextColor.fromRgb(0xAAAAAA)).withItalic(true);

    public static final Style GOLDEN_YELLOW         = Style.EMPTY.withColor(TextColor.fromRgb(0xFFD700));
    public static final Style LIGHT_GOLDEN_YELLOW_I = Style.EMPTY.withColor(TextColor.fromRgb(0xFFEB99)).withItalic(true);

    public static final Style DARK_YELLOW    = Style.EMPTY.withColor(TextColor.fromRgb(0xB8860B));
    public static final Style LIGHT_YELLOW_I = Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFAA)).withItalic(true);

    public static final Style DARK_BLUE = Style.EMPTY.withColor(TextColor.fromRgb(0x1A237E));
    public static final Style BLUE_I    = Style.EMPTY.withColor(TextColor.fromRgb(0x4169E1)).withItalic(true);

    public SignetOfEgoItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                    net.minecraft.client.Minecraft.getInstance().setScreen(new SignetOfEgoScreen()));
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("「真我」 无瑕之人 爱莉希雅 II")
                .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));

        tooltip.add(Component.literal("但来访者，你的道路仍将延续，不是吗？").withStyle(LIGHT_PINK_I));
        tooltip.add(Component.literal("那么，").withStyle(LIGHT_PINK_I));
        tooltip.add(Component.literal("就听凭心意前进吧。").withStyle(LIGHT_PINK_I));
        tooltip.add(Component.literal("沿着脚下的足迹，去见证那段逐火的征程。").withStyle(LIGHT_PINK_I));
        tooltip.add(Component.literal("最后，跨越逝者们的终墓——去创造，").withStyle(LIGHT_PINK_I));
        tooltip.add(Component.literal("「我们」所未能迎接的未来").withStyle(LIGHT_PINK_I));

        tooltip.add(Component.literal("右键查看全部祝福")
                .withStyle(ChatFormatting.GRAY));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
