package com.petrolpark.data.reward.generator;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.petrolpark.data.reward.IReward;
import com.petrolpark.data.reward.RewardGeneratorTypes;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class CombinedRewardGenerator implements IRewardGenerator {

    protected final IRewardGenerator[] values;

    public CombinedRewardGenerator(IRewardGenerator[] children) {
        this.values = children;
    };

    @Override
    public List<IReward> generate(LootContext context) {
        return Stream.of(values).map(rg -> rg.generate(context)).flatMap(List::stream).toList();
    };

    @Override
    public RewardGeneratorType getType() {
        return RewardGeneratorTypes.COMBINED.get();
    };

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Stream.of(values).map(IRewardGenerator::getReferencedContextParams).flatMap(Set::stream).collect(Collectors.toSet());
    };

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CombinedRewardGenerator> {

        @Override
        public void serialize(JsonObject json, CombinedRewardGenerator value, JsonSerializationContext serializationContext) {
            JsonArray array = new JsonArray(value.values.length);
            for (IRewardGenerator child : value.values) array.add(serializationContext.serialize(child, IRewardGenerator.class));
            json.add("values", array);
        };

        @Override
        public CombinedRewardGenerator deserialize(JsonObject json, JsonDeserializationContext serializationContext) {
            JsonArray array = GsonHelper.getAsJsonArray(json, "values");
            IRewardGenerator[] rewardGenerators = new IRewardGenerator[array.size()];
            for (int i = 0; i < array.size(); i++) rewardGenerators[i] = GsonHelper.getAsObject(json, "reward generator", serializationContext, IRewardGenerator.class);
            return new CombinedRewardGenerator(rewardGenerators);
        };

    };
    
};
