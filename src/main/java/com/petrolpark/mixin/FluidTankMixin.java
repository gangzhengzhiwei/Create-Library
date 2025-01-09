package com.petrolpark.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.petrolpark.fluid.FluidMixer;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

@Mixin(FluidTank.class)
public abstract class FluidTankMixin implements IFluidHandler, IFluidTank {

    @Shadow
    public abstract void setFluid(FluidStack stack);
    
    @Overwrite(
        remap = false
    )
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(resource)) return 0;
        FluidStack toAdd = resource.copy();
        FluidStack result = FluidMixer.mixIn(getFluid(), toAdd, getCapacity(), action);
        if (action.execute()) setFluid(result);
        return resource.getAmount() - toAdd.getAmount();
    };
};
