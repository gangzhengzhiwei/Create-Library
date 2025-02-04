package com.petrolpark.shop;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.network.GsonSerializableCodecs;
import com.petrolpark.shop.offer.ShopOrderModifierEntry;
import com.petrolpark.shop.offer.ShopOfferGenerator;

import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class Shop {

    public static final Codec<Shop> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            Codec.list(OfferGeneratorEntry.CODEC).fieldOf("offerGenerators").forGetter(Shop::getOfferGenerators),
            Codec.list(ShopOrderModifierEntry.CODEC).fieldOf("globalOrderModifiers").forGetter(Shop::getGlobalOrderModifiers)
        ).apply(instance, Shop::new)
    );
    
    public final List<OfferGeneratorEntry> offerGenerators;
    protected final List<ShopOrderModifierEntry> globalOrderModifiers;

    public Shop(List<OfferGeneratorEntry> offerGenerators, List<ShopOrderModifierEntry> globalOrderModifiers) {
        this.offerGenerators = offerGenerators;
        this.globalOrderModifiers = globalOrderModifiers;
    };

    public List<OfferGeneratorEntry> getOfferGenerators() {
        return offerGenerators;
    };

    public List<ShopOrderModifierEntry> getGlobalOrderModifiers() {
        return globalOrderModifiers;
    };

    public static record OfferGeneratorEntry(ShopOfferGenerator generator, NumberProvider weight) implements LootContextUser {

        public static final Codec<OfferGeneratorEntry> CODEC = RecordCodecBuilder.create(instance -> 
            instance.group(
                ShopOfferGenerator.CODEC.fieldOf("generator").forGetter(OfferGeneratorEntry::generator),
                GsonSerializableCodecs.NUMBER_PROVIDER.fieldOf("weight").forGetter(OfferGeneratorEntry::weight)
            ).apply(instance, OfferGeneratorEntry::new)
        );

        @Override
        public Set<LootContextParam<?>> getReferencedContextParams() {
            return Sets.union(generator.getReferencedContextParams(), weight.getReferencedContextParams());
        };
    };
};
