package com.he.lastsongofelysian.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ComboOverlay implements IGuiOverlay {

    public static int clientCombo = 0;
    public static int resetTimer = 0;
    private static int pulseTicks = 0;

    public static void setCombo(int combo) {
        if (combo > clientCombo) {
            pulseTicks = 8;
        }

        clientCombo = Math.max(0, combo);
        resetTimer = clientCombo > 0 ? 65 : 0;
    }

    public static void clientTick() {
        if (pulseTicks > 0) {
            pulseTicks--;
        }

        if (resetTimer > 0) {
            resetTimer--;

            if (resetTimer == 0) {
                clientCombo = 0;
            }
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics,
                       float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (
                clientCombo <= 0 ||
                mc.player == null ||
                mc.options.hideGui ||
                mc.options.renderDebug
        ) {
            return;
        }

        float pulse = pulseTicks / 8.0F;
        int width = 116 + Math.round(pulse * 4.0F);
        int height = 22;
        int x = screenWidth - width - 14;
        int y = 74;
        int accent = clientCombo >= 12
                ? 0xFFFFB247
                : clientCombo >= 8
                ? 0xFFFF78C8
                : 0xFF8CE7FF;

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
                0xB0181025,
                0xB0081824
        );
        guiGraphics.fill(
                x,
                y,
                x + 3,
                y + height,
                accent
        );
        guiGraphics.fill(
                x + 5,
                y + 3,
                x + 7,
                y + 5,
                0xFFF5E9FF
        );
        guiGraphics.fill(
                x + width - 7,
                y + height - 5,
                x + width - 5,
                y + height - 3,
                accent
        );

        String label = "连击";
        String value = "x" + clientCombo;
        guiGraphics.drawString(
                mc.font,
                label,
                x + 12,
                y + 7,
                0xFFE8ECF8,
                false
        );
        guiGraphics.drawString(
                mc.font,
                value,
                x + width - mc.font.width(value) - 10,
                y + 7,
                accent,
                true
        );
    }
}
