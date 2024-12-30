package com.petrolpark.block.entity;

import static com.petrolpark.Petrolpark.REGISTRATE;

import com.petrolpark.block.PetrolparkBlocks;
import com.petrolpark.block.renderer.TubeBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class PetrolparkBlockEntityTypes {
    
    public static final BlockEntityEntry<TubeBlockEntity> TUBE = REGISTRATE.blockEntity("tube", TubeBlockEntity::new)
        .validBlock(PetrolparkBlocks.TUBE_BLOCK)
        .renderer(() -> TubeBlockEntityRenderer::new)
        .register();

    public static final void register() {};
};
