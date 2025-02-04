package com.petrolpark.shop.offer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.network.GsonSerializableCodecs;
import com.petrolpark.shop.offer.order.ShopOrderModifier;

import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public record ShopOrderModifierEntry(ShopOrderModifier orderModifier, NumberProvider chance, boolean hidden) {

    public static final Codec<ShopOrderModifierEntry> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            ShopOrderModifier.CODEC.fieldOf("modifier").forGetter(ShopOrderModifierEntry::orderModifier),
            GsonSerializableCodecs.NUMBER_PROVIDER.optionalFieldOf("chance", ConstantValue.exactly(1f)).forGetter(ShopOrderModifierEntry::chance),
            Codec.BOOL.optionalFieldOf("hidden", false).forGetter(ShopOrderModifierEntry::hidden)
        ).apply(instance, ShopOrderModifierEntry::new)
    );
};
