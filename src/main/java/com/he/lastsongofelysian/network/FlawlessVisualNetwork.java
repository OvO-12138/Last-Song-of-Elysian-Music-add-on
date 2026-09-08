package com.he.lastsongofelysian.network;

import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public final class FlawlessVisualNetwork {

    private static final String VERSION = "1";

    public static final SimpleChannel CHANNEL =
            NetworkRegistry.newSimpleChannel(
                    new ResourceLocation(
                            lastsongofelysian.MODID,
                            "flawless_visual"
                    ),
                    () -> VERSION,
                    VERSION::equals,
                    VERSION::equals
            );

    private static boolean registered;

    private FlawlessVisualNetwork() {
    }

    @SubscribeEvent
    public static void onCommonSetup(
            FMLCommonSetupEvent event
    ) {
        event.enqueueWork(
                FlawlessVisualNetwork::register
        );
    }

    private static synchronized void register() {
        if (registered) {
            return;
        }

        CHANNEL.registerMessage(
                0,
                FlawlessVisualPacket.class,
                FlawlessVisualPacket::encode,
                FlawlessVisualPacket::decode,
                FlawlessVisualPacket::handle
        );

        CHANNEL.registerMessage(
                1,
                BloomStackSyncPacket.class,
                BloomStackSyncPacket::encode,
                BloomStackSyncPacket::decode,
                BloomStackSyncPacket::handle
        );

        registered = true;
    }
}
