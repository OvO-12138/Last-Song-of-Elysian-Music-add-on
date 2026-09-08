package com.he.lastsongofelysian.screen;

import com.he.lastsongofelysian.network.ModNetwork;
import com.he.lastsongofelysian.network.SilverPouchActionPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SilverPouchScreen extends Screen {

    private final long balance;

    public SilverPouchScreen(long balance) {
        super(Component.literal("银币袋"));
        this.balance = balance;
    }

    @Override
    protected void init() {
        int cx = width / 2;
        int cy = height / 2;

        addRenderableWidget(Button.builder(
                Component.literal("存入所有银币"),
                btn -> {
                    ModNetwork.CHANNEL.sendToServer(
                            new SilverPouchActionPacket(SilverPouchActionPacket.Action.DEPOSIT_ALL));
                    onClose();
                }
        ).bounds(cx - 75, cy + 5, 150, 20).build());

        addRenderableWidget(Button.builder(
                Component.literal("取出 64 枚"),
                btn -> {
                    ModNetwork.CHANNEL.sendToServer(
                            new SilverPouchActionPacket(SilverPouchActionPacket.Action.WITHDRAW_64));
                    onClose();
                }
        ).bounds(cx - 75, cy + 30, 150, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(font, "✦ 银币袋 ✦", width / 2, height / 2 - 35, 0xFFD700);
        guiGraphics.drawCenteredString(font,
                "余额：" + balance + " 枚", width / 2, height / 2 - 12, 0xFFEE88);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
