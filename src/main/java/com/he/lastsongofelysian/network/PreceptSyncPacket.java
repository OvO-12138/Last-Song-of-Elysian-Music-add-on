package com.he.lastsongofelysian.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import com.he.lastsongofelysian.client.DisciplineHudData;

import java.util.function.Supplier;

public class PreceptSyncPacket {
    private final int precept;

    public PreceptSyncPacket(int precept) {
        this.precept = precept;
    }

    public static void encode(PreceptSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.precept);
    }

    public static PreceptSyncPacket decode(FriendlyByteBuf buf) {
        return new PreceptSyncPacket(buf.readInt());
    }

    public static void handle(PreceptSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        DisciplineHudData.setPrecept(msg.precept)));
        ctx.get().setPacketHandled(true);
    }
}
