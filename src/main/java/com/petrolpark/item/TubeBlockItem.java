package com.petrolpark.item;

import com.petrolpark.block.ITubeBlock;
import com.petrolpark.util.ClientTubePlacementHandler;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class TubeBlockItem extends BlockItem {

    public final ITubeBlock tubeBlock;

    public <B extends Block & ITubeBlock> TubeBlockItem(B block, Properties properties) {
        super(block, properties);
        this.tubeBlock = block;
    };

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientTubePlacementHandler.useOn(context, tubeBlock));
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    };

    
};
