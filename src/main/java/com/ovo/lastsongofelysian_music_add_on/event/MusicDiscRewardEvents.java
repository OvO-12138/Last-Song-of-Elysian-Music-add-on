package com.ovo.lastsongofelysian_music_add_on.event;

import com.ovo.lastsongofelysian_music_add_on.LastSongOfElysianMusicAddOn;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = LastSongOfElysianMusicAddOn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MusicDiscRewardEvents {
    private static final String REWARD_DATA = "LastSongOfElysianMusicRewards";
    private static final String COCOON_CURSES_REVERSED = "CocoonCursesReversed";
    private static final String DA_CAPO_REWARD = "da_capo_disc";
    private static final List<Reward> REWARDS = List.of(
            new Reward("core_of_reason", "cyberangel_disc"),
            new Reward("core_of_void", "befall_disc"),
            new Reward("core_of_thunder", "honkai_world_diva_disc"),
            new Reward("core_of_death", "dual_ego_disc"),
            new Reward("core_of_fire", "nightglow_disc"),
            new Reward("core_of_sentience", "rubia_disc"),
            new Reward("core_of_dominance", "moon_halo_disc"),
            new Reward("core_of_binding", "regression_disc"),
            new Reward("core_of_origin", "true_disc"),
            new Reward("hometown", "millennium_feather_disc")
    );

    private MusicDiscRewardEvents() {
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)
                || player.tickCount % 20 != 0) return;

        CompoundTag persisted = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        CompoundTag rewards = persisted.getCompound(REWARD_DATA);
        boolean changed = false;

        for (Reward reward : REWARDS) {
            if (rewards.getBoolean(reward.rewardId()) || !hasItem(player, reward.trigger())) continue;
            Item disc = ForgeRegistries.ITEMS.getValue(reward.reward());
            if (disc == null) continue;

            ItemStack stack = new ItemStack(disc);
            if (!player.getInventory().add(stack)) player.drop(stack, false);
            rewards.putBoolean(reward.rewardId(), true);
            changed = true;
        }

        if (!rewards.getBoolean(DA_CAPO_REWARD) && persisted.getBoolean(COCOON_CURSES_REVERSED)) {
            Item disc = ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation(LastSongOfElysianMusicAddOn.MOD_ID, DA_CAPO_REWARD)
            );
            if (disc != null) {
                ItemStack stack = new ItemStack(disc);
                if (!player.getInventory().add(stack)) player.drop(stack, false);
                rewards.putBoolean(DA_CAPO_REWARD, true);
                changed = true;
            }
        }

        if (changed) {
            persisted.put(REWARD_DATA, rewards);
            player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persisted);
            player.getInventory().setChanged();
        }
    }

    private static boolean hasItem(ServerPlayer player, ResourceLocation itemId) {
        Item item = ForgeRegistries.ITEMS.getValue(itemId);
        if (item == null) return false;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (player.getInventory().getItem(slot).is(item)) return true;
        }
        return false;
    }

    private record Reward(ResourceLocation trigger, ResourceLocation reward, String rewardId) {
        private Reward(String trigger, String reward) {
            this(new ResourceLocation("lastsongofelysian", trigger),
                    new ResourceLocation(LastSongOfElysianMusicAddOn.MOD_ID, reward), reward);
        }
    }
}
