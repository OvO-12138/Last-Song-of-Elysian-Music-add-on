package com.ovo.lastsongofelysian_music_add_on.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import com.ovo.lastsongofelysian_music_add_on.item.RadioSongDiscItem;
import com.ovo.lastsongofelysian_music_add_on.menu.RadioMenu;
import com.ovo.lastsongofelysian_music_add_on.util.RadioSongs;
import java.util.ArrayList;
import java.util.List;

public final class RadioScreen extends AbstractContainerScreen<RadioMenu> {
    private static final int WIDTH = 360;
    private static final int HEIGHT = 284;
    
    private static final int LIST_X = 100;
    private static final int LIST_Y = 42;
    private static final int LIST_W = 240;
    private static final int ROW_H = 18;
    private static final int MAX_ROWS = 6;
    
    private static final int BTN_Y1 = 155;
    private static final int BTN_Y2 = 180;
    private static final int TEXT = 0xFFF5EEFF;
    private static final int TEXT_DIM = 0xFFBDB0CA;
    private static final int ACCENT = 0xFFE4C6FF;
    private static final int GOLD = 0xFFFFD77A;
    
    private int selected = 0;
    private int scroll = 0;
    private boolean playlistInitialized;
    private List<String> songList = new ArrayList<>();
    private final List<String> pendingRecordedSongs = new ArrayList<>();
    
    private Button btnPlay, btnStop, btnPrev, btnNext, btnImport;
    private Button btnSeq, btnRand, btnLoop;
    private Button btnRecord;

    public RadioScreen(RadioMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        imageWidth = WIDTH;
        imageHeight = HEIGHT;
        titleLabelX = 0;
        titleLabelY = 0;
        inventoryLabelX = 0;
        inventoryLabelY = 0;
    }

    @Override
    protected void init() {
        super.init();
        
        int bx = leftPos + LIST_X;
        int by = topPos + BTN_Y1;
        
        btnPlay = addRenderableWidget(Button.builder(Component.literal("▶"), b -> playSelected())
            .bounds(bx, by, 28, 20).build());
            
        btnStop = addRenderableWidget(Button.builder(Component.literal("■"), b -> RadioPlayback.stop())
            .bounds(bx + 32, by, 28, 20).build());
            
        btnPrev = addRenderableWidget(Button.builder(Component.literal("⏮"), b -> prevSong())
            .bounds(bx + 64, by, 28, 20).build());
            
        btnNext = addRenderableWidget(Button.builder(Component.literal("⏭"), b -> nextSong())
            .bounds(bx + 96, by, 28, 20).build());
            
        btnImport = addRenderableWidget(Button.builder(Component.literal("＋ 导入音乐"), b -> openImporter())
            .bounds(bx + 132, by, 108, 20).build());

        btnRecord = addRenderableWidget(Button.builder(Component.literal("录入唱片"), b -> recordDisc())
                .bounds(leftPos + 20, topPos + 119, 64, 20).build());

        int mx = leftPos + LIST_X;
        int my = topPos + BTN_Y2;
        
        btnSeq = addRenderableWidget(Button.builder(
                Component.literal("顺序"),
                b -> setMode(RadioPlayback.Mode.LOOP_ALL))
            .bounds(mx, my, 76, 18).build());
            
        btnRand = addRenderableWidget(Button.builder(
                Component.literal("随机"),
                b -> setMode(RadioPlayback.Mode.SHUFFLE))
            .bounds(mx + 82, my, 76, 18).build());
            
        btnLoop = addRenderableWidget(Button.builder(
                Component.literal("单曲循环"), 
                b -> setMode(RadioPlayback.Mode.LOOP_ONE))
            .bounds(mx + 164, my, 76, 18).build());

        addRenderableWidget(new VolumeSlider(leftPos + 14, topPos + 166, 76, 20));
        
        updatePlaylist();
        btnRecord.active = canRecordCurrentDisc();
        refreshLabels();
    }

    private void recordDisc() {
        if (minecraft != null && minecraft.gameMode != null) {
            ItemStack input = menu.inputDisc();
            if (!(input.getItem() instanceof RadioSongDiscItem disc)
                    || songList.contains(disc.songId())) return;
            pendingRecordedSongs.add(disc.songId());
            updatePlaylist();
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, RadioMenu.BUTTON_RECORD);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updatePlaylist();
        if (btnRecord != null) btnRecord.active = canRecordCurrentDisc();
    }

    private void setMode(RadioPlayback.Mode mode) {
        RadioPlayback.setCurrentMode(mode);
        refreshLabels();
    }
    
    private void updatePlaylist() {
        List<String> recorded = menu.recordedSongs();
        pendingRecordedSongs.removeIf(recorded::contains);
        List<String> updated = new ArrayList<>(recorded);
        for (String id : pendingRecordedSongs) {
            if (!updated.contains(id)) updated.add(id);
        }
        for (String id : CustomSongManager.ids()) {
            if (!updated.contains(id)) updated.add(id);
        }
        if (playlistInitialized && songList.equals(updated)) return;
        songList = updated;
        playlistInitialized = true;
        RadioPlayback.setPlaylist(songList);
        clampSelection();
    }

    private boolean canRecordCurrentDisc() {
        ItemStack input = menu.inputDisc();
        return input.getItem() instanceof RadioSongDiscItem disc
                && menu.recordedSongs().size() < RadioMenu.MAX_TRACKS
                && !songList.contains(disc.songId());
    }
    
    private void refreshLabels() {
        if (btnSeq != null) btnSeq.setMessage(getModeLabel("列表循环", RadioPlayback.Mode.LOOP_ALL));
        if (btnRand != null) btnRand.setMessage(getModeLabel("随机播放", RadioPlayback.Mode.SHUFFLE));
        if (btnLoop != null) btnLoop.setMessage(getModeLabel("单曲循环", RadioPlayback.Mode.LOOP_ONE));
    }
    
    private Component getModeLabel(String base, RadioPlayback.Mode mode) {
        return Component.literal((RadioPlayback.getCurrentMode() == mode ? "● " : "○ ") + base);
    }
    
    private void clampSelection() {
        if (songList.isEmpty()) {
            selected = 0;
            scroll = 0;
            return;
        }
        selected = Math.max(0, Math.min(selected, songList.size() - 1));
        scroll = Math.max(0, Math.min(scroll, Math.max(0, songList.size() - MAX_ROWS)));
        if (selected < scroll) scroll = selected;
        if (selected >= scroll + MAX_ROWS) scroll = selected - MAX_ROWS + 1;
    }

    private void playSelected() {
        if (songList.isEmpty()) return;
        clampSelection();
        RadioPlayback.play(songList.get(selected));
    }
    
    private void prevSong() {
        if (songList.isEmpty()) return;
        selected--;
        if (selected < 0) selected = songList.size() - 1;
        clampSelection();
        playSelected();
    }
    
    private void nextSong() {
        if (songList.isEmpty()) return;
        selected++;
        if (selected >= songList.size()) selected = 0;
        clampSelection();
        playSelected();
    }
    
    private void openImporter() {
        Minecraft.getInstance().setScreen(new CustomSongImportScreen(this));
    }

    @Override
    protected void renderBg(GuiGraphics g, float pt, int mx, int my) {
        int x = leftPos, y = topPos;
        g.fill(x, y, x + WIDTH, y + HEIGHT, 0xF51A1523);
        g.fill(x + 2, y + 2, x + WIDTH - 2, y + 28, 0xFF2C2138);
        g.fill(x + 2, y + 28, x + WIDTH - 2, y + 29, 0xFF8D68B3);
        g.renderOutline(x, y, WIDTH, HEIGHT, 0xFFB38BD5);
        g.renderOutline(x + 4, y + 4, WIDTH - 8, HEIGHT - 8, 0x553A2C48);

        g.fill(x + 14, y + 38, x + 90, y + 143, 0xB522192B);
        g.renderOutline(x + 14, y + 38, 76, 105, 0x887E6096);
        g.fill(x + 22, y + 46, x + 82, y + 86, 0xFF100D16);
        renderSelectedCover(g, x + 28, y + 42);
        drawSlot(g, x + 42, y + 95);

        int lbx = x + LIST_X, lby = y + LIST_Y;
        int lbh = MAX_ROWS * ROW_H;
        g.fill(lbx, lby, lbx + LIST_W, lby + lbh, 0xD0100C16);
        g.renderOutline(lbx, lby, LIST_W, lbh, 0x886E5585);
        g.enableScissor(lbx + 1, lby + 1, lbx + LIST_W - 1, lby + lbh - 1);
        int end = Math.min(songList.size(), scroll + MAX_ROWS);
        for (int i = scroll; i < end; i++) {
            int ry = lby + (i - scroll) * ROW_H;
            int rx = lbx;
            boolean sel = (i == selected);
            boolean hover = mx >= rx && mx < rx + LIST_W && my >= ry && my < ry + ROW_H;
            if (sel) g.fill(rx + 1, ry, rx + LIST_W - 1, ry + ROW_H, 0xAA4C3561);
            else if (hover) g.fill(rx + 1, ry, rx + LIST_W - 1, ry + ROW_H, 0x553C2D48);
            if (i > scroll) g.fill(rx + 8, ry, rx + LIST_W - 8, ry + 1, 0x332F2438);

            String sid = songList.get(i);
            if (CustomSongManager.isCustom(sid)) {
                g.drawString(font, "♪", rx + 8, ry + 5, ACCENT, false);
            } else {
                ItemStack disc = RadioSongs.disc(sid);
                if (!disc.isEmpty()) g.renderItem(disc, rx + 4, ry + 1);
            }
            Component name = CustomSongManager.isCustom(sid)
                ? Component.literal(CustomSongManager.title(sid))
                : RadioSongs.title(sid);
            String txt = font.plainSubstrByWidth(name.getString(), LIST_W - 52);
            if (!txt.equals(name.getString())) txt += "…";
            g.drawString(font, txt, rx + 25, ry + 5, sel ? TEXT : TEXT_DIM, false);
            if (RadioPlayback.isPlaying(sid)) g.drawString(font, "♫", rx + LIST_W - 17, ry + 5, GOLD, false);
        }
        g.disableScissor();

        if (songList.size() > MAX_ROWS) {
            int sbx = lbx + LIST_W - 4;
            int hs = Math.max(12, lbh * MAX_ROWS / songList.size());
            int maxScroll = songList.size() - MAX_ROWS;
            int hy = lby + (lbh - hs) * scroll / Math.max(1, maxScroll);
            g.fill(sbx, lby + 2, sbx + 2, lby + lbh - 2, 0x66463852);
            g.fill(sbx - 1, hy, sbx + 3, hy + hs, 0xFFB38BD5);
        }

        g.fill(x + 92, y + 198, x + 268, y + 280, 0x77201928);
        g.renderOutline(x + 92, y + 198, 176, 82, 0x665A466A);
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                drawSlot(g, x + 98 + column * 18, y + 201 + row * 18);
            }
        }
        for (int column = 0; column < 9; column++) drawSlot(g, x + 98 + column * 18, y + 259);
    }

    private void renderSelectedCover(GuiGraphics g, int x, int y) {
        if (songList.isEmpty()) return;
        String id = songList.get(selected);
        ResourceLocation cover = CustomSongManager.isCustom(id) ? CustomSongManager.cover(id) : null;
        if (cover != null) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            g.blit(cover, x, y, 0, 0, 48, 48, 48, 48);
            return;
        }
        ItemStack disc = RadioSongs.disc(id);
        if (!disc.isEmpty()) {
            g.pose().pushPose();
            g.pose().translate(x, y, 0);
            g.pose().scale(3.0F, 3.0F, 1.0F);
            g.renderItem(disc, 0, 0);
            g.pose().popPose();
        }
    }

    private void drawSlot(GuiGraphics g, int x, int y) {
        g.fill(x, y, x + 18, y + 18, 0xFF352A40);
        g.fill(x + 1, y + 1, x + 17, y + 17, 0xFF17121D);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawCenteredString(font, title, imageWidth / 2, 10, TEXT);
        g.drawString(font, "曲目列表  ·  " + songList.size() + " 首", LIST_X, 31, ACCENT, false);
        g.drawString(font, "放入唱片", 26, 87, TEXT_DIM, false);
        String current = RadioPlayback.getCurrentSong();
        String status = current == null ? "尚未播放" : songTitle(current);
        g.drawCenteredString(font, font.plainSubstrByWidth(status, 70), 52, 146,
                current == null ? TEXT_DIM : GOLD);
    }

    private String songTitle(String id) {
        return CustomSongManager.isCustom(id) ? CustomSongManager.title(id) : RadioSongs.title(id).getString();
    }

    private static final class VolumeSlider extends AbstractSliderButton {
        private VolumeSlider(int x, int y, int width, int height) {
            super(x, y, width, height, Component.empty(), RadioPlayback.getVolume());
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.literal("音量 " + Math.round(value * 100.0D) + "%"));
        }

        @Override
        protected void applyValue() {
            RadioPlayback.setVolume((float) value);
        }
    }
    
    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        int lbx = leftPos + LIST_X;
        int lby = topPos + LIST_Y;
        int lh = MAX_ROWS * ROW_H;
        
        if (mx >= lbx && mx < lbx + LIST_W && my >= lby && my < lby + lh) {
            int row = (int)((my - lby) / ROW_H);
            int idx = scroll + row;
            if (idx >= 0 && idx < songList.size()) {
                selected = idx;
                if (btn == 0) playSelected();
                return true;
            }
        }
        return super.mouseClicked(mx, my, btn);
    }
    
    @Override
    public boolean mouseScrolled(double mx, double my, double delta) {
        int max = Math.max(0, songList.size() - MAX_ROWS);
        if (max > 0) {
            scroll = Math.max(0, Math.min(max, scroll - (int)Math.signum(delta)));
            return true;
        }
        return false;
    }
    
    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        renderBackground(g);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        super.render(g, mx, my, pt);
        renderTooltip(g, mx, my);
    }
}
