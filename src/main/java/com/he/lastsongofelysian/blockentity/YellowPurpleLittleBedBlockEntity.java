package com.he.lastsongofelysian.blockentity;

import com.he.lastsongofelysian.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class YellowPurpleLittleBedBlockEntity extends BlockEntity {

    public YellowPurpleLittleBedBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        super(
                ModBlockEntities.YELLOW_PURPLE_LITTLE_BED.get(),
                pos,
                state
        );
    }
}
