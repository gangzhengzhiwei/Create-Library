package com.petrolpark.sharedfeatures;

public enum SharedFeatures {

    AGEING_BARREL,
    EXTRUSION_DIE,
    MESH
    ;
    
    private boolean enabled = false;

    public void enable() {
        enabled = true;
    };

    public boolean enabled() {
        return enabled;
    };
};
