package com.he.lastsongofelysian.client.screen;

import com.he.lastsongofelysian.menu.SpiralWorkshopMenu;
import com.he.lastsongofelysian.util.SignetUpgradeData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SpiralWorkshopScreen extends AbstractContainerScreen<SpiralWorkshopMenu> {

    private static final int SKILL_TEXT_X = 83;
    private static final int UPGRADE_BUTTON_X = 304;
    private static final float DESCRIPTION_SCALE = 0.72F;
    private static final int DESCRIPTION_MAX_LINES = 4;
    private static final int SKILL_ROW_START_Y = 50;
    private static final int SKILL_ROW_HEIGHT = 50;

    private final Button[] upgradeButtons = new Button[SpiralWorkshopMenu.MAX_SKILL_ROWS];
    private Button fuseButton;
    private Button pageButton;
    private Button resetButton;

    public SpiralWorkshopScreen(SpiralWorkshopMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 400;
        this.imageHeight = 320;
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < this.upgradeButtons.length; i++) {
            int skillIndex = i;
            this.upgradeButtons[i] = this.addRenderableWidget(
                    Button.builder(Component.literal("升级"), ignored -> clickUpgrade(skillIndex))
                            .bounds(
                                    this.leftPos + UPGRADE_BUTTON_X,
                                    this.topPos + SKILL_ROW_START_Y + 4 + i * SKILL_ROW_HEIGHT,
                                    68,
                                    18
                            )
                            .build()
            );
        }
        this.fuseButton = this.addRenderableWidget(
                Button.builder(Component.literal("融合"), ignored -> clickFuse())
                        .bounds(this.leftPos + 18, this.topPos + 145, 52, 18)
                        .build()
        );
        this.pageButton = this.addRenderableWidget(
                Button.builder(Component.literal("下一页"), ignored -> clickPage())
                        .bounds(this.leftPos + 221, this.topPos + 16, 58, 18)
                        .build()
        );
        this.resetButton = this.addRenderableWidget(
                Button.builder(Component.literal("重置核心"), ignored -> clickReset())
                        .bounds(this.leftPos + 18, this.topPos + 171, 52, 18)
                        .build()
        );
        updateButtons();
    }

    private void clickUpgrade(int skillIndex) {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, skillIndex);
        }
    }

    private void clickFuse() {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(
                    this.menu.containerId,
                    SpiralWorkshopMenu.FUSE_BUTTON
            );
        }
    }

    private void clickPage() {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(
                    this.menu.containerId,
                    SpiralWorkshopMenu.PAGE_BUTTON
            );
        }
    }

    private void clickReset() {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(
                    this.menu.containerId,
                    SpiralWorkshopMenu.RESET_BUTTON
            );
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateButtons();
    }

    private void updateButtons() {
        boolean creative = this.minecraft != null
                && this.minecraft.player != null
                && this.minecraft.player.getAbilities().instabuild;
        for (int i = 0; i < this.upgradeButtons.length; i++) {
            Button button = this.upgradeButtons[i];
            if (button == null) continue;
            SignetUpgradeData.Skill skill = this.menu.getDisplayedSkill(i);
            int level = this.menu.getDisplayedLevel(i);
            button.visible = skill != null;
            button.active = skill != null
                    && level < SignetUpgradeData.MAX_LEVEL
                    && (creative || (
                    this.menu.getDisplayedUpgradeItemCount() >= SignetUpgradeData.UPGRADE_ITEM_COST
                            && this.menu.getDisplayedExperienceLevel()
                            >= SignetUpgradeData.EXPERIENCE_LEVEL_COST
            ));
            button.setMessage(Component.literal(level >= SignetUpgradeData.MAX_LEVEL ? "MAX" : "升级"));
        }
        if (this.fuseButton != null) {
            boolean alreadyFused = this.menu.isSecondaryInputFused();
            this.fuseButton.active = this.menu.canFuseSecondary();
            this.fuseButton.setMessage(Component.literal(alreadyFused ? "已融合" : "融合"));
        }
        if (this.pageButton != null) {
            int pageCount = this.menu.getDisplayedSkillPageCount();
            this.pageButton.visible = pageCount > 1;
            this.pageButton.active = pageCount > 1;
            this.pageButton.setMessage(Component.literal(
                    (this.menu.getDisplayedSkillPage() + 1) + "/" + pageCount
            ));
        }
        if (this.resetButton != null) {
            this.resetButton.visible = this.menu.getInputSignet()
                    == SignetUpgradeData.Signet.DELIVERANCE;
            this.resetButton.active = this.menu.hasDisplayedCoreProgress();
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        guiGraphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xFF24150D);
        guiGraphics.fill(x + 3, y + 3, x + this.imageWidth - 3, y + this.imageHeight - 3, 0xFF6A4127);
        guiGraphics.fill(x + 7, y + 7, x + this.imageWidth - 7, y + this.imageHeight - 7, 0xFF3A2418);
        guiGraphics.fill(x + 11, y + 11, x + this.imageWidth - 11, y + 36, 0xFF7A4B29);
        guiGraphics.fill(x + 14, y + 42, x + this.imageWidth - 14, y + 211, 0xFF2A1A12);
        guiGraphics.fill(x + 14, y + 217, x + this.imageWidth - 14, y + 308, 0xFF2E1D14);
        guiGraphics.fill(x + 74, y + 46, x + 375, y + 208, 0xFF42291A);
        guiGraphics.fill(x + 75, y + 47, x + 374, y + 207, 0xFF5B3822);
        guiGraphics.fill(x + 18, y + 199, x + 70, y + 213, 0xFF1D120D);

        drawRivet(guiGraphics, x + 8, y + 8);
        drawRivet(guiGraphics, x + this.imageWidth - 11, y + 8);
        drawRivet(guiGraphics, x + 8, y + this.imageHeight - 11);
        drawRivet(guiGraphics, x + this.imageWidth - 11, y + this.imageHeight - 11);

        drawSlot(guiGraphics, x + SpiralWorkshopMenu.SIGNET_SLOT_X, y + SpiralWorkshopMenu.SIGNET_SLOT_Y);
        drawSlot(guiGraphics, x + SpiralWorkshopMenu.SECONDARY_SLOT_X, y + SpiralWorkshopMenu.SECONDARY_SLOT_Y);

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                drawSlot(guiGraphics,
                        x + SpiralWorkshopMenu.PLAYER_INVENTORY_X + column * 18,
                        y + SpiralWorkshopMenu.PLAYER_INVENTORY_Y + row * 18);
            }
        }

        for (int column = 0; column < 9; column++) {
            drawSlot(guiGraphics,
                    x + SpiralWorkshopMenu.PLAYER_INVENTORY_X + column * 18,
                    y + SpiralWorkshopMenu.PLAYER_HOTBAR_Y);
        }
    }

    private static void drawSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x - 2, y - 2, x + 18, y + 18, 0xFFB0783F);
        guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, 0xFF5B3822);
        guiGraphics.fill(x, y, x + 16, y + 16, 0xFF1C120D);
    }

    private static void drawRivet(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x, y, x + 3, y + 3, 0xFFD1A35F);
        guiGraphics.fill(x + 1, y + 1, x + 3, y + 3, 0xFF76502D);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.literal("螺旋工坊"), 16, 19, 0xFFFFD991, false);
        guiGraphics.drawString(this.font,
                Component.literal("等级 " + this.menu.getDisplayedExperienceLevel()),
                286, 19, 0xFFFFE2A8, false);
        guiGraphics.drawString(this.font, Component.literal("刻印"), 31, 49, 0xFFD9B98B, false);
        guiGraphics.drawString(this.font, Component.literal("融合刻印"), 23, 98, 0xFFD9B98B, false);

        SignetUpgradeData.Signet signet = this.menu.getInputSignet();
        if (signet == null) {
            guiGraphics.drawString(this.font, Component.literal("放入刻印"), 82, 70, 0xFFD9B98B, false);
        } else {
            SignetUpgradeData.DeliveranceCore core = this.menu.getDisplayedDeliveranceCore();
            if (core != null) {
                guiGraphics.drawString(
                        this.font,
                        Component.literal("核心 · " + core.displayName()),
                        82,
                        38,
                        0xFF80A8FF,
                        false
                );
            }
            for (int i = 0; i < SpiralWorkshopMenu.MAX_SKILL_ROWS; i++) {
                SignetUpgradeData.Skill skill = this.menu.getDisplayedSkill(i);
                if (skill == null) continue;
                int level = this.menu.getDisplayedLevel(i);
                int rowY = SKILL_ROW_START_Y + i * SKILL_ROW_HEIGHT;
                guiGraphics.drawString(this.font,
                        Component.literal(skill.displayName() + (level == 0 ? "" : "  +" + level)),
                        SKILL_TEXT_X, rowY, 0xFFFFD991, false);
                drawSkillDescription(
                        guiGraphics,
                        Component.literal(skill.description(level)),
                        SKILL_TEXT_X,
                        rowY + 11
                );
            }
        }

        boolean creative = this.minecraft != null
                && this.minecraft.player != null
                && this.minecraft.player.getAbilities().instabuild;
        if (signet != null && !creative) {
            guiGraphics.drawString(this.font,
                    Component.literal("×1  ·  10级  ·  持有 " + this.menu.getDisplayedUpgradeItemCount()),
                    42, 202, 0xFFFFC879, false);
        }
    }

    private void drawSkillDescription(
            GuiGraphics guiGraphics,
            Component description,
            int x,
            int y
    ) {
        int availableWidth = UPGRADE_BUTTON_X - x - 6;
        int splitWidth = (int) (availableWidth / DESCRIPTION_SCALE);
        List<FormattedCharSequence> lines = this.font.split(description, splitWidth);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0.0F);
        guiGraphics.pose().scale(DESCRIPTION_SCALE, DESCRIPTION_SCALE, 1.0F);
        for (int line = 0; line < Math.min(lines.size(), DESCRIPTION_MAX_LINES); line++) {
            guiGraphics.drawString(
                    this.font,
                    lines.get(line),
                    0,
                    line * 9,
                    0xFFE8D4B8,
                    false
            );
        }
        guiGraphics.pose().popPose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        SignetUpgradeData.Signet signet = this.menu.getInputSignet();
        boolean creative = this.minecraft != null
                && this.minecraft.player != null
                && this.minecraft.player.getAbilities().instabuild;
        if (signet != null && !creative) {
            ItemStack upgradeItem = new ItemStack(SignetUpgradeData.upgradeItemFor(signet));
            guiGraphics.renderItem(upgradeItem, this.leftPos + 21, this.topPos + 197);
            guiGraphics.renderItemDecorations(
                    this.font,
                    upgradeItem,
                    this.leftPos + 21,
                    this.topPos + 197
            );
        }
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
