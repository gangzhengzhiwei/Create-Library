package com.petrolpark;

import static com.petrolpark.Petrolpark.REGISTRATE;

import com.petrolpark.tube.TubeStructuralBlock;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.material.PushReaction;

public class PetrolparkBlocks {
    
    public static final BlockEntry<TubeStructuralBlock> TUBE_STRUCTURE = REGISTRATE.block("tube", TubeStructuralBlock::new)
    .properties(p -> p
        .noCollission()
        .replaceable()
        .pushReaction(PushReaction.DESTROY)
    ).register();

    public static final void register() {};
};
