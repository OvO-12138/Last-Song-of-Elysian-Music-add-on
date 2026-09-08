package com.he.lastsongofelysian.client.screen;

import com.he.lastsongofelysian.menu.PardofelisShopMenu;
import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class PardofelisShopScreen extends AbstractContainerScreen<PardofelisShopMenu> {

    private static final int CELL_START_X = 18;
    private static final int CELL_STEP_X = 73;
    private static final int FIRST_ROW_Y = 42;
    private static final int ROW_STEP_Y = 67;

    private final Button[] buyButtons = new Button[PardofelisShopMenu.OFFER_COUNT];
    private Button rerollButton;

    public PardofelisShopScreen(
            PardofelisShopMenu menu,
            Inventory playerInventory,
            Component title
    ) {
        super(menu, playerInventory, title);
        this.imageWidth = 400;
        this.imageHeight = 220;
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < this.buyButtons.length; i++) {
            int offerIndex = i;
            int column = i % 5;
            int row = i / 5;
            this.buyButtons[i] = this.addRenderableWidget(
                    Button.builder(
                                    Component.literal("购买"),
                                    ignored -> clickShopButton(offerIndex)
                            )
                            .bounds(
                                    this.leftPos + CELL_START_X + column * CELL_STEP_X + 7,
                                    this.topPos + FIRST_ROW_Y + row * ROW_STEP_Y + 45,
                                    54,
                                    16
                            )
                            .build()
            );
        }
        this.rerollButton = this.addRenderableWidget(
                Button.builder(
                                Component.literal("刷新 50 银币"),
                                ignored -> clickShopButton(PardofelisShopMenu.REROLL_BUTTON)
                        )
                        .bounds(this.leftPos + 270, this.topPos + 187, 108, 20)
                        .build()
        );
        updateButtons();
    }

    private void clickShopButton(int buttonId) {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, buttonId);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateButtons();
    }

    private void updateButtons() {
        int silver = this.menu.getDisplayedSilver();
        for (int i = 0; i < this.buyButtons.length; i++) {
            Button button = this.buyButtons[i];
            if (button == null) continue;
            boolean empty = this.menu.isDisplayedOfferEmpty(i);
            boolean purchased = this.menu.isDisplayedOfferPurchased(i);
            button.visible = !empty;
            button.active = !empty && !purchased && silver >= this.menu.getDisplayedOfferPrice(i);
            button.setMessage(Component.literal(purchased ? "已购买" : "购买"));
        }
        if (this.rerollButton != null) {
            this.rerollButton.active = silver >= PardofelisShopMenu.REROLL_PRICE;
        }
    }

    @Override
    protected void renderBg(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {
        int x = this.leftPos;
        int y = this.topPos;
        guiGraphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xFF17111F);
        guiGraphics.fill(x + 4, y + 4, x + this.imageWidth - 4, y + this.imageHeight - 4, 0xFF2A2034);
        guiGraphics.fill(x + 10, y + 10, x + this.imageWidth - 10, y + 34, 0xFF3B2B4A);
        guiGraphics.fill(x + 12, y + 38, x + this.imageWidth - 12, y + 178, 0xFF1F1828);
        guiGraphics.fill(x + 12, y + 182, x + this.imageWidth - 12, y + 212, 0xFF241B2D);

        for (int i = 0; i < PardofelisShopMenu.OFFER_COUNT; i++) {
            int column = i % 5;
            int row = i / 5;
            int cardX = x + CELL_START_X + column * CELL_STEP_X;
            int cardY = y + FIRST_ROW_Y + row * ROW_STEP_Y;
            guiGraphics.fill(cardX, cardY, cardX + 68, cardY + 63, 0xFF4A385C);
            guiGraphics.fill(cardX + 2, cardY + 2, cardX + 66, cardY + 61, 0xFF30243B);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(
                this.font,
                Component.literal("帕朵菲利斯的小店"),
                15,
                18,
                0xFFFFD36A,
                false
        );
        for (int i = 0; i < PardofelisShopMenu.OFFER_COUNT; i++) {
            int column = i % 5;
            int row = i / 5;
            int cardX = CELL_START_X + column * CELL_STEP_X;
            int cardY = FIRST_ROW_Y + row * ROW_STEP_Y;
            ItemStack offer = this.menu.getDisplayedOffer(i);
            if (offer.isEmpty()) continue;
            guiGraphics.drawCenteredString(
                    this.font,
                    offer.getHoverName(),
                    cardX + 34,
                    cardY + 7,
                    0xFFFFE4AC
            );
            int color = this.menu.isDisplayedOfferPurchased(i)
                    ? 0xFF999999
                    : 0xFFFFD36A;
            guiGraphics.drawString(
                    this.font,
                    Component.literal(String.valueOf(this.menu.getDisplayedOfferPrice(i))),
                    cardX + 45,
                    cardY + 29,
                    color,
                    false
            );
        }
        guiGraphics.drawString(
                this.font,
                Component.literal("银币 " + this.menu.getDisplayedSilver()),
                24,
                193,
                0xFFFFD36A,
                false
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderOfferItems(guiGraphics);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        renderOfferTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderOfferItems(GuiGraphics guiGraphics) {
        for (int i = 0; i < PardofelisShopMenu.OFFER_COUNT; i++) {
            int column = i % 5;
            int row = i / 5;
            int itemX = this.leftPos + CELL_START_X + column * CELL_STEP_X + 6;
            int itemY = this.topPos + FIRST_ROW_Y + row * ROW_STEP_Y + 25;
            ItemStack offer = this.menu.getDisplayedOffer(i);
            if (offer.isEmpty()) continue;
            guiGraphics.renderItem(offer, itemX, itemY);
            guiGraphics.renderItemDecorations(this.font, offer, itemX, itemY);
            guiGraphics.renderItem(
                    new ItemStack(ModItems.SHINY_SILVER.get()),
                    itemX + 21,
                    itemY
            );
        }
    }

    private void renderOfferTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (int i = 0; i < PardofelisShopMenu.OFFER_COUNT; i++) {
            int column = i % 5;
            int row = i / 5;
            int itemX = this.leftPos + CELL_START_X + column * CELL_STEP_X + 6;
            int itemY = this.topPos + FIRST_ROW_Y + row * ROW_STEP_Y + 25;
            if (this.menu.isDisplayedOfferEmpty(i)) continue;
            if (mouseX >= itemX && mouseX < itemX + 16
                    && mouseY >= itemY && mouseY < itemY + 16) {
                guiGraphics.renderTooltip(
                        this.font,
                        this.menu.getDisplayedOffer(i),
                        mouseX,
                        mouseY
                );
                return;
            }
        }
    }
}
