package com.he.lastsongofelysian.network;

import com.he.lastsongofelysian.client.ClientCorrosionCurseData;
import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public final class CorrosionCurseSyncPacket {

    private static final int PACKET_ID = 101;
    private static boolean registered;

    private final boolean acquired;
    private final boolean reversed;

    public CorrosionCurseSyncPacket(boolean acquired, boolean reversed) {
        this.acquired = acquired;
        this.reversed = acquired && reversed;
    }

    public static void encode(CorrosionCurseSyncPacket message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.acquired);
        buffer.writeBoolean(message.reversed);
    }

    public static CorrosionCurseSyncPacket decode(FriendlyByteBuf buffer) {
        return new CorrosionCurseSyncPacket(buffer.readBoolean(), buffer.readBoolean());
    }

    public static void handle(
            CorrosionCurseSyncPacket message,
            Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                Dist.CLIENT,
                () -> () -> ClientCorrosionCurseData.set(message.acquired, message.reversed)
        ));
        context.setPacketHandled(true);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(CorrosionCurseSyncPacket::register);
    }

    public static void register() {
        if (registered) return;

        ModNetwork.CHANNEL.registerMessage(
                PACKET_ID,
                CorrosionCurseSyncPacket.class,
                CorrosionCurseSyncPacket::encode,
                CorrosionCurseSyncPacket::decode,
                CorrosionCurseSyncPacket::handle
        );
        registered = true;
    }
}
