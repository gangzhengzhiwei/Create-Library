package com.petrolpark.network;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired on the {@link MinecraftForge#EVENT_BUS} when Petrolpark packets are registered.
 * Do so with {@link PetrolparkMessages#addC2SPacket}.
 * Not cancellable.
 */
public class RegisterPetrolparkMessagesEvent extends Event {
    
    @Override
    public boolean isCancelable() {
        return false;
    };

    @Override
    public boolean hasResult() {
        return false;
    };
};
