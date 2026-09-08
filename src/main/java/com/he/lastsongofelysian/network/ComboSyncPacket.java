package com.he.lastsongofelysian.network;

import com.he.lastsongofelysian.client.overlay.ComboOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ComboSyncPacket {
    private final int combo;

    public ComboSyncPacket(int combo) { this.combo = combo; }

    public static void encode(ComboSyncPacket p, FriendlyByteBuf buf) {
        buf.writeInt(p.combo);
    }

    public static ComboSyncPacket decode(FriendlyByteBuf buf) {
        return new ComboSyncPacket(buf.readInt());
    }

    public static void handle(ComboSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        ComboOverlay.setCombo(packet.combo)));
        ctx.get().setPacketHandled(true);
    }
}
