package com.he.lastsongofelysian.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class YellowPurpleAxolotlEntity extends Axolotl {

    public YellowPurpleAxolotlEntity(EntityType<? extends Axolotl> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Axolotl.createAttributes();
    }

    public static boolean canSpawn(
            EntityType<? extends Axolotl> type,
            ServerLevelAccessor level,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random
    ) {
        return Axolotl.checkAxolotlSpawnRules(
                type,
                level,
                spawnType,
                pos,
                random
        );
    }
}
