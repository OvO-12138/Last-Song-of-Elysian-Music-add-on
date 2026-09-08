package com.he.lastsongofelysian.network;

import com.he.lastsongofelysian.client.CorruptionHudData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CorruptionSyncPacket {
    private final int energy;

    public CorruptionSyncPacket(int energy) {
        this.energy = energy;
    }

    public static void encode(CorruptionSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.energy);
    }

    public static CorruptionSyncPacket decode(FriendlyByteBuf buf) {
        return new CorruptionSyncPacket(buf.readInt());
    }

    public static void handle(CorruptionSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> CorruptionHudData.corruptionEnergy = msg.energy);
        ctx.get().setPacketHandled(true);
    }
}
