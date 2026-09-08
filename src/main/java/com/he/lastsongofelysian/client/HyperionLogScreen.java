package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class HyperionLogScreen extends Screen {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    lastsongofelysian.MODID,
                    "textures/gui/hyperion_log.png"
            );

    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 192;

    private int left;
    private int top;
    private int displayWidth;
    private int displayHeight;
    private float displayScale;

    public HyperionLogScreen() {
        super(
                Component.translatable(
                        "item.lastsongofelysian.hyperion_log"
                )
        );
    }

    @Override
    protected void init() {
        super.init();

        int availableWidth = Math.max(1, this.width - 24);
        int availableHeight = Math.max(1, this.height - 24);
        float fitScale = Math.min(
                availableWidth / (float) IMAGE_WIDTH,
                availableHeight / (float) IMAGE_HEIGHT
        );
        float responsiveScale = Math.min(
                this.width * 0.82F / IMAGE_WIDTH,
                this.height * 0.82F / IMAGE_HEIGHT
        );

        this.displayScale = fitScale >= 1.0F
                ? Math.min(fitScale, Math.max(1.0F, responsiveScale))
                : fitScale;
        this.displayWidth = Math.max(1, Math.round(IMAGE_WIDTH * this.displayScale));
        this.displayHeight = Math.max(1, Math.round(IMAGE_HEIGHT * this.displayScale));
        this.left = (this.width - this.displayWidth) / 2;
        this.top = (this.height - this.displayHeight) / 2;
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        this.renderBackground(graphics);

        graphics.pose().pushPose();
        graphics.pose().translate(this.left, this.top, 0.0F);
        graphics.pose().scale(this.displayScale, this.displayScale, 1.0F);

        graphics.blit(
                TEXTURE,
                0,
                0,
                0.0F,
                0.0F,
                IMAGE_WIDTH,
                IMAGE_HEIGHT,
                IMAGE_WIDTH,
                IMAGE_HEIGHT
        );

        graphics.drawCenteredString(
                this.font,
                this.title,
                91,
                29,
                0x382C26
        );

        graphics.pose().popPose();

        super.render(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );
    }

    @Override
    public boolean mouseClicked(
            double mouseX,
            double mouseY,
            int button
    ) {
        if (
                button == 0 &&
                mouseX >= this.left + 292 * this.displayScale &&
                mouseX < this.left + 312 * this.displayScale &&
                mouseY >= this.top + 8 * this.displayScale &&
                mouseY < this.top + 28 * this.displayScale
        ) {
            this.onClose();
            return true;
        }

        return super.mouseClicked(
                mouseX,
                mouseY,
                button
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
