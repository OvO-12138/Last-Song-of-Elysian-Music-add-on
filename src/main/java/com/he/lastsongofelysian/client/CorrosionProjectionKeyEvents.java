package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.network.CorrosionProjectionTogglePacket;
import com.he.lastsongofelysian.network.ModNetwork;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public final class CorrosionProjectionKeyEvents {

    private CorrosionProjectionKeyEvents() {
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (event.getAction() != GLFW.GLFW_PRESS
                || event.getKey() != GLFW.GLFW_KEY_RIGHT_BRACKET) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }

        long window = minecraft.getWindow().getWindow();
        boolean shiftDown =
                InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT)
                        || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT);

        if (!shiftDown) {
            return;
        }

        ModNetwork.CHANNEL.sendToServer(
                new CorrosionProjectionTogglePacket()
        );
    }
}
