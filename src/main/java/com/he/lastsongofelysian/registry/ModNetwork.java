package com.he.lastsongofelysian.registry;

import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.network.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(lastsongofelysian.MODID, "main"),
            () -> VERSION, VERSION::equals, VERSION::equals);

    private static int id = 0;

    public static void register() {
        CHANNEL.registerMessage(id++,
                SilverPouchActionPacket.class,
                SilverPouchActionPacket::encode,
                SilverPouchActionPacket::decode,
                SilverPouchActionPacket::handle);
        CHANNEL.registerMessage(id++,
                ComboSyncPacket.class,
                ComboSyncPacket::encode,
                ComboSyncPacket::decode,
                ComboSyncPacket::handle);
        CHANNEL.registerMessage(id++,
                PreceptSyncPacket.class,
                PreceptSyncPacket::encode,
                PreceptSyncPacket::decode,
                PreceptSyncPacket::handle);
        CHANNEL.registerMessage(id++,
                CorruptionSyncPacket.class,
                CorruptionSyncPacket::encode,
                CorruptionSyncPacket::decode,
                CorruptionSyncPacket::handle);
        CHANNEL.registerMessage(id++,
                FlawlessVisualPacket.class,
                FlawlessVisualPacket::encode,
                FlawlessVisualPacket::decode,
                FlawlessVisualPacket::handle);
        CHANNEL.registerMessage(id++,
                SakuraDodgeEffectPacket.class,
                SakuraDodgeEffectPacket::encode,
                SakuraDodgeEffectPacket::decode,
                SakuraDodgeEffectPacket::handle);
    }
}
