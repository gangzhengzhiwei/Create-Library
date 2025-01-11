package com.petrolpark.block.entity;

import java.util.stream.Stream;

import com.petrolpark.contamination.Contaminant;
import com.petrolpark.contamination.GenericContamination;

public interface IShulkerBoxBlockEntityDuck {
    
    public GenericContamination getContamination();

    public void contaminateAll(Stream<Contaminant> contaminants);
};
