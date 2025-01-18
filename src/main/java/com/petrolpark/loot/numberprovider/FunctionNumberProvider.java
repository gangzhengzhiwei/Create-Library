package com.petrolpark.loot.numberprovider;

import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public abstract class FunctionNumberProvider implements NumberProvider {

    protected final NumberProvider[] children;

    public FunctionNumberProvider(NumberProvider[] children) {
        this.children = children;
    };

    @Override
    public final float getFloat(LootContext lootContext) {
        return apply(lootContext, Stream.of(children).mapToDouble(child -> child.getFloat(lootContext)));
    };

    public abstract float apply(LootContext lootContext, DoubleStream childResults);

    @FunctionalInterface
    public static interface Factory<NP extends FunctionNumberProvider> {
        public NP create(NumberProvider[] children);
    };

    public static class Serializer<NP extends FunctionNumberProvider> implements net.minecraft.world.level.storage.loot.Serializer<NP> {

        public final Factory<NP> factory;

        public Serializer(Factory<NP> factory) {
            this.factory = factory;
        };

        @Override
        public void serialize(JsonObject json, NP value, JsonSerializationContext serializationContext) {
            JsonArray childrenElement = new JsonArray(value.children.length);
            for (NumberProvider child : value.children) {
                childrenElement.add(serializationContext.serialize(child, NumberProvider.class));
            };
            json.add("children", childrenElement);
        };

        @Override
        public NP deserialize(JsonObject json, JsonDeserializationContext serializationContext) {
            JsonArray childrenElement = GsonHelper.getAsJsonArray(json, "children");
            NumberProvider[] children = new NumberProvider[childrenElement.size()];
            for (int i = 0; i < childrenElement.size(); i++) {
                children[i] = GsonHelper.convertToObject(childrenElement.get(i), "child "+i, serializationContext, NumberProvider.class);
            };
            return factory.create(children);
        };

    };
    
};
