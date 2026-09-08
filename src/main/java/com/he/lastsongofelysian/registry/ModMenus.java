package com.he.lastsongofelysian.registry;

import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.menu.PardofelisShopMenu;
import com.he.lastsongofelysian.menu.SpiralWorkshopMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, lastsongofelysian.MODID);

    public static final RegistryObject<MenuType<PardofelisShopMenu>> PARDOFELIS_SHOP_MENU =
            MENUS.register("pardofelis_shop_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) ->
                            new PardofelisShopMenu(windowId, inv)
                    ));

    public static final RegistryObject<MenuType<SpiralWorkshopMenu>> SPIRAL_WORKSHOP_MENU =
            MENUS.register("spiral_workshop_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) ->
                            new SpiralWorkshopMenu(windowId, inv)
                    ));
}
