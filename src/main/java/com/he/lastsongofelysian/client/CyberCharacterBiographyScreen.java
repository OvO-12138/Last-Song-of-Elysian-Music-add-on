package com.he.lastsongofelysian.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public final class CyberCharacterBiographyScreen extends Screen {

    private record BiographyPage(String title, String content) {}

    private final List<BiographyPage> pages;
    private int currentPage;
    private int scrollOffset;
    private int maxScroll;
    private int panelLeft;
    private int panelTop;
    private int panelWidth;
    private int panelHeight;
    private int contentTop;
    private int contentBottom;
    private Button previousButton;
    private Button nextButton;
    private static final int LINE_HEIGHT = 12;

    public CyberCharacterBiographyScreen() {
        super(Component.translatable("item.lastsongofelysian.cyber_character_biography"));
        pages = buildPages();
    }

    private List<BiographyPage> buildPages() {
        List<BiographyPage> list = new ArrayList<>();
        list.add(new BiographyPage("啥子",
                "救命\n\n拉屎没纸\n\n你神了\n\n不是有手吗\n\n我难道就要在厕所里过夜了\n\n可以\n\n不聊了，孩子们我该睡了\n\n火车头桑\n\n@三月七 你等一下\n\n如果有人搭高呢\n\n晚安，巴卡玛卡\n\n（恭喜三月七获得头衔：傻了唧吧有纸）\n\n那手机擦吧\n\n有纸了\n\n我翻垃圾桶里的纸\n\n666\n\n一边打一边说垃圾话\n\n不好\n\n手上沾屎了\n\n@三月七 ？\n\n啥子\n\n好\n\n吃\n\n坏了，真就是傻子了\n\n别让我笑了\n\n不\n\n手洗干净了吗\n\n@玛卡八嘎 蹭到身上了\n\n幸亏我没穿衣服\n\n ？\n\n没有人类了\n\n我要笑死了\n\n现在洗干净了\n\n啥子\n\n谁爆的人类星辉\n\n今天都什么破事啊\n\n@三月七 你改名叫扑满吧\n\n给啥子攻击改成扔屎\n\n/不对不对\n\n伤害5点\n\n纯恶心人\n\n20\n\n踩到就死\n\n不\n\n再加反胃\n\n饥饿\n\n虚弱\n\n除非攻速很快每次攻击都加反胃我是不会接受的\n\n@三月七 2s一坨\n\n1秒\n\n反胃5s\n\n10秒\n\n屎王\n\n还是不要这样做了\n\n@落叶归根 我才发现你没睡\n\n我也得睡了"));
        list.add(new BiographyPage("区哥",
                "As we pray that the king of Minecraft “Finality” \nor we call him the brother maggot, \nmay finish the crucial cycle before he reach the end of life and arriving the external life: \nHell\nAs part of the author of “the holy record of the past”\n it is my responsibility to record brother maggot and spread the everlasting gospel of brother maggot.\n“I think, therefore, I suck”-brother maggot \nThe dim stars, don’t be afraid, \nthe brother maggot is here,\n he will bring you, me,\n even the whole universe back to the embrace of finality\nJust go ahead and..\nDriving toward tomorrow.\nThe ideal world of plato may not exist, \nNietzsche’s Amor fati may not help us from the suffers.\nHe will, \nthe brother maggot will. \nHe sacrificed himself to the cocoon of finality, \njust to save the thousands of life.\nSo…\nfor brother maggot, \nthe holy one,\n the unique one, \nthe everlasting one.\nmaggot"));
        list.add(new BiographyPage("区哥",
                "据区哥直播可知\n\n区哥与僵尸，溺尸，小白，小黑，门，干草块，小fish，铁哥，崩坏能，茧有难绷的关系\n\n在首次好的开始的轮回\n\n此次轮回中\n\n被溺尸与小白堵门\n\n之后搭4格打铁哥\n\n然后得到关键道具\n\n并发现重置bug\n\n但是\n\n被骗地带上茧，而吃到负面效果，死亡\n\n之后一次轮回中\n\n被幻翼堵门\n\n看了史书\n\n最终炸了\n\n轮回中\n\n区哥为寻藏宝箱\n\n死亡\n\n箱中有5钻石\n\n轮回中\n\n区哥活了几天\n\n但因理之祖咒而死\n\n轮回中\n\n区哥多次寻找yellow and purple小fish\n\n与\n\n干草块\n\n但二者不可得兼\n\n从未同时拥有\n\n真是可悲可泣啊"));
        list.add(new BiographyPage("被迫的啥子",
                "emmmm\n\n我现在肚子疼想放屁，但是我觉得放出来肯定会有不好的事\n\n还有十几分的车程，我现在皮炎夹得死紧\n\n哦不对，只有几分钟了\n\n我超威好颠簸\n\n差点出来了😭😭😭\n\n？\n\n😭😭😭\n\n为什么还要接人\n\n好漫长\n\n😭😭😭\n\n哦不\n\n为什么全是红灯\n\n😭😭😭\n\n我超威，你设置那么多减速带干什么\n\n😭😭😭\n\n小荷才露尖尖角，我感受到它的存在了，我现在浑身都在使劲夹住\n\n😭😭\n\n为什么😭😭\n\n怎么还有一个红灯\n\n😭😭😭😭\n\n为什么\n\n就差一步\n\n@格蕾修（萝莉正常爵)「繁星」 绘世之卷 刚到厕所还没脱，憋不住了\n\n回家洗裤子吧\n\n666\n\n@雨不停遐 zdjd\n\n但凡不接人我就撑住了\n\n@雨不停遐 可惜了，就差几秒\n\n洗澡去了😭😭😭\n\n差一点，就差一点啊😭😭\n\n为什么就差那一秒\n\n😭"));
        return list;
    }

    @Override
    protected void init() {
        super.init();
        int availableWidth = Math.max(1, this.width - 24);
        int availableHeight = Math.max(1, this.height - 24);
        panelWidth = Math.min(availableWidth, Math.max(370, Math.round(this.width * 0.82F)));
        panelHeight = Math.min(availableHeight, Math.max(228, Math.round(this.height * 0.82F)));
        panelLeft = (this.width - panelWidth) / 2;
        panelTop = (this.height - panelHeight) / 2;
        contentTop = panelTop + 66;
        contentBottom = panelTop + panelHeight - 41;

        previousButton = this.addRenderableWidget(Button.builder(Component.literal("<"), button -> {
            if (currentPage > 0) {
                currentPage--;
                scrollOffset = 0;
            }
        }).bounds(this.width / 2 - 58, panelTop + panelHeight - 31, 24, 20).build());

        nextButton = this.addRenderableWidget(Button.builder(Component.literal(">"), button -> {
            if (currentPage < pages.size() - 1) {
                currentPage++;
                scrollOffset = 0;
            }
        }).bounds(this.width / 2 + 34, panelTop + panelHeight - 31, 24, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        BiographyPage page = pages.get(currentPage);

        guiGraphics.fill(panelLeft + 7, panelTop + 7, panelLeft + panelWidth + 7, panelTop + panelHeight + 7, 0xA0000000);
        guiGraphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelTop + panelHeight, 0xFF020817);
        guiGraphics.fill(panelLeft + 2, panelTop + 2, panelLeft + panelWidth - 2, panelTop + panelHeight - 2, 0xFF0DD8F2);
        guiGraphics.fill(panelLeft + 5, panelTop + 5, panelLeft + panelWidth - 5, panelTop + panelHeight - 5, 0xFF071128);
        guiGraphics.fill(panelLeft + 9, panelTop + 9, panelLeft + panelWidth - 9, panelTop + 42, 0xFF101D3D);
        guiGraphics.fill(panelLeft + 9, panelTop + 42, panelLeft + panelWidth - 9, panelTop + 45, 0xFFFF3EB5);
        guiGraphics.fill(panelLeft + 15, panelTop + 53, panelLeft + panelWidth - 15, contentBottom + 4, 0xFF030B1C);
        guiGraphics.fill(panelLeft + 9, panelTop + 9, panelLeft + 12, panelTop + 36, 0xFF11F0FF);
        guiGraphics.fill(panelLeft + panelWidth - 12, panelTop + 9, panelLeft + panelWidth - 9, panelTop + 36, 0xFFFF3EB5);
        guiGraphics.fill(panelLeft + 20, panelTop + 49, panelLeft + 78, panelTop + 51, 0xFF0DD8F2);
        guiGraphics.fill(panelLeft + panelWidth - 78, panelTop + 49, panelLeft + panelWidth - 20, panelTop + 51, 0xFFFF3EB5);
        guiGraphics.fill(panelLeft + 16, panelTop + 56, panelLeft + 18, panelTop + 66, 0xFF0DD8F2);
        guiGraphics.fill(panelLeft + 18, panelTop + 56, panelLeft + 28, panelTop + 58, 0xFF0DD8F2);

        guiGraphics.drawString(font, "CYBER // PERSONNEL FILE", panelLeft + 19, panelTop + 20, 0xFF7EF7FF, false);
        guiGraphics.drawString(font, "X", panelLeft + panelWidth - 24, panelTop + 20, 0xFFFF6BC8, false);
        guiGraphics.drawString(font, page.title(), panelLeft + 22, panelTop + 55, 0xFFFF73CD, false);

        List<FormattedCharSequence> wrapped = font.split(Component.literal(page.content()), panelWidth - 62);
        int totalHeight = wrapped.size() * LINE_HEIGHT;
        int visibleHeight = contentBottom - contentTop;
        maxScroll = Math.max(0, totalHeight - visibleHeight);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

        guiGraphics.enableScissor(panelLeft + 21, contentTop, panelLeft + panelWidth - 23, contentBottom);
        int y = contentTop - scrollOffset;
        for (FormattedCharSequence line : wrapped) {
            guiGraphics.drawString(font, line, panelLeft + 22, y, 0xFFBCEFFF, false);
            y += LINE_HEIGHT;
        }
        guiGraphics.disableScissor();

        String pageText = (currentPage + 1) + "/" + pages.size();
        guiGraphics.drawCenteredString(font, pageText, this.width / 2, panelTop + panelHeight - 24, 0xFF62E8FF);

        if (maxScroll > 0) {
            int barHeight = Math.max(12, (int) ((visibleHeight / (float) totalHeight) * visibleHeight));
            int barY = contentTop + (int) ((scrollOffset / (float) maxScroll) * (visibleHeight - barHeight));
            int barX = panelLeft + panelWidth - 19;
            guiGraphics.fill(barX, contentTop, barX + 3, contentBottom, 0x553EDFF3);
            guiGraphics.fill(barX, barY, barX + 3, barY + barHeight, 0xFFFF4DBC);
        }

        previousButton.active = currentPage > 0;
        nextButton.active = currentPage < pages.size() - 1;
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0
                && mouseX >= panelLeft + panelWidth - 30
                && mouseX <= panelLeft + panelWidth - 12
                && mouseY >= panelTop + 13
                && mouseY <= panelTop + 34) {
            onClose();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (mouseX >= panelLeft && mouseX <= panelLeft + panelWidth
                && mouseY >= contentTop && mouseY <= contentBottom) {
            scrollOffset -= delta * LINE_HEIGHT;
            scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
