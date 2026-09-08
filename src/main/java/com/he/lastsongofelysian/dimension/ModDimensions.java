package com.he.lastsongofelysian.dimension;

import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public final class ModDimensions {

    public static final ResourceKey<Level> ELYSIAN_REALM =
            ResourceKey.create(
                    Registries.DIMENSION,
                    new ResourceLocation(
                            lastsongofelysian.MODID,
                            "elysian_realm"
                    )
            );

    public static final ResourceKey<DimensionType>
            ELYSIAN_REALM_TYPE =
            ResourceKey.create(
                    Registries.DIMENSION_TYPE,
                    new ResourceLocation(
                            lastsongofelysian.MODID,
                            "elysian_realm_type"
                    )
            );

    private ModDimensions() {
    }
}
