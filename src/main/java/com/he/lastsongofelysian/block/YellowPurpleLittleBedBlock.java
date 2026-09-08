package com.he.lastsongofelysian.block;

import com.he.lastsongofelysian.blockentity.YellowPurpleLittleBedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class YellowPurpleLittleBedBlock extends BedBlock {

    public YellowPurpleLittleBedBlock() {
        super(
                DyeColor.PURPLE,
                Properties.copy(Blocks.PURPLE_BED)
        );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new YellowPurpleLittleBedBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
