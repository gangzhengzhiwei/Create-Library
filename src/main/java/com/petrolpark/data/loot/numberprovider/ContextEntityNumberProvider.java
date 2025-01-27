package com.petrolpark.data.loot.numberprovider;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.petrolpark.data.loot.PetrolparkGson;
import com.petrolpark.data.loot.PetrolparkLootNumberProviderTypes;
import com.petrolpark.data.loot.numberprovider.entity.EntityNumberProvider;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class ContextEntityNumberProvider implements NumberProvider {

    public final LootContext.EntityTarget target;
    public final EntityNumberProvider entityNumberProvider;

    public ContextEntityNumberProvider(EntityTarget target, EntityNumberProvider value) {
        this.target = target;
        this.entityNumberProvider = value;
    };

    @Override
    public float getFloat(LootContext context) {
        Entity entity = context.getParamOrNull(target.getParam());
        if (entity != null) return entityNumberProvider.getFloat(entity, context);
        return 0f;
    };

    @Override
    public LootNumberProviderType getType() {
        return PetrolparkLootNumberProviderTypes.CONTEXT_ENTITY.get();
    };

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Sets.union(entityNumberProvider.getReferencedContextParams(), Set.of(target.getParam()));
    };

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ContextEntityNumberProvider> {

        @Override
        public void serialize(JsonObject json, ContextEntityNumberProvider value, JsonSerializationContext serializationContext) {
            json.addProperty("target", value.target.getName());
            json.add("value", PetrolparkGson.get().toJsonTree(value.entityNumberProvider));
        };

        @Override
        public ContextEntityNumberProvider deserialize(JsonObject json, JsonDeserializationContext deserializationContext) {
            LootContext.EntityTarget entityTarget = GsonHelper.getAsObject(json, "target", deserializationContext, LootContext.EntityTarget.class);
            EntityNumberProvider entityNumberProvider = PetrolparkGson.get().fromJson(GsonHelper.getAsJsonObject(json, "value"), EntityNumberProvider.class);
            return new ContextEntityNumberProvider(entityTarget, entityNumberProvider);
        };

    };
    
};
