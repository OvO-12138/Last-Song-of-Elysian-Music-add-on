package com.he.lastsongofelysian.mixin;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceBlockEntityAccessor {

    @Accessor("cookingProgress")
    int getCookingProgress();

    @Accessor("cookingProgress")
    void setCookingProgress(int value);

    @Accessor("cookingTotalTime")
    int getCookingTotalTime();

    @Invoker("isLit")
    boolean invokeIsLit();
}
