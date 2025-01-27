package com.petrolpark.data.loot.numberprovider.itemstack;

import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.data.ForgeRegistryObjectGSONAdapter;
import com.petrolpark.data.loot.PetrolparkLootItemStackNumberProviderTypes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface ItemStackNumberProvider extends LootContextUser {
    
    public float getFloat(ItemStack stack, LootContext lootContext);

    public LootItemStackNumberProviderType getType();

    public static ForgeRegistryObjectGSONAdapter<ItemStackNumberProvider, LootItemStackNumberProviderType> createGsonAdapter() {
        return ForgeRegistryObjectGSONAdapter.builder(PetrolparkRegistries.Keys.LOOT_ITEM_STACK_NUMBER_PROVIDER_TYPE, "provider", "type", ItemStackNumberProvider::getType).withDefaultType(PetrolparkLootItemStackNumberProviderTypes.COUNT::get).build();
    };
};
