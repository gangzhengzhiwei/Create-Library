package com.petrolpark.dough;

import com.petrolpark.directionalitem.DirectionalTransportedItemStack;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;

import net.minecraft.world.item.ItemStack;

public class DoughTransportedItemStack extends DirectionalTransportedItemStack {

    public final DoughBall doughBall;

    public DoughTransportedItemStack(ItemStack stack) {
        this(stack, DoughBall.get(stack));
    };

    protected DoughTransportedItemStack(ItemStack stack, DoughBall doughBall) {
        super(stack);
        this.doughBall = doughBall;
    };

    @Override
    public TransportedItemStack copy() {
        return copy(this, s -> new DoughTransportedItemStack(s, doughBall));
    };
    
};
