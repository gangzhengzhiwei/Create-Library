package com.petrolpark.contamination;

import java.util.Set;

public abstract class Contaminable<OBJECT, OBJECT_STACK> {
  
    public abstract Contamination<OBJECT, OBJECT_STACK> getContamination(Object stack);

    public abstract Set<Contaminant> getIntrinsicContaminants(OBJECT object);

    public abstract Set<Contaminant> getShownIfAbsentContaminants(OBJECT object);
};
