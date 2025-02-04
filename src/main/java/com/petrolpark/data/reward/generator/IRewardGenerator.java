package com.petrolpark.data.reward.generator;

import java.util.List;

import com.mojang.serialization.Codec;
import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.data.ForgeRegistryObjectGSONAdapter;
import com.petrolpark.data.loot.PetrolparkGson;
import com.petrolpark.data.reward.IReward;
import com.petrolpark.data.reward.RewardGeneratorTypes;
import com.petrolpark.network.GsonSerializableCodecs;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface IRewardGenerator extends LootContextUser {

    public static final Codec<IRewardGenerator> CODEC = GsonSerializableCodecs.GSONserializableCodec("reward_generator", IRewardGenerator.class, PetrolparkGson.get());
    
    public List<IReward> generate(LootContext context);

    public RewardGeneratorType getType();

    public static ForgeRegistryObjectGSONAdapter<IRewardGenerator, RewardGeneratorType> createGsonAdapter() {
        return ForgeRegistryObjectGSONAdapter.builder(PetrolparkRegistries.Keys.REWARD_GENERATOR_TYPE, "reward generator", "type", IRewardGenerator::getType)
            .withInlineSerializer(RewardGeneratorTypes.DIRECT::get, new DirectRewardGenerator.InlineSerializer())
            .build();
    };
};
