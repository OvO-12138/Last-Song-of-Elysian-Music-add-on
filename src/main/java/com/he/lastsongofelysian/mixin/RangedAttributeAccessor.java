package com.he.lastsongofelysian.mixin;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RangedAttribute.class)
public interface RangedAttributeAccessor {

    @Mutable
    @Accessor("maxValue")
    void lastsongofelysian$setMaxValue(double maxValue);
}
