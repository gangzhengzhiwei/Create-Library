package com.petrolpark.recipe;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;
import com.petrolpark.contamination.Contaminant;
import com.petrolpark.contamination.ItemContamination;
import com.petrolpark.util.ItemHelper;

import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class CombineContaminatedItemsRecipe extends CustomRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    public CombineContaminatedItemsRecipe(ResourceLocation id) {
        super(id, CraftingBookCategory.MISC);
    };

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack firstStack = ItemStack.EMPTY;
        boolean atLeastTwo = false;
        boolean atLeastOneContaminant = false;
        for (ItemStack stack : container.getItems()) {
            if (stack.isEmpty()) continue;
            if (ItemContamination.get(stack).hasAnyExtrinsicContaminant()) atLeastOneContaminant = true;
            if (firstStack.isEmpty()) {
                firstStack = stack;
            } else {
                if (!ItemHelper.equalIgnoringTags(stack, firstStack, ItemContamination.TAG_KEY)) return false;
                atLeastTwo = true;
            };
        };
        return atLeastTwo && atLeastOneContaminant;
    };

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        Object2DoubleMap<Contaminant> amounts = new Object2DoubleArrayMap<>();
        int totalItems = 0;
        for (ItemStack stack : container.getItems()) {
            if (stack.isEmpty()) continue;
            totalItems++;
            ItemContamination.get(stack).streamAllContaminants().forEach(c -> amounts.merge(c, 1d, Double::sum));
        };
        ItemStack result = container.getItems().stream().dropWhile(ItemStack::isEmpty).findFirst().get().copyWithCount(totalItems);
        ItemContamination contamination = ItemContamination.get(result);
        contamination.fullyDecontaminate();
        for (Object2DoubleArrayMap.Entry<Contaminant> entry : amounts.object2DoubleEntrySet()) {
            if (entry.getKey().isPreserved(entry.getDoubleValue() / (double)totalItems)) contamination.contaminate(entry.getKey());
        };
        return result;
    };

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    };

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    };

    public static class Serializer implements RecipeSerializer<CombineContaminatedItemsRecipe> {

        @Override
        public CombineContaminatedItemsRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            return new CombineContaminatedItemsRecipe(recipeId);
        };

        @Override
        public @Nullable CombineContaminatedItemsRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return new CombineContaminatedItemsRecipe(recipeId);
        };

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CombineContaminatedItemsRecipe pRecipe) {};

    };

    
};
