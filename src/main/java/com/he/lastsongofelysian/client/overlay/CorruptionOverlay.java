package com.he.lastsongofelysian.client.overlay;

import com.he.lastsongofelysian.client.CorruptionHudData;
import com.he.lastsongofelysian.client.ClientCocoonData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class CorruptionOverlay implements IGuiOverlay {

    private static final int FRAME_WIDTH = 126;
    private static final int FRAME_HEIGHT = 14;
    private static final int TRACK_X_OFFSET = 16;
    private static final int TRACK_Y_OFFSET = 3;
    private static final int TRACK_WIDTH = 73;
    private static final int TRACK_HEIGHT = 8;

    private static final int COLOR_SHADOW = 0x99000000;
    private static final int COLOR_OUTER = 0xFF17131D;
    private static final int COLOR_EDGE = 0xFF756580;
    private static final int COLOR_INNER = 0xFF09070D;
    private static final int COLOR_TRACK = 0xFF17101F;
    private static final int COLOR_FILL_DARK = 0xFF4B1298;
    private static final int COLOR_FILL = 0xFF7E21E8;
    private static final int COLOR_FILL_LIGHT = 0xFFA85CFF;
    private static final int COLOR_TICK = 0xFFB77AFF;
    private static final int COLOR_GLYPH = 0xFFE1C5FF;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || ClientCocoonData.isReversed() || mc.options.renderDebug) return;

        int energy = Mth.clamp(
                CorruptionHudData.corruptionEnergy,
                0,
                10000
        );

        float progress = Mth.clamp(
                energy / 10000.0F,
                0.0F,
                1.0F
        );

        int x = 10;
        int y = screenHeight - 34;

        drawFrame(guiGraphics, x, y);
        drawDiamondGlyph(guiGraphics, x + 8, y + 7);

        int filledWidth = Mth.floor(
                TRACK_WIDTH * progress
        );

        drawProgress(
                guiGraphics,
                x + TRACK_X_OFFSET,
                y + TRACK_Y_OFFSET,
                filledWidth
        );

        String percent = String.format("%.1f%%", energy / 100.0f);
        int textX =
                x + FRAME_WIDTH - 5
                        - mc.font.width(percent);

        guiGraphics.drawString(
                mc.font,
                percent,
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
                x + FRAME_WIDTH + 2,
                y + FRAME_HEIGHT + 2,
                COLOR_SHADOW
        );
        graphics.fill(
                x + 7,
                y,
                x + FRAME_WIDTH - 3,
                y + FRAME_HEIGHT,
                COLOR_OUTER
        );
        graphics.fill(
                x + 8,
                y + 1,
                x + FRAME_WIDTH - 4,
                y + 2,
                COLOR_EDGE
        );
        graphics.fill(
                x + 8,
                y + FRAME_HEIGHT - 2,
                x + FRAME_WIDTH - 4,
                y + FRAME_HEIGHT - 1,
                0xFF31283A
        );
        graphics.fill(
                x + 10,
                y + 2,
                x + FRAME_WIDTH - 6,
                y + FRAME_HEIGHT - 2,
                COLOR_INNER
        );

        graphics.fill(
                x + FRAME_WIDTH - 4,
                y + 3,
                x + FRAME_WIDTH - 1,
                y + FRAME_HEIGHT - 3,
                COLOR_OUTER
        );
        graphics.fill(
                x + FRAME_WIDTH - 1,
                y + 5,
                x + FRAME_WIDTH + 1,
                y + FRAME_HEIGHT - 5,
                COLOR_EDGE
        );

        graphics.fill(
                x + TRACK_X_OFFSET,
                y + TRACK_Y_OFFSET,
                x + TRACK_X_OFFSET + TRACK_WIDTH,
                y + TRACK_Y_OFFSET + TRACK_HEIGHT,
                COLOR_TRACK
        );
    }

    private static void drawProgress(
            GuiGraphics graphics,
            int x,
            int y,
            int filledWidth
    ) {
        if (filledWidth <= 0) {
            return;
        }

        graphics.fill(
                x,
                y,
                x + filledWidth,
                y + TRACK_HEIGHT,
                COLOR_FILL_DARK
        );
        graphics.fill(
                x,
                y + 1,
                x + filledWidth,
                y + TRACK_HEIGHT - 2,
                COLOR_FILL
        );
        graphics.fill(
                x,
                y + 1,
                x + filledWidth,
                y + 2,
                COLOR_FILL_LIGHT
        );

        for (int marker = 10; marker < filledWidth; marker += 12) {
            graphics.fill(
                    x + marker,
                    y + 2,
                    x + marker + 1,
                    y + TRACK_HEIGHT - 2,
                    COLOR_TICK
            );
        }

        graphics.fill(
                x + filledWidth - 1,
                y,
                x + filledWidth,
                y + TRACK_HEIGHT,
                COLOR_GLYPH
        );
    }

    private static void drawDiamondGlyph(
            GuiGraphics graphics,
            int centerX,
            int centerY
    ) {

        for (int row = -6; row <= 6; row++) {
            int halfWidth = 6 - Math.abs(row);

            graphics.fill(
                    centerX - halfWidth,
                    centerY + row,
                    centerX + halfWidth + 1,
                    centerY + row + 1,
                    COLOR_OUTER
            );
        }

        for (int row = -4; row <= 4; row++) {
            int halfWidth = 4 - Math.abs(row);

            graphics.fill(
                    centerX - halfWidth,
                    centerY + row,
                    centerX + halfWidth + 1,
                    centerY + row + 1,
                    row == 0
                            ? COLOR_FILL_LIGHT
                            : COLOR_FILL
            );
        }

        graphics.fill(
                centerX - 1,
                centerY - 1,
                centerX + 2,
                centerY + 2,
                COLOR_GLYPH
        );
    }
}
