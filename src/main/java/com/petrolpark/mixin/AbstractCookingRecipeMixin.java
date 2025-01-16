package com.petrolpark.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.petrolpark.PetrolparkConfig;
import com.petrolpark.contamination.ItemContamination;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;

@Mixin(AbstractCookingRecipe.class)
public class AbstractCookingRecipeMixin {
  
    @Inject(
        method = "Lnet/minecraft/world/item/crafting/AbstractCookingRecipe;assemble(Lnet/minecraft/world/Container;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;",
        at = @At("RETURN"),
        cancellable = true
    )
    public void inAssemble(Container container, RegistryAccess registryAccess, CallbackInfoReturnable<ItemStack> cir) {
        if (PetrolparkConfig.SERVER.cookingPropagatesContaminants.get()) ItemContamination.get(cir.getReturnValue()).contaminateAll(ItemContamination.get(container.getItem(0)).streamAllContaminants());
    };
};
