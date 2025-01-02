package com.petrolpark.tube;

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
import net.minecraft.world.level.block.state.BlockState;

public class TubeBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<TubeBehaviour> TYPE = new BehaviourType<>();

    protected boolean controller = false;
    protected int initializationTicks = 0;
    protected TubeSpline spline;
    protected BlockPos endPos;

    protected boolean disconnecting = false;

    public TubeBehaviour(SmartBlockEntity be) {
        super(be);
    };

    public boolean isController() {
        return controller;
    };

    public TubeSpline getSpline() {
        return spline;
    };

    public void connect(TubeSpline spline) {
        if (!spline.start.getPos().equals(getPos())) throw new IllegalStateException("Mismatch in tube spline start and controller position.");
        controller = true;
        disconnecting = false;
        endPos = spline.end.getPos();
        this.spline = spline;
        for (BlockPos pos : spline.getBlockedPositions()) {
            getWorld().setBlock(pos, PetrolparkBlocks.TUBE_STRUCTURE.getDefaultState(), 3);
        };
        initializationTicks = 3; // Delay to link structural blocks to the controller
        blockEntity.notifyUpdate();
    };

    public void disconnect() {
        if (disconnecting) return;
        disconnecting = true;
        if (spline != null) for (BlockPos pos : spline.getBlockedPositions()) {
            getWorld().destroyBlock(pos, true);
        };
        controller = false;
        endPos = null;
        spline = null;
        blockEntity.notifyUpdate();
    };

    @Override
    public void tick() {
        super.tick();
        if (initializationTicks > 0) {
            initializationTicks--;
            if (initializationTicks == 1 && spline != null) for (BlockPos pos : spline.getBlockedPositions()) {
                getWorld().getBlockEntity(pos, PetrolparkBlockEntityTypes.TUBE_STRUCTURE.get()).ifPresent(be -> be.setController(getPos()));
            };
        };
    };

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        if (!(getWorld().getBlockState(getPos()).getBlock() instanceof ITubeBlock tubeBlock) || !nbt.contains("EndPos", Tag.TAG_COMPOUND) || !nbt.contains("Points", Tag.TAG_LIST)) return;
        controller = true;
        initializationTicks = nbt.getInt("InitializationTicks");
        endPos = getPos().offset(NbtUtils.readBlockPos(nbt.getCompound("EndPos")));
        BlockState endState = getWorld().getBlockState(endPos);
        if (endState.getBlock() != tubeBlock) return;
        spline = new TubeSpline(BlockFace.of(getPos(), tubeBlock.getTubeConnectingFace(getWorld(), getPos(), getWorld().getBlockState(getPos()))), BlockFace.of(endPos, tubeBlock.getTubeConnectingFace(getWorld(), endPos, endState)), nbt.getList("Points", Tag.TAG_LIST).stream().map(t -> NBTHelper.readVec3((ListTag)t, getPos())).toList(), tubeBlock.getTubeMaxAngle(), tubeBlock.getTubeSegmentLength(), tubeBlock.getTubeSegmentRadius());
    };

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        if (!controller || spline == null || endPos == null) return;
        if (initializationTicks > 0) nbt.putInt("InitializationTicks", initializationTicks);
        nbt.put("EndPos", NbtUtils.writeBlockPos(endPos.subtract(getPos())));
        ListTag pointsTag = new ListTag();
        spline.getMiddleControlPoints().forEach(p -> pointsTag.add(NBTHelper.writeVec3(p, getPos())));
        nbt.put("Points", pointsTag);
    };

    @Override
    public BehaviourType<TubeBehaviour> getType() {
        return TYPE;
    };
    
};
