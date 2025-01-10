package com.petrolpark.util;

import com.petrolpark.RequiresCreate;

import net.minecraftforge.fluids.FluidStack;

@RequiresCreate
public class FluidHelper extends com.simibubi.create.foundation.fluid.FluidHelper {
    
    public static boolean equalIgnoringTags(FluidStack stack1, FluidStack stack2, String ...ignoredTagKeys) {
        if (stack1.getFluid() != stack2.getFluid()) return false;
        return NBTHelper.equalIgnoring(stack1.getTag(), stack2.getTag(), ignoredTagKeys);
    };
};
