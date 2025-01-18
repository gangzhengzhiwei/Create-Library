package com.petrolpark.loot;

import static com.petrolpark.Petrolpark.REGISTRATE;

import com.petrolpark.loot.numberprovider.entity.EquipmentNumberProvider;
import com.petrolpark.loot.numberprovider.entity.ExperienceLevelNumberProvider;
import com.petrolpark.loot.numberprovider.entity.LootEntityNumberProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;

public class PetrolparkLootEntityNumberProviderTypes {
    
    public static final RegistryEntry<LootEntityNumberProviderType> EQUIPMENT = REGISTRATE.lootEntityNumberProviderType("equipment_property", new EquipmentNumberProvider.Serializer());
    public static final RegistryEntry<LootEntityNumberProviderType> EXPERIENCE_LEVEL = REGISTRATE.lootEntityNumberProviderType("experience_level", ExperienceLevelNumberProvider::new);

    public static final void register() {};
};
