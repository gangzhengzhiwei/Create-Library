package com.petrolpark.data.loot.predicate.item;

import java.util.List;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

// Will be added fully in 1.20.1
public interface IInformativeItemSubPredicate {
    
    /**
     * Change this example Item Stack so it {@link ItemPredicate#matches matches} this {@link ItemPredicate}.
     * @param exampleStack
     */
    public void modifyExample(ItemStack exampleStack);

    public void addToDescription(List<Component> description);
};
