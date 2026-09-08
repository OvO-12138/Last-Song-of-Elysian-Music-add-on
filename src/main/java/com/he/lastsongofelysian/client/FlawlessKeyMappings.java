package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.network.FlawlessWeaponActionPacket;
import com.he.lastsongofelysian.network.ModNetwork;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

public final class FlawlessKeyMappings {

    private static final String CATEGORY =
            "key.categories.lastsongofelysian";

    public static final KeyMapping SWITCH_MODE =
            new KeyMapping(
                    "key.lastsongofelysian.flawless_switch_mode",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_C,
                    CATEGORY
            );

    public static final KeyMapping WEAPON_SKILL =
            new KeyMapping(
                    "key.lastsongofelysian.flawless_weapon_skill",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_R,
                    CATEGORY
            );

    public static final KeyMapping ULTIMATE =
            new KeyMapping(
                    "key.lastsongofelysian.flawless_ultimate",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_V,
                    CATEGORY
            );

    private FlawlessKeyMappings() {
    }

    @Mod.EventBusSubscriber(
            modid = lastsongofelysian.MODID,
            value = Dist.CLIENT,
            bus = Mod.EventBusSubscriber.Bus.MOD
    )
    public static final class ModEvents {

        private ModEvents() {
        }

        @SubscribeEvent
        public static void registerKeys(
                RegisterKeyMappingsEvent event
        ) {
            event.register(SWITCH_MODE);
            event.register(WEAPON_SKILL);
            event.register(ULTIMATE);
        }
    }

    @Mod.EventBusSubscriber(
            modid = lastsongofelysian.MODID,
            value = Dist.CLIENT,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static final class ForgeEvents {

        private ForgeEvents() {
        }

        @SubscribeEvent
        public static void onClientTick(
                TickEvent.ClientTickEvent event
        ) {
            if (
                    event.phase != TickEvent.Phase.END ||
                    Minecraft.getInstance().player == null
            ) {
                return;
            }

            while (SWITCH_MODE.consumeClick()) {
                send(
                        FlawlessWeaponActionPacket
                                .Action
                                .SWITCH_MODE
                );
            }

            while (WEAPON_SKILL.consumeClick()) {
                send(
                        FlawlessWeaponActionPacket
                                .Action
                                .WEAPON_SKILL
                );
            }

            while (ULTIMATE.consumeClick()) {
                send(
                        FlawlessWeaponActionPacket
                                .Action
                                .ULTIMATE
                );
            }
        }

        private static void send(
                FlawlessWeaponActionPacket.Action action
        ) {
            ModNetwork.CHANNEL.sendToServer(
                    new FlawlessWeaponActionPacket(
                            action
                    )
            );
        }
    }
}
