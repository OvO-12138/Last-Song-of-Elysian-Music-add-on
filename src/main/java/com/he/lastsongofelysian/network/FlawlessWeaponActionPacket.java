package com.he.lastsongofelysian.network;

import com.he.lastsongofelysian.item.FlawlessBenedictionLegacyItem;
import com.he.lastsongofelysian.registry.ModItems;
import com.he.lastsongofelysian.util.FlawlessWeaponSkill;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class FlawlessWeaponActionPacket {

    public enum Action {
        SWITCH_MODE,
        WEAPON_SKILL,
        ULTIMATE
    }

    private final Action action;

    public FlawlessWeaponActionPacket(Action action) {
        this.action = action;
    }

    public static void encode(
            FlawlessWeaponActionPacket packet,
            FriendlyByteBuf buffer
    ) {
        buffer.writeEnum(packet.action);
    }

    public static FlawlessWeaponActionPacket decode(
            FriendlyByteBuf buffer
    ) {
        return new FlawlessWeaponActionPacket(
                buffer.readEnum(Action.class)
        );
    }

    public static void handle(
            FlawlessWeaponActionPacket packet,
            Supplier<NetworkEvent.Context> supplier
    ) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player =
                    context.getSender();

            if (player == null) {
                return;
            }

            ItemStack weapon =
                    player.getMainHandItem();

            if (
                    !weapon.is(
                            ModItems
                                    .FLAWLESS_BENEDICTION_LEGACY
                                    .get()
                    )
            ) {
                return;
            }

            switch (packet.action) {
                case SWITCH_MODE ->
                        FlawlessWeaponSkill.switchMode(
                                player,
                                weapon
                        );

                case WEAPON_SKILL ->
                        FlawlessWeaponSkill.useWeaponSkill(
                                player,
                                weapon
                        );

                case ULTIMATE ->
                        FlawlessWeaponSkill.useUltimate(
                                player,
                                weapon
                        );
            }
        });

        context.setPacketHandled(true);
    }

}
