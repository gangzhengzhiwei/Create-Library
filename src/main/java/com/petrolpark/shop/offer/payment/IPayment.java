package com.petrolpark.shop.offer.payment;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IPayment {
    
    public void pay(LootContext context, float multiplier);

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics graphics);

    public Component getName();
};
