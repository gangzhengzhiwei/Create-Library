package com.petrolpark.shop.offer.payment;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.SerializerType;

public class FixedPaymentType extends SerializerType<IFixedPayment> {

    public FixedPaymentType(Serializer<? extends IFixedPayment> serializer) {
        super(serializer);
    };
    
};
