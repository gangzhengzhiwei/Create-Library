package com.petrolpark.event;

import com.petrolpark.command.ContaminateCommand;
import com.petrolpark.contamination.Contaminant;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ContaminateCommand.register(event.getDispatcher(), event.getBuildContext());
    };

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new Contaminant.ReloadListener(event.getRegistryAccess()));
    };
    
};
