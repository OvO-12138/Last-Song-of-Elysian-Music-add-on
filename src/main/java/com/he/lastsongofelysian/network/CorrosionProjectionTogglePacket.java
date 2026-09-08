package com.he.lastsongofelysian.network;

import com.he.lastsongofelysian.event.CorrosionCurseEvents;
import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public final class CorrosionProjectionTogglePacket {

    private static final int PACKET_ID = 102;
    private static boolean registered;

    public CorrosionProjectionTogglePacket() {
    }

    public static void encode(
            CorrosionProjectionTogglePacket message,
            FriendlyByteBuf buffer
    ) {
    }

    public static CorrosionProjectionTogglePacket decode(FriendlyByteBuf buffer) {
        return new CorrosionProjectionTogglePacket();
    }

    public static void handle(
            CorrosionProjectionTogglePacket message,
            Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer sender = context.getSender();

        if (sender != null) {
            context.enqueueWork(
                    () -> CorrosionCurseEvents.toggleFriendlyProjection(sender)
            );
        }

        context.setPacketHandled(true);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(CorrosionProjectionTogglePacket::register);
    }

    public static void register() {
        if (registered) return;

        ModNetwork.CHANNEL.registerMessage(
                PACKET_ID,
                CorrosionProjectionTogglePacket.class,
                CorrosionProjectionTogglePacket::encode,
                CorrosionProjectionTogglePacket::decode,
                CorrosionProjectionTogglePacket::handle
        );
        registered = true;
    }
}
