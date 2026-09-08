package com.he.lastsongofelysian.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.level.Level;

public class YellowPurpleBabyAxolotlEntity extends YellowPurpleAxolotlEntity {

    public YellowPurpleBabyAxolotlEntity(EntityType<? extends Axolotl> type, Level level) {
        super(type, level);
        setAge(-24000);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide && !isBaby()) {
            setAge(-24000);
        }
    }
}
