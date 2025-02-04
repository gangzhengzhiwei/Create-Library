package com.petrolpark.recipe.ingredient;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.serialization.Codec;
import com.petrolpark.network.GsonSerializableCodecs;

import net.minecraft.world.item.crafting.Ingredient;

/**
 * @deprecated There is a built-in Ingredient Codec in 1.21
 */
@Deprecated
public class IngredientCodec {

    private static final TypeAdapter TYPE_ADAPTER = new TypeAdapter();

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Ingredient.class, TYPE_ADAPTER).create();
    
    public static final Codec<Ingredient> INSTANCE = GsonSerializableCodecs.GSONserializableCodec("ingredient", Ingredient.class, GSON);

    private static class TypeAdapter implements JsonSerializer<Ingredient>, JsonDeserializer<Ingredient> {

        @Override
        public Ingredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Ingredient.fromJson(json);
        };

        @Override
        public JsonElement serialize(Ingredient src, Type typeOfSrc, JsonSerializationContext context) {
            return src.toJson();
        };

    };

};
