package com.he.lastsongofelysian.util;

import com.he.lastsongofelysian.item.SilverPouchItem;
import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class SignetUpgradeData {

    public static final String LEGACY_NBT_LEVEL = "LSESignetUpgradeLevel";
    public static final String NBT_SKILL_LEVELS = "LSESignetSkillLevels";
    public static final String NBT_PLAYER_SKILL_LEVELS = "LSEPersistentSignetSkillLevels";
    public static final String NBT_DELIVERANCE_SECONDARY_UNLOCKS = "LSEDeliveranceSecondaryUnlocks";
    public static final String NBT_DELIVERANCE_CORE = "LSEDeliveranceCore";
    public static final String NBT_DELIVERANCE_CORE_SIGNETS = "LSEDeliveranceCoreSignets";
    public static final String NBT_DELIVERANCE_CORE_PURCHASED = "LSEDeliveranceCorePurchased";
    public static final String NBT_DELIVERANCE_CORE_SIGNETS_PURCHASED = "LSEDeliveranceCoreSignetsPurchased";
    public static final String NBT_DELIVERANCE_CORE_SILVER_SPENT = "LSEDeliveranceCoreSilverSpent";
    public static final String NBT_DELIVERANCE_CORE_EXPERIENCE_SPENT = "LSEDeliveranceCoreExperienceSpent";
    public static final String NBT_SHOP_PAID_SILVER_PER_ITEM = "LSEShopPaidSilverPerItem";
    private static final String NBT_PLAYER_PERSISTED = "PlayerPersisted";
    public static final int MAX_LEVEL = 3;
    public static final int UPGRADE_ITEM_COST = 1;
    public static final int EXPERIENCE_LEVEL_COST = 10;
    private static final float[] FACTORS = {1.0F, 1.15F, 1.30F, 1.50F};
    private static final float EGO_MULTIPLIER = 2.314520F;

    public enum Signet {
        REVERIE("空梦", () -> ModItems.SIGNET_OF_REVERIE.get()),
        VICISSITUDE("浮生", () -> ModItems.SIGNET_OF_VICISSITUDE.get()),
        STARS("繁星", () -> ModItems.SIGNET_OF_STARS.get()),
        INFINITY("无限", () -> ModItems.SIGNET_OF_INFINITY.get()),
        DAYBREAK("旭光", () -> ModItems.SIGNET_OF_DAYBREAK.get()),
        SETSURA("刹那", () -> ModItems.SIGNET_OF_SETSURA.get()),
        BODHI("天慧", () -> ModItems.SIGNET_OF_BODHI.get()),
        DECIMATION("鏖灭", () -> ModItems.SIGNET_OF_DECIMATION.get()),
        HELIX("螺旋", () -> ModItems.SIGNET_OF_HELIX.get()),
        GOLD("黄金", () -> ModItems.SIGNET_OF_GOLD.get()),
        DISCIPLINE("戒律", () -> ModItems.SIGNET_OF_DISCIPLINE.get()),
        DELIVERANCE("救世", () -> ModItems.SIGNET_OF_DELIVERANCE.get());

        private final String displayName;
        private final Supplier<Item> item;

        Signet(String displayName, Supplier<Item> item) {
            this.displayName = displayName;
            this.item = item;
        }

        public String displayName() {
            return this.displayName;
        }

        public Item item() {
            return this.item.get();
        }
    }

    public enum DeliveranceCore {
        KINGS_SWORD(
                1,
                "救世者的王剑",
                "开启武器必杀技后，进入终末之战状态。\n期间所有「救世」普通刻印提供的加成效果提高50%，持续7秒。"
        ),
        LONE_SHADOW(
                2,
                "救世者的孤影",
                "释放武器必杀技后五秒内的攻击累计命中20次后，进入救世之战状态。\n救世之战状态下所有「救世」普通刻印提供的加成效果强制生效，持续10秒。"
        );

        private final int id;
        private final String displayName;
        private final String description;

        DeliveranceCore(int id, String displayName, String description) {
            this.id = id;
            this.displayName = displayName;
            this.description = description;
        }

        public int id() {
            return this.id;
        }

        public String displayName() {
            return this.displayName;
        }

        public String description() {
            return this.description;
        }

        public static DeliveranceCore fromId(int id) {
            for (DeliveranceCore core : values()) {
                if (core.id == id) return core;
            }
            return null;
        }
    }

    public enum Skill {
        REVERIE_EXPERIENCE(Signet.REVERIE, "reverie_experience", "人生，空梦一场",
                "经验获取 +100%", "经验获取 +115%", "经验获取 +130%", "经验获取 +150%"),
        REVERIE_DISCOUNT(Signet.REVERIE, "reverie_discount", "值钱的，闪闪的",
                "商店价格为原价的 80%", "商店价格为原价的 75%", "商店价格为原价的 72%", "商店价格为原价的 70%"),
        REVERIE_SILVER(Signet.REVERIE, "reverie_silver", "行商者的哲学",
                "银币获取 +10%", "银币获取 +11.5%", "银币获取 +13%", "银币获取 +15%"),

        VICISSITUDE_STRENGTH(Signet.VICISSITUDE, "vicissitude_strength", "行路漫漫",
                "每10秒获得一层力量，最多三层", "每9秒获得一层力量，最多三层", "每8秒获得一层力量，最多三层", "每7秒获得一层力量，最多三层"),
        VICISSITUDE_RESISTANCE(Signet.VICISSITUDE, "vicissitude_resistance", "纵使寻得",
                "每10秒获得一层抗性，最多三层", "每9秒获得一层抗性，最多三层", "每8秒获得一层抗性，最多三层", "每7秒获得一层抗性，最多三层"),

        STARS_RED(Signet.STARS, "stars_red", "红色的。热热的",
                "画笔使用红色颜料时，使周围的敌人被染上红色油墨\n使其受到的伤害增加10%", "画笔使用红色颜料时，使周围的敌人被染上红色油墨\n使其受到的伤害增加11.5%", "画笔使用红色颜料时，使周围的敌人被染上红色油墨\n使其受到的伤害增加13%", "画笔使用红色颜料时，使周围的敌人被染上红色油墨\n使其受到的伤害增加15%"),
        STARS_BLUE(Signet.STARS, "stars_blue", "蓝色的。冷冷的",
                "画笔使用蓝色颜料时，使周围的敌人被染上蓝色油墨\n使其造成的伤害降低10%", "画笔使用蓝色颜料时，使周围的敌人被染上蓝色油墨\n使其造成的伤害降低11.5%", "画笔使用蓝色颜料时，使周围的敌人被染上蓝色油墨\n使其造成的伤害降低13%", "画笔使用蓝色颜料时，使周围的敌人被染上蓝色油墨\n使其造成的伤害降低15%"),

        INFINITY_REBIRTH(Signet.INFINITY, "infinity_rebirth", "超越无限的命途",
                "受到致命伤害时，不会直接死亡，而是进入褪麟状态：减少20%的最大血量上限，最多减少到1点。\n生命上限小于或等于1%时，下一次受到致命伤害，会直接死亡并恢复全部血量上限。褪麟持续时间为3分钟", "受到致命伤害时，不会直接死亡，而是进入褪麟状态：减少18%的最大血量上限，最多减少到1点。\n生命上限小于或等于1%时，下一次受到致命伤害，会直接死亡并恢复全部血量上限。褪麟持续时间为3分钟", "受到致命伤害时，不会直接死亡，而是进入褪麟状态：减少16%的最大血量上限，最多减少到1点。\n生命上限小于或等于1%时，下一次受到致命伤害，会直接死亡并恢复全部血量上限。褪麟持续时间为3分钟", "受到致命伤害时，不会直接死亡，而是进入褪麟状态：减少14%的最大血量上限，最多减少到1点。\n生命上限小于或等于1%时，下一次受到致命伤害，会直接死亡并恢复全部血量上限。褪麟持续时间为3分钟"),
        INFINITY_ALLY(Signet.INFINITY, "infinity_ally", "利齿的「V」",
                "场上每有一名协同者或人偶，造成的伤害增加5%，最多叠加三次", "场上每有一名协同者或人偶，造成的伤害增加5.75%，最多叠加三次", "场上每有一名协同者或人偶，造成的伤害增加6.5%，最多叠加三次", "场上每有一名协同者或人偶，造成的伤害增加7.5%，最多叠加三次"),

        DAYBREAK_CLAW(Signet.DAYBREAK, "daybreak_claw", "亵渎不归之「爪」",
                "角色攻击时，对攻击目标施加一层撕裂，每层撕裂每秒造成0.5伤害，持续五秒，可叠加十层。\n佩戴追忆之证「长天，易望难及」时，对拥有撕裂状态下的生物造成的伤害提高15%", "角色攻击时，对攻击目标施加一层撕裂，每层撕裂每秒造成0.575伤害，持续五秒，可叠加十层。\n佩戴追忆之证「长天，易望难及」时，对拥有撕裂状态下的生物造成的伤害提高17.25%", "角色攻击时，对攻击目标施加一层撕裂，每层撕裂每秒造成0.65伤害，持续五秒，可叠加十层。\n佩戴追忆之证「长天，易望难及」时，对拥有撕裂状态下的生物造成的伤害提高19.5%", "角色攻击时，对攻击目标施加一层撕裂，每层撕裂每秒造成0.75伤害，持续五秒，可叠加十层。\n佩戴追忆之证「长天，易望难及」时，对拥有撕裂状态下的生物造成的伤害提高22.5%"),
        DAYBREAK_EYE(Signet.DAYBREAK, "daybreak_eye", "俯视邪渊之「眼」",
                "生物每持有一层撕裂，角色对其防具的伤害提升5%，受到的伤害提高2%", "生物每持有一层撕裂，角色对其防具的伤害提升5.75%，受到的伤害提高2.3%", "生物每持有一层撕裂，角色对其防具的伤害提升6.5%，受到的伤害提高2.6%", "生物每持有一层撕裂，角色对其防具的伤害提升7.5%，受到的伤害提高3%"),

        SETSURA_DODGE(Signet.SETSURA, "setsura_dodge", "御神装·勿忘-樱之舞",
                "角色可以触发闪避，概率为20%，冷却20秒，不可免疫秒杀技", "角色可以触发闪避，概率为23%，冷却18.4秒，不可免疫秒杀技", "角色可以触发闪避，概率为26%，冷却16.8秒，不可免疫秒杀技", "角色可以触发闪避，概率为30%，冷却15秒，不可免疫秒杀技"),
        SETSURA_SPEED(Signet.SETSURA, "setsura_speed", "缭乱百花「梅」",
                "触发极限闪避技能后，攻击速度和移动速度提高10%，持续8秒", "触发极限闪避技能后，攻击速度和移动速度提高11.5%，持续8秒", "触发极限闪避技能后，攻击速度和移动速度提高13%，持续8秒", "触发极限闪避技能后，攻击速度和移动速度提高15%，持续8秒"),

        BODHI_DAMAGE(Signet.BODHI, "bodhi_damage", "宿命之箴言",
                "连击数达到15时清空全部连击数，全伤害提高25%，持续5秒", "连击数达到15时清空全部连击数，全伤害提高28.75%，持续5秒", "连击数达到15时清空全部连击数，全伤害提高32.5%，持续5秒", "连击数达到15时清空全部连击数，全伤害提高37.5%，持续5秒"),
        BODHI_DEFENSE(Signet.BODHI, "bodhi_defense", "天耳之箴言",
                "连击数达到15时清空全部连击数，受到的全伤害降低12%，持续5秒", "连击数达到15时清空全部连击数，受到的全伤害降低13.8%，持续5秒", "连击数达到15时清空全部连击数，受到的全伤害降低15.6%，持续5秒", "连击数达到15时清空全部连击数，受到的全伤害降低18%，持续5秒"),

        DECIMATION_HEALTH(Signet.DECIMATION, "decimation_health", "命路·命舛·命刻",
                "生命上限 +15%", "生命上限 +17.25%", "生命上限 +19.5%", "生命上限 +22.5%"),
        DECIMATION_LOW_HEALTH(Signet.DECIMATION, "decimation_low_health", "狂信·狂人·狂言",
                "每损失1生命减伤0.3%，上限20%", "每损失1生命减伤0.345%，上限23%", "每损失1生命减伤0.39%，上限26%", "每损失1生命减伤0.45%，上限30%"),
        DECIMATION_FIRE(Signet.DECIMATION, "decimation_fire", "赤骨·赤血·赤练",
                "火焰伤害 +50%，受到火伤 -40%", "火焰伤害 +57.5%，受到火伤 -40%", "火焰伤害 +65%，受到火伤 -40%", "火焰伤害 +75%，受到火伤 -40%"),

        HELIX_MAGIC(Signet.HELIX, "helix_magic", "第一幕「魔术」",
                "武器技每层全伤害 +10%", "武器技每层全伤害 +11.5%", "武器技每层全伤害 +13%", "武器技每层全伤害 +15%"),
        HELIX_PENDULUM(Signet.HELIX, "helix_pendulum", "第二幕「钟摆」",
                "武器技使敌人易伤20%", "武器技使敌人易伤23%", "武器技使敌人易伤26%", "武器技使敌人易伤30%"),
        HELIX_PARADOX(Signet.HELIX, "helix_paradox", "第三幕「矛盾」",
                "铁砧消耗降为1", "铁砧额外修复25%耐久", "铁砧额外修复50%耐久", "铁砧额外修复75%耐久"),

        GOLD_STREAM(Signet.GOLD, "gold_stream", "溪流的宣叙",
                "正面效果 +30%，矿物与熔炉 +200%", "正面效果 +34.5%，矿物与熔炉 +230%", "正面效果 +39%，矿物与熔炉 +260%", "正面效果 +45%，矿物与熔炉 +300%"),
        GOLD_PARADISE(Signet.GOLD, "gold_paradise", "乐园的宣叙",
                "每10能量伤害 +5%，上限65%", "每10能量伤害 +5.75%，上限74.75%", "每10能量伤害 +6.5%，上限84.5%", "每10能量伤害 +7.5%，上限97.5%"),

        DISCIPLINE_ONE(Signet.DISCIPLINE, "discipline_one", "其一，不可背叛",
                "每点规诫使普攻伤害 +0.3%", "每点规诫使普攻伤害 +0.345%", "每点规诫使普攻伤害 +0.39%", "每点规诫使普攻伤害 +0.45%"),
        DISCIPLINE_TWO(Signet.DISCIPLINE, "discipline_two", "其二，不可欺瞒",
                "每点规诫使伤害抵抗 +0.15%", "每点规诫使伤害抵抗 +0.1725%", "每点规诫使伤害抵抗 +0.195%", "每点规诫使伤害抵抗 +0.225%"),
        DISCIPLINE_THREE(Signet.DISCIPLINE, "discipline_three", "其三，不可暴戾",
                "每点规诫使能量回复 +0.3%", "每点规诫使能量回复 +0.345%", "每点规诫使能量回复 +0.39%", "每点规诫使能量回复 +0.45%"),

        DELIVERANCE_ULTIMATE(Signet.DELIVERANCE, "deliverance_ultimate", "施予者的金杯",
                "开启武器必杀技后，全伤害提高25%，持续7秒。",
                "开启武器必杀技后，全伤害提高28.75%，持续7秒。",
                "开启武器必杀技后，全伤害提高32.5%，持续7秒。",
                "开启武器必杀技后，全伤害提高37.5%，持续7秒。"),
        DELIVERANCE_EFFECT(Signet.DELIVERANCE, "deliverance_effect", "守望者的坠饰",
                "自身受到的负面效果持续时间缩短30%。",
                "自身受到的负面效果持续时间缩短34.5%。",
                "自身受到的负面效果持续时间缩短39%。",
                "自身受到的负面效果持续时间缩短45%。"),
        DELIVERANCE_ARMOR(Signet.DELIVERANCE, "deliverance_armor", "不死者的烙印",
                "自身的护甲值提高15%。",
                "自身的护甲值提高17.25%。",
                "自身的护甲值提高19.5%。",
                "自身的护甲值提高22.5%。"),
        DELIVERANCE_HUNTERS_MASK(Signet.DELIVERANCE, "deliverance_hunters_mask", "猎杀者的假面",
                "开启武器必杀技后，伤害穿透提高15%，持续7秒。",
                "开启武器必杀技后，伤害穿透提高20%，持续7秒。",
                "开启武器必杀技后，伤害穿透提高25%，持续7秒。",
                "开启武器必杀技后，伤害穿透提高30%，持续7秒。"),
        DELIVERANCE_RESTRAINERS_RELIC(Signet.DELIVERANCE, "deliverance_restrainers_relic", "制约者的圣器",
                "开启武器必杀技后，获得相当于最大生命值20%的护盾，并提高抗打断能力，持续7秒。",
                "开启武器必杀技后，获得相当于最大生命值25%的护盾，并提高抗打断能力，持续7秒。",
                "开启武器必杀技后，获得相当于最大生命值30%的护盾，并提高抗打断能力，持续7秒。",
                "开启武器必杀技后，获得相当于最大生命值35%的护盾，并提高抗打断能力，持续7秒。"),
        DELIVERANCE_SEEKERS_ROBE(Signet.DELIVERANCE, "deliverance_seekers_robe", "求道者的法衣",
                "开启武器必杀技后，每秒回复3点能量，持续7秒。",
                "开启武器必杀技后，每秒回复4点能量，持续7秒。",
                "开启武器必杀技后，每秒回复5点能量，持续7秒。",
                "开启武器必杀技后，每秒回复6点能量，持续7秒。"),

        DELIVERANCE_KINGS_MUSTER(Signet.DELIVERANCE, "deliverance_kings_muster", "救世者的麋集",
                "终末之战状态结束时，对状态期间命中过的敌人追加相当于累计伤害50%的伤害，并重置玩家的战斗状态。",
                "终末之战状态结束时，对状态期间命中过的敌人追加相当于累计伤害57.5%的伤害，并重置玩家的战斗状态。",
                "终末之战状态结束时，对状态期间命中过的敌人追加相当于累计伤害65%的伤害，并重置玩家的战斗状态。",
                "终末之战状态结束时，对状态期间命中过的敌人追加相当于累计伤害75%的伤害，并重置玩家的战斗状态。"),
        DELIVERANCE_KINGS_EXPEDITION(Signet.DELIVERANCE, "deliverance_kings_expedition", "救世者的远征",
                "处于终末之战状态时，造成的全伤害提高20%。",
                "处于终末之战状态时，造成的全伤害提高23%。",
                "处于终末之战状态时，造成的全伤害提高26%。",
                "处于终末之战状态时，造成的全伤害提高30%。"),
        DELIVERANCE_KINGS_ECHO(Signet.DELIVERANCE, "deliverance_kings_echo", "救世者的余响",
                "终末之战状态和所有「救世」普通刻印提供的加成效果的持续时间延长至9秒。",
                "终末之战状态和所有「救世」普通刻印提供的加成效果的持续时间延长至10秒。",
                "终末之战状态和所有「救世」普通刻印提供的加成效果的持续时间延长至11秒。",
                "终末之战状态和所有「救世」普通刻印提供的加成效果的持续时间延长至12秒。"),

        DELIVERANCE_LONE_RESIDUAL_DREAM(Signet.DELIVERANCE, "deliverance_lone_residual_dream", "救世者的残梦",
                "处于救世之战状态时，所有「救世」普通刻印提供的加成效果提高60%。",
                "处于救世之战状态时，所有「救世」普通刻印提供的加成效果提高69%。",
                "处于救世之战状态时，所有「救世」普通刻印提供的加成效果提高78%。",
                "处于救世之战状态时，所有「救世」普通刻印提供的加成效果提高90%。"),
        DELIVERANCE_LONE_DECISION(Signet.DELIVERANCE, "deliverance_lone_decision", "救世者的决断",
                "处于救世之战状态时，每次攻击命中敌人后，额外提高2%全伤害，最多提高至50%。",
                "处于救世之战状态时，每次攻击命中敌人后，额外提高2.3%全伤害，最多提高至50%。",
                "处于救世之战状态时，每次攻击命中敌人后，额外提高2.6%全伤害，最多提高至50%。",
                "处于救世之战状态时，每次攻击命中敌人后，额外提高3%全伤害，最多提高至50%。"),
        DELIVERANCE_LONE_TRIUMPH(Signet.DELIVERANCE, "deliverance_lone_triumph", "救世者的凯旋",
                "进入救世之战状态后获得强化。首次攻击命中敌人后的4秒内，攻击无视敌人的防御；强化结束时，使附近所有非玩家单位受到的全伤害提高20%。",
                "进入救世之战状态后获得强化。首次攻击命中敌人后的5秒内，攻击无视敌人的防御；强化结束时，使附近所有非玩家单位受到的全伤害提高23%。",
                "进入救世之战状态后获得强化。首次攻击命中敌人后的6秒内，攻击无视敌人的防御；强化结束时，使附近所有非玩家单位受到的全伤害提高26%。",
                "进入救世之战状态后获得强化。首次攻击命中敌人后的7秒内，攻击无视敌人的防御；强化结束时，使附近所有非玩家单位受到的全伤害提高30%。");

        private final Signet signet;
        private final String id;
        private final String displayName;
        private final String[] descriptions;

        Skill(Signet signet, String id, String displayName, String... descriptions) {
            this.signet = signet;
            this.id = id;
            this.displayName = displayName;
            this.descriptions = descriptions;
        }

        public Signet signet() {
            return this.signet;
        }

        public String id() {
            return this.id;
        }

        public String displayName() {
            return this.displayName;
        }

        public String description(int level) {
            return this.descriptions[Mth.clamp(level, 0, MAX_LEVEL)];
        }

        public String tooltipDescription(int level) {
            String value = description(level);
            return switch (this) {
                case VICISSITUDE_STRENGTH, VICISSITUDE_RESISTANCE ->
                        "未受伤时" + value + "，受伤后层数清零";
                case DECIMATION_HEALTH -> value.replace("生命上限 +", "生命上限提高");
                case DECIMATION_LOW_HEALTH -> "生命越低受到的全伤害越低，"
                        + value.replace("每损失1生命减伤", "每损失1生命值，受到的伤害降低");
                case DECIMATION_FIRE -> value.split("，")[0].replace("火焰伤害 +", "灼烧伤害提高");
                case HELIX_MAGIC -> "使用武器技后，来源于武器技的全伤害提高"
                        + value.substring(value.indexOf('+') + 1) + "，最大叠加4层，效果持续10秒";
                case HELIX_PENDULUM -> "使用武器技后，使场上的敌人受到的全伤害提高"
                        + value.replace("武器技使敌人易伤", "") + "，持续4.5秒，重复触发时刷新时间。";
                case HELIX_PARADOX -> "附魔时获得高等级附魔（V–VIII）\n修复物品时，使用的材料和经验减少到1"
                        + (level > 0 ? "\n" + value : "");
                case GOLD_STREAM -> value.replace("正面效果 +", "正面效果时间延长")
                        .replace("，矿物与熔炉 +", "\n熔炉速度加快")
                        + "\n矿物获取提升" + value.substring(value.lastIndexOf('+') + 1);
                case GOLD_PARADISE -> "武器能量越多造成的伤害越高，"
                        + value.replace("每10能量伤害 +", "每有10能量提高");
                case DISCIPLINE_ONE -> "每秒增加5点「规诫」计数，最高100点，按「规诫」计数"
                        + value.replace("每点规诫使普攻伤害 +", "")
                        + "的比例提高普攻造成的伤害，触发极限闪避技能时扣除当前50%的「规诫」计数";
                case DISCIPLINE_TWO -> "按「规诫」计数"
                        + value.replace("每点规诫使伤害抵抗 +", "")
                        + "的比例提高角色的伤害抵抗，召唤物或协同者发动技能时扣除当前50%的「规诫」计数。";
                case DISCIPLINE_THREE -> "按「规诫」计数"
                        + value.replace("每点规诫使能量回复 +", "")
                        + "的比例提高每秒回复的额外能量，连击数到达15时扣除当前50%的「规诫」计数";
                case DELIVERANCE_ARMOR -> value.replace("自身的护甲值", "开启武器必杀技后，护甲值");
                case DELIVERANCE_KINGS_MUSTER -> value.replace("对状态期间命中过的敌人追加相当于累计伤害", "造成期间的")
                        .replace("并重置玩家的战斗状态", "并重置玩家的状态");
                case DELIVERANCE_LONE_TRIUMPH -> value.replace("附近所有非玩家单位", "当前场上所有除玩家以外、64格内的单位");
                default -> value;
            };
        }

        public boolean isDeliveranceSecondary() {
            return switch (this) {
                case DELIVERANCE_HUNTERS_MASK,
                        DELIVERANCE_RESTRAINERS_RELIC,
                        DELIVERANCE_SEEKERS_ROBE -> true;
                default -> false;
            };
        }

        public int deliveranceSecondaryBit() {
            return switch (this) {
                case DELIVERANCE_HUNTERS_MASK -> 1;
                case DELIVERANCE_RESTRAINERS_RELIC -> 1 << 1;
                case DELIVERANCE_SEEKERS_ROBE -> 1 << 2;
                default -> 0;
            };
        }

        public boolean isDeliveranceCoreSignet() {
            return deliveranceCore() != null;
        }

        public DeliveranceCore deliveranceCore() {
            return switch (this) {
                case DELIVERANCE_KINGS_MUSTER,
                        DELIVERANCE_KINGS_EXPEDITION,
                        DELIVERANCE_KINGS_ECHO -> DeliveranceCore.KINGS_SWORD;
                case DELIVERANCE_LONE_RESIDUAL_DREAM,
                        DELIVERANCE_LONE_DECISION,
                        DELIVERANCE_LONE_TRIUMPH -> DeliveranceCore.LONE_SHADOW;
                default -> null;
            };
        }

        public int deliveranceCoreSignetBit() {
            return switch (this) {
                case DELIVERANCE_KINGS_MUSTER -> 1;
                case DELIVERANCE_KINGS_EXPEDITION -> 1 << 1;
                case DELIVERANCE_KINGS_ECHO -> 1 << 2;
                case DELIVERANCE_LONE_RESIDUAL_DREAM -> 1 << 3;
                case DELIVERANCE_LONE_DECISION -> 1 << 4;
                case DELIVERANCE_LONE_TRIUMPH -> 1 << 5;
                default -> 0;
            };
        }

        public boolean isDeliveranceOrdinary() {
            return this.signet == Signet.DELIVERANCE && !isDeliveranceCoreSignet();
        }
    }

    private SignetUpgradeData() {
    }

    public static Signet fromStack(ItemStack stack) {
        if (stack.isEmpty()) return null;
        for (Signet signet : Signet.values()) {
            if (stack.is(signet.item())) return signet;
        }
        return null;
    }

    public static boolean isUpgradeableSignet(ItemStack stack) {
        return fromStack(stack) != null;
    }

    public static Item upgradeItemFor(Signet signet) {
        if (signet == null) return null;
        return switch (signet) {
            case REVERIE -> ModItems.REVERIE_SIGNET_UPGRADE.get();
            case VICISSITUDE -> ModItems.VICISSITUDE_SIGNET_UPGRADE.get();
            case STARS -> ModItems.STARS_SIGNET_UPGRADE.get();
            case INFINITY -> ModItems.INFINITY_SIGNET_UPGRADE.get();
            case DAYBREAK -> ModItems.DAYBREAK_SIGNET_UPGRADE.get();
            case SETSURA -> ModItems.SETSURA_SIGNET_UPGRADE.get();
            case BODHI -> ModItems.BODHI_SIGNET_UPGRADE.get();
            case DECIMATION -> ModItems.DECIMATION_SIGNET_UPGRADE.get();
            case HELIX -> ModItems.HELIX_SIGNET_UPGRADE.get();
            case GOLD -> ModItems.GOLD_SIGNET_UPGRADE.get();
            case DISCIPLINE -> ModItems.DISCIPLINE_SIGNET_UPGRADE.get();
            case DELIVERANCE -> ModItems.DELIVERANCE_SIGNET_UPGRADE.get();
        };
    }

    public static int countUpgradeItems(Player player, Signet signet) {
        Item upgradeItem = upgradeItemFor(signet);
        if (upgradeItem == null) return 0;
        int count = 0;
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.is(upgradeItem)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static boolean removeUpgradeItems(Player player, Signet signet, int amount) {
        if (amount <= 0 || countUpgradeItems(player, signet) < amount) return false;
        Item upgradeItem = upgradeItemFor(signet);
        Inventory inventory = player.getInventory();
        int remaining = amount;
        for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.is(upgradeItem)) continue;
            int removed = Math.min(remaining, stack.getCount());
            stack.shrink(removed);
            remaining -= removed;
        }
        inventory.setChanged();
        return remaining == 0;
    }

    public static List<Skill> skillsFor(Signet signet) {
        if (signet == null) return List.of();
        return Arrays.stream(Skill.values())
                .filter(skill -> skill.signet() == signet
                        && !skill.isDeliveranceSecondary()
                        && !skill.isDeliveranceCoreSignet())
                .toList();
    }

    private static List<Skill> allSkillsFor(Signet signet) {
        if (signet == null) return List.of();
        return Arrays.stream(Skill.values())
                .filter(skill -> skill.signet() == signet)
                .toList();
    }

    public static int getDeliveranceSecondaryUnlockMask(Player player) {
        if (player == null) return 0;
        return getPlayerPersistedData(player).getInt(NBT_DELIVERANCE_SECONDARY_UNLOCKS);
    }

    public static int getStackDeliveranceSecondaryUnlockMask(ItemStack stack) {
        if (stack.isEmpty() || fromStack(stack) != Signet.DELIVERANCE) return 0;
        CompoundTag tag = stack.getTag();
        return tag == null ? 0 : tag.getInt(NBT_DELIVERANCE_SECONDARY_UNLOCKS);
    }

    private static void setStackDeliveranceSecondaryUnlockMask(ItemStack stack, int mask) {
        if (stack.isEmpty() || fromStack(stack) != Signet.DELIVERANCE) return;
        stack.getOrCreateTag().putInt(NBT_DELIVERANCE_SECONDARY_UNLOCKS, mask);
    }

    public static boolean isSecondarySkillUnlocked(Player player, Skill skill) {
        if (skill == null || !skill.isDeliveranceSecondary()) return false;
        return (getDeliveranceSecondaryUnlockMask(player) & skill.deliveranceSecondaryBit()) != 0;
    }

    public static Skill secondarySkillFromItem(ItemStack stack) {
        if (stack.is(ModItems.DELIVERANCE_HUNTERS_MASK.get())) {
            return Skill.DELIVERANCE_HUNTERS_MASK;
        }
        if (stack.is(ModItems.DELIVERANCE_RESTRAINERS_RELIC.get())) {
            return Skill.DELIVERANCE_RESTRAINERS_RELIC;
        }
        if (stack.is(ModItems.DELIVERANCE_SEEKERS_ROBE.get())) {
            return Skill.DELIVERANCE_SEEKERS_ROBE;
        }
        return null;
    }

    public static boolean isDeliveranceSecondaryItem(ItemStack stack) {
        return secondarySkillFromItem(stack) != null;
    }

    public static DeliveranceCore deliveranceCoreFromItem(ItemStack stack) {
        if (stack.is(ModItems.DELIVERANCE_KINGS_SWORD.get())) {
            return DeliveranceCore.KINGS_SWORD;
        }
        if (stack.is(ModItems.DELIVERANCE_LONE_SHADOW.get())) {
            return DeliveranceCore.LONE_SHADOW;
        }
        return null;
    }

    public static Skill deliveranceCoreSignetFromItem(ItemStack stack) {
        if (stack.is(ModItems.DELIVERANCE_KINGS_MUSTER.get())) {
            return Skill.DELIVERANCE_KINGS_MUSTER;
        }
        if (stack.is(ModItems.DELIVERANCE_KINGS_EXPEDITION.get())) {
            return Skill.DELIVERANCE_KINGS_EXPEDITION;
        }
        if (stack.is(ModItems.DELIVERANCE_KINGS_ECHO.get())) {
            return Skill.DELIVERANCE_KINGS_ECHO;
        }
        if (stack.is(ModItems.DELIVERANCE_LONE_RESIDUAL_DREAM.get())) {
            return Skill.DELIVERANCE_LONE_RESIDUAL_DREAM;
        }
        if (stack.is(ModItems.DELIVERANCE_LONE_DECISION.get())) {
            return Skill.DELIVERANCE_LONE_DECISION;
        }
        if (stack.is(ModItems.DELIVERANCE_LONE_TRIUMPH.get())) {
            return Skill.DELIVERANCE_LONE_TRIUMPH;
        }
        return null;
    }

    public static boolean isDeliveranceFusionItem(ItemStack stack) {
        return secondarySkillFromItem(stack) != null
                || deliveranceCoreFromItem(stack) != null
                || deliveranceCoreSignetFromItem(stack) != null;
    }

    public static boolean fuseSecondarySkill(Player player, ItemStack deliveranceStack, Skill skill) {
        if (player == null
                || fromStack(deliveranceStack) != Signet.DELIVERANCE
                || skill == null
                || !skill.isDeliveranceSecondary()) {
            return false;
        }
        synchronizeLevels(player, deliveranceStack, Signet.DELIVERANCE);
        CompoundTag persisted = getPlayerPersistedData(player);
        int bit = skill.deliveranceSecondaryBit();
        int current = persisted.getInt(NBT_DELIVERANCE_SECONDARY_UNLOCKS);
        if ((current & bit) != 0) return false;
        int fusedMask = current | bit;
        persisted.putInt(NBT_DELIVERANCE_SECONDARY_UNLOCKS, fusedMask);
        setStackDeliveranceSecondaryUnlockMask(deliveranceStack, fusedMask);
        synchronizeLevels(player, deliveranceStack, Signet.DELIVERANCE);
        return true;
    }

    public static DeliveranceCore getDeliveranceCore(Player player) {
        if (player == null) return null;
        return DeliveranceCore.fromId(
                getPlayerPersistedData(player).getInt(NBT_DELIVERANCE_CORE)
        );
    }

    public static DeliveranceCore getStackDeliveranceCore(ItemStack stack) {
        if (stack.isEmpty() || fromStack(stack) != Signet.DELIVERANCE) return null;
        CompoundTag tag = stack.getTag();
        return tag == null ? null : DeliveranceCore.fromId(tag.getInt(NBT_DELIVERANCE_CORE));
    }

    private static void setStackDeliveranceCore(ItemStack stack, DeliveranceCore core) {
        if (stack.isEmpty() || fromStack(stack) != Signet.DELIVERANCE) return;
        if (core == null) {
            stack.getOrCreateTag().remove(NBT_DELIVERANCE_CORE);
        } else {
            stack.getOrCreateTag().putInt(NBT_DELIVERANCE_CORE, core.id());
        }
    }

    public static int getDeliveranceCoreSignetMask(Player player) {
        if (player == null) return 0;
        return getPlayerPersistedData(player).getInt(NBT_DELIVERANCE_CORE_SIGNETS);
    }

    public static int getStackDeliveranceCoreSignetMask(ItemStack stack) {
        if (stack.isEmpty() || fromStack(stack) != Signet.DELIVERANCE) return 0;
        CompoundTag tag = stack.getTag();
        return tag == null ? 0 : tag.getInt(NBT_DELIVERANCE_CORE_SIGNETS);
    }

    private static void setStackDeliveranceCoreSignetMask(ItemStack stack, int mask) {
        if (stack.isEmpty() || fromStack(stack) != Signet.DELIVERANCE) return;
        stack.getOrCreateTag().putInt(NBT_DELIVERANCE_CORE_SIGNETS, mask);
    }

    public static boolean hasDeliveranceCoreSignet(Player player, Skill skill) {
        if (skill == null || !skill.isDeliveranceCoreSignet()) return false;
        DeliveranceCore core = getDeliveranceCore(player);
        return core == skill.deliveranceCore()
                && (getDeliveranceCoreSignetMask(player)
                & skill.deliveranceCoreSignetBit()) != 0;
    }

    public static boolean fuseDeliveranceCore(
            Player player,
            ItemStack deliveranceStack,
            DeliveranceCore core
    ) {
        if (player == null
                || fromStack(deliveranceStack) != Signet.DELIVERANCE
                || core == null
                || getDeliveranceCore(player) != null) {
            return false;
        }
        DeliveranceCore selected = getSelectedDeliveranceCore(player);
        if (selected != null && selected != core) return false;
        CompoundTag persisted = getPlayerPersistedData(player);
        persisted.putInt(NBT_DELIVERANCE_CORE, core.id());
        setStackDeliveranceCore(deliveranceStack, core);
        return true;
    }

    public static boolean fuseDeliveranceCoreSignet(
            Player player,
            ItemStack deliveranceStack,
            Skill skill
    ) {
        if (player == null
                || fromStack(deliveranceStack) != Signet.DELIVERANCE
                || skill == null
                || !skill.isDeliveranceCoreSignet()
                || getDeliveranceCore(player) != skill.deliveranceCore()) {
            return false;
        }
        CompoundTag persisted = getPlayerPersistedData(player);
        int bit = skill.deliveranceCoreSignetBit();
        int current = persisted.getInt(NBT_DELIVERANCE_CORE_SIGNETS);
        if ((current & bit) != 0) return false;
        int fusedMask = current | bit;
        persisted.putInt(NBT_DELIVERANCE_CORE_SIGNETS, fusedMask);
        setStackDeliveranceCoreSignetMask(deliveranceStack, fusedMask);
        synchronizeLevels(player, deliveranceStack, Signet.DELIVERANCE);
        return true;
    }

    public static List<Skill> workshopSkills(Player player, Signet signet) {
        return workshopSkills(
                signet,
                getDeliveranceSecondaryUnlockMask(player),
                getDeliveranceCore(player),
                getDeliveranceCoreSignetMask(player)
        );
    }

    public static List<Skill> workshopSkills(Signet signet, int secondaryUnlockMask) {
        return workshopSkills(signet, secondaryUnlockMask, null, 0);
    }

    public static List<Skill> workshopSkills(
            Signet signet,
            int secondaryUnlockMask,
            DeliveranceCore core,
            int coreSignetMask
    ) {
        if (signet == null) return List.of();
        return allSkillsFor(signet).stream()
                .filter(skill -> {
                    if (skill.isDeliveranceSecondary()) {
                        return (secondaryUnlockMask & skill.deliveranceSecondaryBit()) != 0;
                    }
                    if (skill.isDeliveranceCoreSignet()) {
                        return core == skill.deliveranceCore()
                                && (coreSignetMask & skill.deliveranceCoreSignetBit()) != 0;
                    }
                    return true;
                })
                .toList();
    }

    public static Skill skillAt(Signet signet, int index) {
        List<Skill> skills = skillsFor(signet);
        return index >= 0 && index < skills.size() ? skills.get(index) : null;
    }

    public static Skill skillAt(Player player, Signet signet, int index) {
        List<Skill> skills = workshopSkills(player, signet);
        return index >= 0 && index < skills.size() ? skills.get(index) : null;
    }

    public static Skill skillAt(Signet signet, int secondaryUnlockMask, int index) {
        List<Skill> skills = workshopSkills(signet, secondaryUnlockMask);
        return index >= 0 && index < skills.size() ? skills.get(index) : null;
    }

    public static Skill skillAt(
            Signet signet,
            int secondaryUnlockMask,
            DeliveranceCore core,
            int coreSignetMask,
            int index
    ) {
        List<Skill> skills = workshopSkills(signet, secondaryUnlockMask, core, coreSignetMask);
        return index >= 0 && index < skills.size() ? skills.get(index) : null;
    }

    public static int getLevel(ItemStack stack, Skill skill) {
        if (stack.isEmpty() || skill == null || fromStack(stack) != skill.signet()) return 0;
        var tag = stack.getTag();
        if (tag == null) return 0;
        if (tag.contains(NBT_SKILL_LEVELS)) {
            var levels = tag.getCompound(NBT_SKILL_LEVELS);
            if (levels.contains(skill.id())) {
                return Mth.clamp(levels.getInt(skill.id()), 0, MAX_LEVEL);
            }
        }
        if (skill.isDeliveranceSecondary() || skill.isDeliveranceCoreSignet()) return 0;
        return Mth.clamp(tag.getInt(LEGACY_NBT_LEVEL), 0, MAX_LEVEL);
    }

    private static void setStackLevel(ItemStack stack, Skill skill, int level) {
        if (stack.isEmpty() || skill == null || fromStack(stack) != skill.signet()) return;
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag levels = tag.getCompound(NBT_SKILL_LEVELS);
        levels.putInt(skill.id(), Mth.clamp(level, 0, MAX_LEVEL));
        tag.put(NBT_SKILL_LEVELS, levels);
    }

    private static CompoundTag getPlayerPersistedData(Player player) {
        CompoundTag root = player.getPersistentData();
        if (!root.contains(NBT_PLAYER_PERSISTED)) {
            root.put(NBT_PLAYER_PERSISTED, new CompoundTag());
        }
        return root.getCompound(NBT_PLAYER_PERSISTED);
    }

    public static int getPlayerLevel(Player player, Skill skill) {
        if (player == null || skill == null) return 0;
        CompoundTag levels = getPlayerPersistedData(player).getCompound(NBT_PLAYER_SKILL_LEVELS);
        return Mth.clamp(levels.getInt(skill.id()), 0, MAX_LEVEL);
    }

    private static void setPlayerLevel(Player player, Skill skill, int level) {
        if (player == null || skill == null) return;
        CompoundTag persisted = getPlayerPersistedData(player);
        CompoundTag levels = persisted.getCompound(NBT_PLAYER_SKILL_LEVELS);
        levels.putInt(skill.id(), Mth.clamp(level, 0, MAX_LEVEL));
        persisted.put(NBT_PLAYER_SKILL_LEVELS, levels);
    }

    public static void synchronizeLevels(Player player, ItemStack stack, Signet signet) {
        if (player == null || stack.isEmpty() || signet == null || fromStack(stack) != signet) return;
        if (signet == Signet.DELIVERANCE) {
            CompoundTag persisted = getPlayerPersistedData(player);
            int unlockMask = persisted.getInt(NBT_DELIVERANCE_SECONDARY_UNLOCKS)
                    | getStackDeliveranceSecondaryUnlockMask(stack);
            persisted.putInt(NBT_DELIVERANCE_SECONDARY_UNLOCKS, unlockMask);
            setStackDeliveranceSecondaryUnlockMask(stack, unlockMask);

            DeliveranceCore playerCore = DeliveranceCore.fromId(
                    persisted.getInt(NBT_DELIVERANCE_CORE)
            );
            DeliveranceCore stackCore = getStackDeliveranceCore(stack);
            DeliveranceCore core = playerCore != null ? playerCore : stackCore;
            if (core != null) {
                persisted.putInt(NBT_DELIVERANCE_CORE, core.id());
                setStackDeliveranceCore(stack, core);
            }

            int coreSignetMask = persisted.getInt(NBT_DELIVERANCE_CORE_SIGNETS)
                    | getStackDeliveranceCoreSignetMask(stack);
            persisted.putInt(NBT_DELIVERANCE_CORE_SIGNETS, coreSignetMask);
            setStackDeliveranceCoreSignetMask(stack, coreSignetMask);
        }
        for (Skill skill : allSkillsFor(signet)) {
            int level = Math.max(getPlayerLevel(player, skill), getLevel(stack, skill));
            setPlayerLevel(player, skill, level);
            setStackLevel(stack, skill, level);
        }
    }

    public static void synchronizeOwnedLevels(Player player, Signet signet) {
        if (player == null || signet == null) return;
        CuriosApi.getCuriosHelper()
                .findFirstCurio(player, stack -> stack.is(signet.item()))
                .ifPresent(slot -> synchronizeLevels(player, slot.stack(), signet));
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.is(signet.item())) {
                synchronizeLevels(player, stack, signet);
            }
        }
    }

    public static int getEquippedLevel(Player player, Skill skill) {
        synchronizeOwnedLevels(player, skill.signet());
        return getPlayerLevel(player, skill);
    }

    public static boolean upgradeStack(Player player, ItemStack stack, Skill skill) {
        if (skill == null || fromStack(stack) != skill.signet()) return false;
        synchronizeLevels(player, stack, skill.signet());
        int current = getPlayerLevel(player, skill);
        if (current >= MAX_LEVEL) return false;
        setPlayerLevel(player, skill, current + 1);
        setStackLevel(stack, skill, current + 1);
        return true;
    }

    public static boolean isSignetMaxed(Player player, Signet signet) {
        if (player == null || signet == null) return false;
        synchronizeOwnedLevels(player, signet);
        List<Skill> skills = workshopSkills(player, signet);
        return !skills.isEmpty() && skills.stream()
                .allMatch(skill -> getPlayerLevel(player, skill) >= MAX_LEVEL);
    }

    public static DeliveranceCore getPurchasedDeliveranceCore(Player player) {
        if (player == null) return null;
        return DeliveranceCore.fromId(
                getPlayerPersistedData(player).getInt(NBT_DELIVERANCE_CORE_PURCHASED)
        );
    }

    public static DeliveranceCore getSelectedDeliveranceCore(Player player) {
        if (player == null) return null;
        DeliveranceCore purchased = getPurchasedDeliveranceCore(player);
        if (purchased != null) return purchased;
        DeliveranceCore fused = getDeliveranceCore(player);
        if (fused != null) return fused;
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            DeliveranceCore held = deliveranceCoreFromItem(inventory.getItem(i));
            if (held != null) return held;
        }
        return null;
    }

    public static boolean hasPurchasedDeliveranceCoreSignet(Player player, Skill skill) {
        if (player == null || skill == null || !skill.isDeliveranceCoreSignet()) return false;
        int mask = getPlayerPersistedData(player)
                .getInt(NBT_DELIVERANCE_CORE_SIGNETS_PURCHASED);
        return (mask & skill.deliveranceCoreSignetBit()) != 0;
    }

    public static void recordDeliveranceCorePurchase(
            Player player,
            DeliveranceCore core,
            int actualSilverPaid
    ) {
        if (player == null || core == null) return;
        CompoundTag persisted = getPlayerPersistedData(player);
        persisted.putInt(NBT_DELIVERANCE_CORE_PURCHASED, core.id());
        addDeliveranceCoreSilverSpent(player, actualSilverPaid);
    }

    public static void recordDeliveranceCoreSignetPurchase(
            Player player,
            Skill skill,
            int actualSilverPaid
    ) {
        if (player == null || skill == null || !skill.isDeliveranceCoreSignet()) return;
        CompoundTag persisted = getPlayerPersistedData(player);
        int mask = persisted.getInt(NBT_DELIVERANCE_CORE_SIGNETS_PURCHASED);
        persisted.putInt(
                NBT_DELIVERANCE_CORE_SIGNETS_PURCHASED,
                mask | skill.deliveranceCoreSignetBit()
        );
        addDeliveranceCoreSilverSpent(player, actualSilverPaid);
    }

    public static void setShopPaidSilverPerItem(ItemStack stack, int actualSilverPaid) {
        if (stack.isEmpty()) return;
        stack.getOrCreateTag().putInt(
                NBT_SHOP_PAID_SILVER_PER_ITEM,
                Math.max(0, actualSilverPaid)
        );
    }

    public static int removeUpgradeItemsWithTrackedCost(
            Player player,
            Signet signet,
            int amount
    ) {
        if (amount <= 0 || countUpgradeItems(player, signet) < amount) return -1;
        Item upgradeItem = upgradeItemFor(signet);
        Inventory inventory = player.getInventory();
        int remaining = amount;
        int silverCost = 0;
        for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.is(upgradeItem)) continue;
            int removed = Math.min(remaining, stack.getCount());
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                silverCost += Math.max(0, tag.getInt(NBT_SHOP_PAID_SILVER_PER_ITEM)) * removed;
            }
            stack.shrink(removed);
            remaining -= removed;
        }
        inventory.setChanged();
        return remaining == 0 ? silverCost : -1;
    }

    public static void recordDeliveranceCoreUpgrade(
            Player player,
            int actualSilverSpent,
            int experienceLevelsSpent
    ) {
        addDeliveranceCoreSilverSpent(player, actualSilverSpent);
        if (player == null || experienceLevelsSpent <= 0) return;
        CompoundTag persisted = getPlayerPersistedData(player);
        persisted.putInt(
                NBT_DELIVERANCE_CORE_EXPERIENCE_SPENT,
                Math.max(0, persisted.getInt(NBT_DELIVERANCE_CORE_EXPERIENCE_SPENT))
                        + experienceLevelsSpent
        );
    }

    private static void addDeliveranceCoreSilverSpent(Player player, int amount) {
        if (player == null || amount <= 0) return;
        CompoundTag persisted = getPlayerPersistedData(player);
        long total = (long) Math.max(0, persisted.getInt(NBT_DELIVERANCE_CORE_SILVER_SPENT))
                + amount;
        persisted.putInt(
                NBT_DELIVERANCE_CORE_SILVER_SPENT,
                (int) Math.min(Integer.MAX_VALUE, total)
        );
    }

    public static boolean hasDeliveranceCoreProgress(Player player) {
        if (player == null) return false;
        CompoundTag persisted = getPlayerPersistedData(player);
        return persisted.getInt(NBT_DELIVERANCE_CORE_PURCHASED) != 0
                || persisted.getInt(NBT_DELIVERANCE_CORE) != 0
                || persisted.getInt(NBT_DELIVERANCE_CORE_SIGNETS_PURCHASED) != 0
                || persisted.getInt(NBT_DELIVERANCE_CORE_SIGNETS) != 0
                || getSelectedDeliveranceCore(player) != null;
    }

    public record ResetRefund(int silver, int experienceLevels) {
    }

    public static ResetRefund resetDeliveranceCoreProgress(
            Player player,
            ItemStack workshopDeliveranceStack
    ) {
        if (player == null) return new ResetRefund(0, 0);
        CompoundTag persisted = getPlayerPersistedData(player);
        int silverRefund = Math.max(0, persisted.getInt(NBT_DELIVERANCE_CORE_SILVER_SPENT)) / 2;
        int experienceRefund = Math.max(
                0,
                persisted.getInt(NBT_DELIVERANCE_CORE_EXPERIENCE_SPENT)
        ) / 2;

        persisted.remove(NBT_DELIVERANCE_CORE);
        persisted.remove(NBT_DELIVERANCE_CORE_SIGNETS);
        persisted.remove(NBT_DELIVERANCE_CORE_PURCHASED);
        persisted.remove(NBT_DELIVERANCE_CORE_SIGNETS_PURCHASED);
        persisted.remove(NBT_DELIVERANCE_CORE_SILVER_SPENT);
        persisted.remove(NBT_DELIVERANCE_CORE_EXPERIENCE_SPENT);

        CompoundTag playerLevels = persisted.getCompound(NBT_PLAYER_SKILL_LEVELS);
        for (Skill skill : Skill.values()) {
            if (skill.isDeliveranceCoreSignet()) {
                playerLevels.remove(skill.id());
            }
        }
        persisted.put(NBT_PLAYER_SKILL_LEVELS, playerLevels);

        clearDeliveranceCoreStack(workshopDeliveranceStack);
        CuriosApi.getCuriosHelper()
                .findFirstCurio(player, stack -> stack.is(ModItems.SIGNET_OF_DELIVERANCE.get()))
                .ifPresent(slot -> clearDeliveranceCoreStack(slot.stack()));

        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.is(ModItems.SIGNET_OF_DELIVERANCE.get())) {
                clearDeliveranceCoreStack(stack);
            }
        }
        inventory.setChanged();

        addSilver(player, silverRefund);
        if (experienceRefund > 0) {
            player.giveExperienceLevels(experienceRefund);
        }
        return new ResetRefund(silverRefund, experienceRefund);
    }

    public static boolean isDeliveranceCoreSystemItem(ItemStack stack) {
        return deliveranceCoreFromItem(stack) != null
                || deliveranceCoreSignetFromItem(stack) != null;
    }

    private static void clearDeliveranceCoreStack(ItemStack stack) {
        if (stack.isEmpty() || fromStack(stack) != Signet.DELIVERANCE) return;
        CompoundTag tag = stack.getOrCreateTag();
        tag.remove(NBT_DELIVERANCE_CORE);
        tag.remove(NBT_DELIVERANCE_CORE_SIGNETS);
        CompoundTag levels = tag.getCompound(NBT_SKILL_LEVELS);
        for (Skill skill : Skill.values()) {
            if (skill.isDeliveranceCoreSignet()) {
                levels.remove(skill.id());
            }
        }
        tag.put(NBT_SKILL_LEVELS, levels);
    }

    public static float factorForLevel(int level) {
        return FACTORS[Mth.clamp(level, 0, MAX_LEVEL)];
    }

    public static float scaleBonus(Player player, Skill skill, float baseValue) {
        return baseValue * factorForLevel(getEquippedLevel(player, skill));
    }

    public static double scaleBonus(Player player, Skill skill, double baseValue) {
        return baseValue * factorForLevel(getEquippedLevel(player, skill));
    }

    public static int vicissitudeChargeTicks(Player player, Skill skill) {
        return switch (getEquippedLevel(player, skill)) {
            case 1 -> 180;
            case 2 -> 160;
            case 3 -> 140;
            default -> 200;
        };
    }

    public static float setsuraCooldownMultiplier(Player player) {
        return switch (getEquippedLevel(player, Skill.SETSURA_DODGE)) {
            case 1 -> 0.92F;
            case 2 -> 0.84F;
            case 3 -> 0.75F;
            default -> 1.0F;
        };
    }

    public static double infinityHealthLoss(Player player) {
        return switch (getEquippedLevel(player, Skill.INFINITY_REBIRTH)) {
            case 1 -> 0.18D;
            case 2 -> 0.16D;
            case 3 -> 0.14D;
            default -> 0.20D;
        };
    }

    public static int reverieShopPercent(Player player) {
        return switch (getEquippedLevel(player, Skill.REVERIE_DISCOUNT)) {
            case 1 -> 75;
            case 2 -> 72;
            case 3 -> 70;
            default -> 80;
        };
    }

    public static float helixRepairBonus(Player player) {
        return switch (getEquippedLevel(player, Skill.HELIX_PARADOX)) {
            case 1 -> 0.25F;
            case 2 -> 0.50F;
            case 3 -> 0.75F;
            default -> 0.0F;
        };
    }

    public static float egoMultiplier(Player player) {
        return EGO_MULTIPLIER;
    }

    public static int countSilver(Player player) {
        long count = 0L;
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.is(ModItems.SHINY_SILVER.get())) {
                count += stack.getCount();
            } else if (stack.is(ModItems.SILVER_POUCH.get())) {
                count += SilverPouchItem.getBalance(stack);
            }
            if (count >= Integer.MAX_VALUE) return Integer.MAX_VALUE;
        }
        return (int) count;
    }

    public static boolean removeSilver(Player player, int amount) {
        if (amount <= 0 || countSilver(player) < amount) return false;
        Inventory inventory = player.getInventory();
        int remaining = amount;

        for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.is(ModItems.SILVER_POUCH.get())) continue;
            long balance = SilverPouchItem.getBalance(stack);
            int removed = (int) Math.min((long) remaining, balance);
            if (removed <= 0) continue;
            SilverPouchItem.setBalance(stack, balance - removed);
            remaining -= removed;
        }

        for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.is(ModItems.SHINY_SILVER.get())) continue;
            int removed = Math.min(remaining, stack.getCount());
            stack.shrink(removed);
            remaining -= removed;
        }
        inventory.setChanged();
        return remaining == 0;
    }

    public static void addSilver(Player player, int amount) {
        if (player == null || amount <= 0) return;
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.is(ModItems.SILVER_POUCH.get())) {
                SilverPouchItem.setBalance(
                        stack,
                        SilverPouchItem.getBalance(stack) + amount
                );
                inventory.setChanged();
                return;
            }
        }

        int remaining = amount;
        int maxStackSize = ModItems.SHINY_SILVER.get().getMaxStackSize();
        while (remaining > 0) {
            int count = Math.min(maxStackSize, remaining);
            ItemStack refund = new ItemStack(ModItems.SHINY_SILVER.get(), count);
            if (!player.addItem(refund)) {
                player.drop(refund, false);
            }
            remaining -= count;
        }
        inventory.setChanged();
    }

    public static String levelSuffix(ItemStack stack, Skill skill) {
        int level = getLevel(stack, skill);
        return level == 0 ? "" : "  +" + level;
    }

    public static void addSkillTooltips(List<Component> tooltip, ItemStack stack, Signet signet,
                                        int nameColor, int effectColor) {
        for (Skill skill : skillsFor(signet)) {
            int level = getLevel(stack, skill);
            tooltip.add(Component.empty());
            tooltip.add(Component.literal(skill.displayName() + levelSuffix(stack, skill))
                    .withStyle(style -> style.withColor(nameColor)));
            for (String line : skill.tooltipDescription(level).split("\n")) {
                tooltip.add(Component.literal(line)
                        .withStyle(style -> style.withColor(effectColor).withItalic(true)));
            }
        }
    }

    public static void addUnlockedDeliveranceSecondaryTooltips(
            List<Component> tooltip,
            ItemStack stack,
            int nameColor,
            int effectColor
    ) {
        int unlockMask = getStackDeliveranceSecondaryUnlockMask(stack);
        for (Skill skill : allSkillsFor(Signet.DELIVERANCE)) {
            if (!skill.isDeliveranceSecondary()
                    || (unlockMask & skill.deliveranceSecondaryBit()) == 0) {
                continue;
            }
            int level = getLevel(stack, skill);
            tooltip.add(Component.empty());
            tooltip.add(Component.literal(skill.displayName() + levelSuffix(stack, skill))
                    .withStyle(style -> style.withColor(nameColor)));
            tooltip.add(Component.literal(skill.tooltipDescription(level))
                    .withStyle(style -> style.withColor(effectColor).withItalic(true)));
        }
    }

    public static void addDeliveranceCoreTooltips(
            List<Component> tooltip,
            ItemStack stack,
            int nameColor,
            int effectColor
    ) {
        DeliveranceCore core = getStackDeliveranceCore(stack);
        if (core == null) return;
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("核心 · " + core.displayName())
                .withStyle(style -> style.withColor(nameColor)));
        for (String line : core.description().split("\n")) {
            tooltip.add(Component.literal(line)
                    .withStyle(style -> style.withColor(effectColor).withItalic(true)));
        }

        int mask = getStackDeliveranceCoreSignetMask(stack);
        for (Skill skill : Skill.values()) {
            if (!skill.isDeliveranceCoreSignet()
                    || skill.deliveranceCore() != core
                    || (mask & skill.deliveranceCoreSignetBit()) == 0) {
                continue;
            }
            int level = getLevel(stack, skill);
            tooltip.add(Component.empty());
            tooltip.add(Component.literal(skill.displayName() + levelSuffix(stack, skill))
                    .withStyle(style -> style.withColor(nameColor)));
            tooltip.add(Component.literal(skill.tooltipDescription(level))
                    .withStyle(style -> style.withColor(effectColor).withItalic(true)));
        }
    }
}
