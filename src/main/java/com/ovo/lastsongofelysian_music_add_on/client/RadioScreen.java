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
    private static final int WIDTH = 256;
    private static final int HEIGHT = 270;
    
    private static final int LIST_X = 68;
    private static final int LIST_Y = 30;
    private static final int LIST_W = 180;
    private static final int ROW_H = 18;
    private static final int MAX_ROWS = 5;
    
    private static final int BTN_Y1 = 124;
    private static final int BTN_Y2 = 149;

    // Vanilla container palette (the same greys used by chest/furnace screens).
    private static final int PANEL = 0xFFC6C6C6;
    private static final int PANEL_LIGHT = 0xFFFFFFFF;
    private static final int PANEL_SHADOW = 0xFF555555;
    private static final int SLOT = 0xFF8B8B8B;
    private static final int TEXT = 0xFF404040;
    private static final int TEXT_DIM = 0xFF606060;
    private static final int PLAYING = 0xFF208020;
    
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
        
        btnPlay = addRenderableWidget(Button.builder(
                Component.translatable("gui.lastsongofelysian_music_add_on.play"), b -> playSelected())
            .bounds(bx, by, 42, 20).build());
            
        btnStop = addRenderableWidget(Button.builder(
                Component.translatable("gui.lastsongofelysian_music_add_on.stop"), b -> RadioPlayback.stop())
            .bounds(bx + 46, by, 42, 20).build());
            
        btnPrev = addRenderableWidget(Button.builder(
                Component.translatable("gui.lastsongofelysian_music_add_on.previous"), b -> prevSong())
            .bounds(bx + 92, by, 42, 20).build());
            
        btnNext = addRenderableWidget(Button.builder(
                Component.translatable("gui.lastsongofelysian_music_add_on.next"), b -> nextSong())
            .bounds(bx + 138, by, 42, 20).build());
            
        btnImport = addRenderableWidget(Button.builder(
                Component.translatable("gui.lastsongofelysian_music_add_on.import"), b -> openImporter())
            .bounds(leftPos + 8, by, 54, 20).build());

        btnRecord = addRenderableWidget(Button.builder(
                Component.translatable("gui.lastsongofelysian_music_add_on.record"), b -> recordDisc())
                .bounds(leftPos + 8, topPos + 104, 54, 16).build());

        int mx = leftPos + LIST_X;
        int my = topPos + BTN_Y2;
        
        btnSeq = addRenderableWidget(Button.builder(
                Component.translatable("gui.lastsongofelysian_music_add_on.repeat_all"),
                b -> setMode(RadioPlayback.Mode.LOOP_ALL))
            .bounds(mx, my, 58, 20).build());
            
        btnRand = addRenderableWidget(Button.builder(
                Component.translatable("gui.lastsongofelysian_music_add_on.shuffle"),
                b -> setMode(RadioPlayback.Mode.SHUFFLE))
            .bounds(mx + 61, my, 58, 20).build());
            
        btnLoop = addRenderableWidget(Button.builder(
                Component.translatable("gui.lastsongofelysian_music_add_on.repeat_one"),
                b -> setMode(RadioPlayback.Mode.LOOP_ONE))
            .bounds(mx + 122, my, 58, 20).build());

        addRenderableWidget(new VolumeSlider(leftPos + 8, topPos + BTN_Y2, 54, 20));
        
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
        if (btnSeq != null) btnSeq.setMessage(getModeLabel(
                "gui.lastsongofelysian_music_add_on.repeat_all", RadioPlayback.Mode.LOOP_ALL));
        if (btnRand != null) btnRand.setMessage(getModeLabel(
                "gui.lastsongofelysian_music_add_on.shuffle", RadioPlayback.Mode.SHUFFLE));
        if (btnLoop != null) btnLoop.setMessage(getModeLabel(
                "gui.lastsongofelysian_music_add_on.repeat_one", RadioPlayback.Mode.LOOP_ONE));
    }
    
    private Component getModeLabel(String translationKey, RadioPlayback.Mode mode) {
        Component label = Component.translatable(translationKey);
        return RadioPlayback.getCurrentMode() == mode
                ? Component.literal("[").append(label).append("]")
                : label;
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
        drawVanillaPanel(g, x, y, WIDTH, HEIGHT);

        // Album art and the physical disc input occupy the left-hand machine area.
        drawInset(g, x + 8, y + 18, 54, 50);
        renderSelectedCover(g, x + 11, y + 19);
        drawSlot(g, x + 24, y + 82);

        int lbx = x + LIST_X, lby = y + LIST_Y;
        int lbh = MAX_ROWS * ROW_H;
        drawInset(g, lbx, lby, LIST_W, lbh);
        g.enableScissor(lbx + 2, lby + 2, lbx + LIST_W - 2, lby + lbh - 2);
        int end = Math.min(songList.size(), scroll + MAX_ROWS);
        for (int i = scroll; i < end; i++) {
            int ry = lby + (i - scroll) * ROW_H;
            int rx = lbx;
            boolean sel = (i == selected);
            boolean hover = mx >= rx && mx < rx + LIST_W && my >= ry && my < ry + ROW_H;
            if (sel) g.fill(rx + 2, ry + 1, rx + LIST_W - 2, ry + ROW_H, 0xFFC6C6C6);
            else if (hover) g.fill(rx + 2, ry + 1, rx + LIST_W - 2, ry + ROW_H, 0xFFA0A0A0);
            if (i > scroll) g.fill(rx + 4, ry, rx + LIST_W - 4, ry + 1, PANEL_SHADOW);

            String sid = songList.get(i);
            if (CustomSongManager.isCustom(sid)) {
                g.drawString(font, "♪", rx + 8, ry + 5, TEXT, false);
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
            if (RadioPlayback.isPlaying(sid)) g.drawString(font, ">", rx + LIST_W - 14, ry + 5, PLAYING, false);
        }
        g.disableScissor();

        if (songList.size() > MAX_ROWS) {
            int sbx = lbx + LIST_W - 4;
            int hs = Math.max(12, lbh * MAX_ROWS / songList.size());
            int maxScroll = songList.size() - MAX_ROWS;
            int hy = lby + (lbh - hs) * scroll / Math.max(1, maxScroll);
            g.fill(sbx, lby + 2, sbx + 2, lby + lbh - 2, PANEL_SHADOW);
            drawRaised(g, sbx - 2, hy, 6, hs);
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                drawSlot(g, x + 47 + column * 18, y + 190 + row * 18);
            }
        }
        for (int column = 0; column < 9; column++) drawSlot(g, x + 47 + column * 18, y + 248);
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
        g.fill(x, y, x + 18, y + 18, SLOT);
        g.fill(x, y, x + 18, y + 1, PANEL_SHADOW);
        g.fill(x, y, x + 1, y + 18, PANEL_SHADOW);
        g.fill(x, y + 17, x + 18, y + 18, PANEL_LIGHT);
        g.fill(x + 17, y, x + 18, y + 18, PANEL_LIGHT);
    }

    private void drawVanillaPanel(GuiGraphics g, int x, int y, int width, int height) {
        g.fill(x, y, x + width, y + height, PANEL);
        g.fill(x, y, x + width, y + 2, PANEL_LIGHT);
        g.fill(x, y, x + 2, y + height, PANEL_LIGHT);
        g.fill(x, y + height - 2, x + width, y + height, PANEL_SHADOW);
        g.fill(x + width - 2, y, x + width, y + height, PANEL_SHADOW);
        g.renderOutline(x + 2, y + 2, width - 4, height - 4, 0xFFAAAAAA);
    }

    private void drawInset(GuiGraphics g, int x, int y, int width, int height) {
        g.fill(x, y, x + width, y + height, SLOT);
        g.fill(x, y, x + width, y + 2, PANEL_SHADOW);
        g.fill(x, y, x + 2, y + height, PANEL_SHADOW);
        g.fill(x, y + height - 2, x + width, y + height, PANEL_LIGHT);
        g.fill(x + width - 2, y, x + width, y + height, PANEL_LIGHT);
    }

    private void drawRaised(GuiGraphics g, int x, int y, int width, int height) {
        g.fill(x, y, x + width, y + height, PANEL);
        g.fill(x, y, x + width, y + 1, PANEL_LIGHT);
        g.fill(x, y, x + 1, y + height, PANEL_LIGHT);
        g.fill(x, y + height - 1, x + width, y + height, PANEL_SHADOW);
        g.fill(x + width - 1, y, x + width, y + height, PANEL_SHADOW);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(font, title, 8, 6, TEXT, false);
        g.drawString(font, Component.translatable("gui.lastsongofelysian_music_add_on.playlist", songList.size()),
                LIST_X, 19, TEXT, false);
        g.drawString(font, Component.translatable("gui.lastsongofelysian_music_add_on.insert_disc"),
                8, 72, TEXT, false);
        g.drawString(font, playerInventoryTitle, 47, 179, TEXT, false);
    }

    private static final class VolumeSlider extends AbstractSliderButton {
        private VolumeSlider(int x, int y, int width, int height) {
            super(x, y, width, height, Component.empty(), RadioPlayback.getVolume());
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.translatable("gui.lastsongofelysian_music_add_on.volume_percent",
                    Math.round(value * 100.0D)));
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
