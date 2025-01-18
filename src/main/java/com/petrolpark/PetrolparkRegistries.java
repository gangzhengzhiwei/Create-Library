package com.petrolpark;

import static com.petrolpark.Petrolpark.REGISTRATE;

import com.petrolpark.badge.Badge;
import com.petrolpark.contamination.Contaminant;
import com.petrolpark.dough.Dough;
import com.petrolpark.dough.DoughCut;
import com.petrolpark.loot.numberprovider.entity.LootEntityNumberProviderType;
import com.petrolpark.loot.numberprovider.itemstack.LootItemStackNumberProviderType;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PetrolparkRegistries {

    public static <OBJECT> ForgeRegistry<OBJECT> getRegistry(ResourceKey<Registry<OBJECT>> key) {
        return RegistryManager.ACTIVE.getRegistry(key);
    };

    public static <OBJECT> Registry<OBJECT> getDataRegistry(ResourceKey<Registry<OBJECT>> key) {
        return ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(key);
    };
    
    public static class Keys {
        // Core
        public static final ResourceKey<Registry<Contaminant>> CONTAMINANT = REGISTRATE.makeRegistry("contaminant", RegistryBuilder::new);
        public static final ResourceKey<Registry<Badge>> BADGE = REGISTRATE.makeRegistry("badge", RegistryBuilder::new);

        // Loot
        public static final ResourceKey<Registry<LootItemStackNumberProviderType>> LOOT_ITEM_STACK_NUMBER_PROVIDER_TYPE = REGISTRATE.makeRegistry("loot_item_stack_number_provider_type", RegistryBuilder::new);
        public static final ResourceKey<Registry<LootEntityNumberProviderType>> LOOT_ENTITY_NUMBER_PROVIDER_TYPE = REGISTRATE.makeRegistry("loot_entity_number_provider_type", RegistryBuilder::new);

        // Dough
        public static final ResourceKey<Registry<Dough>> DOUGH = REGISTRATE.makeRegistry("dough", RegistryBuilder::new);
        public static final ResourceKey<Registry<DoughCut>> DOUGH_CUT = REGISTRATE.makeRegistry("dough_cut", RegistryBuilder::new);

    };

    public static final void register() {};
};
