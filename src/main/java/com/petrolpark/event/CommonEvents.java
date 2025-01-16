package com.petrolpark.event;

import java.util.stream.Stream;

import com.petrolpark.Petrolpark;
import com.petrolpark.PetrolparkConfig;
import com.petrolpark.badge.BadgesCapability;
import com.petrolpark.command.ContaminateCommand;
import com.petrolpark.contamination.Contaminant;
import com.petrolpark.contamination.ItemContamination;
import com.petrolpark.itemdecay.IDecayingItem;
import com.petrolpark.itemdecay.DecayingItemHandler.ServerDecayingItemHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CommonEvents {

    // CORE/REGISTRATION
    
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

    // GAMEPLAY

    /**
     * Preserve Contaminants of Potions, and start decaying newly brewed Potions.
     * @param event
     */
    @SubscribeEvent
    public static void onPotionBrewed(PotionBrewEvent.Post event) {
        for (int slot = 0; slot < 3; slot++) {
            ItemStack potion = event.getItem(slot);
            IDecayingItem.startDecay(potion);
            if (PetrolparkConfig.SERVER.brewingPropagatesContaminants.get()) ItemContamination.perpetuateSingle(Stream.of(event.getItem(3), potion).dropWhile(s -> PetrolparkConfig.SERVER.brewingWaterBottleContaminantsIgnored.get() && PotionUtils.getPotion(s) == Potions.WATER), potion);
        };
    };
    
};
