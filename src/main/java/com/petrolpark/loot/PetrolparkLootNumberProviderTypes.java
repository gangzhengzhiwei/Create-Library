package com.petrolpark.loot;

import static com.petrolpark.Petrolpark.REGISTRATE;

import com.petrolpark.loot.numberprovider.ContextEntityNumberProvider;
import com.petrolpark.loot.numberprovider.FunctionNumberProvider;
import com.petrolpark.loot.numberprovider.MaxNumberProvider;
import com.petrolpark.loot.numberprovider.MinNumberProvider;
import com.petrolpark.loot.numberprovider.ProductNumberProvider;
import com.petrolpark.loot.numberprovider.SigmoidNumberProvider;
import com.petrolpark.loot.numberprovider.SumNumberProvider;
import com.petrolpark.loot.numberprovider.ToolNumberProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;

public class PetrolparkLootNumberProviderTypes {
    
    public static final RegistryEntry<LootNumberProviderType> MAX = REGISTRATE.lootNumberProviderType("max", new FunctionNumberProvider.Serializer<>(MaxNumberProvider::new));
    public static final RegistryEntry<LootNumberProviderType> MIN = REGISTRATE.lootNumberProviderType("min", new FunctionNumberProvider.Serializer<>(MinNumberProvider::new));
    public static final RegistryEntry<LootNumberProviderType> SUM = REGISTRATE.lootNumberProviderType("sum", new FunctionNumberProvider.Serializer<>(SumNumberProvider::new));
    public static final RegistryEntry<LootNumberProviderType> PRODUCT = REGISTRATE.lootNumberProviderType("product", new FunctionNumberProvider.Serializer<>(ProductNumberProvider::new));
    public static final RegistryEntry<LootNumberProviderType> SIGMOID = REGISTRATE.lootNumberProviderType("sigmoid", new SigmoidNumberProvider.Serializer());

    public static final RegistryEntry<LootNumberProviderType> CONTEXT_ENTITY = REGISTRATE.lootNumberProviderType("context_entity_property", new ContextEntityNumberProvider.Serializer());
    public static final RegistryEntry<LootNumberProviderType> TOOL = REGISTRATE.lootNumberProviderType("tool_property", new ToolNumberProvider.Serializer());

    public static final void register() {};
};
