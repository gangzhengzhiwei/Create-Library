package com.petrolpark.data.loot.numberprovider.entity;

import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.petrolpark.data.loot.PetrolparkGson;
import com.petrolpark.data.loot.PetrolparkLootEntityNumberProviderTypes;
import com.petrolpark.data.loot.numberprovider.itemstack.ItemStackNumberProvider;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class EquipmentNumberProvider implements EntityNumberProvider {

    public final EquipmentSlot equipmentSlot;
    public final ItemStackNumberProvider itemNumberProvider;

    public EquipmentNumberProvider(EquipmentSlot equipmentSlot, ItemStackNumberProvider itemNumberProvider) {
        this.equipmentSlot = equipmentSlot;
        this.itemNumberProvider = itemNumberProvider;
    };

    @Override
    public float getFloat(Entity entity, LootContext lootContext) {
        if (entity instanceof LivingEntity livingEntity) return itemNumberProvider.getFloat(livingEntity.getItemBySlot(equipmentSlot), lootContext);
        return 0f;
    };

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return itemNumberProvider.getReferencedContextParams();
    };

    @Override
    public LootEntityNumberProviderType getType() {
        return PetrolparkLootEntityNumberProviderTypes.EQUIPMENT.get();
    };

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<EquipmentNumberProvider> {

        @Override
        public void serialize(JsonObject json, EquipmentNumberProvider value, JsonSerializationContext serializationContext) {
            json.addProperty("slot", value.equipmentSlot.getName());
            json.add("value", PetrolparkGson.get().toJsonTree(value.itemNumberProvider));
        };

        @Override
        public EquipmentNumberProvider deserialize(JsonObject json, JsonDeserializationContext serializationContext) {
            return new EquipmentNumberProvider(
                EquipmentSlot.byName(GsonHelper.getAsString(json, "slot")), 
                PetrolparkGson.get().fromJson(GsonHelper.getAsJsonObject(json, "value"), ItemStackNumberProvider.class)
            );
        };

    };
    
};
