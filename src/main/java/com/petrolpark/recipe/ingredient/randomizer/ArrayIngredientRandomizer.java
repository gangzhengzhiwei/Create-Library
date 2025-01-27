package com.petrolpark.recipe.ingredient.randomizer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootContext;

public class ArrayIngredientRandomizer implements IngredientRandomizer {
    
    public final Ingredient[] ingredients;

    public ArrayIngredientRandomizer(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    };

    @Override
    public Ingredient generate(LootContext context) {
        if (ingredients.length == 1) return ingredients[0];
        return ingredients[context.getRandom().nextInt(ingredients.length)];
    };

    @Override
    public IngredientRandomizerType getType() {
        return IngredientRandomizerTypes.FROM_ARRAY.get();
    };

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ArrayIngredientRandomizer> {

        @Override
        public void serialize(JsonObject json, ArrayIngredientRandomizer value, JsonSerializationContext serializationContext) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'serialize'");
        };

        @Override
        public ArrayIngredientRandomizer deserialize(JsonObject json, JsonDeserializationContext deserializationContext) {
            JsonArray ingredientsElement = GsonHelper.getAsJsonArray(json, "values");
            Ingredient[] ingredients = new Ingredient[ingredientsElement.size()];
            for (int i = 0; i < ingredientsElement.size(); i++) {
                ingredients[i] = Ingredient.fromJson(ingredientsElement.get(i));
            };
            return new ArrayIngredientRandomizer(ingredients);
        };

    };
};
