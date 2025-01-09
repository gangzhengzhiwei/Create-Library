package com.petrolpark.contamination;

import com.petrolpark.fluid.FluidMixer.IFluidMixer;
import com.petrolpark.util.FluidHelper;

import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidContamination extends Contamination<Fluid, FluidStack> {

    public static final String TAG_KEY = "Contamination";

    public static FluidContamination get(FluidStack stack) {
        return new FluidContamination(stack);
    };

    protected FluidContamination(FluidStack stack) {
        super(stack);
        if (stack.getTag() != null && stack.getTag().contains(TAG_KEY, Tag.TAG_LIST)) extrinsicOrphanContaminants.addAll(stack.getTag().getList(TAG_KEY, Tag.TAG_STRING).stream().map(Tag::getAsString).map(ResourceLocation::new).map(Contaminant::get).toList());
    };

    @Override
    public Contaminable<Fluid, FluidStack> getContaminable() {
        return Contaminables.FORGE_FLUID;
    };

    @Override
    public Fluid getType() {
        return stack.getFluid();
    };

    @Override
    public double getAmount() {
        return stack.getAmount();
    };

    @Override
    public void save() {
        stack.removeChildTag(TAG_KEY);
        if (!extrinsicOrphanContaminants.isEmpty()) stack.getOrCreateTag().put(TAG_KEY, writeToNBT());
    };

    public static final IFluidMixer MIXER = new IFluidMixer() {

        @Override
        public int getMix2Priority(FluidStack fluidStack1, FluidStack fluidStack2) {
            return FluidHelper.equalIgnoringTags(fluidStack1, fluidStack2, TAG_KEY) ? 1 : -1;
        };

        @Override
        public int getMixPriority(FluidStack... fluidStacks) {
            FluidStack fluidStack0 = fluidStacks[0];
            for (int i = 1; i < fluidStacks.length; i++) {
                if (!FluidHelper.equalIgnoringTags(fluidStack0, fluidStacks[i], TAG_KEY)) return -1;
            };
            return 1;
        };

        @Override
        public FluidStack mix2(FluidStack fluidStack1, FluidStack fluidStack2) {
            FluidStack result = fluidStack1.copy();
            result.grow(fluidStack2.getAmount());
            return result;
        };

        @Override
        public FluidStack mix(FluidStack... fluidStacks) {
            FluidStack result = fluidStacks[0];
            for (int i = 1; i < fluidStacks.length; i++) {
                result.grow(fluidStacks[i].getAmount());
            };
            return result;
        };

        @Override
        public void afterMix(FluidStack result, FluidStack... fluidStacks) {
            Object2DoubleMap<Contaminant> amounts = new Object2DoubleArrayMap<>();
            double totalAmount = 0;
            for (FluidStack fluidStack : fluidStacks) {
                totalAmount += fluidStack.getAmount();
                get(fluidStack).streamAllContaminants().forEach(c -> amounts.merge(c, fluidStack.getAmount(), Double::sum));
            };
            FluidContamination contamination = get(result);
            contamination.fullyDecontaminate();
            for (Object2DoubleArrayMap.Entry<Contaminant> entry : amounts.object2DoubleEntrySet()) {
                if (entry.getKey().isPreserved(entry.getDoubleValue() / totalAmount)) contamination.contaminate(entry.getKey());
            };
        };
        
    };
    
};
