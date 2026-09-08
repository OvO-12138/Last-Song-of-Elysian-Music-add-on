package com.he.lastsongofelysian.network;

import com.he.lastsongofelysian.client.SakuraDodgeVisual;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class SakuraDodgeEffectPacket {

    public static void encode(
            SakuraDodgeEffectPacket packet,
            FriendlyByteBuf buffer
    ) {
    }

    public static SakuraDodgeEffectPacket decode(
            FriendlyByteBuf buffer
    ) {
        return new SakuraDodgeEffectPacket();
    }

    public static void handle(
            SakuraDodgeEffectPacket packet,
            Supplier<NetworkEvent.Context> supplier
    ) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(
                () -> DistExecutor.unsafeRunWhenOn(
                        Dist.CLIENT,
                        () -> SakuraDodgeVisual::trigger
                )
        );
        context.setPacketHandled(true);
    }
}
