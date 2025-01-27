package com.petrolpark.recipe.ingredient.modifier;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.SerializerType;

public class IngredientModifierType extends SerializerType<IngredientModifier> {

    public IngredientModifierType(Serializer<? extends IngredientModifier> serializer) {
        super(serializer);
    };
    
};
