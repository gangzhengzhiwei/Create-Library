package com.petrolpark.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.foundation.outliner.Outliner.OutlineEntry;

@Mixin(OutlineEntry.class)
public interface OutlineEntryAccessor {
    
    @Accessor("ticksTillRemoval")
    public void setTicksTillRemoval(int ticksTillRemoval);
};
