package com.ovo.lastsongofelysian_music_add_on.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.nio.file.Path;

public final class CustomSongImportScreen extends Screen {
    private static final int PANEL = 0xFFC6C6C6;
    private static final int PANEL_LIGHT = 0xFFFFFFFF;
    private static final int PANEL_SHADOW = 0xFF555555;
    private static final int TEXT = 0xFF404040;

    private final RadioScreen parent;
    private EditBox nameBox;
    private Path oggPath;
    private Path coverPath;
    private String message = "";
    private int messageColor = 0xFFE05B6F;

    public CustomSongImportScreen(RadioScreen parent) {
        super(Component.literal("上传自己的音乐"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int top = height / 2 - 105;

        nameBox = new EditBox(font, centerX - 116, top + 48, 232, 20, Component.literal("音乐名字"));
        nameBox.setMaxLength(80);
        nameBox.setHint(Component.literal("输入曲目名称"));
        addRenderableWidget(nameBox);

        addRenderableWidget(Button.builder(Component.literal("♫ 选择 OGG 音乐"), button -> chooseOgg())
                .bounds(centerX - 116, top + 82, 112, 22)
                .build());

        addRenderableWidget(Button.builder(Component.literal("▣ 选择封面图片"), button -> chooseCover())
                .bounds(centerX + 4, top + 82, 112, 22)
                .build());

        addRenderableWidget(Button.builder(Component.literal("保存并加入收音机"), button -> save())
                .bounds(centerX - 116, top + 158, 156, 22)
                .build());

        addRenderableWidget(Button.builder(Component.literal("取消"), button -> returnToRadio())
                .bounds(centerX + 48, top + 158, 68, 22)
                .build());

        setInitialFocus(nameBox);
    }

    private void chooseOgg() {
        String selected = TinyFileDialogs.tinyfd_openFileDialog(
                "选择 OGG 音乐", "", null, null, false);
        if (selected == null) {
            return;
        }
        Path path = Path.of(selected);
        if (!path.getFileName().toString().toLowerCase().endsWith(".ogg")) {
            showError("音乐必须是 .ogg 文件");
            return;
        }
        oggPath = path;
        if (nameBox.getValue().isBlank()) {
            String fileName = path.getFileName().toString();
            nameBox.setValue(fileName.substring(0, fileName.length() - 4));
        }
        showSuccess("已选择：" + path.getFileName());
    }

    private void chooseCover() {
        String selected = TinyFileDialogs.tinyfd_openFileDialog(
                "选择照片", "", null, null, false);
        if (selected == null) {
            return;
        }
        Path path = Path.of(selected);
        String lower = path.getFileName().toString().toLowerCase();
        if (!lower.endsWith(".png") && !lower.endsWith(".jpg") && !lower.endsWith(".jpeg")) {
            showError("照片必须是 PNG、JPG 或 JPEG");
            return;
        }
        coverPath = path;
        showSuccess("已选择：" + path.getFileName());
    }

    private void save() {
        if (oggPath == null) {
            showError("请先选择 .ogg 音乐");
            return;
        }
        if (coverPath == null) {
            showError("请先选择照片");
            return;
        }
        try {
            CustomSongManager.importSong(nameBox.getValue(), oggPath, coverPath);
            returnToRadio();
        } catch (Exception exception) {
            showError(exception.getMessage() == null ? "保存失败" : exception.getMessage());
        }
    }

    private void showError(String value) {
        message = value;
        messageColor = 0xFFE05B6F;
    }

    private void showSuccess(String value) {
        message = value;
        messageColor = 0xFF66C28A;
    }

    private void returnToRadio() {
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }

    @Override
    public void onClose() {
        returnToRadio();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        int centerX = width / 2;
        int top = height / 2 - 105;
        drawVanillaPanel(graphics, centerX - 136, top, 272, 210);
        graphics.drawCenteredString(font, title, centerX, top + 13, TEXT);
        graphics.drawString(font, "曲目名称", centerX - 116, top + 38, TEXT, false);

        drawInset(graphics, centerX - 116, top + 111, 112, 29);
        drawInset(graphics, centerX + 4, top + 111, 112, 29);
        String audioName = oggPath == null ? "尚未选择音乐" : oggPath.getFileName().toString();
        String coverName = coverPath == null ? "尚未选择封面" : coverPath.getFileName().toString();
        graphics.drawCenteredString(font, font.plainSubstrByWidth(audioName, 102), centerX - 60, top + 121,
                oggPath == null ? 0xFF606060 : 0xFF208020);
        graphics.drawCenteredString(font, font.plainSubstrByWidth(coverName, 102), centerX + 60, top + 121,
                coverPath == null ? 0xFF606060 : 0xFF208020);
        graphics.drawCenteredString(font, message, centerX, top + 190, messageColor);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawVanillaPanel(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.fill(x, y, x + width, y + height, PANEL);
        graphics.fill(x, y, x + width, y + 2, PANEL_LIGHT);
        graphics.fill(x, y, x + 2, y + height, PANEL_LIGHT);
        graphics.fill(x, y + height - 2, x + width, y + height, PANEL_SHADOW);
        graphics.fill(x + width - 2, y, x + width, y + height, PANEL_SHADOW);
        graphics.renderOutline(x + 2, y + 2, width - 4, height - 4, 0xFFAAAAAA);
    }

    private void drawInset(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.fill(x, y, x + width, y + height, 0xFF8B8B8B);
        graphics.fill(x, y, x + width, y + 2, PANEL_SHADOW);
        graphics.fill(x, y, x + 2, y + height, PANEL_SHADOW);
        graphics.fill(x, y + height - 2, x + width, y + height, PANEL_LIGHT);
        graphics.fill(x + width - 2, y, x + width, y + height, PANEL_LIGHT);
    }
}
