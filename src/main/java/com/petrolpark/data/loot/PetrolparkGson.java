package com.petrolpark.data.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petrolpark.data.loot.numberprovider.entity.EntityNumberProvider;
import com.petrolpark.data.loot.numberprovider.itemstack.ItemStackNumberProvider;
import com.petrolpark.data.loot.numberprovider.team.TeamNumberProvider;
import com.petrolpark.data.reward.IReward;
import com.petrolpark.data.reward.generator.IRewardGenerator;
import com.petrolpark.recipe.ingredient.modifier.IngredientModifier;
import com.petrolpark.recipe.ingredient.randomizer.IngredientRandomizer;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public class PetrolparkGson {
    
    protected static Gson GSON = null;

    public static Gson get() {
        if (GSON == null) GSON = new GsonBuilder()
            // Number Providers
            .registerTypeAdapter(NumberProvider.class, NumberProviders.createGsonAdapter())
            .registerTypeAdapter(ItemStackNumberProvider.class, ItemStackNumberProvider.createGsonAdapter())
            .registerTypeAdapter(EntityNumberProvider.class, EntityNumberProvider.createGsonAdapter())
            .registerTypeAdapter(TeamNumberProvider.class, TeamNumberProvider.createGsonAdapter())
            // Recipe
            .registerTypeAdapter(IngredientRandomizer.class, IngredientRandomizer.createGsonAdapter())
            .registerTypeAdapter(IngredientModifier.class, IngredientModifier.createGsonAdapter())
            // Shop
            .registerTypeAdapter(IRewardGenerator.class, IRewardGenerator.createGsonAdapter())
            .registerTypeAdapter(IReward.class, IReward.createGsonAdapter())
            // Non-Petrolpark
            .registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer())
            .create();
        return GSON;
    };
};
