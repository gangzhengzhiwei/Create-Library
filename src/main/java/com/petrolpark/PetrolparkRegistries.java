package com.petrolpark;

import static com.petrolpark.Petrolpark.REGISTRATE;

import com.petrolpark.badge.Badge;
import com.petrolpark.contamination.Contaminant;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.IForgeRegistry.ValidateCallback;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PetrolparkRegistries {

    public static <OBJECT> ForgeRegistry<OBJECT> getRegistry(ResourceKey<Registry<OBJECT>> key) {
        return RegistryManager.ACTIVE.getRegistry(key);
    };

    public static <OBJECT> Registry<OBJECT> getDataRegistry(ResourceKey<Registry<OBJECT>> key) {
        return ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(key);
    };
    
    public static class Keys {
        public static final ResourceKey<Registry<Badge>> BADGE = REGISTRATE.makeRegistry("badge", RegistryBuilder::new);
        public static final ResourceKey<Registry<Contaminant>> CONTAMINANT = REGISTRATE.makeRegistry("contaminant", () -> new RegistryBuilder<Contaminant>().onValidate(thingy)) ;
    };

    public static final void register() {};

    public static ValidateCallback<Contaminant> thingy = new ValidateCallback<Contaminant>() {

        @Override
        public void onValidate(IForgeRegistryInternal<Contaminant> owner, RegistryManager stage, int id, ResourceLocation key, Contaminant obj) {
            Petrolpark.LOGGER.info("maybe this one will fire.");
        }

        
        
    };
};
