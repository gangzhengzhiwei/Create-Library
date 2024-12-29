package com.petrolpark.util;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import org.joml.Random;
import org.joml.Vector3f;

import com.petrolpark.block.ITubeBlock;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.utility.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public class ClientTubePlacementHandler {

    protected static Random r = new Random();
    
    protected static ItemStack currentStack = ItemStack.EMPTY;
    protected static UseOnContext firstConnection = null;
    protected static BlockPos secondLocation = null;
    protected static ClampedCubicSpline spline = null;

    protected static boolean blocked;

    @SubscribeEvent
    public static void tick(ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || mc.player.getMainHandItem() != currentStack) {
            cancel();
            return;
        };

        if (spline == null) return;

        boolean buildable = !blocked && !spline.tooSharp;
        Vector3f color = new Vector3f(buildable ? 0.3f : 0.9f, buildable ? 0.9f : 0.3f, 0.5f);

        for (Vec3 point : spline.getPoints()) {
            if (r.nextInt(10) == 0) mc.level.addParticle(new DustParticleOptions(color, 1), point.x, point.y, point.z, 0, 0, 0);
        };
        blocked = false; // Start by assuming not blocked
        for (BlockPos pos : spline.getBlockedPositions()) {
            if (pos.equals(firstConnection.getClickedPos()) || pos.equals(secondLocation)) continue;
            if (!mc.level.getBlockState(pos).canBeReplaced()) {
                CreateClient.OUTLINER.chaseAABB(Pair.of("blocking_tube", pos), new AABB(pos)).colored(0xFF_ff5d6c);
                blocked = true;
            };
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
            Vec3 endDir = new Vec3(context.getClickedFace().step());
            Vec3 startVec = Vec3.atCenterOf(firstConnection.getClickedPos()).add(startDir.scale(0.5d));
            Vec3 endVec = Vec3.atCenterOf(secondLocation).add(endDir.scale(0.5d));
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
    };

};
