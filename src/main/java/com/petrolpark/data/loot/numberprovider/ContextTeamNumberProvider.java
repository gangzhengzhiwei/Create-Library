package com.petrolpark.data.loot.numberprovider;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.petrolpark.data.loot.PetrolparkGson;
import com.petrolpark.data.loot.PetrolparkLootContextParams;
import com.petrolpark.data.loot.PetrolparkLootNumberProviderTypes;
import com.petrolpark.data.loot.numberprovider.team.TeamNumberProvider;
import com.petrolpark.team.ITeam;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class ContextTeamNumberProvider implements NumberProvider {

    protected final TeamNumberProvider teamNumberProvider;

    public ContextTeamNumberProvider(TeamNumberProvider teamNumberProvider) {
        this.teamNumberProvider = teamNumberProvider;
    };

    @Override
    public float getFloat(LootContext context) {
        ITeam<?> team = context.getParam(PetrolparkLootContextParams.TEAM);
        if (team != null) return teamNumberProvider.getFloat(team, context);
        return 0f;
    };

    @Override
    public LootNumberProviderType getType() {
        return PetrolparkLootNumberProviderTypes.CONTEXT_TEAM.get();
    };

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Sets.union(Set.of(PetrolparkLootContextParams.TEAM), teamNumberProvider.getReferencedContextParams());
    };

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ContextTeamNumberProvider> {

        @Override
        public void serialize(JsonObject json, ContextTeamNumberProvider value, JsonSerializationContext serializationContext) {
            json.add("value", PetrolparkGson.get().toJsonTree(value.teamNumberProvider));
        };

        @Override
        public ContextTeamNumberProvider deserialize(JsonObject json, JsonDeserializationContext serializationContext) {
            return new ContextTeamNumberProvider(PetrolparkGson.get().fromJson(GsonHelper.getAsJsonObject(json, "value"), TeamNumberProvider.class));
        };
        
    };
    
};
