package com.he.lastsongofelysian;

import com.he.lastsongofelysian.client.overlay.ComboOverlay;
import com.he.lastsongofelysian.client.overlay.FancyCorruptionOverlay;
import com.he.lastsongofelysian.client.overlay.DisciplineOverlay;
import com.he.lastsongofelysian.client.screen.PardofelisShopScreen;
import com.he.lastsongofelysian.network.ModNetwork;
import com.he.lastsongofelysian.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.he.lastsongofelysian.client.ModItemProperties;
import com.he.lastsongofelysian.network.CocoonStageSyncPacket;
import org.slf4j.Logger;

@Mod(lastsongofelysian.MODID)
public class lastsongofelysian {

    public static final String MODID = "lastsongofelysian";
    private static final Logger LOGGER = LogUtils.getLogger();

    public lastsongofelysian() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ModBlock.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModItems.CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        ModNetwork.register();
        CocoonStageSyncPacket.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(
            modid = MODID,
            bus = Mod.EventBusSubscriber.Bus.MOD,
            value = Dist.CLIENT
    )
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            event.enqueueWork(() -> {
                ModItemProperties.register();

                MenuScreens.register(
                        ModMenus.PARDOFELIS_SHOP_MENU.get(),
                        PardofelisShopScreen::new
                );
            });
        }

        @SubscribeEvent
        public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("combo_counter", new ComboOverlay());
            event.registerAboveAll("discipline_counter", new DisciplineOverlay());
            event.registerAboveAll(
                    "corruption_fancy_v11",
                    new FancyCorruptionOverlay()
            );
        }
    }
}
