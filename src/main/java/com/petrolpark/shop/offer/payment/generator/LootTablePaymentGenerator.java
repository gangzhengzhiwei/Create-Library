package com.petrolpark.shop.offer.payment.generator;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import com.petrolpark.shop.offer.payment.ItemStackShopPayment;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class LootTablePaymentGenerator implements IPaymentGenerator<ItemStackShopPayment> {

    public final LootTable lootTable;

    public LootTablePaymentGenerator(LootTable lootTable) {
        this.lootTable = lootTable;
    };

    @Override
    public List<ItemStackShopPayment> generate(LootContext context) {
        List<ItemStackShopPayment> payments = new ArrayList<>();
        lootTable.getRandomItems(context, stack -> payments.add(new ItemStackShopPayment(stack)));
        return payments;
    };

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return lootTable.getParamSet().getAllowed();
    };

    @Override
    public PaymentGeneratorType getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    };
    
};
