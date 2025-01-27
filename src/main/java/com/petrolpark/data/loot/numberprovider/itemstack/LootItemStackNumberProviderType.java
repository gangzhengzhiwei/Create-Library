package com.petrolpark.data.loot.numberprovider.itemstack;

import java.util.function.Supplier;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.SerializerType;

public class LootItemStackNumberProviderType extends SerializerType<ItemStackNumberProvider> {

    public LootItemStackNumberProviderType(Serializer<? extends ItemStackNumberProvider> serializer) {
        super(serializer);
    };

    public LootItemStackNumberProviderType(Supplier<? extends ItemStackNumberProvider> simpleSupplier) {
        super(new SimpleSerializer<>(simpleSupplier));
    };

    public static final class SimpleSerializer<NP extends ItemStackNumberProvider> implements Serializer<NP> {

        public final Supplier<NP> factory;

        public SimpleSerializer(Supplier<NP> factory) {
            this.factory = factory;
        };

        @Override
        public void serialize(JsonObject json, NP value, JsonSerializationContext serializationContext) {};

        @Override
        public NP deserialize(JsonObject json, JsonDeserializationContext serializationContext) {
            return factory.get();
        };
    };
    
};
