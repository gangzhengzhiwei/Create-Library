package com.petrolpark;

import com.petrolpark.contamination.HasContaminantItemAttribute;
import com.simibubi.create.content.logistics.filter.ItemAttribute;

@RequiresCreate
public class PetrolparkItemAttributes {
    
    public static final ItemAttribute HAS_CONTAMINANT = ItemAttribute.register(new HasContaminantItemAttribute(null));

    public static void register() {};
};
