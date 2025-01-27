package com.petrolpark.recipe.ingredient.randomizer;

import static com.petrolpark.Petrolpark.REGISTRATE;

import com.tterrag.registrate.util.entry.RegistryEntry;

public class IngredientRandomizerTypes {
  
    public static final RegistryEntry<IngredientRandomizerType> FROM_ARRAY = REGISTRATE.ingredientRandomizerType("from_array", new ArrayIngredientRandomizer.Serializer());
};
