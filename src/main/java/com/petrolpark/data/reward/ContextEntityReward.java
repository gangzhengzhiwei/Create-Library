package com.petrolpark.data.reward;

import java.util.Set;

import com.petrolpark.data.IEntityTarget;

import java.util.Collections;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public abstract class ContextEntityReward implements IReward {

    public final IEntityTarget target;

    public ContextEntityReward(IEntityTarget target) {
        this.target = target;
    };

    @Override
    public final void reward(LootContext context, float multiplier) {
        Entity entity = target.get(context);
        if (entity != null) reward(entity, context, multiplier);
    };

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Collections.singleton(target.getReferencedParam());
    };

    public abstract void reward(Entity entity, LootContext context, float multiplier);
    
};
