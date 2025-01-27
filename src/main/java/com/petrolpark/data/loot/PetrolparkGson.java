package com.petrolpark.data.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petrolpark.data.loot.numberprovider.entity.EntityNumberProvider;
import com.petrolpark.data.loot.numberprovider.itemstack.ItemStackNumberProvider;
import com.petrolpark.recipe.ingredient.modifier.IngredientModifier;
import com.petrolpark.shop.offer.payment.IFixedPayment;
import com.petrolpark.shop.offer.payment.generator.IPaymentGenerator;

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
            // Recipe
            .registerTypeAdapter(IngredientModifier.class, IngredientModifier.createGsonAdapter())
            // Shop
            .registerTypeAdapter(IPaymentGenerator.class, IPaymentGenerator.createGsonAdapter())
            .registerTypeAdapter(IFixedPayment.class, IFixedPayment.createGsonAdapter())
            // Non-Petrolpark
            .registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer())
            .create();
        return GSON;
    };
};
