package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.dimension.ModDimensions;
import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class ElysianRealmClientRestrictions {

    private ElysianRealmClientRestrictions() {
    }

    private static boolean isInElysianRealm() {
        Minecraft minecraft =
                Minecraft.getInstance();

        return minecraft.player != null
                && minecraft.player.level()
                .dimension()
                .equals(
                        ModDimensions
                                .ELYSIAN_REALM
                );
    }

    private static boolean containsMapKeyword(
            String value
    ) {
        String lower =
                value.toLowerCase(
                        Locale.ROOT
                );

        return lower.contains("map")
                || lower.contains("worldmap")
                || lower.contains("minimap")
                || lower.contains("waypoint")
                || lower.contains("atlas")
                || lower.contains("cartography")
                || lower.contains("xaero")
                || lower.contains("journey")
                || lower.contains("voxel")
                || lower.contains("ftbchunks")
                || lower.contains("mapfrontiers")
                || lower.contains("antiqueatlas")
                || lower.contains("mapatlases")
                || lower.contains("地图")
                || lower.contains("地圖")
                || lower.contains("路径点");
    }

    private static boolean isMapLike(
            ItemStack stack
    ) {
        if (stack.isEmpty()) {
            return false;
        }

        if (
                stack.is(Items.MAP) ||
                stack.is(Items.FILLED_MAP)
        ) {
            return true;
        }

        ResourceLocation id =
                ForgeRegistries.ITEMS.getKey(
                        stack.getItem()
                );

        String registryName =
                id == null
                        ? ""
                        : id.toString();

        String className =
                stack.getItem()
                        .getClass()
                        .getName();

        return containsMapKeyword(
                registryName
        ) || containsMapKeyword(
                className
        );
    }

    private static boolean isMapScreen(
            Screen screen
    ) {
        return containsMapKeyword(
                screen.getClass()
                        .getName()
        ) || containsMapKeyword(
                screen.getTitle()
                        .getString()
        );
    }

    private static boolean isMapKey(
            KeyMapping keyMapping
    ) {
        return containsMapKeyword(
                keyMapping.getName()
        ) || containsMapKeyword(
                keyMapping.getCategory()
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onScreenOpening(
            ScreenEvent.Opening event
    ) {
        if (!isInElysianRealm()) {
            return;
        }

        Screen screen =
                event.getNewScreen();

        if (
                screen != null &&
                isMapScreen(screen)
        ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onClientTick(
            TickEvent.ClientTickEvent event
    ) {
        if (
                event.phase != TickEvent.Phase.END ||
                !isInElysianRealm()
        ) {
            return;
        }

        Minecraft minecraft =
                Minecraft.getInstance();

        for (
                KeyMapping keyMapping :
                minecraft.options.keyMappings
        ) {
            if (!isMapKey(keyMapping)) {
                continue;
            }

            while (keyMapping.consumeClick()) {

            }
        }

        if (
                minecraft.screen != null &&
                isMapScreen(
                        minecraft.screen
                )
        ) {
            minecraft.setScreen(null);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderHand(
            RenderHandEvent event
    ) {
        if (
                isInElysianRealm() &&
                isMapLike(
                        event.getItemStack()
                )
        ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderOverlay(
            RenderGuiOverlayEvent.Pre event
    ) {
        if (
                isInElysianRealm() &&
                containsMapKeyword(
                        event.getOverlay()
                                .id()
                                .toString()
                )
        ) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onSystemMessage(
            ClientChatReceivedEvent.System event
    ) {
        if (!isInElysianRealm()) {
            return;
        }

        String message =
                event.getMessage()
                        .getString();

        boolean oldRestrictionMessage =
                message.contains("往世乐土")
                        && (
                        message.contains("无法")
                                || message.contains("不能")
                                || message.contains("不可")
                );

        if (oldRestrictionMessage) {
            event.setCanceled(true);
        }
    }
}
