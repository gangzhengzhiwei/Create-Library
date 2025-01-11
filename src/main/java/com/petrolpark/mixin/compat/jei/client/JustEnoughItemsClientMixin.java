package com.petrolpark.mixin.compat.jei.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.petrolpark.compat.jei.PetrolparkJEI;

import mezz.jei.api.IModPlugin;
import mezz.jei.common.config.IServerConfig;
import mezz.jei.common.network.ClientPacketRouter;
import mezz.jei.forge.JustEnoughItemsClient;
import mezz.jei.forge.events.PermanentEventSubscriptions;
import mezz.jei.forge.network.ConnectionToServer;
import mezz.jei.forge.network.NetworkHandler;
import mezz.jei.gui.config.InternalKeyMappings;

@Mixin(JustEnoughItemsClient.class)
public class JustEnoughItemsClientMixin {
    
    @Inject(
        method = "<init>",
        at = @At("RETURN"),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void inInit(NetworkHandler networkHandler, PermanentEventSubscriptions subscriptions, IServerConfig serverConfig, CallbackInfo ci, ConnectionToServer serverConnection, InternalKeyMappings keyMappings, ClientPacketRouter packetRouter, List<IModPlugin> plugins) {
        plugins.add(new PetrolparkJEI());
    };
};
