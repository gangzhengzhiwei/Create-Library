package com.petrolpark;

import com.petrolpark.contamination.Contaminant;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;

public class PetrolparkTags {
    
    public enum MenuTypes {

        ALWAYS_SHOWS_EXTENDED_INVENTORY,
        NEVER_SHOWS_EXTENDED_INVENTORY,
        ALLOWS_MANUAL_ONLY_CRAFTING,
        ;

        public final TagKey<MenuType<?>> tag;

        MenuTypes() {
            tag = TagKey.create(Registries.MENU, Petrolpark.asResource(Lang.asId(name())));
        };

        public boolean matches(AbstractContainerMenu menu) {
            try {
                return matches(menu.getType());
            } catch (UnsupportedOperationException e) {
                return false;
            }
        };

        public boolean matches(MenuType<?> menuType) {
            return ForgeRegistries.MENU_TYPES.getHolder(menuType).orElseThrow().is(tag);
        };
    };

    public enum Contaminants {

        HIDDEN,
        ;

        public final TagKey<Contaminant> tag;

        Contaminants() {
            tag = TagKey.create(PetrolparkRegistries.Keys.CONTAMINANT, Petrolpark.asResource(Lang.asId(name())));
        };

        public boolean matches(Contaminant contaminant) {
            return PetrolparkRegistries.getDataRegistry(PetrolparkRegistries.Keys.CONTAMINANT).getHolder(PetrolparkRegistries.getDataRegistry(PetrolparkRegistries.Keys.CONTAMINANT).getId(contaminant)).orElseThrow().is(tag);
        };
    };
};
