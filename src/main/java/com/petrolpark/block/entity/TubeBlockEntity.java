package com.petrolpark.block.entity;

import java.util.List;

import com.petrolpark.block.entity.behaviour.TubeBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TubeBlockEntity extends SmartBlockEntity {

    public TubeBehaviour tube;

    public TubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tube = new TubeBehaviour(this);
        behaviours.add(tube);
    };
    
};
