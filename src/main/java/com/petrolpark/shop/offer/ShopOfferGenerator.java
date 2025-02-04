package com.petrolpark.shop.offer;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.data.reward.generator.IRewardGenerator;
import com.petrolpark.recipe.ingredient.randomizer.IngredientRandomizer;
import com.petrolpark.shop.offer.order.ShopOrder;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public class ShopOfferGenerator implements LootContextUser {

    public static final Codec<ShopOfferGenerator> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            IRewardGenerator.CODEC.fieldOf("reward").forGetter(ShopOfferGenerator::getRewardGenerator),
            IngredientRandomizer.CODEC.fieldOf("order").forGetter(ShopOfferGenerator::getOrderRandomizer),
            Codec.list(ShopOrderModifierEntry.CODEC).fieldOf("orderModifiers").forGetter(ShopOfferGenerator::getOrderModifiers)
        ).apply(instance, ShopOfferGenerator::new)
    );

    public final IRewardGenerator rewardGenerator;
    public final IngredientRandomizer orderRandomizer;
    public final List<ShopOrderModifierEntry> orderModifiers;
    
    public ShopOfferGenerator(IRewardGenerator rewardGenerator, IngredientRandomizer orderRandomizer, List<ShopOrderModifierEntry> orderModifiers) {
        this.rewardGenerator = rewardGenerator;
        this.orderRandomizer = orderRandomizer;
        this.orderModifiers = orderModifiers;
    };

    @Deprecated
    public ShopOffer generate(LootContext context) {
        return new ShopOffer(rewardGenerator.generate(context), new ShopOrder(orderRandomizer.generate(context), orderModifiers.stream().filter(ome -> ome.chance().getFloat(context) < context.getRandom().nextFloat()).map(ShopOrderModifierEntry::orderModifier).toList()));
    };

    public IRewardGenerator getRewardGenerator() {
        return rewardGenerator;
    };

    public IngredientRandomizer getOrderRandomizer() {
        return orderRandomizer;
    };

    public List<ShopOrderModifierEntry> getOrderModifiers() {
        return orderModifiers;
    };
};
