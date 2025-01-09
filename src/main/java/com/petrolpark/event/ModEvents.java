package com.petrolpark.event;

import com.petrolpark.Petrolpark;
import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.contamination.Contaminant;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Petrolpark.MOD_ID)
public class ModEvents {
    
    @SubscribeEvent
    public static void addDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(PetrolparkRegistries.Keys.CONTAMINANT, Contaminant.CODEC, Contaminant.CODEC);
    };
};
