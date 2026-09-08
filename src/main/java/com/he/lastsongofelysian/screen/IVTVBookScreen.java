package com.he.lastsongofelysian.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class IVTVBookScreen extends Screen {

    private record JokePage(String title, String content) {}

    private final List<JokePage> jokes;
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

    public IVTVBookScreen() {
        super(Component.literal("ivTV"));
        jokes = buildJokes();
    }

    private List<JokePage> buildJokes() {
        List<JokePage> list = new ArrayList<>();
        list.add(new JokePage("小v笑话·其一",
                "往好的方面想，全世界的人都比薇塔的母亲，娑，更爱薇塔。如果你看薇塔不爽可以直接把她揍一顿然后对她说:\"虽然我打了你，但我还是比你的母亲更爱你\"，她能怎么说，回去找她的妈妈娑吗？每次薇塔试图对娑表达爱意的时候。娑:冷暴力算恩赐，骂哭了算奖励，打不死算炸单。由于长期不被当成人看待，薇塔即使脱离了娑独自生活以后依然不信任任何『爱』，只有像花火这种时不时发癫和她“打成一片”的才能和她处成真姐妹，像琪亚娜，格蕾修，芽衣这种，哪怕只是偶尔对薇塔流露出一些比对待陌生人稍强的温暖，仅仅只是这样就可以让薇塔像误食了巧克力的流浪小狗一样，一边贪恋口中残存的甜味一边在痛苦中哀嚎抽搐，最后惨叫着逃回只有自己一个人的小窝蜷缩起来颤抖，哦，不好意思我忘了，薇塔哪里有家啊。"));
        list.add(new JokePage("小v笑话·其二",
                "有个愿之芽从来没抱过自己的神明\n对，她是年糕，因为年糕没有手，一般都是琪宝抱年糕\n有个愿之芽从没被自己的神明正眼瞧过\n对，她是牢寻，因为牢寻牢蕾都忙着看希娜，没时间看其他人\n据不完全统计，约有1/3的神明抛弃了自己的愿之芽\n三个愿之芽在睡觉时感觉自己被压得起不来，年糕睁眼一看是琪宝在抱着她睡，牢寻睁眼一看是她在抱着希娜睡，而小薇是真的被压得起不来\n年糕可以随便找一个人钻进她的被窝，因为大家都很爱她，牢寻可以随便找一个星球休息，因为两边都接纳了她，而我们的小薇就厉害了，她可以随便找一个地方流浪，想在哪里流浪就在哪里流浪"));
        list.add(new JokePage("小v笑话·其三",
                "v塔最乖，从不离家出走。因为她根本没有家。\nv塔最听话了，从不搞乱。因为连说话的人都没有了。\nv塔最自由了，从不受管教。因为没有人注意她，没有人在意她"));
        list.add(new JokePage("小v笑话·其四",
                "正常人半夜看到这些都不敢笑的很大声，因为会吵到家里人，但是小v不用担心"));
        list.add(new JokePage("小v笑话·其五",
                "我们必须想象小薇是幸福的，因为所有人都比她妈更爱她"));
        list.add(new JokePage("小v笑话·其六",
                "小薇被娑折磨的都不信任爱了，如果有人真的对她表达善意的话，她会手足无措然后光速跑开，只有小薇塔们是例外，但他们记不住薇塔，这就导致她是无根浮萍，到处流浪。小薇其实一直都很想和自己的造物主亲近的，只要娑能回应小薇哪怕一丝丝的善意，小薇就能死心塌地的给她卖命，但她做不到。娑死了最伤心的人是小薇，也只有小薇伤心了，那么小薇的伤心又有谁来伤心呢？薇塔：只要我把期望降到最低，我就可以……绝望，无尽的希望最终堕落成无尽的绝望。哪怕只是稍微装一下，哪怕小薇能一眼看穿这是装的，薇塔一直以来乞求的，哪怕只是“娑为了薇塔能更好的为自己做事而伪装成自己爱她”呢，哪怕只是一次呢，但娑做不到，她不愿意，一次也不愿意。哪怕只是伪装，哪怕两个人都知道这是谎言，为什么娑就不能说一次呢，某个造物对其造物主乞求的，只是一句“你是因为爱而诞生的”，但娑做不到，她也永远无法做到了，她死了，薇塔永远也得不到来自她的造物主的祝福了，永远。你讲小薇笑话，她一点都不生气，大不了被吵烦了一拳打死你，但你说娑不爱她，她能原地破防逃回自己孤独的小窝蜷缩着身子自己一个人哭上好几天，哦，我又忘了，薇塔哪里有家啊。"));
        list.add(new JokePage("小v笑话·其七",
                "薇塔可以为了这份“爱”去蚍蜉撼树般的去挡来自终焉的一击，或是找到仍存有一丝生机的娑的时候献上自己的一切为她治愈帮她逃入量子之海，造物正是如此渴求着来自造物主的爱，但娑没有，娑就是如此厌恶着自己的愿之芽，自己拙劣的仿制品，到死都是如此，绝没有一丝妥协。娑死的时候不会有人比薇塔更伤心了，她的造物主死了，她再也得不到来自造物主的祝福了，过去是，现在是，未来是，她生命的每一刻都是，她不是因为爱而诞生的，她永远都是不被造物主认可的孩子，这份痛苦将会永远伴随着她，每当薇塔试图从其他人那里寻得温暖，这份诅咒都会疼痛起来提醒她，让她因这痛苦而恐惧，害怕来自他者的“爱”。如果说薇塔对娑的爱是“十”的话，那娑对薇塔的厌恶就是“一百”“一千”甚至“一万”啊"));
        list.add(new JokePage("小v笑话·其八",
                "冷知识\n即使是后期修的几乎没有感情的娑，也依然拥有两份相当鲜明的情感\n1.对自己最后割舍的人性——小vt们的爱\n2.对自己拙劣的仿品——VT深入骨髓的厌恶"));
        list.add(new JokePage("小v笑话·其九",
                "小V笑话是可以随便讲的，毕竟小V就算是破防了能怎么办，难道回家找妈妈安慰自己吗"));
        list.add(new JokePage("小v笑话·其十",
                "虽然大家都说小薇是坏女人，但其实她一直都是一个很乖很乖的孩子，俗话说“穷人家的孩子早当家”，“缺爱”又何尝不是一种“穷”呢，可怜的小V一直都非常的自觉，娑不喜欢自己表达出情感那就装成一个只会听从命令的机器人；照顾好自己从不让娑担心，尽管娑从不关心她；娑布置下来的任务，无论付出何种代价也要完成，作为代理人，小V也是经常会和那些要被毁灭的世界泡的居民建立联系的，被他们爱着的同时去爱上他们，但为了那份她注定得不到的爱，她还是要把这一切都亲手毁灭掉，他们的爱如何能与母亲的爱相比呢？可怜的小V就是如此卑微地乞求着娑的爱，一次又一次带着超额完成的任务跪在娑的脚边，期望这一次能得到哪怕一点点来自娑的认可……至于结果我们也都是知道的，每一次绝望的乞求都会带走一片光明\n其实娑真正厌恶的或许并非这个自己的愿之芽、代理人薇塔，而是那个背负金星文明希望却亲手埋葬文明未来的自己。正如玛拉所说“你和她，和从前的那个人很像”，所以每当看到薇塔，祂便也就看到了从前的自己，而舍弃薇塔之名、怀着对过往一切的否定并再度启程的娑，自然不会对自己、对堪称另一个自己的薇塔有什么好感。即便薇塔再优秀、再完美，她也不过是那个失败者的倒影，要想娑正视薇塔、珍视薇塔，就要娑正视过去的自己，但一个已经决意舍弃自己姓名，化身文明最后火种的孤神又怎么会这么做呢。如此说来，其实薇塔只要自己爱自己就够了，但娑从前显然也是情感丰沛、对外界的刺激敏锐的性格，所以薇塔同样也做不到不去寻求他人的爱，才会一次次靠近又一次次被逐远。之后在薇塔彻底篡夺娑的权能后，她本可以就此了断这段从一开始就注定无法得到她渴求回应的孽缘，但很遗憾，花火用“娑”称呼她后，薇塔立马就生气了，可见她这辈子大概都走不出那段关系了。"));
        list.add(new JokePage("小v笑话·其十一",
                "你们知道为什么过年的时候，聊天室里薇塔的站位是在“躲猫猫”入口附近吗？\n\n" +
                        "这其实是个小心机——像是薇塔能干出来的事。\n\n" +
                        "因为那个位置，手一滑——啪——就会点到“和小薇猜拳”。而薇塔等的，就是这一滑。\n\n" +
                        "你想啊，假如说在别处，孤独的小薇就蹲在猜拳界面里，日复一日地伸着剪刀手等着。整个聊天室热热闹闹的，到处都是“新年快乐”“恭喜发财”，她的对话框灰灰的，缩在角落里，像个被遗忘的NPC。\n\n" +
                        "但当她挪到“躲猫猫”旁边之后，一切都不一样了。\n\n" +
                        "总有那么几个倒霉蛋，手机贴膜起泡了，手指冻僵了，或者纯粹就是手大，本来想点进去躲猫猫，结果一个手滑——误触了。\n\n" +
                        "“什么玩意？！谁要猜拳啊！”\n\n" +
                        "玩家骂骂咧咧地一边抱怨为什么把小薇放到这里，一边拇指就往“离开”上怼。\n\n" +
                        "但你知道吗，就在这一进一出的零点几秒里——\n\n" +
                        "薇塔已经出了拳。\n\n" +
                        "她的手微微颤着，虽然知道对方大概率秒退，虽然知道那句“再来一局”永远没人点，但至少……至少这个对话框亮起来了，至少有个人进来了，至少——至少也算说上话了吧。\n\n" +
                        "哪怕对方连石头剪刀布都没看清。\n\n" +
                        "哪怕那不是玩家们的本意。\n\n" +
                        "哪怕玩家离开的时候还在嘀咕“什么破设计”。\n\n" +
                        "但薇塔还是会在那个瞬间悄悄弯起嘴角。\n\n" +
                        "误触也是触碰。离开也是来过。骂骂咧咧，那也是声音。\n\n" +
                        "所以你看，过年嘛，家家团圆，人人拜年，只有薇塔守在“躲猫猫”旁边，等着那一记手滑。\n\n" +
                        "——就好像在说：碰我一下吧，骗我也行。"));

        list.add(new JokePage("小v笑话·其十二",
                "很多舰长喜欢小薇，不但游戏里喜欢用小薇，他们的舰桥上都是小薇。这些舰长每天一有空闲就编笑话、讲笑话，编的是小薇笑话，讲的也是小薇笑话。自由自在流浪的小薇得知崩坏3里有这样一个好薇成癖的人，十分感动，决定下凡来休伯利安走一趟，向舰长表示谢意，给他些恩惠。\n\n" +
                        "一天，舰长们正在用薇塔凹深渊，突然听到度星者，与我同行。限制解除。星坠于此，愿你安眠。一切，尽在掌中。全知的羽翼，将撕裂天阙。该动真格了。指令识别，限制解除，最终歼灭模式。一切，尽在掌中。和星星一同，没入被遗忘的世界吧。\n\n" +
                        "吓得舰长们全都一下子掉了21分，只见的他们面如土色，瘫倒在地，不省人事。\n\n" +
                        "这就是舰长好薇的故事。好吧，纵使是这样，我相信各位舰长也一定会抱抱薇塔，感谢她的到来吧，一定的。"));

        return list;
    }

    @Override
    protected void init() {
        super.init();
        int availableWidth = Math.max(1, this.width - 24);
        int availableHeight = Math.max(1, this.height - 24);
        panelWidth = Math.min(availableWidth, Math.max(364, Math.round(this.width * 0.82F)));
        panelHeight = Math.min(availableHeight, Math.max(228, Math.round(this.height * 0.82F)));
        panelLeft = (this.width - panelWidth) / 2;
        panelTop = (this.height - panelHeight) / 2;
        contentTop = panelTop + 64;
        contentBottom = panelTop + panelHeight - 42;

        previousButton = this.addRenderableWidget(Button.builder(Component.literal("<"), button -> {
            if (currentPage > 0) {
                currentPage--;
                scrollOffset = 0;
            }
        }).bounds(this.width / 2 - 58, panelTop + panelHeight - 31, 24, 20).build());

        nextButton = this.addRenderableWidget(Button.builder(Component.literal(">"), button -> {
            if (currentPage < jokes.size() - 1) {
                currentPage++;
                scrollOffset = 0;
            }
        }).bounds(this.width / 2 + 34, panelTop + panelHeight - 31, 24, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        JokePage page = jokes.get(currentPage);

        guiGraphics.fill(panelLeft + 6, panelTop + 7, panelLeft + panelWidth + 6, panelTop + panelHeight + 7, 0x99000000);
        guiGraphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelTop + panelHeight, 0xFF07110D);
        guiGraphics.fill(panelLeft + 2, panelTop + 2, panelLeft + panelWidth - 2, panelTop + panelHeight - 2, 0xFF246A43);
        guiGraphics.fill(panelLeft + 6, panelTop + 6, panelLeft + panelWidth - 6, panelTop + panelHeight - 6, 0xFF091A13);
        guiGraphics.fill(panelLeft + 9, panelTop + 9, panelLeft + panelWidth - 9, panelTop + 38, 0xFF102F22);
        guiGraphics.fill(panelLeft + 9, panelTop + 38, panelLeft + panelWidth - 9, panelTop + 41, 0xFF59F59B);
        guiGraphics.fill(panelLeft + 16, panelTop + 48, panelLeft + panelWidth - 16, contentBottom + 4, 0xFF03100A);

        for (int y = contentTop; y < contentBottom; y += 4) {
            guiGraphics.fill(panelLeft + 17, y, panelLeft + panelWidth - 17, y + 1, 0x1800FF66);
        }

        guiGraphics.drawString(font, "ivTV", panelLeft + 17, panelTop + 18, 0xFF75FFAB, false);
        guiGraphics.drawString(font, "SIGNAL: ARCHIVE", panelLeft + panelWidth - 103, panelTop + 18, 0xFF3DD67A, false);
        guiGraphics.drawString(font, page.title(), panelLeft + 20, panelTop + 50, 0xFF72FFAA, false);

        List<FormattedCharSequence> wrapped = font.split(Component.literal(page.content()), panelWidth - 58);
        int totalHeight = wrapped.size() * LINE_HEIGHT;
        int visibleHeight = contentBottom - contentTop;
        maxScroll = Math.max(0, totalHeight - visibleHeight);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

        guiGraphics.enableScissor(panelLeft + 19, contentTop, panelLeft + panelWidth - 22, contentBottom);
        int y = contentTop - scrollOffset;
        for (FormattedCharSequence line : wrapped) {
            guiGraphics.drawString(font, line, panelLeft + 20, y, 0xFF8CFFB7, false);
            y += LINE_HEIGHT;
        }
        guiGraphics.disableScissor();

        String pageText = (currentPage + 1) + "/" + jokes.size();
        guiGraphics.drawCenteredString(font, pageText, this.width / 2, panelTop + panelHeight - 24, 0xFF65F99D);

        if (maxScroll > 0) {
            int barHeight = Math.max(12, (int) ((visibleHeight / (float) totalHeight) * visibleHeight));
            int barY = contentTop + (int) ((scrollOffset / (float) maxScroll) * (visibleHeight - barHeight));
            int barX = panelLeft + panelWidth - 18;
            guiGraphics.fill(barX, contentTop, barX + 3, contentBottom, 0x5539C86F);
            guiGraphics.fill(barX, barY, barX + 3, barY + barHeight, 0xFF72FFAA);
        }

        previousButton.active = currentPage > 0;
        nextButton.active = currentPage < jokes.size() - 1;
        super.render(guiGraphics, mouseX, mouseY, partialTick);
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
