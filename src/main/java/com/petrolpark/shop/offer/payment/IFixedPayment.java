package com.petrolpark.shop.offer.payment;

import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.data.ForgeRegistryObjectGSONAdapter;

import net.minecraft.world.level.storage.loot.LootContextUser;

public interface IFixedPayment extends IPayment, LootContextUser {
    
    public FixedPaymentType getType();

    public static ForgeRegistryObjectGSONAdapter<IFixedPayment, FixedPaymentType> createGsonAdapter() {
        return ForgeRegistryObjectGSONAdapter.builder(PetrolparkRegistries.Keys.FIXED_PAYMENT_TYPE, "fixed_payment", "type", IFixedPayment::getType).build();
    };
};
