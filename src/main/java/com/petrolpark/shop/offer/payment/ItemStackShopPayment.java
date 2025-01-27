package com.petrolpark.shop.offer.payment;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class ItemStackShopPayment implements IFixedPayment {

    protected final ItemStack stack;

    public ItemStackShopPayment(ItemStack stack) {
        this.stack = stack;
    };

    @Override
    public void pay(LootContext context, float multiplier) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pay'");
    };

    @Override
    public void render(GuiGraphics graphics) {
        graphics.renderItem(stack, 0, 0);
    };

    @Override
    public Component getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public FixedPaymentType getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    };
    
};
