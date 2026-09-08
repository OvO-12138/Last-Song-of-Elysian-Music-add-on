package com.he.lastsongofelysian.network;

import com.he.lastsongofelysian.client.ClientCocoonPacketHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class CocoonStageSyncPacket {

    private static final int PACKET_ID = 100;
    private static boolean registered = false;

    private final int stage;
    private final boolean reversed;

    public CocoonStageSyncPacket(int stage, boolean reversed) {
        this.stage = Math.max(0, Math.min(12, stage));
        this.reversed = reversed;
    }

    public int getStage() {
        return stage;
    }

    public boolean isReversed() {
        return reversed;
    }

    public static void encode(
            CocoonStageSyncPacket message,
            FriendlyByteBuf buffer
    ) {
        buffer.writeVarInt(message.stage);
        buffer.writeBoolean(message.reversed);
    }

    public static CocoonStageSyncPacket decode(FriendlyByteBuf buffer) {
        return new CocoonStageSyncPacket(buffer.readVarInt(), buffer.readBoolean());
    }

    public static void handle(
            CocoonStageSyncPacket message,
            Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(
                () -> DistExecutor.unsafeRunWhenOn(
                        Dist.CLIENT,
                        () -> () -> ClientCocoonPacketHandler.handle(message)
                )
        );

        context.setPacketHandled(true);
    }

    public static void register() {
        if (registered) {
            return;
        }

        ModNetwork.CHANNEL.registerMessage(
                PACKET_ID,
                CocoonStageSyncPacket.class,
                CocoonStageSyncPacket::encode,
                CocoonStageSyncPacket::decode,
                CocoonStageSyncPacket::handle
        );

        registered = true;
    }
}
