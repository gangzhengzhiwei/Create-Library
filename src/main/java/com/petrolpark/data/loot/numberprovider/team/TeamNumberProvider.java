package com.petrolpark.data.loot.numberprovider.team;

import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.data.ForgeRegistryObjectGSONAdapter;
import com.petrolpark.team.ITeam;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface TeamNumberProvider extends LootContextUser {
    
    public float getFloat(ITeam<?> team, LootContext context);

    public LootTeamNumberProviderType getType();

    public static ForgeRegistryObjectGSONAdapter<TeamNumberProvider, LootTeamNumberProviderType> createGsonAdapter() {
        return ForgeRegistryObjectGSONAdapter.builder(PetrolparkRegistries.Keys.LOOT_TEAM_NUMBER_PROVIDER_TYPE, "provider", "type", TeamNumberProvider::getType).build();
    };
};
