package com.petrolpark.tube;

import com.petrolpark.PetrolparkBlockEntityTypes;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TubeStructuralBlock extends Block implements IBE<TubeStructuralBlockEntity> {

    public TubeStructuralBlock(Properties properties) {
        super(properties);
    };

    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level pLevel, BlockState pState, BlockEntityType<S> pBlockEntityType) {
        return null; // Doesn't need to tick
    };

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean pIsMoving) {
        IBE.onRemove(state, level, pos, newState);
        super.onRemove(state, level, pos, newState, pIsMoving);
    };

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    };

    @Override
    public Class<TubeStructuralBlockEntity> getBlockEntityClass() {
        return TubeStructuralBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends TubeStructuralBlockEntity> getBlockEntityType() {
        return PetrolparkBlockEntityTypes.TUBE_STRUCTURE.get();
    };
    
};
