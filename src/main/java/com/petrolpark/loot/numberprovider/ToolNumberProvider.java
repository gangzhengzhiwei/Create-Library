package com.petrolpark.loot.numberprovider;

import java.util.Collections;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.petrolpark.loot.PetrolparkGson;
import com.petrolpark.loot.PetrolparkLootNumberProviderTypes;
import com.petrolpark.loot.numberprovider.itemstack.ItemStackNumberProvider;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class ToolNumberProvider implements NumberProvider {

    public final ItemStackNumberProvider itemNumberProvider;

    public ToolNumberProvider(ItemStackNumberProvider itemNumberProvider) {
        this.itemNumberProvider = itemNumberProvider;
    };

    @Override
    public float getFloat(LootContext lootContext) {
        ItemStack tool = lootContext.getParamOrNull(LootContextParams.TOOL);
        if (tool != null) return itemNumberProvider.getFloat(tool, lootContext);
        return 0f;
    };

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Collections.singleton(LootContextParams.TOOL);
    };

    @Override
    public LootNumberProviderType getType() {
        return PetrolparkLootNumberProviderTypes.TOOL.get();
    };

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ToolNumberProvider> {

        @Override
        public void serialize(JsonObject json, ToolNumberProvider value, JsonSerializationContext serializationContext) {
            json.add("value", PetrolparkGson.get().toJsonTree(value.itemNumberProvider));
        };

        @Override
        public ToolNumberProvider deserialize(JsonObject json, JsonDeserializationContext serializationContext) {
            return new ToolNumberProvider(PetrolparkGson.get().fromJson(GsonHelper.getAsJsonObject(json, "value"), ItemStackNumberProvider.class));
        };

    };
    
};
