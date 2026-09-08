package com.he.lastsongofelysian.network;

import com.he.lastsongofelysian.client.FlawlessAnimationRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class FlawlessVisualPacket {

    public enum Type {
        MODE_WEATHER,
        MODE_SUN_MOON,
        MODE_FLAWLESS,
        WEATHER_SKILL,
        SUN_MOON_SKILL,
        BLOOM_STACK,
        FLAWLESS_BLOOM,
        ULTIMATE,
        ULTIMATE_CLOSE,
        ARROW_TRAIL,
        ARROW_IMPACT
    }

    private final Type type;
    private final int entityId;
    private final double x;
    private final double y;
    private final double z;
    private final int duration;
    private final int variant;

    public FlawlessVisualPacket(
            Type type,
            int entityId,
            double x,
            double y,
            double z,
            int duration,
            int variant
    ) {
        this.type = type;
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.duration = duration;
        this.variant = variant;
    }

    public static void encode(
            FlawlessVisualPacket packet,
            FriendlyByteBuf buffer
    ) {
        buffer.writeEnum(packet.type);
        buffer.writeVarInt(packet.entityId);
        buffer.writeDouble(packet.x);
        buffer.writeDouble(packet.y);
        buffer.writeDouble(packet.z);
        buffer.writeVarInt(packet.duration);
        buffer.writeVarInt(packet.variant);
    }

    public static FlawlessVisualPacket decode(FriendlyByteBuf buffer) {
        return new FlawlessVisualPacket(
                buffer.readEnum(Type.class),
                buffer.readVarInt(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readVarInt(),
                buffer.readVarInt()
        );
    }

    public static void handle(
            FlawlessVisualPacket packet,
            Supplier<NetworkEvent.Context> supplier
    ) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(
                () -> DistExecutor.unsafeRunWhenOn(
                        Dist.CLIENT,
                        () -> () ->
                                FlawlessAnimationRenderer.start(packet)
                )
        );
        context.setPacketHandled(true);
    }

    public Type type() {
        return type;
    }

    public int entityId() {
        return entityId;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public int duration() {
        return duration;
    }

    public int variant() {
        return variant;
    }
}
