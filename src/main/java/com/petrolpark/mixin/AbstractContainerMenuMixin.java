package com.petrolpark.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.inventory.AbstractContainerMenu;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    
    // @Redirect(
    //     method = "Lnet/minecraft/world/inventory/AbstractContainerMenu;doClick(IILnet/minecraft/world/inventory/ClickType;Lnet/minecraft/world/entity/player/Player;)V",
    //     at = @At(
    //         value = "INVOKE",
    //         target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTag(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
    //     )
    // )
    // private static boolean areItemsStackable(ItemStack stack, ItemStack other) {
    //     return ItemHelper.equalIgnoringTags(stack, other, ItemContamination.TAG_KEY);
    // };
};
