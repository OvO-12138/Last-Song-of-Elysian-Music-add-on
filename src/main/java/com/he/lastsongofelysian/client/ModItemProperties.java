package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.item.FlawlessBenedictionLegacyItem;
import com.he.lastsongofelysian.lastsongofelysian;
import com.he.lastsongofelysian.registry.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public final class ModItemProperties {

    private ModItemProperties() {
    }

    public static void register() {

        registerNormalChargeProperties(
                ModItems.WHISPER_OF_THE_PAST.get()
        );

        registerFlawlessChargeProperties();
    }

    private static void registerNormalChargeProperties(
            Item item
    ) {
        ItemProperties.register(
                item,
                new ResourceLocation(
                        lastsongofelysian.MODID,
                        "pulling"
                ),
                (stack, level, entity, seed) -> {
                    if (entity == null) {
                        return 0.0F;
                    }

                    return entity.isUsingItem()
                            && entity.getUseItem() == stack
                            ? 1.0F
                            : 0.0F;
                }
        );

        ItemProperties.register(
                item,
                new ResourceLocation(
                        lastsongofelysian.MODID,
                        "pull"
                ),
                (stack, level, entity, seed) -> {
                    if (
                            entity == null ||
                            !entity.isUsingItem() ||
                            entity.getUseItem() != stack
                    ) {
                        return 0.0F;
                    }

                    int usedTicks =
                            stack.getUseDuration()
                                    - entity.getUseItemRemainingTicks();

                    return usedTicks / 20.0F;
                }
        );
    }

    private static void registerFlawlessChargeProperties() {
        Item item =
                ModItems.FLAWLESS_BENEDICTION_LEGACY.get();

        ItemProperties.register(
                item,
                new ResourceLocation(
                        lastsongofelysian.MODID,
                        "pulling"
                ),
                (stack, level, entity, seed) -> {
                    if (entity == null) {
                        return 0.0F;
                    }

                    return entity.isUsingItem()
                            && entity.getUseItem() == stack
                            ? 1.0F
                            : 0.0F;
                }
        );

        ItemProperties.register(
                item,
                new ResourceLocation(
                        lastsongofelysian.MODID,
                        "pull"
                ),
                (stack, level, entity, seed) -> {
                    if (
                            entity == null ||
                            !entity.isUsingItem() ||
                            entity.getUseItem() != stack
                    ) {
                        return 0.0F;
                    }

                    int usedTicks =
                            stack.getUseDuration()
                                    - entity.getUseItemRemainingTicks();

                    return FlawlessBenedictionLegacyItem
                            .getVisualPull(
                                    stack,
                                    level,
                                    entity,
                                    usedTicks
                            );
                }
        );
    }
}
