package com.petrolpark.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.petrolpark.block.entity.behaviour.TubeBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.minecraft.client.renderer.MultiBufferSource;

public class TubeBlockEntityRenderer extends SafeBlockEntityRenderer<SmartBlockEntity> {

    @Override
    protected void renderSafe(SmartBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        TubeBehaviour tube = be.getBehaviour(TubeBehaviour.TYPE);
        if (tube == null) return;
    };
    
};
