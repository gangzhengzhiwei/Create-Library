package com.petrolpark.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.foundation.outliner.Outline.OutlineParams;

@Mixin(OutlineParams.class)
public interface OutlineParamsAccessor {
    
    @Accessor("lightmap")
    public int getLightmap();

    @Accessor("disableLineNormals")
    public boolean getDisableLineNormals();
};
