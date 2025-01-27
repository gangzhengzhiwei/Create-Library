package com.petrolpark.data.loot.numberprovider.entity;

import com.petrolpark.data.loot.PetrolparkLootEntityNumberProviderTypes;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;

public class ExperienceLevelNumberProvider implements EntityNumberProvider {

    @Override
    public float getFloat(Entity entity, LootContext lootContext) {
        if (entity instanceof Player player) return player.experienceLevel;
        return 0f;
    };

    @Override
    public LootEntityNumberProviderType getType() {
        return PetrolparkLootEntityNumberProviderTypes.EXPERIENCE_LEVEL.get();
    };
    
};
