package com.petrolpark.shop.offer;

import java.util.List;

import com.petrolpark.shop.offer.order.ShopOrder;
import com.petrolpark.shop.offer.payment.IPayment;

import java.util.ArrayList;

public class ShopOffer {
  
    protected final List<IPayment> payments = new ArrayList<>();
    protected final List<ShopOrder> orders = new ArrayList<>();
};
