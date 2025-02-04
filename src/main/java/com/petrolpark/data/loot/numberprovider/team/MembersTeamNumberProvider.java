package com.petrolpark.data.loot.numberprovider.team;

import com.petrolpark.data.loot.PetrolparkLootTeamNumberProviders;
import com.petrolpark.team.ITeam;

import net.minecraft.world.level.storage.loot.LootContext;

public class MembersTeamNumberProvider implements TeamNumberProvider {

    @Override
    public float getFloat(ITeam<?> team, LootContext context) {
        return team.streamMemberUsernames(context.getLevel()).count();
    };

    @Override
    public LootTeamNumberProviderType getType() {
        return PetrolparkLootTeamNumberProviders.MEMBERS.get();
    };
    
};
