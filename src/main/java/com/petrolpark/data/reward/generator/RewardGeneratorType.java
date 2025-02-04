package com.petrolpark.data.reward.generator;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.SerializerType;

public class RewardGeneratorType extends SerializerType<IRewardGenerator> {

    public RewardGeneratorType(Serializer<? extends IRewardGenerator> serializer) {
        super(serializer);
    };
    
};
