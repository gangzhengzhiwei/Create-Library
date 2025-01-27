package com.petrolpark.shop.offer.order;

import java.util.List;
import java.util.ArrayList;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.network.GsonSerializableCodecs;
import com.petrolpark.recipe.ingredient.modifier.IngredientModifier;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class ShopOrderModifier implements LootContextUser {

    public static final Codec<ShopOrderModifier> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            IngredientModifier.CODEC.fieldOf("requirement").forGetter(ShopOrderModifier::getIngredientModifier),
            GsonSerializableCodecs.NUMBER_PROVIDER.fieldOf("success").forGetter(ShopOrderModifier::getSuccessMultiplier),
            GsonSerializableCodecs.NUMBER_PROVIDER.fieldOf("failure").forGetter(ShopOrderModifier::getFailureNumberProvider)
        ).apply(instance, ShopOrderModifier::new)
    );
    
    public final IngredientModifier ingredientModifier;
    public final NumberProvider successMultiplier;
    public final NumberProvider failureMultiplier;

    public ShopOrderModifier(IngredientModifier ingredientModifier, NumberProvider successMultiplier, NumberProvider failureMultiplier) {
        this.ingredientModifier = ingredientModifier;
        this.successMultiplier = successMultiplier;
        this.failureMultiplier = failureMultiplier;
    };

    public IngredientModifier getIngredientModifier() {
        return ingredientModifier;
    };

    public NumberProvider getSuccessMultiplier() {
        return successMultiplier;
    };

    public NumberProvider getFailureNumberProvider() {
        return failureMultiplier;
    };

    public List<Component> getDescription() {
        List<Component> description = new ArrayList<>();
        ingredientModifier.addToDescription(description);
        return description;
    };

};
