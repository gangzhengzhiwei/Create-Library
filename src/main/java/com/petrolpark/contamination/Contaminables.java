package com.petrolpark.contamination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.petrolpark.PetrolparkTags;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class Contaminables {
  
    protected static final List<Contaminable<?, ?>> CONTAMINABLES = new ArrayList<>();

    public static void register(Contaminable<?, ?> contaminable) {
        CONTAMINABLES.add(contaminable);
    };

    public static Stream<Contaminable<?, ?>> streamContaminables() {
        return CONTAMINABLES.stream();
    };

    public static final Contaminable<Object, Object> NOT = new Contaminable<Object,Object> () {

        @Override
        public boolean isContaminable(Object stack) {
            return false;
        };

        @Override
        public IContamination<Object, Object> getContamination(Object stack) {
            return IncontaminableContamination.INSTANCE;
        };

        @Override
        public Set<Contaminant> getIntrinsicContaminants(Object object) {
            return Collections.emptySet();
        };

        @Override
        public Set<Contaminant> getShownIfAbsentContaminants(Object object) {
            return Collections.emptySet();
        };
        
    };

    public static final Contaminable<Item, ItemStack> ITEM = new Contaminable<>() {

        @Override
        public boolean isContaminable(ItemStack stack) {
            if (stack.isEmpty()) return false;
            return stack.getItem() instanceof BlockItem ? !PetrolparkTags.Items.INCONTAMINABLE.matches(stack) : PetrolparkTags.Items.CONTAMINABLE_BLOCK.matches(stack);
        };

        @Override
        public ItemContamination getContamination(Object stack) {
            if (stack instanceof ItemStack itemStack && isContaminable(itemStack)) return new ItemContamination(itemStack);
            return null;
        };

        @Override
        @SuppressWarnings("deprecation")
        public Set<Contaminant> getIntrinsicContaminants(Item object) {
            return object.builtInRegistryHolder().tags().map(Contaminant::getFromIntrinsicTag).filter(Objects::nonNull).collect(Collectors.toSet());
        };

        @Override
        @SuppressWarnings("deprecation")
        public Set<Contaminant> getShownIfAbsentContaminants(Item object) {
            return object.builtInRegistryHolder().tags().map(Contaminant::getFromShowIfAbsentTag).filter(Objects::nonNull).collect(Collectors.toSet());
        };
        
    };

    public static final Contaminable<Fluid, FluidStack> FORGE_FLUID = new Contaminable<>() {

        @Override
        public boolean isContaminable(FluidStack stack) {
            return !stack.isEmpty() && !PetrolparkTags.Fluids.INCONTAMINABLE.matches(stack);
        };

        @Override
        public FluidContamination getContamination(Object stack) {
            if (stack instanceof FluidStack fluidStack && isContaminable(fluidStack)) return new FluidContamination(fluidStack);
            return null;
        };

        @Override
        public Set<Contaminant> getIntrinsicContaminants(Fluid object) {
            return ForgeRegistries.FLUIDS.getDelegateOrThrow(object).tags().map(Contaminant::getFromIntrinsicTag).filter(Objects::nonNull).collect(Collectors.toSet());
        };

        @Override
        public Set<Contaminant> getShownIfAbsentContaminants(Fluid object) {
            return ForgeRegistries.FLUIDS.getDelegateOrThrow(object).tags().map(Contaminant::getFromShowIfAbsentTag).filter(Objects::nonNull).collect(Collectors.toSet());
        };
        
    };

    static {
        register(NOT);
        register(ITEM);
        register(FORGE_FLUID);
    };
};
