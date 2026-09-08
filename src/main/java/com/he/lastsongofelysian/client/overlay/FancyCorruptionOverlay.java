package com.he.lastsongofelysian.client.overlay;

import com.he.lastsongofelysian.client.CorruptionHudData;
import com.he.lastsongofelysian.client.ClientCocoonData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public final class FancyCorruptionOverlay
        implements IGuiOverlay {

    private static final int WIDTH = 126;
    private static final int HEIGHT = 14;
    private static final int TRACK_X = 16;
    private static final int TRACK_Y = 3;
    private static final int TRACK_WIDTH = 73;
    private static final int TRACK_HEIGHT = 8;

    @Override
    public void render(
            ForgeGui gui,
            GuiGraphics graphics,
            float partialTick,
            int screenWidth,
            int screenHeight
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (
                minecraft.player == null
                        || ClientCocoonData.isReversed()
                        || minecraft.options.renderDebug
        ) {
            return;
        }

        int energy = Mth.clamp(
                CorruptionHudData.corruptionEnergy,
                0,
                10000
        );
        float progress = energy / 10000.0F;

        int x = 10;
        int y = screenHeight - 36;

        drawFrame(graphics, x, y);
        drawDiamond(graphics, x + 8, y + 7);

        int filled = Mth.floor(
                TRACK_WIDTH * progress
        );

        drawFill(
                graphics,
                x + TRACK_X,
                y + TRACK_Y,
                filled
        );

        String text = String.format(
                "%.1f%%",
                energy / 100.0F
        );
        int textX =
                x + WIDTH - 5
                        - minecraft.font.width(text);

        graphics.drawString(
                minecraft.font,
                text,
                textX,
                y + 3,
                0xFFF4ECFF,
                true
        );
    }

    private static void drawFrame(
            GuiGraphics graphics,
            int x,
            int y
    ) {

        graphics.fill(
                x + 3,
                y + 2,
                x + WIDTH + 2,
                y + HEIGHT + 2,
                0x99000000
        );

        graphics.fill(
                x + 7,
                y,
                x + WIDTH - 3,
                y + HEIGHT,
                0xFF17131D
        );
        graphics.fill(
                x + 8,
                y + 1,
                x + WIDTH - 4,
                y + 2,
                0xFF756580
        );
        graphics.fill(
                x + 8,
                y + HEIGHT - 2,
                x + WIDTH - 4,
                y + HEIGHT - 1,
                0xFF31283A
        );
        graphics.fill(
                x + 10,
                y + 2,
                x + WIDTH - 6,
                y + HEIGHT - 2,
                0xFF09070D
        );

        graphics.fill(
                x + WIDTH - 4,
                y + 3,
                x + WIDTH - 1,
                y + HEIGHT - 3,
                0xFF17131D
        );
        graphics.fill(
                x + WIDTH - 1,
                y + 5,
                x + WIDTH + 1,
                y + HEIGHT - 5,
                0xFF756580
        );

        graphics.fill(
                x + TRACK_X,
                y + TRACK_Y,
                x + TRACK_X + TRACK_WIDTH,
                y + TRACK_Y + TRACK_HEIGHT,
                0xFF17101F
        );
    }

    private static void drawFill(
            GuiGraphics graphics,
            int x,
            int y,
            int filled
    ) {
        if (filled <= 0) {
            return;
        }

        graphics.fill(
                x,
                y,
                x + filled,
                y + TRACK_HEIGHT,
                0xFF4B1298
        );
        graphics.fill(
                x,
                y + 1,
                x + filled,
                y + TRACK_HEIGHT - 2,
                0xFF7E21E8
        );
        graphics.fill(
                x,
                y + 1,
                x + filled,
                y + 2,
                0xFFA85CFF
        );

        for (int marker = 10; marker < filled; marker += 12) {
            graphics.fill(
                    x + marker,
                    y + 2,
                    x + marker + 1,
                    y + TRACK_HEIGHT - 2,
                    0xFFB77AFF
            );
        }

        graphics.fill(
                x + filled - 1,
                y,
                x + filled,
                y + TRACK_HEIGHT,
                0xFFE1C5FF
        );
    }

    private static void drawDiamond(
            GuiGraphics graphics,
            int centerX,
            int centerY
    ) {
        for (int row = -6; row <= 6; row++) {
            int half = 6 - Math.abs(row);

            graphics.fill(
                    centerX - half,
                    centerY + row,
                    centerX + half + 1,
                    centerY + row + 1,
                    0xFF17131D
            );
        }

        for (int row = -4; row <= 4; row++) {
            int half = 4 - Math.abs(row);

            graphics.fill(
                    centerX - half,
                    centerY + row,
                    centerX + half + 1,
                    centerY + row + 1,
                    row == 0
                            ? 0xFFA85CFF
                            : 0xFF7E21E8
            );
        }

        graphics.fill(
                centerX - 1,
                centerY - 1,
                centerX + 2,
                centerY + 2,
                0xFFE1C5FF
        );
    }
}
