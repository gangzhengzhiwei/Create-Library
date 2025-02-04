package com.petrolpark.data.reward;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.SerializerType;

public class RewardType extends SerializerType<IReward> {

    public RewardType(Serializer<? extends IReward> serializer) {
        super(serializer);
    };
    
};
