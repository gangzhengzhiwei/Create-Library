package com.petrolpark.data.reward.generator;

import java.util.Set;
import java.util.Collections;

import com.petrolpark.data.IEntityTarget;

import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public abstract class ContextEntityRewardGenerator implements IRewardGenerator {
    
    protected final IEntityTarget target;

    public ContextEntityRewardGenerator(IEntityTarget target) {
        this.target = target;
    };

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Collections.singleton(target.getReferencedParam());
    };
};
