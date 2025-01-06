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
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TubeBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<TubeBehaviour> TYPE = new BehaviourType<>();

    protected final ITubeBlockEntity tubeBlockEntity;

    protected boolean controller = false;
    protected int initializationTicks = 0;
    private List<Vec3> middleControlPoints = new ArrayList<>(); // Used when reading from save while Level is unavailable only. Use getSpline().getMiddleControlPoints() instead
    protected TubeSpline spline = null;
    protected BlockPos otherEndPos = null;

    protected boolean disconnecting = false;

    public <BE extends SmartBlockEntity & ITubeBlockEntity> TubeBehaviour(BE be) {
        super(be);
        tubeBlockEntity = be;
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
            tubeBlockEntity.invalidateTubeRenderBoundingBox();
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
        playSound(false);
        tubeBlockEntity.afterTubeConnect();
        blockEntity.notifyUpdate();
    };

    public void disconnect() {
        if (disconnecting) return;
        disconnecting = true;
        if (controller) {
            tubeBlockEntity.beforeTubeDisconnect();
            get(getWorld(), otherEndPos).ifPresent(tube -> {
                tube.tubeBlockEntity.beforeTubeDisconnect();
                tube.otherEndPos = null;
                tube.blockEntity.notifyUpdate();
            });
            if (getSpline() != null) for (BlockPos pos : getSpline().getBlockedPositions()) {
                getWorld().destroyBlock(pos, true);
            };
            sendDestroyTubeParticles();
            playSound(true);
            controller = false;
            otherEndPos = null;
            spline = null;
            middleControlPoints = Collections.emptyList();
            blockEntity.notifyUpdate();
        } else { // If the other block is the controller, disconnect that
            get(getWorld(), otherEndPos).ifPresent(TubeBehaviour::disconnect);
        };
    };

    public void sendDestroyTubeParticles() {
        if (!(getWorld() instanceof ServerLevel level)) return;
        BlockParticleOption data = new BlockParticleOption(ParticleTypes.BLOCK, blockEntity.getBlockState());
        for (Vec3 point : spline.getPoints()) level.sendParticles(data, point.x, point.y, point.z, 1, 0, 0, 0, 0);
    };

    public void playSound(boolean destroy) {
        BlockState state = blockEntity.getBlockState();
        SoundType soundType = state.getSoundType();
        getWorld().playSound(null, getPos(), destroy ? soundType.getBreakSound() : soundType.getPlaceSound(), SoundSource.BLOCKS, soundType.getVolume(), soundType.getPitch());
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
            tubeBlockEntity.invalidateTubeRenderBoundingBox();
        };
    };

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);

        boolean hadSpline = spline != null;

        // Reset when client recieves the packet in case the tube has been destroyed
        controller = false;
        middleControlPoints = Collections.emptyList();
        spline = null;
        
        if (nbt.contains("OtherEndPos", Tag.TAG_COMPOUND)) otherEndPos = getPos().offset(NbtUtils.readBlockPos(nbt.getCompound("OtherEndPos")));
        if (nbt.contains("Points", Tag.TAG_LIST)) {
            controller = true;
            initializationTicks = nbt.getInt("InitializationTicks");
            middleControlPoints = nbt.getList("Points", Tag.TAG_LIST).stream().map(t -> NBTHelper.readVec3((ListTag)t, getPos())).toList();
        };
        if (blockEntity.hasLevel() && hadSpline == (getSpline() == null)) tubeBlockEntity.invalidateTubeRenderBoundingBox();
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
