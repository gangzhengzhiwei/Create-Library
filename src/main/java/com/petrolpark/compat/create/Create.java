package com.petrolpark.compat.create;

import com.petrolpark.RequiresCreate;
import com.petrolpark.compat.create.block.entity.behaviour.AbstractRememberPlacerBehaviour;
import com.petrolpark.compat.create.event.CreateCommonEvents;
import com.petrolpark.compat.create.loot.CreateGlobalLootModifierSerializers;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@RequiresCreate
public class Create {
  
    public static void ctor(IEventBus modEventBus, IEventBus forgeEventBus) {

        // Registrations
        CreateBlockEntityTypes.register();
        CreateBlocks.register();
        CreateGlobalLootModifierSerializers.register();

        // Event Bus Subscribers
        modEventBus.addListener(Create::init);
        forgeEventBus.register(CreateCommonEvents.class);
        forgeEventBus.register(AbstractRememberPlacerBehaviour.class);

        // Client
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CreateClient.clientCtor(modEventBus, forgeEventBus));
    };

    private static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CreateMessages.register();
            PetrolparkItemAttributes.register();
        });
    };
};
