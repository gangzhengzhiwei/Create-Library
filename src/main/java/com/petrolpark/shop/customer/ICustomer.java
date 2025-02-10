package com.petrolpark.shop.customer;

import com.petrolpark.data.loot.PetrolparkLootContextParams;
import com.petrolpark.shop.Shop;
import com.petrolpark.shop.offer.ShopOffer;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.loot.LootParams;

public interface ICustomer {

    public static final int INFINITE_ORDER_TIME = -1;

    public static void addLootParams(ICustomer customer, LootParams.Builder builder) {
        customer.supplyLootParams(builder
            .withParameter(PetrolparkLootContextParams.CUSTOMER, customer)
            .withParameter(PetrolparkLootContextParams.SHOP, customer.getShop())
        );
    };

    public int getOrderTime();

    public int getElapsedOrderTime();
    
    public ShopOffer getOpenOffer();

    public Shop getShop();

    public void clearOpenOffer();

    public Component getName();

    void supplyLootParams(LootParams.Builder builder);
};
