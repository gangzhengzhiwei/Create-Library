package com.petrolpark.shop;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.network.GsonSerializableCodecs;
import com.petrolpark.shop.offer.ShopOfferGenerator;

import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class Shop {
    
    public final List<OfferGeneratorEntry> offerGenerators;

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
