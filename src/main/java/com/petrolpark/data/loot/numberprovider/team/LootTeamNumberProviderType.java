package com.petrolpark.data.loot.numberprovider.team;

import java.util.function.Supplier;

import com.petrolpark.data.loot.SimpleSerializer;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.SerializerType;

public class LootTeamNumberProviderType extends SerializerType<TeamNumberProvider> {

    public LootTeamNumberProviderType(Serializer<? extends TeamNumberProvider> serializer) {
        super(serializer);
    };

    public LootTeamNumberProviderType(Supplier<? extends TeamNumberProvider> factory) {
        super(new SimpleSerializer<>(factory));
    };
    
};
