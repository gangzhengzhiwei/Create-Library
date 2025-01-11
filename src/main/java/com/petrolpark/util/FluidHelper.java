package com.petrolpark.util;

import net.minecraftforge.fluids.FluidStack;

public class FluidHelper {
    
    public static boolean equalIgnoringTags(FluidStack stack1, FluidStack stack2, String ...ignoredTagKeys) {
        if (stack1.getFluid() != stack2.getFluid()) return false;
        return NBTHelper.equalIgnoring(stack1.getTag(), stack2.getTag(), ignoredTagKeys);
    };
};
