package com.petrolpark.mixin.accessor;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.foundation.outliner.Outliner;
import com.simibubi.create.foundation.outliner.Outliner.OutlineEntry;

@Mixin(Outliner.class)
public interface OutlinerAccessor {
    
    @Accessor("outlines")
    public Map<Object, OutlineEntry> getOutlines();

};
