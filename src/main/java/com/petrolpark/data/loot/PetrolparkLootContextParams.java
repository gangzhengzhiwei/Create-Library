package com.petrolpark.data.loot;

import com.petrolpark.Petrolpark;
import com.petrolpark.data.IEntityTarget;
import com.petrolpark.team.ITeam;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class PetrolparkLootContextParams {

    public static final LootContextParam<Entity> CUSTOMER = createEntity("customer_entity");
    public static final LootContextParam<ITeam<?>> TEAM = create("team");

    private static <E extends Entity> LootContextParam<E> createEntity(String id) {
        LootContextParam<E> param = create(id);
        IEntityTarget.register(param);
        return param;
    };
  
    private static <T> LootContextParam<T> create(String id) {
        return new LootContextParam<>(Petrolpark.asResource(id));
    };
};
