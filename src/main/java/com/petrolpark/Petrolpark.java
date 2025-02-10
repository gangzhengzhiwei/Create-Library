package com.petrolpark;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.petrolpark.badge.Badges;
import com.petrolpark.compat.CompatMods;
import com.petrolpark.compat.create.Create;
import com.petrolpark.compat.curios.Curios;
import com.petrolpark.compat.jei.category.ITickableCategory;
import com.petrolpark.data.loot.PetrolparkGlobalLootModifierSerializers;
import com.petrolpark.data.loot.PetrolparkLootConditionTypes;
import com.petrolpark.data.loot.PetrolparkLootEntityNumberProviderTypes;
import com.petrolpark.data.loot.PetrolparkLootItemStackNumberProviderTypes;
import com.petrolpark.data.loot.PetrolparkLootNumberProviderTypes;
import com.petrolpark.data.loot.PetrolparkLootTeamNumberProviders;
import com.petrolpark.data.reward.RewardGeneratorTypes;
import com.petrolpark.data.reward.RewardTypes;
import com.petrolpark.itemdecay.DecayingItemHandler;
import com.petrolpark.mobeffects.PetrolparkMobEffects;
import com.petrolpark.network.PetrolparkMessages;
import com.petrolpark.recipe.IPetrolparkRecipeTypes;
import com.petrolpark.recipe.ingredient.modifier.IngredientModifierTypes;
import com.petrolpark.recipe.ingredient.randomizer.IngredientRandomizerTypes;
import com.petrolpark.registrate.PetrolparkRegistrate;
import com.petrolpark.shop.ShopMenuItem;
import com.petrolpark.team.TeamTypes;
import com.petrolpark.team.data.TeamDataTypes;
import com.petrolpark.team.scoreboard.ScoreboardTeamManager;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Petrolpark.MOD_ID)
public class Petrolpark {

    public static final String MOD_ID = "petrolpark";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final PetrolparkRegistrate REGISTRATE = new PetrolparkRegistrate(MOD_ID);
    public static final PetrolparkRegistrate DESTROY_REGISTRATE = CompatMods.DESTROY.registrate();

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    };

    public static final ThreadLocal<DecayingItemHandler> DECAYING_ITEM_HANDLER = ThreadLocal.withInitial(() -> DecayingItemHandler.DUMMY);
    public static final ScoreboardTeamManager SCOREBOARD_TEAMS = new ScoreboardTeamManager();

    static {
        PetrolparkItemDisplayContexts.register();
    };

    public Petrolpark() {
        //ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        REGISTRATE.registerEventListeners(modEventBus);
        DESTROY_REGISTRATE.registerEventListeners(modEventBus);

        // Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PetrolparkConfig.serverSpec);

        // Registration
        PetrolparkRegistries.register();
        Badges.register();
        IPetrolparkRecipeTypes.register(modEventBus);
        PetrolparkMobEffects.register();
        TeamTypes.register();
        TeamDataTypes.register();
        // Registration - loot
        PetrolparkLootConditionTypes.register();
        PetrolparkLootNumberProviderTypes.register();
        PetrolparkLootItemStackNumberProviderTypes.register();
        PetrolparkLootEntityNumberProviderTypes.register();
        PetrolparkLootTeamNumberProviders.register();
        PetrolparkGlobalLootModifierSerializers.register();
        RewardGeneratorTypes.register();
        RewardTypes.register();
        IngredientModifierTypes.register();
        IngredientRandomizerTypes.register();

        // Client
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PetrolparkClient.clientCtor(modEventBus, forgeEventBus));

        // Register ourselves for server and other game events we are interested in
        forgeEventBus.register(this);
    
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::init);

        // Compat
        if (CompatMods.JEI.isLoading()) forgeEventBus.register(ITickableCategory.ClientEvents.class);
        CompatMods.CREATE.executeIfInstalled(() -> () -> Create.ctor(modEventBus, forgeEventBus));
        CompatMods.CURIOS.executeIfInstalled(() -> () -> Curios.ctor(modEventBus, forgeEventBus));
    };

    private void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PetrolparkMessages.register();
        });
    };

    public static final ItemEntry<ShopMenuItem> MENU = REGISTRATE.item("menu", ShopMenuItem::new).register();

};
