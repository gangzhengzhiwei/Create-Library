package com.petrolpark.loot;

import static com.petrolpark.Petrolpark.REGISTRATE;

import com.petrolpark.loot.numberprovider.itemstack.CountItemStackNumberProvider;
import com.petrolpark.loot.numberprovider.itemstack.EnchantmentLevelNumberProvider;
import com.petrolpark.loot.numberprovider.itemstack.LootItemStackNumberProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;

public class PetrolparkLootItemStackNumberProviderTypes {
    
    public static final RegistryEntry<LootItemStackNumberProviderType> COUNT = REGISTRATE.lootItemStackNumberProviderType("count", CountItemStackNumberProvider::new);
    public static final RegistryEntry<LootItemStackNumberProviderType> ENCHANTMENT_LEVEL = REGISTRATE.lootItemStackNumberProviderType("enchantment_level", new EnchantmentLevelNumberProvider.Serializer());

    public static final void register() {};

};
