package com.petrolpark.contamination;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.network.GsonSerializableCodecs;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class ContaminateGlobalLootModifier extends LootModifier {

    public static final Codec<ContaminateGlobalLootModifier> CODEC = RecordCodecBuilder.create(instance -> 
        codecStart(instance)
        .and(ResourceLocation.CODEC.fieldOf("contaminant").forGetter(cglm -> cglm.contaminantLocation))
        .and(GsonSerializableCodecs.NUMBER_PROVIDER.fieldOf("chance").forGetter(ContaminateGlobalLootModifier::getChanceProvider))
        .apply(instance, ContaminateGlobalLootModifier::new)
    );

    private final ResourceLocation contaminantLocation;
    protected Contaminant contaminant;
    protected final NumberProvider chanceProvider;

    protected ContaminateGlobalLootModifier(LootItemCondition[] conditionsIn, ResourceLocation contaminantLocation, NumberProvider chanceProvider) {
        super(conditionsIn);
        this.contaminantLocation = contaminantLocation;
        this.chanceProvider = chanceProvider;
    };

    public NumberProvider getChanceProvider() {
        return chanceProvider;
    };

    public Contaminant getContaminant() {
        if (contaminant == null) contaminant = Contaminant.get(contaminantLocation);
        return contaminant;
    };

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    };

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (getContaminant() == null) throw new JsonSyntaxException("Unknown Contaminant in contaminate Global Loot Modifier: "+contaminantLocation.toString());
        float chance = chanceProvider.getFloat(context);
        if (chance <= 0f) return generatedLoot;
        for (ItemStack stack : generatedLoot) {
            if (context.getRandom().nextFloat() > chance) continue;
            ItemContamination.get(stack).contaminate(getContaminant());
        };
        return generatedLoot;
    };
    
};
