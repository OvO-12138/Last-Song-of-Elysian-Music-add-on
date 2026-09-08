package com.he.lastsongofelysian.event;

import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.menu.SpiralWorkshopMenu;
import com.he.lastsongofelysian.registry.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class VillVWorkshopInteractionEvents {

    private VillVWorkshopInteractionEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        handleInteraction(event, event.getTarget());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        handleInteraction(event, event.getTarget());
    }

    private static void handleInteraction(PlayerInteractEvent event, Entity target) {
        if (target.getType() != ModEntities.VILL_V_NPC.get()) return;

        Player player = event.getEntity();
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(player.level().isClientSide()));

        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(
                    serverPlayer,
                    new SimpleMenuProvider(
                            (containerId, inventory, menuPlayer) ->
                                    new SpiralWorkshopMenu(containerId, inventory),
                            Component.literal("螺旋工坊")
                    )
            );
        }
    }
}
