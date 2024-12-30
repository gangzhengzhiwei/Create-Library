package com.petrolpark.util;

import java.util.function.Predicate;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemHelper {
    
    /**
     * @param inv
     * @param test
     * @param maxCount
     * @return Actual number of actual Items removed
     */
    public static int removeItems(IItemHandler inv, Predicate<ItemStack> test, int maxCount) {
        int removed = 0;
        for (int slot = 0; slot < inv.getSlots(); slot++) {
            if (test.test(inv.getStackInSlot(slot))) removed += inv.extractItem(slot, maxCount - removed, false).getCount();
        };
        return removed;
    };
};
