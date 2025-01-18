package com.petrolpark.loot.numberprovider;

import java.util.stream.DoubleStream;

import com.petrolpark.loot.PetrolparkLootNumberProviderTypes;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class MaxNumberProvider extends FunctionNumberProvider {

    public MaxNumberProvider(NumberProvider[] children) {
        super(children);
    };

    @Override
    public float apply(LootContext lootContext, DoubleStream childResults) {
        return (float)childResults.max().orElse(0f);
    };

    @Override
    public LootNumberProviderType getType() {
        return PetrolparkLootNumberProviderTypes.MAX.get();
    };
    
};
