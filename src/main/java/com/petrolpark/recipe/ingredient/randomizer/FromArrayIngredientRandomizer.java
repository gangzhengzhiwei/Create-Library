package com.petrolpark.recipe.ingredient.randomizer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
import net.minecraft.world.level.storage.loot.LootContext;

public class FromArrayIngredientRandomizer implements IngredientRandomizer {
    
    public final Ingredient[] ingredients;

    public FromArrayIngredientRandomizer(Ingredient[] ingredients) {
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

    public JsonArray asJsonArray() {
        JsonArray array = new JsonArray(ingredients.length);
        for (Ingredient ingredient : ingredients) array.add(ingredient.toJson());
        return array;
    };

    public static FromArrayIngredientRandomizer fromJsonArray(JsonArray array) throws JsonSyntaxException {
        Ingredient[] ingredients = new Ingredient[array.size()];
        for (int i = 0; i < array.size(); i++) {
            ingredients[i] = Ingredient.fromJson(array.get(i));
        };
        return new FromArrayIngredientRandomizer(ingredients);
    };

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<FromArrayIngredientRandomizer> {

        @Override
        public void serialize(JsonObject json, FromArrayIngredientRandomizer value, JsonSerializationContext serializationContext) {
            json.add("values", value.asJsonArray());
        };

        @Override
        public FromArrayIngredientRandomizer deserialize(JsonObject json, JsonDeserializationContext deserializationContext) {
            return fromJsonArray(GsonHelper.getAsJsonArray(json, "values"));
        };

    };

    public static class InlineSerializer implements GsonAdapterFactory.InlineSerializer<FromArrayIngredientRandomizer> {

        @Override
        public JsonElement serialize(FromArrayIngredientRandomizer value, JsonSerializationContext context) {
            return value.asJsonArray();
        };

        @Override
        public FromArrayIngredientRandomizer deserialize(JsonElement json, JsonDeserializationContext context) {
            try {
                return new FromArrayIngredientRandomizer(new Ingredient[]{Ingredient.fromJson(json)});
            } catch (JsonSyntaxException e) {
                return fromJsonArray(GsonHelper.convertToJsonArray(json, "values"));
            }
        };

    };
};
