package com.he.lastsongofelysian.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CoreOfOriginItem extends Item {

    public CoreOfOriginItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(SignetOfEgoItem.PINK);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {

        tooltip.add(Component.literal("…以我为始")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("始源核心，")
                .withStyle(SignetOfEgoItem.PINK));
        tooltip.add(Component.literal("是爱莉希雅留给世界的礼物，")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        tooltip.add(Component.literal("也是人类拒绝既定命运的第一声回答。")
                .withStyle(SignetOfEgoItem.PINK));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「嗨，感觉如何♪」")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        tooltip.add(Component.literal("无论何时何地，爱莉希雅都会回应你的期待")
                .withStyle(SignetOfEgoItem.PINK));
        tooltip.add(Component.literal("至此，我们的故事结束了")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        tooltip.add(Component.literal("接下来")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        tooltip.add(Component.literal("就是你的路了")
                .withStyle(SignetOfEgoItem.PINK));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「今后，群星将为你闪耀，因为你曾来过」")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        tooltip.add(Component.literal("「今后，百花将为你绽放，因为你从未离去」")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「我梦见一颗星，一座光明之岛，我将在它轻快的闲暇深处诞生」")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        tooltip.add(Component.literal("「在那里，我的生命将完成它的事业，就像秋日下的稻田」")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("这个世界如此美丽，它充满活力和生机，爱意与希望，让人想要去守护")
                .withStyle(SignetOfEgoItem.PINK));
        tooltip.add(Component.literal("这个世界又如此残酷，它催生矛盾，孵化罪恶…让一切美好，都不得不永久的战斗下去")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("但—这就是自由，肩负重任却能随心所欲，普度众生而依旧天真灿烂")
                .withStyle(SignetOfEgoItem.PINK));
        tooltip.add(Component.literal("我们是律者…在善恶之前，就已然是自然的代言，是信条的化身")
                .withStyle(SignetOfEgoItem.PINK));
        tooltip.add(Component.literal("正因为如此，我们能够成为天地之间的桥梁，人世与彼岸的连接…")
                .withStyle(SignetOfEgoItem.PINK));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("没有天使，就创造天使")
                .withStyle(SignetOfEgoItem.PINK));
        tooltip.add(Component.literal("没有「乐园」，便创造「乐园」")
                .withStyle(SignetOfEgoItem.PINK));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("世界上不会再有第二个爱莉希雅")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        tooltip.add(Component.literal("但没有关系，因为…每个人都将是「爱莉希雅」")
                .withStyle(SignetOfEgoItem.PINK));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「我们想要守护这个世界，因为曾有人为她所爱的事物付出一切」")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        tooltip.add(Component.literal("「我们想要守护这个世界，因为我们与重要之人于此相遇，在此欢笑，为此战斗」")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        tooltip.add(Component.literal("「我们想要守护这个世界，因为这是我们的存在之地，无可替代的唯一归处」")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));

        tooltip.add(Component.empty());

        tooltip.add(Component.literal("「如飞花般绚丽的少女。」")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));
        tooltip.add(Component.literal("「愿你前行的道路，永远有花盛开。」")
                .withStyle(SignetOfEgoItem.LIGHT_PINK_I));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
