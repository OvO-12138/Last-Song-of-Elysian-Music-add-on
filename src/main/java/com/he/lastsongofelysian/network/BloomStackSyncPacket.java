package com.he.lastsongofelysian.network;

import com.he.lastsongofelysian.client.FlawlessBloomClientData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public final class BloomStackSyncPacket {

    private final UUID entityUuid;
    private final int stacks;

    public BloomStackSyncPacket(
            UUID entityUuid,
            int stacks
    ) {
        this.entityUuid = entityUuid;
        this.stacks = Math.max(
                0,
                Math.min(
                        3,
                        stacks
                )
        );
    }

    public static void encode(
            BloomStackSyncPacket packet,
            FriendlyByteBuf buffer
    ) {
        buffer.writeUUID(packet.entityUuid);
        buffer.writeVarInt(packet.stacks);
    }

    public static BloomStackSyncPacket decode(
            FriendlyByteBuf buffer
    ) {
        return new BloomStackSyncPacket(
                buffer.readUUID(),
                buffer.readVarInt()
        );
    }

    public static void handle(
            BloomStackSyncPacket packet,
            Supplier<NetworkEvent.Context> supplier
    ) {
        NetworkEvent.Context context =
                supplier.get();

        context.enqueueWork(
                () -> DistExecutor.unsafeRunWhenOn(
                        Dist.CLIENT,
                        () -> () ->
                                FlawlessBloomClientData.setStacks(
                                        packet.entityUuid,
                                        packet.stacks
                                )
                )
        );

        context.setPacketHandled(true);
    }
}
