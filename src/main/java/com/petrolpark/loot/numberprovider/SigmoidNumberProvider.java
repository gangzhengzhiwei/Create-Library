package com.petrolpark.loot.numberprovider;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class SigmoidNumberProvider implements NumberProvider {

    public final NumberProvider k;
    public final NumberProvider midpoint;
    public final NumberProvider value;

    public SigmoidNumberProvider(NumberProvider k, NumberProvider midpoint, NumberProvider x) {
        this.k = k;
        this.midpoint = midpoint;
        this.value = x;
    };

    @Override
    public float getFloat(LootContext lootContext) {
        return 1f / (1f + (float)Math.exp(-k.getFloat(lootContext) * (value.getFloat(lootContext) - midpoint.getFloat(lootContext))));
    };

    @Override
    public LootNumberProviderType getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    };

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Sets.union(k.getReferencedContextParams(), midpoint.getReferencedContextParams());
    };

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<SigmoidNumberProvider> {

        @Override
        public SigmoidNumberProvider deserialize(JsonObject json, JsonDeserializationContext deserializationContext) {
            NumberProvider k = GsonHelper.getAsObject(json, "k", deserializationContext, NumberProvider.class);
            NumberProvider midpoint = GsonHelper.getAsObject(json, "midpoint", deserializationContext, NumberProvider.class);
            NumberProvider value = GsonHelper.getAsObject(json, "value", deserializationContext, NumberProvider.class);
            return new SigmoidNumberProvider(k, midpoint, value);
        };

        @Override
        public void serialize(JsonObject json, SigmoidNumberProvider value, JsonSerializationContext serializationContext) {
            json.add("k", serializationContext.serialize(value.k));
            json.add("midpoint", serializationContext.serialize(value.midpoint));
            json.add("value", serializationContext.serialize(value.value));
        };
    };


    
};
