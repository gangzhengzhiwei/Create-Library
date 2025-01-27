package com.petrolpark.recipe.ingredient.modifier;

import java.util.List;

import com.mojang.serialization.Codec;
import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.data.ForgeRegistryObjectGSONAdapter;
import com.petrolpark.data.loot.PetrolparkGson;
import com.petrolpark.network.GsonSerializableCodecs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface IngredientModifier extends LootContextUser {

    public static final Codec<IngredientModifier> CODEC = GsonSerializableCodecs.GSONserializableCodec("ingredient modifier", IngredientModifier.class, PetrolparkGson.get());

    public boolean test(ItemStack stack);

    public void modifyExamples(List<ItemStack> exampleStacks);

    public void addToDescription(List<Component> description);

    public IngredientModifierType getType();

    public static ForgeRegistryObjectGSONAdapter<IngredientModifier, IngredientModifierType> createGsonAdapter() {
        return ForgeRegistryObjectGSONAdapter.builder(PetrolparkRegistries.Keys.INGREDIENT_MODIFIER_TYPE, "ingredient_modifier", "type", IngredientModifier::getType).build();
    };
};
