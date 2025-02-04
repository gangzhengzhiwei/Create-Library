package com.petrolpark.data.reward.generator;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.petrolpark.data.reward.IReward;
import com.petrolpark.data.reward.RewardGeneratorTypes;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class DirectRewardGenerator implements IRewardGenerator {

    public final List<IReward> rewards;

    public DirectRewardGenerator(List<IReward> payments) {
        this.rewards = payments;
    };

    @Override
    public List<IReward> generate(LootContext context) {
        return rewards;
    };

    @Override
    public RewardGeneratorType getType() {
        return RewardGeneratorTypes.DIRECT.get();
    };

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return rewards.stream().flatMap(p -> p.getReferencedContextParams().stream()).collect(Collectors.toSet());
    };

    public JsonArray asJsonArray(JsonSerializationContext serializationContext) {
        JsonArray jsonArray = new JsonArray();
        for (IReward reward : rewards) jsonArray.add(serializationContext.serialize(reward));
        return jsonArray;
    };

    public static DirectRewardGenerator fromJsonArray(JsonArray array, JsonDeserializationContext deserializationContext) {
        List<IReward> rewards = new ArrayList<>(array.size());
        for (JsonElement element : array) {
            IReward reward = GsonHelper.convertToObject(element, "reward", deserializationContext, IReward.class);
            rewards.add(reward);
        };
        return new DirectRewardGenerator(rewards);
    };

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<DirectRewardGenerator> {

        @Override
        public void serialize(JsonObject json, DirectRewardGenerator value, JsonSerializationContext serializationContext) {
            json.add("rewards", value.asJsonArray(serializationContext));
        };

        @Override
        public DirectRewardGenerator deserialize(JsonObject json, JsonDeserializationContext deserializationContext) {
            return fromJsonArray(GsonHelper.getAsJsonArray(json, "rewards"), deserializationContext);
        };
        
    };

    public static class InlineSerializer implements GsonAdapterFactory.InlineSerializer<DirectRewardGenerator> {

        @Override
        public JsonElement serialize(DirectRewardGenerator value, JsonSerializationContext context) {
            return value.asJsonArray(context);
        };

        @Override
        public DirectRewardGenerator deserialize(JsonElement json, JsonDeserializationContext deserializationContext) {
            try {
                return new DirectRewardGenerator(List.of(GsonHelper.convertToObject(json, "reward", deserializationContext, IReward.class)));
            } catch (JsonSyntaxException e) {
                return fromJsonArray(GsonHelper.convertToJsonArray(json, "rewards"), deserializationContext);
            }
        };

    };
    
};
