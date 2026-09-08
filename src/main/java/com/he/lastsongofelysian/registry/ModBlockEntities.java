package com.he.lastsongofelysian.registry;

import com.he.lastsongofelysian.blockentity.YellowPurpleLittleBedBlockEntity;
import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(
                    ForgeRegistries.BLOCK_ENTITY_TYPES,
                    lastsongofelysian.MODID
            );

    public static final RegistryObject<BlockEntityType<YellowPurpleLittleBedBlockEntity>>
            YELLOW_PURPLE_LITTLE_BED =
            BLOCK_ENTITY_TYPES.register(
                    "yellow_purple_little_bed",
                    () -> BlockEntityType.Builder.of(
                            YellowPurpleLittleBedBlockEntity::new,
                            ModBlock.YELLOW_PURPLE_LITTLE_BED.get()
                    ).build(null)
            );
}
