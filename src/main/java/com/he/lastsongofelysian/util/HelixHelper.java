package com.he.lastsongofelysian.util;

import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

public class HelixHelper {
    public static boolean isHelixEquipped(Player player) {
        return CuriosApi.getCuriosHelper()
                .findFirstCurio(player, stack -> stack.is(ModItems.SIGNET_OF_HELIX.get()))
                .isPresent();
    }
    public static boolean isEgoEquipped() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return false;
        return CuriosApi.getCuriosHelper()
                .findFirstCurio(player, stack -> stack.is(ModItems.SIGNET_OF_EGO.get()))
                .isPresent();
    }
}
