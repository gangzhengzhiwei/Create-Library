package com.petrolpark.recipe.ingredient.modifier;

import static com.petrolpark.Petrolpark.REGISTRATE;

import com.tterrag.registrate.util.entry.RegistryEntry;

public class IngredientModifierTypes {

    public static final RegistryEntry<IngredientModifierType>

    CONTAMINATED = REGISTRATE.ingredientModifierType("contaminated", new ContaminatedIngredientModifier.Serializer());
    
    public static final void register() {};
};
