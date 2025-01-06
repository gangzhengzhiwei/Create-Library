package com.petrolpark;

import com.petrolpark.block.partial.PetrolparkPartialModels;
import com.petrolpark.client.outline.Outliner;
import com.petrolpark.itemdecay.DecayingItemHandler.ClientDecayingItemHandler;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class PetrolparkClient {

    public static final Outliner OUTLINER = new Outliner();
    
    public static void clientInit(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> { // Work which must be done on main thread
            Petrolpark.DECAYING_ITEM_HANDLER.set(new ClientDecayingItemHandler());
        });
    };

    public static void clientCtor(IEventBus modEventBus, IEventBus forgeEventBus) {
        PetrolparkPartialModels.init();
    };
};
