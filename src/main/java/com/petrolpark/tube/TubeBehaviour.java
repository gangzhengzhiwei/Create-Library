package com.petrolpark.tube;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Collections;

import com.petrolpark.PetrolparkBlockEntityTypes;
import com.petrolpark.PetrolparkBlocks;
import com.petrolpark.util.BlockFace;
import com.petrolpark.util.NBTHelper;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TubeBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<TubeBehaviour> TYPE = new BehaviourType<>();

    protected boolean controller = false;
    protected int initializationTicks = 0;
    private List<Vec3> middleControlPoints = new ArrayList<>(); // Used when reading from save while Level is unavailable only. Use getSpline().getMiddleControlPoints() instead
    protected TubeSpline spline;
    protected BlockPos otherEndPos;
    protected Runnable renderBoxInvalidator;

    protected boolean disconnecting = false;

    public TubeBehaviour(SmartBlockEntity be, Runnable renderBoxInvalidator) {
        super(be);
        this.renderBoxInvalidator = renderBoxInvalidator;
    };

    public boolean isController() {
        return controller;
    };

    public TubeSpline getSpline() {
        if (spline == null && controller) {
            if (!(blockEntity.getBlockState().getBlock() instanceof ITubeBlock tubeBlock)) return null;
            BlockState endState = getWorld().getBlockState(otherEndPos);
            if (endState.getBlock() != tubeBlock) return null;
            spline = new TubeSpline(BlockFace.of(getPos(), tubeBlock.getTubeConnectingFace(getWorld(), getPos(), getWorld().getBlockState(getPos()))), BlockFace.of(otherEndPos, tubeBlock.getTubeConnectingFace(getWorld(), otherEndPos, endState)), middleControlPoints, tubeBlock.getTubeMaxAngle(), tubeBlock.getTubeSegmentLength(), tubeBlock.getTubeSegmentRadius());
            renderBoxInvalidator.run();
        };
        return spline;
    };

    public BlockPos getOtherEndPos() {
        return otherEndPos;
    };

    public void connect(TubeSpline spline) {
        if (!spline.start.getPos().equals(getPos())) throw new IllegalStateException("Mismatch in tube spline start and controller position.");
        controller = true;
        disconnecting = false;
        otherEndPos = spline.end.getPos();
        this.spline = spline;
        middleControlPoints = getSpline().getMiddleControlPoints();
        for (BlockPos pos : getSpline().getBlockedPositions()) {
            getWorld().setBlock(pos, PetrolparkBlocks.TUBE_STRUCTURE.getDefaultState(), 3);
        };
        initializationTicks = 3; // Delay to link structural blocks to the controller
        get(getWorld(), otherEndPos).ifPresent(tube -> {
            tube.otherEndPos = getPos();
            tube.disconnecting = false;
        });
        blockEntity.notifyUpdate();
        renderBoxInvalidator.run();
    };

    public void disconnect() {
        if (disconnecting) return;
        disconnecting = true;
        if (controller) {
            if (getSpline() != null) for (BlockPos pos : spline.getBlockedPositions()) {
                getWorld().destroyBlock(pos, true);
            };
            controller = false;
            get(getWorld(), otherEndPos).ifPresent(tube -> tube.otherEndPos = null);
            otherEndPos = null;
            spline = null;
            middleControlPoints = Collections.emptyList();
            blockEntity.notifyUpdate();
            renderBoxInvalidator.run();
        } else { // If the other block is the controller, disconnect that
            get(getWorld(), otherEndPos).ifPresent(TubeBehaviour::disconnect);
        };
    };

    public static Optional<TubeBehaviour> get(Level level, BlockPos pos) {
        if (pos == null) return Optional.empty();
        return Optional.ofNullable(BlockEntityBehaviour.get(level, pos, TYPE));
    };

    @Override
    public void tick() {
        super.tick();
        if (initializationTicks > 0) {
            initializationTicks--;
            if (controller && initializationTicks == 1 && getSpline() != null) for (BlockPos pos : getSpline().getBlockedPositions()) {
                getWorld().getBlockEntity(pos, PetrolparkBlockEntityTypes.TUBE_STRUCTURE.get()).ifPresent(be -> be.setController(getPos()));
            };
        };
    };

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        controller = false; // Start by assuming not the controller
        if (clientPacket) { // Reset when client recieves the packet in case the tube has been destroyed
            middleControlPoints = Collections.emptyList();
            spline = null;
        };
        if (nbt.contains("OtherEndPos", Tag.TAG_COMPOUND)) otherEndPos = getPos().offset(NbtUtils.readBlockPos(nbt.getCompound("OtherEndPos")));
        if (!nbt.contains("Points", Tag.TAG_LIST)) return;
        controller = true;
        initializationTicks = nbt.getInt("InitializationTicks");
        middleControlPoints = nbt.getList("Points", Tag.TAG_LIST).stream().map(t -> NBTHelper.readVec3((ListTag)t, getPos())).toList();
    };

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        if (otherEndPos != null) nbt.put("OtherEndPos", NbtUtils.writeBlockPos(otherEndPos.subtract(getPos())));
        if (!controller || getSpline() == null) return;
        if (initializationTicks > 0) nbt.putInt("InitializationTicks", initializationTicks);
        ListTag pointsTag = new ListTag();
        getSpline().getMiddleControlPoints().forEach(p -> pointsTag.add(NBTHelper.writeVec3(p, getPos())));
        nbt.put("Points", pointsTag);
    };

    @Override
    public BehaviourType<TubeBehaviour> getType() {
        return TYPE;
    };
    
};
