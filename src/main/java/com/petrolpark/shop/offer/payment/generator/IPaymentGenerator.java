package com.petrolpark.shop.offer.payment.generator;

import java.util.List;

import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.data.ForgeRegistryObjectGSONAdapter;
import com.petrolpark.shop.offer.payment.IPayment;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface IPaymentGenerator<P extends IPayment> extends LootContextUser {
    
    public List<P> generate(LootContext context);

    public PaymentGeneratorType getType();

    public static ForgeRegistryObjectGSONAdapter<IPaymentGenerator<?>, PaymentGeneratorType> createGsonAdapter() {
        return ForgeRegistryObjectGSONAdapter.builder(PetrolparkRegistries.Keys.PAYMENT_GENERATOR_TYPE, "payment_generator", "type", IPaymentGenerator::getType).build();
    };
};
