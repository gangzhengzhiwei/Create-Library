package com.petrolpark.util;

import java.util.List;
import java.util.ArrayList;

import com.mojang.blaze3d.platform.InputConstants;
import com.petrolpark.block.ITubeBlock;
import com.petrolpark.util.RayHelper.CustomHitResult;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public class ClientTubePlacementHandler {
    
    protected static ItemStack currentStack = ItemStack.EMPTY;
    protected static UseOnContext firstConnection = null;
    protected static BlockPos secondLocation = null;
    protected static ClampedCubicSpline spline = null;

    protected static int targetedControlPoint = -1;
    protected static double distanceToSelectedControlPoint = 0f;
    protected static boolean draggingSelectedControlPoint = false;
    protected static List<AABB> controlPointBoxes = new ArrayList<>();

    protected static boolean blocked = false;

    @SubscribeEvent
    public static void tick(ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || mc.player.getMainHandItem() != currentStack) {
            cancel();
            return;
        };

        if (spline == null) return;

        boolean buildable = !blocked && !spline.tooSharp;
        int color = buildable ? 0xFF_4DE680 : 0xFF_E64D80;

        // Render path of spline
        for (int i = 0; i < spline.getPoints().size() - 1; i++) {
            Vec3 point = spline.getPoints().get(i);
            Vec3 tangent = spline.getTangents().get(i);
            CreateClient.OUTLINER.showLine(Pair.of(point, tangent), point, spline.getPoints().get(i+1)).colored(color);
        };

        // Render blocking Blocks and decide if the Tube is being blocked
        blocked = false; // Start by assuming not blocked
        for (BlockPos pos : spline.getBlockedPositions()) {
            if (pos.equals(firstConnection.getClickedPos()) || pos.equals(secondLocation)) continue;
            if (!mc.level.getBlockState(pos).canBeReplaced()) {
                CreateClient.OUTLINER.chaseAABB(Pair.of("blocking_tube", pos), new AABB(pos)).colored(0xFF_E64D80);
                blocked = true;
            };
        };

        // Locate targeted Control Point, or move it if there already is one
        if (!draggingSelectedControlPoint && !controlPointBoxes.isEmpty()) {
            if (RayHelper.getHitResult(controlPointBoxes, mc.player, AnimationTickHolder.getPartialTicks(), false) instanceof CustomHitResult hr) targetedControlPoint = hr.index; else targetedControlPoint = -1;
        } else {
            if (targetedControlPoint != -1) relocateControlPoint(mc.player); // The check to see its not -1 should be redundant
        };

        if (controlPointBoxes.isEmpty()) controlPointBoxes = spline.getControlPoints().stream().map(v -> new AABB(v.subtract(1 / 32d, 1 / 32d, 1 / 32d), v.add(1 / 32d, 1 / 32d, 1 / 32d))).toList();

        // Render Control Points
        for (int i = 0; i < controlPointBoxes.size(); i++) {
            AABB controlPointBox = controlPointBoxes.get(i);
            CreateClient.OUTLINER.chaseAABB(Pair.of("control_point", i), controlPointBox).colored(color).lineWidth(i == targetedControlPoint && i > 0 && i < controlPointBoxes.size() - 1 ? 2 / 16f : 1.25f / 16f);
        };
    };

    @SubscribeEvent
    public static void onUseMouse(InputEvent.MouseButton event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && spline != null && targetedControlPoint > 0 && targetedControlPoint < spline.getControlPoints().size() - 1 && event.getButton() == InputConstants.MOUSE_BUTTON_RIGHT && draggingSelectedControlPoint == (event.getAction() == InputConstants.RELEASE)) {
            draggingSelectedControlPoint = !draggingSelectedControlPoint;
            distanceToSelectedControlPoint = mc.player.getEyePosition().distanceTo(spline.getControlPoints().get(targetedControlPoint));
            event.setCanceled(true);
        };
    };

    @SubscribeEvent
    public static void onScrollMouse(InputEvent.MouseScrollingEvent event) {
        if (draggingSelectedControlPoint) {
            Minecraft mc = Minecraft.getInstance();
            distanceToSelectedControlPoint = Mth.clamp(distanceToSelectedControlPoint + event.getScrollDelta() / 8d, Math.min(distanceToSelectedControlPoint, 0.5d), Math.max(distanceToSelectedControlPoint, 6d));
            relocateControlPoint(mc.player);
            event.setCanceled(true);
        };
    };

    protected static void relocateControlPoint(LocalPlayer player) {
        if (spline.moveControlPoint(targetedControlPoint, player.getEyePosition().add(player.getViewVector(AnimationTickHolder.getPartialTicks()).scale(distanceToSelectedControlPoint)))) {
            controlPointBoxes = new ArrayList<>(); // All control points need to be moved
        };
    };

    public static void useOn(UseOnContext context, ITubeBlock tubeBlock) {
        if (firstConnection == null) { // If placing the first Block
            currentStack = context.getItemInHand();
            firstConnection = context;
            spline = null;
        } else if (spline == null) { // If placing the second Block
            secondLocation = context.getClickedPos();
            Vec3 startDir = new Vec3(firstConnection.getClickedFace().step());
            Vec3 endDir = new Vec3(context.getClickedFace().getOpposite().step());
            Vec3 startVec = Vec3.atCenterOf(firstConnection.getClickedPos()).add(startDir.scale(0.5d));
            Vec3 endVec = Vec3.atCenterOf(secondLocation).add(endDir.scale(-0.5d));
            //TODO determine if too close to connect
            spline = new ClampedCubicSpline(startVec, endVec, startDir, endDir, tubeBlock.getTubeMaxAngle(), tubeBlock.getTubeSegmentLength(), tubeBlock.getTubeSegmentRadius());
            spline.addControlPoint(startVec.add(endVec).scale(0.5d).add(new Vec3(0d, 1d, 0d)));
        } else {
            cancel(); //TODO remove
        }
    };

    public static void cancel() {
        currentStack = ItemStack.EMPTY;
        firstConnection = null;
        secondLocation = null;
        spline = null;
        controlPointBoxes = new ArrayList<>();
        draggingSelectedControlPoint = false;
    };

};
