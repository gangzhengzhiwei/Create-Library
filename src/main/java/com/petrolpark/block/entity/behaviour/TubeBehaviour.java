package com.petrolpark.block.entity.behaviour;

import com.petrolpark.block.ITubeBlock;
import com.petrolpark.util.TubeSpline;
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
    protected TubeSpline spline;
    protected BlockPos endPos;

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
        endPos = spline.end.getPos();
        this.spline = spline;
        blockEntity.sendData();
    };

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        if (!(getWorld().getBlockState(getPos()).getBlock() instanceof ITubeBlock tubeBlock) || !nbt.contains("EndPos", Tag.TAG_COMPOUND) || !nbt.contains("Points", Tag.TAG_LIST)) return;
        controller = true;
        endPos = getPos().offset(NbtUtils.readBlockPos(nbt.getCompound("EndPos")));
        BlockState endState = getWorld().getBlockState(endPos);
        if (endState.getBlock() != tubeBlock) return;
        spline = new TubeSpline(BlockFace.of(getPos(), tubeBlock.getTubeConnectingFace(getWorld(), getPos(), getWorld().getBlockState(getPos()))), BlockFace.of(endPos, tubeBlock.getTubeConnectingFace(getWorld(), endPos, endState)), nbt.getList("Points", Tag.TAG_LIST).stream().map(t -> NBTHelper.readVec3((ListTag)t, getPos())).toList(), tubeBlock.getTubeMaxAngle(), tubeBlock.getTubeSegmentLength(), tubeBlock.getTubeSegmentRadius());
        
    };

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        if (!controller || spline == null || endPos == null) return;
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
