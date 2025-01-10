package com.petrolpark.dough;

import com.petrolpark.directionalitem.DirectionalTransportedItemStack;
import com.petrolpark.directionalitem.IDirectionalOnBelt;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;

import net.minecraft.world.item.Item;

public class DoughBallItem extends Item implements IDirectionalOnBelt {

    public DoughBallItem(Properties properties) {
        super(properties);
    };

    @Override
    public DirectionalTransportedItemStack makeDirectionalTransportedItemStack(TransportedItemStack transportedItemStack) {
        return IDirectionalOnBelt.super.makeDirectionalTransportedItemStack(transportedItemStack);
    };
    
};
