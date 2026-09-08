package com.he.lastsongofelysian.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SignetOfEgoScreen extends Screen {

    private static final Style PINK                = Style.EMPTY.withColor(0xFFB6C1);
    private static final Style GRAY                = Style.EMPTY.withColor(0xAAAAAA);
    private static final Style LIGHT_GRAY_I        = Style.EMPTY.withColor(0xAAAAAA).withItalic(true);
    private static final Style ORANGE              = Style.EMPTY.withColor(0xFFA500);
    private static final Style LIGHT_ORANGE_I      = Style.EMPTY.withColor(0xFFA500).withItalic(true);
    private static final Style LIGHT_BLUE          = Style.EMPTY.withColor(0xADD8E6);
    private static final Style LIGHT_PINK_I        = Style.EMPTY.withColor(0xFFB6C1).withItalic(true);
    private static final Style DARK_GREEN          = Style.EMPTY.withColor(0x006400);
    private static final Style TEAL_I              = Style.EMPTY.withColor(0x00FF7F).withItalic(true);
    private static final Style GREEN               = Style.EMPTY.withColor(0x00FF00);
    private static final Style LIGHT_GREEN_I       = Style.EMPTY.withColor(0x90EE90).withItalic(true);
    private static final Style LAKE_BLUE           = Style.EMPTY.withColor(0x00CED1);
    private static final Style LIGHT_LAKE_BLUE_I   = Style.EMPTY.withColor(0xCCFFFF).withItalic(true);
    private static final Style ICE_GREEN           = Style.EMPTY.withColor(0x98FB98);
    private static final Style LIGHT_ICE_GREEN_I   = Style.EMPTY.withColor(0x98FB98).withItalic(true);
    private static final Style RED                 = Style.EMPTY.withColor(0xFF0000);
    private static final Style LIGHT_RED_I         = Style.EMPTY.withColor(0xFF9999).withItalic(true);
    private static final Style BROWN               = Style.EMPTY.withColor(0x8B4513);
    private static final Style GRAY_I              = Style.EMPTY.withColor(0xAAAAAA).withItalic(true);
    private static final Style GOLDEN_YELLOW       = Style.EMPTY.withColor(0xFFD700);
    private static final Style LIGHT_GOLDEN_YELLOW_I = Style.EMPTY.withColor(0xFFD700).withItalic(true);
    private static final Style DARK_YELLOW         = Style.EMPTY.withColor(0xB8860B);
    private static final Style LIGHT_YELLOW_I      = Style.EMPTY.withColor(0xFFFFAA).withItalic(true);
    private static final Style DARK_BLUE           = Style.EMPTY.withColor(0x00008B);
    private static final Style BLUE_I              = Style.EMPTY.withColor(0x4444FF).withItalic(true);

    private record Line(String text, Style style) {
        static Line of(String text, Style style) { return new Line(text, style); }
        static Line blank() { return new Line("", Style.EMPTY); }
    }

    private List<FormattedCharSequence> renderLines;
    private double scrollAmount = 0;
    private int maxScroll = 0;
    private int contentHeight = 0;
    private boolean draggingScrollbar = false;

    private int panelX, panelY, panelWidth, panelHeight;
    private int viewX, viewY, viewWidth, viewHeight;
    private static final int LINE_HEIGHT = 10;
    private static final int HEADER_HEIGHT = 24;
    private static final int PADDING = 12;
    private static final int BORDER_COLOR = 0xFFDB7FA8;

    public SignetOfEgoScreen() {
        super(Component.literal("真我祝福"));
    }

    @Override
    protected void init() {
        panelWidth = Math.min(420, this.width - 40);
        panelHeight = Math.min(320, this.height - 40);
        panelX = (this.width - panelWidth) / 2;
        panelY = (this.height - panelHeight) / 2;

        viewX = panelX + PADDING;
        viewY = panelY + HEADER_HEIGHT + PADDING;
        viewWidth = panelWidth - PADDING * 2 - 10;
        viewHeight = panelHeight - HEADER_HEIGHT - PADDING * 2 - 24;

        buildContent();

        addRenderableWidget(Button.builder(Component.literal("关闭"), btn -> onClose())
                .bounds(panelX + panelWidth / 2 - 40, panelY + panelHeight - 24, 80, 20)
                .build());
    }

    private void buildContent() {
        List<Line> lines = new ArrayList<>();

        lines.add(Line.of("让无人传颂的歌谣，再度启唱。", PINK));
        lines.add(Line.of("为历史尘封的故事，续写新章。", PINK));
        lines.add(Line.of("高洁而纯真的灵魂，踏上旅途。", PINK));
        lines.add(Line.blank());

        lines.add(Line.of("愿掠集之兽爱佑其身，赐予「空梦」的自由", GRAY));
        lines.add(Line.of("免疫摔落伤害, 移动速度提高39%", LIGHT_GRAY_I));
        lines.add(Line.blank());

        lines.add(Line.of("愿渡尘之羽爱佑其身，赐予「浮生」的坚忍", ORANGE));
        lines.add(Line.of("免疫击退，获得48%伤害免疫", LIGHT_ORANGE_I));
        lines.add(Line.blank());

        lines.add(Line.of("愿绘世之卷爱佑其身，赐予「繁星」的纯真", LIGHT_BLUE));
        lines.add(Line.of("粉色的，真我的", PINK));
        lines.add(Line.of("攻击时无视防御", LIGHT_PINK_I));
        lines.add(Line.blank());

        lines.add(Line.of("愿噬界之蛇爱佑其身，赐予「无限」的渴求", DARK_GREEN));
        lines.add(Line.of("召唤物与协同者造成的伤害提高31.415926%", TEAL_I));
        lines.add(Line.blank());

        lines.add(Line.of("愿黎明之哨爱佑其身，赐予「旭光」的意志", GREEN));
        lines.add(Line.of("撕裂上限提高18，撕裂伤害无视防御", LIGHT_GREEN_I));
        lines.add(Line.blank());

        lines.add(Line.of("愿寸断之刃爱佑其身，赐予「刹那」的决绝", LAKE_BLUE));
        lines.add(Line.of("闪避冷却减少32%", LIGHT_LAKE_BLUE_I));
        lines.add(Line.blank());

        lines.add(Line.of("愿善法之瞳爱佑其身，赐予「天慧」的智识", ICE_GREEN));
        lines.add(Line.of("获得永久夜视效果", LIGHT_ICE_GREEN_I));
        lines.add(Line.blank());

        lines.add(Line.of("愿坏劫之焱爱佑其身，赐予「鏖灭」的赤诚", RED));
        lines.add(Line.of("血量上限提升60%", LIGHT_RED_I));
        lines.add(Line.blank());

        lines.add(Line.of("愿愚戏之匣爱佑其身，赐予「螺旋」的奇迹", BROWN));
        lines.add(Line.of("武器技能伤害提高64%", GRAY_I));
        lines.add(Line.blank());

        lines.add(Line.of("愿璀耀之歌爱佑其身，赐予「黄金」的光辉", GOLDEN_YELLOW));
        lines.add(Line.of("可以进行40秒的短暂飞行，获得永久水下呼吸", LIGHT_GOLDEN_YELLOW_I));
        lines.add(Line.blank());

        lines.add(Line.of("愿深罪之槛爱佑其身，赐予「戒律」的慈爱", DARK_YELLOW));
        lines.add(Line.of("规诫计数提升30，清除数量将为25%", LIGHT_YELLOW_I));
        lines.add(Line.blank());

        lines.add(Line.of("愿无烬之剑爱佑其身，赐予「救世」的理想", DARK_BLUE));
        lines.add(Line.of("基础攻击力提高100%", BLUE_I));
        lines.add(Line.blank());

        lines.add(Line.of("愿无瑕之人爱佑其身，赐予「真我」的回归", PINK));
        lines.add(Line.of("所有刻印基础效果提升131.4520%", LIGHT_PINK_I));
        lines.add(Line.blank());

        lines.add(Line.of("我们于此赞颂，始源之律者的诞生", LIGHT_PINK_I));
        lines.add(Line.of("「我这支歌将抚摸你的前额，犹如那祝福的亲吻」", LIGHT_PINK_I));
        lines.add(Line.of("「当你独自一人时，它会坐在身旁与你耳语；当你陷入人群时，它又会保护你远离喧嚣」", LIGHT_PINK_I));
        lines.add(Line.of("「我的歌将成为你梦想的羽翼，它将载着你的心到那未知的边缘」", LIGHT_PINK_I));
        lines.add(Line.of("「当黑夜遮蔽了你的路时，它又成为了照耀在你头顶的忠实星光」", LIGHT_PINK_I));
        lines.add(Line.of("于是，「始源」的故事迎来落幕", LIGHT_PINK_I));
        lines.add(Line.of("于是，「始源」的故事迎来伊始", LIGHT_PINK_I));
        lines.add(Line.of("以我为终……", LIGHT_PINK_I));

        renderLines = new ArrayList<>();
        for (Line line : lines) {
            if (line.text().isEmpty()) {
                renderLines.add(FormattedCharSequence.EMPTY);
            } else {
                renderLines.addAll(font.split(Component.literal(line.text()).setStyle(line.style()), viewWidth));
            }
        }

        contentHeight = renderLines.size() * LINE_HEIGHT;
        maxScroll = Math.max(0, contentHeight - viewHeight);
        scrollAmount = Math.min(scrollAmount, maxScroll);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);

        g.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0xEE1A1220);
        g.fill(panelX, panelY, panelX + panelWidth, panelY + 1, BORDER_COLOR);
        g.fill(panelX, panelY + panelHeight - 1, panelX + panelWidth, panelY + panelHeight, BORDER_COLOR);
        g.fill(panelX, panelY, panelX + 1, panelY + panelHeight, BORDER_COLOR);
        g.fill(panelX + panelWidth - 1, panelY, panelX + panelWidth, panelY + panelHeight, BORDER_COLOR);

        g.drawCenteredString(font, "「真我」 无瑕之人 爱莉希雅 II",
                panelX + panelWidth / 2, panelY + 8, BORDER_COLOR);

        g.enableScissor(viewX, viewY, viewX + viewWidth, viewY + viewHeight);
        int y = viewY - (int) scrollAmount;
        for (FormattedCharSequence seq : renderLines) {
            if (y + LINE_HEIGHT >= viewY && y <= viewY + viewHeight) {
                g.drawString(font, seq, viewX, y, 0xFFFFFF);
            }
            y += LINE_HEIGHT;
        }
        g.disableScissor();

        if (maxScroll > 0) {
            int barX = viewX + viewWidth + 4;
            int thumbHeight = Math.max(20, viewHeight * viewHeight / contentHeight);
            int thumbY = viewY + (int) ((viewHeight - thumbHeight) * (scrollAmount / maxScroll));
            g.fill(barX, viewY, barX + 4, viewY + viewHeight, 0x33FFFFFF);
            g.fill(barX, thumbY, barX + 4, thumbY + thumbHeight, BORDER_COLOR);
        }

        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scrollAmount -= delta * LINE_HEIGHT * 3;
        scrollAmount = Math.max(0, Math.min(scrollAmount, maxScroll));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (maxScroll > 0) {
            int barX = viewX + viewWidth + 4;
            int thumbHeight = Math.max(20, viewHeight * viewHeight / contentHeight);
            int thumbY = viewY + (int) ((viewHeight - thumbHeight) * (scrollAmount / maxScroll));
            if (mouseX >= barX && mouseX <= barX + 4 && mouseY >= thumbY && mouseY <= thumbY + thumbHeight) {
                draggingScrollbar = true;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingScrollbar && maxScroll > 0) {
            int thumbHeight = Math.max(20, viewHeight * viewHeight / contentHeight);
            double track = viewHeight - thumbHeight;
            if (track > 0) {
                scrollAmount += (dragY / track) * maxScroll;
                scrollAmount = Math.max(0, Math.min(scrollAmount, maxScroll));
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingScrollbar = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
