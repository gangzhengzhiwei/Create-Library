package com.petrolpark.contamination;

import java.util.List;

import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.util.NBTHelper;
import com.simibubi.create.content.logistics.filter.ItemAttribute;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class HasContaminantItemAttribute implements ItemAttribute {

    public final Contaminant contaminant;

    public HasContaminantItemAttribute(Contaminant contaminant) {
        this.contaminant = contaminant;
    };

    @Override
    public boolean appliesTo(ItemStack stack) {
        return ItemContamination.get(stack).has(contaminant);
    };

    @Override
    public List<ItemAttribute> listAttributesOf(ItemStack stack) {
        IContamination<?, ?> contamination = ItemContamination.get(stack);
        List<ItemAttribute> list = contamination.streamAllContaminants().map(HasContaminantItemAttribute::new).map(ItemAttribute.class::cast).toList();
        IntrinsicContaminants.getShownIfAbsent(contamination).forEach(c -> {if (!contamination.has(contaminant)) list.add(new HasContaminantItemAttribute(c));});
        return list;
    };

    @Override
    public String getTranslationKey() {
        return "has_contaminant";
    };

    @Override
    public Object[] getTranslationParameters() {
        return new Object[]{contaminant.getName()};
    };

    @Override
    public void writeNBT(CompoundTag nbt) {
        NBTHelper.writeRegistryObject(nbt, "Contaminant", PetrolparkRegistries.Keys.CONTAMINANT, contaminant);
    };

    @Override
    public ItemAttribute readNBT(CompoundTag nbt) {
        return new HasContaminantItemAttribute(NBTHelper.readRegistryObject(nbt, "Contaminant", PetrolparkRegistries.Keys.CONTAMINANT));
    };
    
};
