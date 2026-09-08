package com.he.lastsongofelysian.client.overlay;

import com.he.lastsongofelysian.client.DisciplineHudData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class DisciplineOverlay implements IGuiOverlay {
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (
                mc.player == null ||
                mc.options.hideGui ||
                mc.options.renderDebug
        ) {
            return;
        }

        int precept = DisciplineHudData.preceptCount;
        if (precept <= 0) {
            return;
        }

        int width = 116;
        int height = 24;
        int x = screenWidth - width - 14;
        int y = 44;
        int fillWidth = Math.min(
                width - 14,
                Math.max(
                        0,
                        Math.round((width - 14) * precept / 100.0F)
                )
        );
        int glow = DisciplineHudData.pulseTicks > 0
                ? 0xFFFFF1A1
                : 0xFFFFC95D;

        guiGraphics.fill(
                x - 2,
                y - 2,
                x + width + 2,
                y + height + 2,
                0x4A000000
        );
        guiGraphics.fillGradient(
                x,
                y,
                x + width,
                y + height,
                0xC0241708,
                0xC00D111A
        );
        guiGraphics.fill(
                x,
                y,
                x + 3,
                y + height,
                glow
        );
        guiGraphics.fill(
                x + 8,
                y + height - 6,
                x + 8 + fillWidth,
                y + height - 3,
                0xBFFF9E35
        );
        guiGraphics.fill(
                x + 8,
                y + height - 6,
                x + 8 + Math.max(1, fillWidth / 2),
                y + height - 5,
                glow
        );

        String label = "规戒";
        String value = Integer.toString(precept);
        guiGraphics.drawString(
                mc.font,
                label,
                x + 10,
                y + 6,
                0xFFFFE3A2,
                false
        );
        guiGraphics.drawString(
                mc.font,
                value,
                x + width - mc.font.width(value) - 10,
                y + 6,
                glow,
                true
        );
    }
}
