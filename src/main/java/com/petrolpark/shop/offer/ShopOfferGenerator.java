package com.petrolpark.shop.offer;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.network.GsonSerializableCodecs;
import com.petrolpark.shop.offer.order.ShopOrderModifier;
import com.petrolpark.shop.offer.payment.generator.IPaymentGenerator;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class ShopOfferGenerator implements LootContextUser {

    public static final Codec<ShopOfferGenerator> CODEC = null;

    public final IPaymentGenerator<?> paymentGenerator;
    public final List<OrderModifierEntry> orderModifiers;
    
    public List<ShopOffer> generate(LootContext context) {

    };

    public static record OrderModifierEntry(ShopOrderModifier orderModifier, NumberProvider chance) {

        public static final Codec<OrderModifierEntry> CODEC = RecordCodecBuilder.create(instance -> 
            instance.group(
                ShopOrderModifier.CODEC.fieldOf("modifier").forGetter(OrderModifierEntry::orderModifier),
                GsonSerializableCodecs.NUMBER_PROVIDER.fieldOf("chance").forGetter(OrderModifierEntry::chance)
            ).apply(instance, OrderModifierEntry::new)
        );
    };
};
