package com.petrolpark.block;

import com.petrolpark.block.entity.PetrolparkBlockEntityTypes;
import com.petrolpark.block.entity.TubeBlockEntity;
import com.petrolpark.util.TubeSpline;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;

public class TubeBlock extends DirectionalBlock implements ITubeBlock, IBE<TubeBlockEntity> {

    public TubeBlock(Properties properties) {
        super(properties);
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
    };

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getClickedFace());
    };

    @Override
    public double getTubeSegmentRadius() {
        return 0.5d;
    };

    @Override
    public double getTubeSegmentLength() {
        return 3 / 16d;
    };

    @Override
    public double getTubeMaxAngle() {
        return 20d * Mth.PI / 180d;
    };

    @Override
    public int getItemsForTubeLength(double length) {
        return (int)Math.round(length / 1f);
    };

    @Override
    public Class<TubeBlockEntity> getBlockEntityClass() {
        return TubeBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends TubeBlockEntity> getBlockEntityType() {
        return PetrolparkBlockEntityTypes.TUBE.get();
    }

    @Override
    public Direction getTubeConnectingFace(Level level, BlockPos pos, BlockState state) {
        return state.getValue(FACING);
    };

    @Override
    public void connectTube(Level level, TubeSpline spline) {
        level.getBlockEntity(spline.start.getPos(), PetrolparkBlockEntityTypes.TUBE.get()).ifPresent(tbe -> tbe.tube.connect(spline));
    };
    
};
