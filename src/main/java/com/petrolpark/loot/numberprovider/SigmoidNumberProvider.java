package com.petrolpark.loot.numberprovider;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.petrolpark.loot.PetrolparkLootNumberProviderTypes;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class SigmoidNumberProvider implements NumberProvider {

    public final NumberProvider shallowness;
    public final NumberProvider midpoint;
    public final NumberProvider value;

    public SigmoidNumberProvider(NumberProvider k, NumberProvider midpoint, NumberProvider x) {
        this.shallowness = k;
        this.midpoint = midpoint;
        this.value = x;
    };

    @Override
    public float getFloat(LootContext lootContext) {
        float shallowness = this.shallowness.getFloat(lootContext);
        if (shallowness == 0f) return 1f;
        return 1f / (1f + (float)Math.exp((midpoint.getFloat(lootContext) - value.getFloat(lootContext)) / shallowness));
    };

    @Override
    public LootNumberProviderType getType() {
        return PetrolparkLootNumberProviderTypes.SIGMOID.get();
    };

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Sets.union(shallowness.getReferencedContextParams(), midpoint.getReferencedContextParams());
    };

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<SigmoidNumberProvider> {

        @Override
        public SigmoidNumberProvider deserialize(JsonObject json, JsonDeserializationContext deserializationContext) {
            NumberProvider k = GsonHelper.getAsObject(json, "shallowness", deserializationContext, NumberProvider.class);
            NumberProvider midpoint = GsonHelper.getAsObject(json, "midpoint", deserializationContext, NumberProvider.class);
            NumberProvider value = GsonHelper.getAsObject(json, "value", deserializationContext, NumberProvider.class);
            return new SigmoidNumberProvider(k, midpoint, value);
        };

        @Override
        public void serialize(JsonObject json, SigmoidNumberProvider value, JsonSerializationContext serializationContext) {
            json.add("shallowness", serializationContext.serialize(value.shallowness));
            json.add("midpoint", serializationContext.serialize(value.midpoint));
            json.add("value", serializationContext.serialize(value.value));
        };
    };


    
};
