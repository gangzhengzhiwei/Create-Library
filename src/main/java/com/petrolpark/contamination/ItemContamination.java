package com.petrolpark.contamination;

import java.util.stream.Stream;

import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemContamination extends Contamination<Item, ItemStack> {

    public static final String TAG_KEY = "Contamination";

    public static IContamination<?, ?> get(ItemStack stack) {
        if (!Contaminables.ITEM.isContaminable(stack)) return IncontaminableContamination.INSTANCE;
        return new ItemContamination(stack);
    };

    public static final void perpetuateSingle(Stream<ItemStack> inputs, ItemStack output) {
        perpetuate(inputs.map(stack -> stack.copyWithCount(1)), output);
    };

    public static final void perpetuate(Stream<ItemStack> inputs, ItemStack output) {
        IContamination.perpetuate(inputs.dropWhile(ItemStack::isEmpty), output, ItemContamination::get);
    };

    protected ItemContamination(ItemStack stack) {
        super(stack);
        if (stack.getTag() != null && stack.getTag().contains(TAG_KEY, Tag.TAG_LIST)) orphanContaminants.addAll(stack.getTag().getList(TAG_KEY, Tag.TAG_STRING).stream().map(Tag::getAsString).map(ResourceLocation::new).map(Contaminant::get).toList());
        for (Contaminant contaminant : orphanContaminants) {
            contaminants.add(contaminant);
            contaminants.addAll(contaminant.getChildren());
        };
    };

    @Override
    public Contaminable<Item, ItemStack> getContaminable() {
        return Contaminables.ITEM;
    };

    @Override
    public Item getType() {
        return stack.getItem();
    };

    @Override
    public double getAmount() {
        return stack.getCount();
    };

    @Override
    public void save() {
        stack.removeTagKey(TAG_KEY);
        if (!orphanContaminants.isEmpty()) stack.getOrCreateTag().put(TAG_KEY, writeToNBT());
    };
    
};
