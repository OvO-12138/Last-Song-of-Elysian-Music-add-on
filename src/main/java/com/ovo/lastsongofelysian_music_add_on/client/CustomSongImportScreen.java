package com.ovo.lastsongofelysian_music_add_on.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.nio.file.Path;

public final class CustomSongImportScreen extends Screen {
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
        graphics.fill(centerX - 136, top, centerX + 136, top + 210, 0xF51A1523);
        graphics.fill(centerX - 134, top + 2, centerX + 134, top + 34, 0xFF2C2138);
        graphics.fill(centerX - 134, top + 34, centerX + 134, top + 35, 0xFF8D68B3);
        graphics.renderOutline(centerX - 136, top, 272, 210, 0xFFB38BD5);
        graphics.renderOutline(centerX - 132, top + 4, 264, 202, 0x553A2C48);
        graphics.drawCenteredString(font, title, centerX, top + 13, 0xFFF5EEFF);
        graphics.drawString(font, "曲目名称", centerX - 116, top + 38, 0xFFDCCDE9, false);

        graphics.fill(centerX - 116, top + 111, centerX - 4, top + 140, 0x8822192B);
        graphics.fill(centerX + 4, top + 111, centerX + 116, top + 140, 0x8822192B);
        graphics.renderOutline(centerX - 116, top + 111, 112, 29, oggPath == null ? 0x665A466A : 0xFF66C28A);
        graphics.renderOutline(centerX + 4, top + 111, 112, 29, coverPath == null ? 0x665A466A : 0xFF66C28A);
        String audioName = oggPath == null ? "尚未选择音乐" : oggPath.getFileName().toString();
        String coverName = coverPath == null ? "尚未选择封面" : coverPath.getFileName().toString();
        graphics.drawCenteredString(font, font.plainSubstrByWidth(audioName, 102), centerX - 60, top + 121,
                oggPath == null ? 0xFF94859F : 0xFF9BE0B7);
        graphics.drawCenteredString(font, font.plainSubstrByWidth(coverName, 102), centerX + 60, top + 121,
                coverPath == null ? 0xFF94859F : 0xFF9BE0B7);
        graphics.drawCenteredString(font, message, centerX, top + 190, messageColor);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
