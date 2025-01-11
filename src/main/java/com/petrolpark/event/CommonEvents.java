package com.petrolpark.event;

import com.petrolpark.Petrolpark;
import com.petrolpark.badge.BadgesCapability;
import com.petrolpark.command.ContaminateCommand;
import com.petrolpark.contamination.Contaminant;
import com.petrolpark.itemdecay.DecayingItemHandler.ServerDecayingItemHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CommonEvents {
    
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ContaminateCommand.register(event.getDispatcher(), event.getBuildContext());
    };

    @SubscribeEvent
    public static void onTick(LevelTickEvent event) {
        // Decaying Items
        if (event.phase == LevelTickEvent.Phase.END && !event.level.isClientSide() && event.level.getServer().overworld() == event.level) ((ServerDecayingItemHandler)Petrolpark.DECAYING_ITEM_HANDLER.get()).gameTime++;
    };

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new Contaminant.ReloadListener(event.getRegistryAccess()));
    };

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            // Add Badge Capability
            if (!player.getCapability(BadgesCapability.Provider.PLAYER_BADGES).isPresent()) {
                event.addCapability(Petrolpark.asResource("badges"), new BadgesCapability.Provider());
            };
        };
    };

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // Copy Badge data
            event.getOriginal().getCapability(BadgesCapability.Provider.PLAYER_BADGES).ifPresent(oldStore -> {
                event.getEntity().getCapability(BadgesCapability.Provider.PLAYER_BADGES).ifPresent(newStore -> {
                    newStore.setBadges(oldStore.getBadges());
                });
            });
        };
    };
    
};
