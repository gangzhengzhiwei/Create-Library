package com.petrolpark.loot.numberprovider.itemstack;

import com.petrolpark.loot.PetrolparkLootItemStackNumberProviderTypes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class CountItemStackNumberProvider implements ItemStackNumberProvider {

    @Override
    public float getFloat(ItemStack stack, LootContext lootContext) {
        return stack.getCount();
    };

    @Override
    public LootItemStackNumberProviderType getType() {
        return PetrolparkLootItemStackNumberProviderTypes.COUNT.get();
    };
    
};
