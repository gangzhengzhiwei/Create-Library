package com.petrolpark.compat.create.event;

import com.petrolpark.Petrolpark;
import com.petrolpark.network.PetrolparkMessages;
import com.petrolpark.network.RegisterPetrolparkMessagesEvent;
import com.petrolpark.recipe.advancedprocessing.firsttimelucky.FirstTimeLuckyRecipesCapability;
import com.petrolpark.tube.BuildTubePacket;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CreateCommonEvents {

    @SubscribeEvent
    public static void onRegisterPetrolparkMessages(RegisterPetrolparkMessagesEvent event) {
        PetrolparkMessages.addC2SPacket(BuildTubePacket.class, BuildTubePacket::new);
    };
    
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            // Add Lucky first recipe Capability
            if (!player.getCapability(FirstTimeLuckyRecipesCapability.Provider.PLAYER_LUCKY_FIRST_RECIPES).isPresent()) {
                event.addCapability(Petrolpark.asResource("lucky_first_recipes"), new FirstTimeLuckyRecipesCapability.Provider());
            };
        };
    };

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // Copy Lucky First Recipe data
            event.getOriginal().getCapability(FirstTimeLuckyRecipesCapability.Provider.PLAYER_LUCKY_FIRST_RECIPES).ifPresent(oldStore -> {
                event.getEntity().getCapability(FirstTimeLuckyRecipesCapability.Provider.PLAYER_LUCKY_FIRST_RECIPES).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        };
    };
};
