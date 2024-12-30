package com.petrolpark.block.renderer;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.petrolpark.block.entity.behaviour.TubeBehaviour;
import com.petrolpark.block.partial.PetrolparkPartialModels;
import com.petrolpark.util.MathsHelper;
import com.petrolpark.util.TubeSpline;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

public class TubeBlockEntityRenderer extends SafeBlockEntityRenderer<SmartBlockEntity> {

    public TubeBlockEntityRenderer(BlockEntityRendererProvider.Context context) {};

    @Override
    protected void renderSafe(SmartBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        TubeBehaviour tube = be.getBehaviour(TubeBehaviour.TYPE);
        if (tube == null || !tube.isController()) return;
        TubeSpline spline = tube.getSpline();
        VertexConsumer vc = bufferSource.getBuffer(RenderType.solid());
        for (int i = 0; i < spline.getPoints().size() - 1; i++) {
            CachedBufferer.partial(getTubeSegmentModel(be), be.getBlockState())
                .translateBack(Vec3.atLowerCornerOf(be.getBlockPos()))
                .translate(spline.getPoints().get(i))
                .rotateXRadians(MathsHelper.inclination(spline.getTangents().get(i)))
                .rotateYRadians(MathsHelper.azimuth(spline.getTangents().get(i)))
                .renderInto(ms, vc);
        };
    };

    public PartialModel getTubeSegmentModel(SmartBlockEntity be) {
        return PetrolparkPartialModels.STAINLESS_STEEL_PIPE;
    };
    
};
