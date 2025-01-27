package com.petrolpark.recipe.ingredient.modifier;

import java.util.List;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemPredicateIngredientModifier implements IngredientModifier {

    public final ItemPredicate predicate;

    public ItemPredicateIngredientModifier(ItemPredicate predicate) {
        this.predicate = predicate;
    };

    @Override
    public boolean test(ItemStack stack) {
        return predicate.matches(stack);
    };

    @Override
    public void modifyExamples(List<ItemStack> exampleStacks) {
        exampleStacks.forEach(stack -> {
            //TODO
        });
    };

    @Override
    public void addToDescription(List<Component> description) {
        description.add(Component.literal("Coming in 1.20.1"));
    }

    @Override
    public IngredientModifierType getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    };
    
};
