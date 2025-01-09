package com.petrolpark.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.inventory.Slot;

@Mixin(Slot.class)
public class SlotMixin {
    
    // @Inject(
    //     method = "Lnet/minecraft/world/inventory/Slot;safeInsert(Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;",
    //     at = @At("HEAD"),
    //     cancellable = true,
    //     locals = LocalCapture.PRINT
    // )
    // public void inSafeInsert(ItemStack stack, int increment, CallbackInfoReturnable<ItemStack> cir) {

    // };
};
