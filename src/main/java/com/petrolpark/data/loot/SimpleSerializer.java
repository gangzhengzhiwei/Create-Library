package com.petrolpark.data.loot;

import java.util.function.Supplier;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.world.level.storage.loot.Serializer;

public class SimpleSerializer<T> implements Serializer<T> {

    public final Supplier<T> factory;

    public SimpleSerializer(Supplier<T> factory) {
        this.factory = factory;
    };

    @Override
    public void serialize(JsonObject pJson, T pValue, JsonSerializationContext pSerializationContext) {};

    @Override
    public T deserialize(JsonObject pJson, JsonDeserializationContext pSerializationContext) {
        return factory.get();
    };
    
};
