package com.petrolpark.shop.offer.payment.generator;

import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;

import com.petrolpark.shop.offer.payment.IFixedPayment;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class DirectPaymentGenerator implements IPaymentGenerator<IFixedPayment> {

    public final List<IFixedPayment> payments;

    public DirectPaymentGenerator(List<IFixedPayment> payments) {
        this.payments = payments;
    };

    @Override
    public List<IFixedPayment> generate(LootContext context) {
        return payments;
    }

    @Override
    public PaymentGeneratorType getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    };

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return payments.stream().flatMap(p -> p.getReferencedContextParams().stream()).collect(Collectors.toSet());
    };
    
};
