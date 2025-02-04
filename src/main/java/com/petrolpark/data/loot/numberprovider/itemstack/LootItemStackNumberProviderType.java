package com.petrolpark.data.loot.numberprovider.itemstack;

import java.util.function.Supplier;

import com.petrolpark.data.loot.SimpleSerializer;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.SerializerType;

public class LootItemStackNumberProviderType extends SerializerType<ItemStackNumberProvider> {

    public LootItemStackNumberProviderType(Serializer<? extends ItemStackNumberProvider> serializer) {
        super(serializer);
    };

    public LootItemStackNumberProviderType(Supplier<? extends ItemStackNumberProvider> simpleSupplier) {
        super(new SimpleSerializer<>(simpleSupplier));
    };
    
};
