package com.petrolpark.recipe.ingredient.randomizer;

import com.mojang.serialization.Codec;
import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.data.ForgeRegistryObjectGSONAdapter;
import com.petrolpark.data.loot.PetrolparkGson;
import com.petrolpark.network.GsonSerializableCodecs;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface IngredientRandomizer extends LootContextUser {

    public static final Codec<IngredientRandomizer> CODEC = GsonSerializableCodecs.GSONserializableCodec("ingredient randomizer", IngredientRandomizer.class, PetrolparkGson.get());
    
    public Ingredient generate(LootContext context);

    public IngredientRandomizerType getType();

    public static ForgeRegistryObjectGSONAdapter<IngredientRandomizer, IngredientRandomizerType> createGsonAdapter() {
        return ForgeRegistryObjectGSONAdapter.builder(PetrolparkRegistries.Keys.INGREDIENT_RANDOMIZER_TYPE, "ingredient randomizer", "type", IngredientRandomizer::getType)
            .withInlineSerializer(IngredientRandomizerTypes.FROM_ARRAY::get, new FromArrayIngredientRandomizer.InlineSerializer())
            .build();
    };
};
