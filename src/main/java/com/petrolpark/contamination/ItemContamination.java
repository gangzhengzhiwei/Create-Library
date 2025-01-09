package com.petrolpark.contamination;

import java.util.Set;
import java.util.stream.Stream;

import com.petrolpark.PetrolparkTags;

import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemContamination extends Contamination<Item, ItemStack> {

    public static final String TAG_KEY = "Contamination";

    public static ItemContamination get(ItemStack stack) {
        return new ItemContamination(stack);
    };

    protected ItemContamination(ItemStack stack) {
        super(stack);
        if (stack.getTag() != null && stack.getTag().contains(TAG_KEY, Tag.TAG_LIST)) extrinsicContaminants.addAll(stack.getTag().getList(TAG_KEY, Tag.TAG_STRING).stream().map(Tag::getAsString).map(ResourceLocation::new).map(Contaminant::get).toList());
    };

    public Stream<Contaminant> streamShownContaminants() {
        Set<Contaminant> shownIfAbsent = IntrinsicContaminants.getShownIfAbsent(this);
        return streamAllContaminants().dropWhile(PetrolparkTags.Contaminants.HIDDEN::matches).dropWhile(shownIfAbsent::contains);
    };

    public Stream<Contaminant> streamShownAbsentContaminants() {
        return IntrinsicContaminants.getShownIfAbsent(this).stream().dropWhile(this::has).dropWhile(PetrolparkTags.Contaminants.HIDDEN::matches);
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
        if (!extrinsicContaminants.isEmpty()) stack.getOrCreateTag().put(TAG_KEY, writeToNBT());
    };
    
};
