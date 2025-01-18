package com.petrolpark.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petrolpark.loot.numberprovider.entity.EntityNumberProvider;
import com.petrolpark.loot.numberprovider.itemstack.ItemStackNumberProvider;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public class PetrolparkGson {
    
    protected static Gson GSON = null;

    public static Gson get() {
        if (GSON == null) GSON = new GsonBuilder()
            .registerTypeAdapter(NumberProvider.class, NumberProviders.createGsonAdapter())
            .registerTypeAdapter(ItemStackNumberProvider.class, ItemStackNumberProvider.createGsonAdapter())
            .registerTypeAdapter(EntityNumberProvider.class, EntityNumberProvider.createGsonAdapter())
            .registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer())
            .create();
        return GSON;
    };
};
