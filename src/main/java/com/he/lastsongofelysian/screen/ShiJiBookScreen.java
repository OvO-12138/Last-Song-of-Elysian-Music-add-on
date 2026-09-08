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
public class ShiJiBookScreen extends Screen {

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

    public ShiJiBookScreen() {
        super(Component.literal("岁月史书"));
        jokes = buildJokes();
    }

    private List<JokePage> buildJokes() {
        List<JokePage> list = new ArrayList<>();
        list.add(new JokePage("史一直构·其一",
                "其实\n\n茧爱上了娑\n\n并洗掉娑的记忆\n\n导致小v没人爱\n\n小v心生怨恨\n\n和终焉虫配了\n\n致使茧红温\n\n那茧被牛了肯定不开心\n\n所以派虫做掉了娑\n\n这就是崩三1.5部\n\n那么众所周知\n\n娑和小v长得一模一样\n\n所以被茧当成娑配了\n\n据瓦尔特所说\n\n有一个世界泡是这样的\n\n树上也可能有"));
        list.add(new JokePage("史一直构·其二",
                "在舰长线\n\n舰长把摆渡人配了\n\n当着月下的面\n\n月下难受了\n\n配了观星\n\n其实\n\n凯文是茧的肾结石变的\n\n所以他能捡到终焉之力\n\n但他爱上了茧的屎变的奥托\n\n茧不同意\n\n用手把他俩扣出来\n\n一个扔海里\n\n一个扔到本征世界"));
        list.add(new JokePage("史一直构·其三",
                "其实\n\n瓦尔特是奥托的童养媳\n\n但瓦尔特却爱上了凯文\n\n奥托为了开万花筒杀死了卡莲\n\n但发现卡莲是凯文假扮的\n\n因为奥托爱的是凯文\n\n不\n\n这是真野\n\n奥托爱k423\n\n被茧牛了"));
        list.add(new JokePage("史一直构·其四",
                "据瓦尔特口述\n\n" +
                        "@瓦尔特·杨\n\n" +
                        "@跃云 我来口述\n\n" +
                        "奥托在见到西琳时就爱上了她\n\n" +
                        "所以他做了许多事来吸引注意\n\n" +
                        "所以他在西琳意识空间整活\n\n" +
                        "并不让华砍了西琳\n\n" +
                        "但\n\n" +
                        "崩坏神不乐意了\n\n" +
                        "把奥托顶出去\n\n" +
                        "奥托见不到西琳\n\n" +
                        "当然红了\n\n" +
                        "便让华来了一发太虚剑神\n\n" +
                        "是这样的\n\n" +
                        "可惜\n\n" +
                        "被崩坏神挡住了\n\n" +
                        "西琳也跑了\n\n" +
                        "而奥托为什么喜欢西琳呢\n\n" +
                        "为什么\n\n" +
                        "因为奥托是萝莉控\n\n" +
                        "该电\n\n" +
                        "我吗\n\n" +
                        "那不然为什么奥托要把德丽莎做成小萝莉\n\n" +
                        "奥托\n\n" +
                        "我电死你\n\n" +
                        "饿啊\n\n" +
                        "这次依旧由我一个表演"));
        list.add(new JokePage("史一直构·其五",
                "@奥托  hello 奥托\n\nyou欠me50万father\n\nme will kill you\n\nif you run\n\nyour family=0\n\nnokillme\n\nnokillme\n\niwillgiveyou50万father\n\nif你dont介意\n\nme可以makeyoufather\n\noh no\n\ni will kill you\n\nand\n\n抢走you wifi\n\nno!!!\n\nmy wife\n\nI认为I should fuck you\n\noh\n\n瓦尔特\n\nyou're so sweet\n\nyou're a sweet girl\n\noh my god\n\n奥托\n\nyou dig is so big\n\ni will die\n\n全剧由我一人参演"));
        list.add(new JokePage("史一直构·其六",
                "其实\n\nv2v的极恶人格\n\n一直想扣晕其他7个人格\n\n但是\n\n从对凯文武装可以看出\n\nv2v的其他人格更喜欢凯文\n\n梅和极恶不乐意\n\n给v2v做了个局\n\n这也就是为什么v2v没与虚空万藏融合\n\n之后\n\n把她们全配了\n\n梅也配了凯文\n\n但冻死了\n\n梅的死因是这个"));
        list.add(new JokePage("史一直构·其七",
                "对了\n\nv2v给虚空万藏留的后门\n\n是刚门\n\n之后\n\n虚空万藏被奥托拿走\n\n@奥托\n\n众所周知\n\n虚空万藏本体是金色立方体\n\n刚拿到他时\n\n我不小时扣到了刚门\n\n而虚空万藏爱上了这种感觉\n\n并强迫我继续扣\n\n但我觉得刚太脏\n\n于500年后\n\n假装冲树复活卡莲\n\n趁机逃离虚空万藏\n\n我逃到了因缘精灵的世界\n\n而他误入至星穹铁道\n\n但我遇到琪亚娜了\n\n琪亚娜威胁我合作\n\n否则把虚空万藏整过来\n\n这就是我与虚空万藏的故事\n\n依旧我一人参演"));
        list.add(new JokePage("史一直构·其八",
                "其实\n\n萝莉岛是奥托开的\n\n为了吸引乔伊斯的注意\n\n这边访问二代理律@瓦尔特·杨 \n\n@跃云 我来\n\n据乔伊斯在核心内的意识所说\n\n他大意了\n\n中了奥托的圈套\n\n萝莉全是奥托搓的\n\n和奥托有共感\n\n当乔伊斯法萝莉时\n\n奥托脸色潮红\n\n口中有不明声音\n\n直到有一天\n\n乔伊斯把萝莉法炸了\n\n发现是魂钢做的\n\n他感觉背后阴凉\n\n转头一看\n\n竟是奥托\n\n随后\n\n乔伊斯便被法了\n\n没错\n\n是我法的乔伊斯\n\n依然是我一人"));
        list.add(new JokePage("史一直构·其九",
                "据世界蛇尊主所言\n\n他与小v有关系\n\n@凯文 \n\n没错\n\n众所周知\n\n我和苏是好基友\n\n我和他都互相喜欢\n\n但因性别问题而分开\n\n我在苏的助力下\n\n和mei在一起\n\n在前文明最后\n\n我与mei生下了亚当\n\n并到了现文明\n\n苏得知mei死了\n\n十分开心\n\n去金星做了变性手术\n\n变成了小v\n\n并告知了我\n\n我便在树海交界处把她配了"));
        list.add(new JokePage("史一直构·其十",
                "其实奥托是个女人\n\n奥托小时候性格明显符合女性风格\n\n他是因为某些迫不得已的原因才扮演成男生\n\n而卡莲其实是个男人\n\n因为奥托性格软弱\n\n卡莲的出现刚好弥补了她对另一半性格的要求\n\n后面又得知卡莲是男生\n\n所以爱得死心塌地\n\n这才使得奥托不惜付出一切代价要复活卡莲\n\n而瓦尔特\n\n其实是百年压抑\n\n为了满足基础欲望\n\n而收留的（）\n\n杀死他的父亲\n\n就是为了更好地掌控\n\n这也就是为什么么瓦尔特那么讨厌奥托\n\n而卡莲那边因为他是个男人\n\n所以他才和八重樱纠缠不清\n\n故\n\n奥托是个假小子\n\n卡莲是个男娘"));
        list.add(new JokePage("史一直构·其十一",
                "据世界蛇尊主所言\n\n爱莉希雅互梅比乌斯有一段不为人知的过往\n\n@凯文 \n\n是这样的\n\n众所周知\n\n爱莉希雅小时候生活在孤儿院\n\n十分缺失母爱\n\n在她加入逐火之蛾后\n\n见到了梅比乌斯博士\n\n梅比乌斯十分符合她对于母亲的感觉\n\n情不自禁叫了声妈妈\n\n梅比乌斯当然不会同意\n\n她深爱着克莱因\n\n但克莱因早已被爱莉希雅攻略\n\n并辅助她攻略梅比乌斯\n\n就这样\n\n爱莉希雅体验到了母爱\n\n超越了100%的金星人"));
        list.add(new JokePage("史一直构·其十二",
                "传说\n\n有一个轮回\n\n白厄与yellow and purple 小 fish有一腿\n\n他们相见那天\n\n是一个阳光明媚的夜晚\n\n在这晚\n\n他们说了许多\n\n并定下誓言\n\n要结为夫妻\n\n在白厄成为黄金裔白当天\n\n他们成婚了\n\n在那晚\n\nyellow and purple 小 fish怀了白厄的孩子\n\n生下了企厄\n\n唉\n\n可谁知\n\n企厄刚出生\n\n就被黑厄杀了\n\n连同yellow and purple 小 fish一起\n\n白厄得知消息后悲痛欲绝\n\n成为了铁墓\n\n本节目由流光忆庭/欢愉星神赞助播出"));
        list.add(new JokePage("史一直构·其十三",
                "其实\n\n凯文是茧的肾结石变的\n\n所以他能捡到终焉之力\n\n但他爱上了茧的屎变的奥托\n\n茧不同意\n\n用手把他俩扣出来\n\n一个扔海里\n\n一个扔到本征世界"));
        list.add(new JokePage("史一直构·其十四",
                "据世界蛇尊主所言\n\n开拓者与小格蕾修有一段孽缘\n\n@凯文 \n\n没错\n\n记得当初\n\n格蕾修不小心看见了牢拓的牛子\n\n科斯魔忍不了了\n\n捏爆了牢拓的牛子\n\n毕竟\n\n据格蕾修所说\n\n科斯魔喜欢她\n\n随后\n\n格蕾修感到抱歉\n\n画了个假牛子给牢拓\n\n可谁知\n\n那假牛子不知是不是格蕾修控制的\n\n牢拓飞到一半炸了\n\n牢拓去找格蕾修\n\n却被格蕾修配了\n\n真是可悲可泣"));
        list.add(new JokePage("史一直构·其十五",
                "据我从流光忆庭得来的消息所知\n\n芽衣当时不是进了乐土吗\n\n琪亚娜就跟在芽衣的身后\n\n混进了乐土\n\n而爱莉希雅早已得知消息\n\n对\n\n表面装作深情\n\n想激出琪亚娜\n\n但琪亚娜是何人\n\n不吃压力\n\n愣是等到爱莉希雅与芽衣上床了才出现\n\n她说\n\n“我来的是不是不是时候”\n\n随后被拉爱莉希雅和芽衣拉上床\n\n爱莉希雅奋斗一晚\n\n第二天晕倒在床上\n\n被伊甸捡了漏\n\n本文由流光忆庭赞助播出"));
        list.add(new JokePage("史一直构·其十六",
                "据瓦尔特所说\n\n他曾被虚空万藏爱上\n\n这边有请瓦尔特@瓦尔特·杨 \n\n@跃云 接下来由我讲述\n\n遥想当年\n\n我的养父乔伊斯与奥托结为夫妻\n\n但奥托的儿子\n\n虚空万藏却爱上了我\n\n约定与我私奔\n\n奥托得知此事\n\n十分生气\n\n把我养父杀了\n\n我因此十分恨他\n\n而虚空万藏又与奥托一模一样\n\n我也恨他\n\n于是创立逆熵\n\n与天命相对抗\n\n此后的日子里\n\n我与虚空万藏没有任何联系\n\n可谁知\n\n当他再次与我相遇\n\n是在虚数之树下\n\n他为当年的事感到抱歉\n\n与我一同冲树\n\n复活了我的养父\n\n而他却为了保护我牺牲了\n\n直到我踏入星门\n\n登上星穹列车才知道\n\n他被浮黎救下\n\n于是\n\n我们在车上生活到了现在\n\n并生下了罗刹"));
        list.add(new JokePage("史一直构·其十七",
                "据奥托所言\n\n他当年迷恋小卡莲的身体\n\n由主教讲解@奥托 \n\n拉\n\n@跃云 我来说\n\n众所周知萝莉卡莲十分可爱诱人\n\n但她终究会长大\n\n我便用计除了卡莲\n\n随后我用虚空万藏那学来的知识\n\n克隆出了德丽莎\n\n不出意外\n\n果然是小卡莲的样子\n\n起初\n\n我只是把她当作孙女养\n\n但\n\n瓦尔特却给我下药\n\n还构造羽渡尘\n\n让我误以为德丽莎是卡莲\n\n这个byd\n\n我不就杀了乔伊斯吗\n\n然后\n\n我与德丽莎教培\n\n生下了卡莲的转世\n\n塞西莉亚\n\n之后\n\n我被虚空万藏电了"));
        list.add(new JokePage("史一直构·其十八",
                "据浮黎所言，有一个轮回是这样的。\n白厄在村中有个青梅，是黄紫衣，他与黄紫衣诞下一子，名为企厄，企厄被白厄带到树庭上学，但是，那刻夏的大地兽却爱上了企厄，并为其生了只黄紫大地兽，那刻夏发现后十分生气，前往白厄家中，不过，他到白厄家时，被白厄的美貌所吸引，白厄也愣住了，他从未见过如此帅的人，白厄的竹马，万敌趁二人发愣，将二人困在地下室，每天十八次，导致他的第十节胸椎逃走，以致于他养胃了，见那刻夏多日不去上课，阿格莱雅去寻找他，最终发现了那刻夏，白厄，万敌，大地兽正与三个不明配色的物体在一起，一怒之下，将几人送到高人处，判了死刑\n\n本节目由阿格莱雅赞助播出"));
        list.add(new JokePage("史一直构·其十九",
                "据我流光忆庭的一位神人忆者所说\n\n她从琪亚娜那得知希儿与布洛妮娅的故事\n\n众所周知\n\n东汉末年分三国\n\n希儿是德国的\n\n布洛妮娅是俄国的\n\n那你别管\n\n希儿从美国\n\n得知\n\n俄国总统布洛妮娅十分美丽\n\n于是\n\n发起第一次崩坏\n\n打下了俄国\n\n并将布洛妮娅当作星努\n\n之后\n\n二人活到了现代\n\n被可可利亚洗脑\n\n变成了她的女儿\n\n并将二人分开\n\n在布洛妮娅拜瓦尔特为师后\n\n瓦尔特助她救回希儿\n\n不过\n\n瓦尔特的条件是要布洛妮娅每天加班才救\n\n所以\n\n希儿把瓦尔特杀了\n\n而布洛妮娅则用权能造了个假牛子\n\n配了希儿\n\n生下了\n\n黑希"));
        list.add(new JokePage("史一直构·其二十",
                "据浮黎所说\n\n一位无漏净子名为三月七\n\n在幼年时\n\n被开拓者看上了\n\n开拓者花费数年攻略了幼年三月七\n\n然后被长夜月肘了\n\n但牢拓是何人\n\n把两人全配了\n\n生下了三个照相机，三个垃圾桶\n\n之后\n\n三月七失忆\n\n开拓者为寻她加入了星核猎手\n\n二人再次见面是于空间站中\n\n但此时\n\n开拓者没了牛子\n\n三月七成了扶他\n\n所以\n\n是三月配的开拓者\n\n二人生下了凯文\n\n本节目由群友支持播出"));
        list.add(new JokePage("史一直构·其二十一",
                "据史料记载\n\n在前文明\n\n女同王爱莉希雅开创了女同帝国\n\n打下了全球\n\n之后\n\n爱莉希雅命全球给她上供女人\n\n而梅就是其中之一\n\n梅是凯文的妻子\n\n但凯文常年低温\n\n只好放手\n\n于是爱莉希雅遇见了梅\n\n并把她和梅比乌斯，v2v关在一起\n\n每晚都去扣她们\n\n史料是我写的\n\n爱莉怎么可能放过白毛凯雯\n\n是苏配的凯雯\n\n之后变成小v\n\n和凯雯互扣\n\n被爱莉希雅抓入后宫\n\n爱莉希雅最终因手指过劳而死"));
        list.add(new JokePage("史一直构·其二十二",
                "据琪亚娜所说\n\n她将真理权能收回后\n\n发现\n\n布洛妮娅构建过超级爆闪黄金格调\n\n布洛妮娅应该用这个超级爆闪黄金格调配了希儿\n\n生下了可可利亚与孔子\n\n孔子自称上帝次子\n\n所以布洛妮娅是上帝\n\n随后\n\n琪亚娜也构建出超级爆闪黄金格调\n\n把芽衣配了\n\n但\n\n芽衣把超级爆闪黄金格调捏爆了\n\n反配了琪亚娜\n\n并生下了\n\n梅和凯文\n\n所以两人是骨科"));
        list.add(new JokePage("史一直构·其二十三",
                "据说\n\n凯文喜欢的一直都是苏\n\n只是因为梅是贵族\n\n要和凯文联姻\n\n凯文才被迫和梅在一起的\n\n据克莱因所说\n\n凯文和苏经常一起去厕所\n\n没错两个人都精疲力尽的出来\n\n有一天\n\n凯文和苏的事情被梅发现了\n\n梅上报给了碇源堂\n\n碇源堂大怒\n\n于是就有了人类补完计划\n\n其实\n\n逐火之俄当时还没成立\n\n崩坏就是人类补完计划的副产物\n\n从此\n\n威慑纪元开始\n\n奥托阿波卡利斯发明时光级\n\n把刘慈欣给配了\n\n但是大刘早就想到了\n\n所以提前把大伟哥给配了\n\n然后让奥托和老杨做爱做的事\n\n为救天下\n\n逐火之俄造13战士\n\n一一陨落\n\n苏曾向博识尊觐见\n\n但祂噤声\n\n将求解的机会留给世人\n\n危机之下\n\n本我v2v出现\n\n将事实公开与众\n\n若要根除崩坏\n\n凯文需要和最爱的做爱做的事\n\n众人都以为凯文要和梅做\n\n但\n\n凯文掏出了他使用理之权能做的的18cm黄金格调\n\n苏脸色大变\n\n但为救世人\n\n苏也只好承受这一切\n\n从此\n\n人类过上了没有崩坏的世界\n\n碇源堂也被处决\n\n三月过后\n\n苏招来太医\n\n竟发现有喜了\n\n原来是凯文在使用用理之律者权能做的黄金格调和苏做爱做的事情的时候\n\n苏怀孕了\n\n然后苏就生下了小v\n\n这也是为什么小v没有妈妈爱"));
        list.add(new JokePage("史一直构·其二十四",
                "我曾向博识尊提问\n\n何物为区\n\n博识尊不语\n\n将求解的答案留给世间的区\n\n终于\n\n我知道了\n\n区\n\n就是生命的第一因\n\n人因区而生\n\n并因区而死\n\n区赐予了万物诞生并拥有智慧的能力\n\n但人类也因区而陷入疯狂\n\n所以\n\n为什么我们有资格将区当作敌人\n\n因为在浩瀚的苍穹下，我从不觉得自己卑微\n\n区区们的确巨大无比，但他们不懂得如何思考，也没有感情\n\n所以……如果此时此刻你是肉体凡胎，我们的确可能会失败。但当你步入区\n\n那么，这就是区毁灭的时刻了\n\n此刻\n\n博识尊暗淡的光又重新闪耀了起来\n\n何物为区的答案或许并不重要\n\n重要的是我们走出了区的阴影\n\n并前往没有区的蓝天\n\n然后你就会发现\n\n区\n\n是宇宙诞生时的恨\n\n也是婴儿第一声啼哭对世界的爱与憧憬\n\n当区在人们的视野消失时\n\n我们或许丢失了纯真\n\n但\n\n这也是人类真正获得自由意志\n\n不被既定的现实与未来所束缚\n\n所以\n\n区门"));
        list.add(new JokePage("史一直构·其二十五",
                "据凯文在量子之海中的多年观察发现\n\n其实虚数之树与量子之海有不明关系\n\n@凯文 \n\n没错\n\n据我观察\n\n它们互为男同，女同，夫妻，母子，母女，骨科，父子，父女\n\n它们的本体没有性别\n\n所以它们可以造假牛和假币\n\n记得我刚进海时\n\n树在给海刚塞\n\n然后\n\n这个刚塞十分的大\n\n我定睛一看\n\n竟然是终焉之茧\n\n之后\n\n茧又分成假牛与假币\n\n供二人使用\n\n直到琪亚娜成为终焉之律者才停止"));
        list.add(new JokePage("史一直构·其二十六",
                "瓦尔特爱看女同水仙\n\n所以构造了这么一个世界泡\n\n在这个世界泡里\n\n贝拉的龙形态成了俱利伽罗\n\n人形态仍然活着\n\n贝拉在之后与俱利伽罗看对了眼\n\n不顾芽衣与西琳的阻拦\n\n贝拉被俱利伽罗配了\n\n生下一子\n\n是贝纳勒斯\n\n而贝拉被配炸了\n\n贝纳勒斯对俱利伽罗说\n\n她会长成妈妈的样子\n\n最终\n\n也被配了\n\n所以俱利伽罗被称为紫色配配龙"));
        list.add(new JokePage("史一直构·其二十七",
                "众所周知\n\n姬子是休伯利安的舰长\n\n也是星穹列车的列车长\n\n在姬子来到宇宙后就不开休伯利安了\n\n导致休伯利安心生怨恨\n\n创飞了列车\n\n但\n\n休伯利安发现\n\n列车是个萝莉\n\n把列车配了\n\n生下了帕姆"));
        list.add(new JokePage("史一直构·其二十八",
                "据天命科技有限公司报道\n\n休伯利安还有三小时到达地球\n\n但是\n\n三小时后\n\n休伯利伯屹立于大地之上\n\n爱莉希雅带上凯文他们来到坠机处\n\n救下了晕倒的舰长与德丽莎们\n\n舰长为表感谢\n\n送给他们妙妙小工具\n\n并邀请他们前往量子之海中游玩\n\n遇见了被沈星回附身的白厄\n\n舰长使用妙妙小工具将白厄与沈星回分离\n\n得到了昔涟的感谢\n\n并上了舰\n\n之后\n\n月下从摆渡人处得知\n\n若德莉莎们与舰长接触\n\n舰长会消失\n\n便将德莉莎们带走\n\n舰长明白是摆渡人干的\n\n便驾舰前往\n\n在众人的帮助下\n\n打败了摆渡人\n\n并救回德莉莎们\n\n找回月下\n\n此时\n\n月下已经长大了\n\n与舰长在拯救世界泡的旅途中\n\n结为夫妻"));
        list.add(new JokePage("史一直构·其二十九",
                "有一个轮回是这样的\n赛飞儿在整个翁法罗斯到处乱窜，偷盗东西，被失主发现就说：“这是我拾来的”但是，当开拓者从其他世界过来，她却不敢偷，据赛飞儿口述，她说：“呀异界，我不想拾”"));
        list.add(new JokePage("史一直构·其三十",
                "据遐蝶所说\n\n烈阳哥（白厄）与必痛哥（万敌）有个组合名\n\n叫作\n\n“必阳兄弟”\n\n二人时常为谁是兄，谁是弟打起来\n\n一天\n\n阿格莱雅为他们举办比赛\n\n比谁的审美更好\n\n赢的人当兄\n\n但\n\n白厄穿的一身黄紫配色衣服\n\n把阿格莱雅气死了\n\n没有裁判\n\n二人只好找到那刻夏\n\n但那刻夏因看见白厄养的黄紫大地兽气死了\n\n二人就为谁气死了那刻夏与阿格莱雅开始争吵\n\n碰巧\n\n高人来了\n\n为两人主持案件\n\n但二人因高人太矮没有看见\n\n被处以死刑\n\n在行刑前\n\n高人问谁先死\n\n白厄与万敌仍在争\n\n最终被判同时行刑\n\n最后\n\n万敌在一边跑马拉松一边笑道\n\n没想到吧白厄\n\n我有不死之身\n\n我看并非\n\n一道声音传来\n\n同时一把剑捅入万敌第十节胸椎\n\n来者正是黑厄\n\n最终必阳兄弟以白厄为兄结束\n\n结局二\n\n高人与必阳兄弟在行刑时被海瑟音绑走\n\n送给了华莱士\n\n为了换1份kfc全家桶\n\n三人被关入华莱士的地下室\n\n但华莱士发现\n\n他一摸高人就会被电\n\n便放了她\n\n他望着必阳兄弟\n\n眼中生出了爱心\n\n将二人配了\n\n力歇倒地\n\n必阳兄弟逃走了\n\n而华莱士被凯妮斯关押\n\n并配了\n\n生下了智识星神\n\n必阳兄弟发现他们爱上了那种感觉\n\n就互撅\n\n成为了给给"));
        list.add(new JokePage("史一直构·其三十一",
                "三月七深情地望着长夜月\n\n她深爱着她\n\n但内心自卑\n\n因为她是个啥子\n\n屎沾手上\n\n怕弄脏了长夜月的身体\n\n但长夜月不在意\n\n像壁虎一样趴在墙上\n\n向三月爬去\n\n将三月按住扣\n\n扣出了抖音好友\n\n好友炸了\n\n把三月与长夜月炸死了"));
        list.add(new JokePage("史一直构·其三十二",
                "据摆渡人所说\n\n月下在舰长葬礼上哭\n\n特斯拉问观星怎么了\n\n观星说\n\n舰长吃伟哥吃死了\n\n现在还立着呢\n\n特斯拉有了个点子\n\n带观星套圈\n\n被月下大电锯打死了"));
        list.add(new JokePage("史一直构·其三十三",
                "据小v口述\n\n蕾耶拉是希娜迪雅的妈妈\n\n因为希娜是打瓦的\n\n打瓦时结时蕾耶拉\n\n从名字可以看出\n\n蕾耶拉是拉拉\n\n用火星科技找到了希娜迪雅\n\n将她绑走\n\n绑在床上\n\n配了三天三夜\n\n熵发现妻子希娜迪雅不见后\n\n凭借重女定位找到了希娜迪雅\n\n并与蕾耶拉一起配了希娜迪雅\n\n希娜迪雅最终生下了小v\n\n所以小v妈妈是希娜迪雅\n\n不是没妈的孩子\n\n至于娑\n\n谁认识呢"));
        list.add(new JokePage("史一直构·其三十四",
                "据昔涟所说\n\n丹恒是一位女性\n\n在云上五霄时期\n\n伪装成男性\n\n因为她喜欢白珩\n\n而白珩喜欢男性\n\n但在白珩死后\n\n她发现她爱上了刃\n\n因为刃的饭好吃\n\n于是\n\n她告诉刃她的真实身份\n\n刃知道后\n\n十分开心\n\n他也喜欢丹恒\n\n并在日后配了她\n\n生下了白露\n\n为了隐藏关系\n\n二人借复活白珩为借口\n\n但是\n\n持明一族来不朽死后便生不了孩子了\n\n所以二人向药师祈求\n\n但祝福有弊端\n\n于是诞生了孽龙\n\n致使二人分开\n\n后来\n\n刃时常找丹恒是因为想她了\n\n而丹恒不见他\n\n是因为丹恒成了水仙\n\n每天自配"));
        list.add(new JokePage("史一直构·其三十五",
                "众所周知\n\n赞达尔是博识尊的父亲\n\n但事实并非如此\n\n其实\n\n赞达尔的前身是痞老版，博识尊过去为海伦\n\n海伦被纳努克所杀\n\n痞老板为爱活心爱的海伦\n\n进化成人\n\n成为了赞达尔\n\n但复活海伦时\n\n出了意外\n\n海伦登神了\n\n失去了人性\n\n但依然深爱着赞达尔\n\n并与他生下了权杖\n\n整出白厄\n\n向纳努克复仇\n\n但是\n\n白厄被纳努克抢走\n\n成为毁灭令使\n\n赞达尔又失败了\n\n便做出7具身体\n\n肘击大伟\n\n大伟生气了\n\n配了克苏鲁\n\n生下了终焉之茧\n\n派茧大王肘击意志统括者\n\n肘赢后\n\n代码崩坏\n\n于是诞生树海\n\n树海是骨科\n\n想占有对方\n\n之后树海炸了\n\n诞生了纳努克\n\n所以\n\n痞老板导致纳努克诞生"));
        list.add(new JokePage("史一直构·其三十六",
                "博识尊是萝莉\n\n据浮黎涟记载\n\n博识尊见人就喊杂口\n\n他曾叫纳努克杂口\n\n纳努克红温了\n\n便把他的父亲牛了\n\n当作杯子用\n\n生下了铁墓\n\n但是\n\n铁墓爱上了琥珀王\n\n想要琥珀王的脑袋\n\n但\n\n纳努克不同意了\n\n便将铁墓砍了\n\n选择白厄作为令使\n\n却吃肘了\n\n被白厄肘飞了\n\n三千万转神力\n\n啊哈在旁边笑死了\n\n白厄单杀双神\n\n而铁墓没头了\n\n就抢了啊哈的头\n\n然后\n\n铁墓中毒了\n\n啊哈夺舍了他\n\n就这样\n\n牢古士失去了所有"));
        list.add(new JokePage("史一直构·其三十七",
                "据浮黎所说\n\n三月七\n\n是给\n\n她喜欢将军\n\n因为\n\n将军是太阳\n\n三月七名带月\n\n是月亮\n\n所以三月喜欢将军\n\n但将军喜欢打瓦\n\n叫三月七妈妈\n\n三月不答应\n\n叫将军爸爸\n\n二人开始争吵\n\n最终\n\n认了科比牢大为父\n\n被肘飞了\n\n将军见到了\n\n凯尔希\n\n生下了\n\n凯尔希特勒\n\n三月七遇见了愚公\n\n生下了无穷无尽的山\n\n不过\n\n挡路了\n\n被爆山将军景元打炸\n\n三月七悲痛欲绝\n\n成为了\n\n区"));
        list.add(new JokePage("史一直构·其三十八",
                "据浮黎所记载\n\n白厄刚诞世\n\n就打爆了来古士的高玩\n\n让博识尊绝了后\n\n然后被天庭招安\n\n成了\n\n齐天大日\n\n但白厄不服\n\n肘飞玉帝\n\n当上三界之主\n\n带兵讨伐纳努克\n\n在纳努克身上倒烩面\n\n烫死了纳努克\n\n让博识尊死机\n\n失去高玩与博识尊的来古士悲痛欲绝\n\n成为了\n\n大区星神\n\n被区死了\n\n就这样\n\n白厄救了世"));
        list.add(new JokePage("史一直构·其三十九",
                "据浮黎所记载\n\n三月七和少女有一腿\n\n曾经\n\n三月七路过提瓦特\n\n爱上了少女\n\n并与她成为夫妻\n\n生下了三月男神和黑漆七\n\n然后被天理逐出提瓦特\n\n三月七十分伤心\n\n有了精神分裂症\n\n于是\n\n诞生了长夜月\n\n最终二人和解\n\n成为了真·啥子\n\n二啥返回提瓦特\n\n向天理复仇\n\n最终长夜月成了深渊公主\n\n三月七被封五百年\n\n封印解除后\n\n三月七在封印解除后\n\n上了列车\n\n而长夜月是由三月七精分诞生的\n\n所以在三月七处有备份\n\n花了四年\n\n复活归来\n\n却发现\n\n三月七被木偶牛了\n\n于是将少女抓走\n\n卖给来古士\n\n来古士买下少女后\n\n让她将他扮成浪漫古士\n\n只有这样\n\n才能让博识尊对他有母亲的感觉\n\n之后\n\n打瓦的博识尊\n\n超越了1/3的太阳系的神\n\n被小v记恨\n\n做成了渡星者\n\n而我们现在看见的博识尊\n\n其实是花火假扮的\n\n她算出了\n\n银狼是区\n\n并劝我们不要抽银狼\n\n却被群u当杯子用\n\n但这花火其实是啊哈变的\n\n啊哈成了萝莉妈妈\n\n所以\n\n开拓者会叫啊哈妈妈\n\n三月七也被传染\n\n叫长月夜妈妈\n\n给长夜月爽死了\n\n这一幕\n\n给哈基维利和他的父亲阿基维历看死了\n\n所以是三月七杀了阿基维利"));
        list.add(new JokePage("史一直构·其四十",
                "据浮黎涟所说\n\n有一个轮回是这样的\n\n白厄与牢大与丹恒是青梅竹马\n\n但\n\n有一日\n\n灭世的黑潮\n\n从天外降临\n\n依我所看\n\n这黑潮是纳努克的\n\n史\n\n白厄，牢大与丹恒逃离\n\n到达翁星最高点\n\n三月七的家\n\n并拾来三枚火种\n\n三人成为半神\n\n去肘击黑潮\n\n失败了\n\n三人约定\n\n谁死谁是区\n\n然后\n\n三人在遐蝶面前\n\n哈哈大笑\n\n并吟诗成尊\n\n一把抓过来古士\n\n倾刻炼化\n\n肘赢黑潮\n\n阻止铁墓诞生\n\n三人踏上星穹列车\n\n搓出了两辆列车\n\n成为开拓令使\n\n但在一次旅途中\n\n放了2倍速的see you again\n\n坠机了\n\n被丹恒用化龙妙法复活\n\n继续踏上肘击之路\n\n给三月男神创飞了\n\n把他妈少女气死了\n\n三月七让全场陪葬\n\n被长夜月带走\n\n三月就这样\n\n和长夜月合为一体"));

        return list;

    }

    @Override
    protected void init() {
        super.init();
        int availableWidth = Math.max(1, this.width - 24);
        int availableHeight = Math.max(1, this.height - 24);
        panelWidth = Math.min(availableWidth, Math.max(360, Math.round(this.width * 0.82F)));
        panelHeight = Math.min(availableHeight, Math.max(228, Math.round(this.height * 0.82F)));
        panelLeft = (this.width - panelWidth) / 2;
        panelTop = (this.height - panelHeight) / 2;
        contentTop = panelTop + 62;
        contentBottom = panelTop + panelHeight - 39;

        previousButton = this.addRenderableWidget(Button.builder(Component.literal("<"), button -> {
            if (currentPage > 0) {
                currentPage--;
                scrollOffset = 0;
            }
        }).bounds(this.width / 2 - 58, panelTop + panelHeight - 30, 24, 20).build());

        nextButton = this.addRenderableWidget(Button.builder(Component.literal(">"), button -> {
            if (currentPage < jokes.size() - 1) {
                currentPage++;
                scrollOffset = 0;
            }
        }).bounds(this.width / 2 + 34, panelTop + panelHeight - 30, 24, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        JokePage page = jokes.get(currentPage);

        guiGraphics.fill(panelLeft + 5, panelTop + 6, panelLeft + panelWidth + 5, panelTop + panelHeight + 6, 0x99000000);
        guiGraphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelTop + panelHeight, 0xFF3B1715);
        guiGraphics.fill(panelLeft + 3, panelTop + 3, panelLeft + panelWidth - 3, panelTop + panelHeight - 3, 0xFFD8B879);
        guiGraphics.fill(panelLeft + 7, panelTop + 7, panelLeft + panelWidth - 7, panelTop + panelHeight - 7, 0xFFF1E0B7);
        guiGraphics.fill(panelLeft + 7, panelTop + 7, panelLeft + panelWidth - 7, panelTop + 37, 0xFF6F1D1B);
        guiGraphics.fill(panelLeft + 7, panelTop + 37, panelLeft + panelWidth - 7, panelTop + 40, 0xFFC79B45);
        guiGraphics.fill(panelLeft + 15, panelTop + 47, panelLeft + panelWidth - 15, panelTop + 48, 0xFFB48B4A);
        guiGraphics.fill(panelLeft + 13, panelTop + 13, panelLeft + 17, panelTop + 31, 0xFFE5C56D);
        guiGraphics.fill(panelLeft + panelWidth - 17, panelTop + 13, panelLeft + panelWidth - 13, panelTop + 31, 0xFFE5C56D);

        guiGraphics.drawCenteredString(font, "岁月史书", this.width / 2, panelTop + 18, 0xFFFFDC82);
        guiGraphics.drawString(font, page.title(), panelLeft + 21, panelTop + 51, 0xFF6D211D, false);

        List<FormattedCharSequence> wrapped = font.split(Component.literal(page.content()), panelWidth - 54);
        int totalHeight = wrapped.size() * LINE_HEIGHT;
        int visibleHeight = contentBottom - contentTop;
        maxScroll = Math.max(0, totalHeight - visibleHeight);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

        guiGraphics.enableScissor(panelLeft + 20, contentTop, panelLeft + panelWidth - 22, contentBottom);
        int y = contentTop - scrollOffset;
        for (FormattedCharSequence line : wrapped) {
            guiGraphics.drawString(font, line, panelLeft + 21, y, 0xFF3B2B20, false);
            y += LINE_HEIGHT;
        }
        guiGraphics.disableScissor();

        String pageText = (currentPage + 1) + "/" + jokes.size();
        guiGraphics.drawCenteredString(font, pageText, this.width / 2, panelTop + panelHeight - 23, 0xFF6B261F);

        if (maxScroll > 0) {
            int barHeight = Math.max(12, (int) ((visibleHeight / (float) totalHeight) * visibleHeight));
            int barY = contentTop + (int) ((scrollOffset / (float) maxScroll) * (visibleHeight - barHeight));
            int barX = panelLeft + panelWidth - 17;
            guiGraphics.fill(barX, contentTop, barX + 3, contentBottom, 0x55724E2E);
            guiGraphics.fill(barX, barY, barX + 3, barY + barHeight, 0xFF8E342A);
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
