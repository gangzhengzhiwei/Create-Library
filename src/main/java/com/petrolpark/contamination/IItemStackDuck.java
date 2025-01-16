package com.petrolpark.contamination;

public interface IItemStackDuck {

    public IContamination<?, ?> getContamination();
    
    public void onContaminationSaved();
};
