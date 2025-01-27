package com.petrolpark.data.loot.numberprovider.entity;

import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.data.ForgeRegistryObjectGSONAdapter;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface EntityNumberProvider extends LootContextUser {

    public float getFloat(Entity entity, LootContext lootContext);

    public LootEntityNumberProviderType getType();

    public static ForgeRegistryObjectGSONAdapter<EntityNumberProvider, LootEntityNumberProviderType> createGsonAdapter() {
        return ForgeRegistryObjectGSONAdapter.builder(PetrolparkRegistries.Keys.LOOT_ENTITY_NUMBER_PROVIDER_TYPE, "provider", "type", EntityNumberProvider::getType).build();
    };
};
