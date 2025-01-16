package com.petrolpark.compat.create.loot;

import org.jetbrains.annotations.NotNull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.PetrolparkTags;
import com.petrolpark.compat.create.block.entity.behaviour.ContaminationBehaviour;
import com.petrolpark.contamination.ItemContamination;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class ContaminatedKineticBlockLootModifier extends LootModifier {

    public static final Codec<ContaminatedKineticBlockLootModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, ContaminatedKineticBlockLootModifier::new));

    public ContaminatedKineticBlockLootModifier() {
        this(new LootItemCondition[]{});
    };

    protected ContaminatedKineticBlockLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    };

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    };

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        BlockEntity be = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
        if (be == null || !(be instanceof KineticBlockEntity kbe && PetrolparkTags.BlockEntityTypes.CONTAMINABLE_KINETIC.matches(kbe))) return generatedLoot;
        ContaminationBehaviour behaviour = kbe.getBehaviour(ContaminationBehaviour.TYPE);
        if (behaviour == null) return generatedLoot;
        generatedLoot.stream().filter(stack -> stack.getItem() == kbe.getBlockState().getBlock().asItem()).map(ItemContamination::get).forEach(c -> c.contaminateAll(behaviour.getContamination().streamAllContaminants()));
        return generatedLoot;
    };
    
};
