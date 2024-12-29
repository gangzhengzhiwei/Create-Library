package com.petrolpark.block;

import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;

public class TubeBlock extends Block implements ITubeBlock {

    public TubeBlock(Properties properties) {
        super(properties);
    };

    @Override
    public double getTubeSegmentRadius() {
        return 0.5d;
    };

    @Override
    public double getTubeSegmentLength() {
        return 4 /16d;
    };

    @Override
    public double getTubeMaxAngle() {
        return 5d * Mth.PI / 180d;
    };
    
};
