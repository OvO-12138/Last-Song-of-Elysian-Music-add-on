package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class CorrosionCurseClientEvents {

    private static final String[] HIDDEN_OVERLAYS = {
            "player_health",
            "armor_level",
            "food_level",
            "experience_bar",
            "potion_icons"
    };

    private CorrosionCurseClientEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onOverlayPre(RenderGuiOverlayEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null
                || !ClientCorrosionCurseData.isAcquired()
                || ClientCorrosionCurseData.isReversed()) {
            return;
        }

        float ratio = getCorruptionRatio();

        int cycle = Math.floorMod(minecraft.player.tickCount, 200);
        int hiddenDuration = 30 + Math.round(ratio * 70.0F);
        if (cycle >= hiddenDuration) return;

        ResourceLocation overlayId = event.getOverlay().id();
        String path = overlayId.getPath();
        int first = Math.floorMod(minecraft.player.tickCount / 200, HIDDEN_OVERLAYS.length);

        if (path.contains(HIDDEN_OVERLAYS[first])) {
            event.setCanceled(true);
            return;
        }

        if (ratio >= 0.75F) {
            int second = (first + 2) % HIDDEN_OVERLAYS.length;
            if (path.contains(HIDDEN_OVERLAYS[second])) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onOverlayPost(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (!event.getOverlay().id().getPath().contains("hotbar")) return;

        if (minecraft.player == null
                || !ClientCorrosionCurseData.isAcquired()
                || ClientCorrosionCurseData.isReversed()) {
            return;
        }

        float ratio = getCorruptionRatio();
        float visualStrength = 0.22F + ratio * 0.78F;
        float pulse = 0.85F + 0.15F * (float) Math.sin(minecraft.player.tickCount * 0.18F);

        GuiGraphics graphics = event.getGuiGraphics();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        int border = Math.max(6, Math.round((7.0F + visualStrength * 24.0F) * pulse));
        int alpha = Math.min(165, Math.round(42.0F + visualStrength * 105.0F));
        int edgeColor = (alpha << 24) | 0x6A165F;

        graphics.fill(0, 0, width, border, edgeColor);
        graphics.fill(0, height - border, width, height, edgeColor);
        graphics.fill(0, border, border, height - border, edgeColor);
        graphics.fill(width - border, border, width, height - border, edgeColor);

        Random random = new Random(
                minecraft.player.tickCount * 341873128712L
                        + CorruptionHudData.corruptionEnergy * 31L
        );
        int lines = Math.max(4, Math.round(4.0F + visualStrength * 18.0F));

        for (int i = 0; i < lines; i++) {
            int y = random.nextInt(Math.max(1, height));
            int x = random.nextInt(Math.max(1, width));
            int length = 6 + random.nextInt(Math.max(7, width / 5));
            int noiseAlpha = 20 + random.nextInt(Math.max(1, Math.min(80, alpha)));
            int color = (noiseAlpha << 24) | (random.nextBoolean() ? 0xB02D9B : 0x3A062F);
            graphics.fill(x, y, Math.min(width, x + length), y + 1, color);
        }
    }

    private static float getCorruptionRatio() {
        return Math.max(
                0.0F,
                Math.min(1.0F, CorruptionHudData.corruptionEnergy / 10000.0F)
        );
    }
}
