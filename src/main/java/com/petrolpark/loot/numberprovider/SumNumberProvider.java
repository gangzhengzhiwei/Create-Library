package com.petrolpark.loot.numberprovider;

import java.util.stream.DoubleStream;

import com.petrolpark.loot.PetrolparkLootNumberProviderTypes;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class SumNumberProvider extends FunctionNumberProvider {

    public SumNumberProvider(NumberProvider[] children) {
        super(children);
    };

    @Override
    public float apply(LootContext lootContext, DoubleStream children) {
        return (float)children.sum();
    };

    @Override
    public LootNumberProviderType getType() {
        return PetrolparkLootNumberProviderTypes.SUM.get();
    };
    
};
