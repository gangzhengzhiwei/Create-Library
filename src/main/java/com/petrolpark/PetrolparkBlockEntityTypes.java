package com.petrolpark;

import static com.petrolpark.Petrolpark.REGISTRATE;

import com.petrolpark.tube.TubeStructuralBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

@RequiresCreate
public class PetrolparkBlockEntityTypes {

    public static final BlockEntityEntry<TubeStructuralBlockEntity> TUBE_STRUCTURE = REGISTRATE.blockEntity("tube_structure", TubeStructuralBlockEntity::new)
        .validBlock(PetrolparkBlocks.TUBE_STRUCTURE)
        .register();

    public static final void register() {};
};
