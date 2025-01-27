package com.petrolpark.recipe.ingredient.randomizer;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface IngredientRandomizer extends LootContextUser {
    
    public Ingredient generate(LootContext context);

    public IngredientRandomizerType getType();
};
