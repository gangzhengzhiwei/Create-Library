package com.petrolpark.shop.offer.order;

import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class ShopOrder {
    
    public final Ingredient requiredItem;
    protected final List<ShopOrderModifier> requirementModifiers;

    public ShopOrder(Ingredient requiredItem, List<ShopOrderModifier> modifiers) {
        this.requiredItem = requiredItem;
        this.requirementModifiers = modifiers;
    };
};
