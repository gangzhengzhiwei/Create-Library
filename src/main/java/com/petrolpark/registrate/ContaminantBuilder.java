package com.petrolpark.registrate;

import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.contamination.Contaminant;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonnullType;

public class ContaminantBuilder<P> extends AbstractBuilder<Contaminant, Contaminant, P, ContaminantBuilder<P>> {

    protected double preservationProportion = 0.5d;
    protected int color = 5635925;
    protected int absentColor = 16733525;

    public ContaminantBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback) {
        super(owner, parent, name, callback, PetrolparkRegistries.Keys.CONTAMINANT);
    };

    public ContaminantBuilder<P> preservationProportion(double preservationProportion) {
        this.preservationProportion = preservationProportion;
        return this;
    };

    public ContaminantBuilder<P> color(int color) {
        this.color = color;
        return this;
    };

    public ContaminantBuilder<P> absentColor(int absentColor) {
        this.absentColor = absentColor;
        return this;
    };

    @Override
    protected @NonnullType Contaminant createEntry() {
        return new Contaminant(preservationProportion, color, absentColor);
    };
    
};
