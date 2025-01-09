package com.petrolpark.event;

import com.petrolpark.command.ContaminateCommand;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ContaminateCommand.register(event.getDispatcher(), event.getBuildContext());
    };
    
};
