package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.Random;

@Mod.EventBusSubscriber(modid = lastsongofelysian.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HonkaiEnergyEvents {

    private static final Random RAND = new Random();

    private static final String NBT_PLAYER_PERSISTED = "PlayerPersisted";
    private static final String NBT_GOT_STARTER_MEDIUM_INHIBITOR = "GotStarterMediumHonkaiEnergyInhibitor";

    private static final String NBT_EASTER_EGG_INHIBITOR_COOLDOWN = "EasterEggHighInhibitorCooldown";
    private static final long EASTER_EGG_COOLDOWN_MS = 4L * 60L * 60L * 1000L;

    private static final String NBT_NOTHING_COOLDOWN = "EasterEggNothingCooldown";
    private static final long NOTHING_COOLDOWN_MS = 3L * 60L * 60L * 1000L;

    private static final String NBT_GOT_DELIVERANCE_EASTER_EGG = "GotDeliveranceEasterEgg";
    private static final String NBT_GOT_GOLD_EASTER_EGG = "GotGoldEasterEgg";

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.level().isClientSide()) return;
        if (entity instanceof Player) return;

        int amount = 0;

        if (entity instanceof Enemy) {
            amount = 1 + RAND.nextInt(3);
        }

        else if (entity instanceof AgeableMob) {
            amount = RAND.nextInt(2);
        }

        if (amount <= 0) return;

        ItemStack stack = new ItemStack(ModItems.BIOLOGICAL_RESIDUE.get(), amount);
        ItemEntity drop = new ItemEntity(
                entity.level(),
                entity.getX(),
                entity.getY() + 0.5,
                entity.getZ(),
                stack
        );

        drop.setDefaultPickUpDelay();
        event.getDrops().add(drop);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        CompoundTag data = getPermanentData(player);

        if (data.getBoolean(NBT_GOT_STARTER_MEDIUM_INHIBITOR)) return;

        data.putBoolean(NBT_GOT_STARTER_MEDIUM_INHIBITOR, true);

        ItemStack stack = new ItemStack(ModItems.HONKAI_ENERGY_INHIBITOR.get());

        if (!player.addItem(stack)) {
            player.drop(stack, false);
        }

        player.displayClientMessage(
                Component.literal("你获得了一个中等崩坏能抑制剂。")
                        .withStyle(ChatFormatting.AQUA),
                true
        );
    }

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String message = event.getRawText().trim();

        if ("我是区".equals(message)) {
            event.setCanceled(true);

            CompoundTag data = getPermanentData(player);

            long now = System.currentTimeMillis();
            long lastUse = data.getLong(NBT_EASTER_EGG_INHIBITOR_COOLDOWN);
            long remaining = EASTER_EGG_COOLDOWN_MS - (now - lastUse);

            if (remaining > 0) {
                long remainingMinutes = Math.max(1, remaining / 1000L / 60L);

                player.displayClientMessage(
                        Component.literal("彩蛋冷却中，还需要约 " + remainingMinutes + " 分钟。")
                                .withStyle(ChatFormatting.GRAY),
                        true
                );
                return;
            }

            data.putLong(NBT_EASTER_EGG_INHIBITOR_COOLDOWN, now);

            ItemStack stack = new ItemStack(ModItems.HIGH_HONKAI_ENERGY_INHIBITOR.get());

            if (!player.addItem(stack)) {
                player.drop(stack, false);
            }

            player.level().playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.PLAYER_LEVELUP,
                    SoundSource.PLAYERS,
                    0.8F,
                    1.4F
            );

            player.displayClientMessage(
                    Component.literal("彩蛋触发：你获得了一个高等崩坏能抑制剂。")
                            .withStyle(ChatFormatting.LIGHT_PURPLE),
                    true
            );

            return;
        }

        if ("我什么都做不到".equals(message)) {
            event.setCanceled(true);

            CompoundTag data = getPermanentData(player);

            long now = System.currentTimeMillis();
            long lastUse = data.getLong(NBT_NOTHING_COOLDOWN);
            long remaining = NOTHING_COOLDOWN_MS - (now - lastUse);

            if (remaining > 0) {
                long remainingMinutes = Math.max(1, remaining / 1000L / 60L);

                player.displayClientMessage(
                        Component.literal("彩蛋冷却中，还需要约 " + remainingMinutes + " 分钟。")
                                .withStyle(ChatFormatting.GRAY),
                        true
                );
                return;
            }

            data.putLong(NBT_NOTHING_COOLDOWN, now);

            int duration = 20 * 60;

            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_BOOST,
                    duration,
                    4,
                    false,
                    true,
                    true
            ));

            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE,
                    duration,
                    4,
                    false,
                    true,
                    true
            ));

            player.addEffect(new MobEffectInstance(
                    MobEffects.NIGHT_VISION,
                    duration,
                    0,
                    false,
                    true,
                    true
            ));

            player.level().playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.BEACON_ACTIVATE,
                    SoundSource.PLAYERS,
                    0.8F,
                    1.2F
            );

            player.displayClientMessage(
                    Component.literal("彩蛋触发：即使如此，你仍然向前。")
                            .withStyle(ChatFormatting.AQUA),
                    true
            );

            return;
        }

        if ("cool～".equals(message) || "cool~".equals(message)) {
            event.setCanceled(true);

            CompoundTag data = getPermanentData(player);

            if (data.getBoolean(NBT_GOT_DELIVERANCE_EASTER_EGG)) {
                player.displayClientMessage(
                        Component.literal("这个彩蛋你已经触发过了。")
                                .withStyle(ChatFormatting.GRAY),
                        true
                );
                return;
            }

            data.putBoolean(NBT_GOT_DELIVERANCE_EASTER_EGG, true);

            ItemStack stack = new ItemStack(ModItems.SIGNET_OF_DELIVERANCE.get());

            if (!player.addItem(stack)) {
                player.drop(stack, false);
            }

            player.level().playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.PLAYER_LEVELUP,
                    SoundSource.PLAYERS,
                    0.8F,
                    1.0F
            );

            player.displayClientMessage(
                    Component.literal("彩蛋触发：你获得了救世刻印。")
                            .withStyle(ChatFormatting.GOLD),
                    true
            );

            return;
        }

        if ("愿时光永驻此刻".equals(message)) {
            event.setCanceled(true);

            CompoundTag data = getPermanentData(player);

            if (data.getBoolean(NBT_GOT_GOLD_EASTER_EGG)) {
                player.displayClientMessage(
                        Component.literal("这个彩蛋你已经触发过了。")
                                .withStyle(ChatFormatting.GRAY),
                        true
                );
                return;
            }

            data.putBoolean(NBT_GOT_GOLD_EASTER_EGG, true);

            ItemStack stack = new ItemStack(ModItems.SIGNET_OF_GOLD.get());

            if (!player.addItem(stack)) {
                player.drop(stack, false);
            }

            player.level().playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.PLAYER_LEVELUP,
                    SoundSource.PLAYERS,
                    0.8F,
                    1.0F
            );

            player.displayClientMessage(
                    Component.literal("彩蛋触发：愿明日如黄金般辉煌")
                            .withStyle(ChatFormatting.GOLD),
                    true
            );

            return;
        }
    }
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        CompoundTag oldData = getPermanentData(event.getOriginal());
        CompoundTag newData = getPermanentData(event.getEntity());

        if (oldData.getBoolean(NBT_GOT_STARTER_MEDIUM_INHIBITOR)) {
            newData.putBoolean(NBT_GOT_STARTER_MEDIUM_INHIBITOR, true);
        }

        if (oldData.contains(NBT_EASTER_EGG_INHIBITOR_COOLDOWN)) {
            newData.putLong(
                    NBT_EASTER_EGG_INHIBITOR_COOLDOWN,
                    oldData.getLong(NBT_EASTER_EGG_INHIBITOR_COOLDOWN)
            );
        }
    }

    private static CompoundTag getPermanentData(Player player) {
        CompoundTag root = player.getPersistentData();

        if (!root.contains(NBT_PLAYER_PERSISTED)) {
            root.put(NBT_PLAYER_PERSISTED, new CompoundTag());
        }

        return root.getCompound(NBT_PLAYER_PERSISTED);
    }
}
