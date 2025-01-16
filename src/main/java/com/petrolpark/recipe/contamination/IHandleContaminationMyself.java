package com.petrolpark.recipe.contamination;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public interface IHandleContaminationMyself<C extends Container> extends Recipe<C> {
    
    public boolean contaminationHandled(C container, RegistryAccess registryAccess);
};
