package com.petrolpark.shop.offer.payment.generator;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.SerializerType;

public class PaymentGeneratorType extends SerializerType<IPaymentGenerator<?>> {

    public PaymentGeneratorType(Serializer<? extends IPaymentGenerator<?>> serializer) {
        super(serializer);
    };
    
};
