package com.petrolpark.recipe.ingredient.randomizer;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.SerializerType;

public class IngredientRandomizerType extends SerializerType<IngredientRandomizer> {

    public IngredientRandomizerType(Serializer<? extends IngredientRandomizer> serializer) {
        super(serializer);
    };
    
};
