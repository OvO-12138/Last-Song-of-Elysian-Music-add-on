package com.he.lastsongofelysian.network;

import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(lastsongofelysian.MODID, "main"),
            () -> VERSION, VERSION::equals, VERSION::equals);

    private static boolean registered;

    public static synchronized void register() {
        if (registered) {
            return;
        }

        CHANNEL.registerMessage(0,
                SilverPouchActionPacket.class,
                SilverPouchActionPacket::encode,
                SilverPouchActionPacket::decode,
                SilverPouchActionPacket::handle);
        CHANNEL.registerMessage(1,
                ComboSyncPacket.class,
                ComboSyncPacket::encode,
                ComboSyncPacket::decode,
                ComboSyncPacket::handle);
        CHANNEL.registerMessage(2,
                PreceptSyncPacket.class,
                PreceptSyncPacket::encode,
                PreceptSyncPacket::decode,
                PreceptSyncPacket::handle);
        CHANNEL.registerMessage(3,
                CorruptionSyncPacket.class,
                CorruptionSyncPacket::encode,
                CorruptionSyncPacket::decode,
                CorruptionSyncPacket::handle);
        CHANNEL.registerMessage(4,
                FlawlessVisualPacket.class,
                FlawlessVisualPacket::encode,
                FlawlessVisualPacket::decode,
                FlawlessVisualPacket::handle);
        CHANNEL.registerMessage(5,
                SakuraDodgeEffectPacket.class,
                SakuraDodgeEffectPacket::encode,
                SakuraDodgeEffectPacket::decode,
                SakuraDodgeEffectPacket::handle);
        CHANNEL.registerMessage(6,
                FlawlessWeaponActionPacket.class,
                FlawlessWeaponActionPacket::encode,
                FlawlessWeaponActionPacket::decode,
                FlawlessWeaponActionPacket::handle);
        CHANNEL.registerMessage(7,
                BloomStackSyncPacket.class,
                BloomStackSyncPacket::encode,
                BloomStackSyncPacket::decode,
                BloomStackSyncPacket::handle);

        registered = true;
    }
}
