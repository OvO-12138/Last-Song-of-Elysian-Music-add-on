package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = lastsongofelysian.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ShiJiProtectionEvents {

    private static final String NBT_GOT_SHI_JI = "GotShiJi";
    private static final int CHECK_INTERVAL = 100;

    private static boolean isShiJi(ItemStack stack) {
        return stack.is(ModItems.SHI_JI.get());
    }

    private static boolean hasShiJi(ServerPlayer player) {
        Inventory inv = player.getInventory();

        for (ItemStack stack : inv.items) {
            if (isShiJi(stack)) return true;
        }

        for (ItemStack stack : inv.armor) {
            if (isShiJi(stack)) return true;
        }

        for (ItemStack stack : inv.offhand) {
            if (isShiJi(stack)) return true;
        }

        return false;
    }

    private static void giveShiJi(ServerPlayer player) {
        ItemStack stack = new ItemStack(ModItems.SHI_JI.get());

        if (!player.addItem(stack)) {
            player.drop(stack, false);
        }

        player.displayClientMessage(
                Component.literal("岁月史书不可遗失。新的史书已被补回。"),
                true
        );
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != LogicalSide.SERVER) return;
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer player)) return;

        if (player.tickCount % CHECK_INTERVAL != 0) return;

        boolean gotShiJi = player.getPersistentData().getBoolean(NBT_GOT_SHI_JI);

        if (!gotShiJi) {
            player.getPersistentData().putBoolean(NBT_GOT_SHI_JI, true);
            if (!hasShiJi(player)) {
                giveShiJi(player);
            }
            return;
        }

        if (!hasShiJi(player)) {
            giveShiJi(player);
        }
    }

    @SubscribeEvent
    public static void onItemExpire(ItemExpireEvent event) {
        ItemEntity entity = event.getEntity();

        if (isShiJi(entity.getItem())) {
            event.setCanceled(true);
            entity.setInvulnerable(true);
            entity.clearFire();
            entity.setUnlimitedLifetime();
        }
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        ItemEntity entity = event.getEntity();

        if (isShiJi(entity.getItem())) {
            entity.setInvulnerable(true);
            entity.clearFire();
            entity.setUnlimitedLifetime();
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        List<?> affectedEntities = event.getAffectedEntities();

        affectedEntities.removeIf(entity -> {
            if (entity instanceof ItemEntity itemEntity) {
                return isShiJi(itemEntity.getItem());
            }
            return false;
        });
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        boolean gotShiJi = event.getOriginal().getPersistentData().getBoolean(NBT_GOT_SHI_JI);
        if (gotShiJi) {
            event.getEntity().getPersistentData().putBoolean(NBT_GOT_SHI_JI, true);
        }
    }
}
