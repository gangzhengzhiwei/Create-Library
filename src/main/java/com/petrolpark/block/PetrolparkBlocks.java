package com.petrolpark.block;

import static com.petrolpark.Petrolpark.REGISTRATE;

import com.petrolpark.item.TubeBlockItem;
import com.tterrag.registrate.util.entry.BlockEntry;

public class PetrolparkBlocks {
    
    public static final BlockEntry<TubeBlock> TUBE_BLOCK = REGISTRATE.block("tube", TubeBlock::new)
        .item(TubeBlockItem::new)
        .build()
        .register();

    public static final void register() {};
};
