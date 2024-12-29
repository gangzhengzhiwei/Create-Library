package com.petrolpark.block.entity.behaviour;

import com.petrolpark.block.ITubeBlock;
import com.petrolpark.util.ClampedCubicSpline;
import com.petrolpark.util.NBTHelper;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class TubeBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<TubeBehaviour> TYPE = new BehaviourType<>();

    protected ClampedCubicSpline spline;

    public TubeBehaviour(SmartBlockEntity be) {
        super(be);
    };

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        if (clientPacket || !(getWorld().getBlockState(getPos()).getBlock() instanceof ITubeBlock tubeBlock) || !nbt.contains("Points", Tag.TAG_LIST)) return;
        spline = new ClampedCubicSpline(nbt.getList("Points", Tag.TAG_LIST).stream().map(t -> (ListTag)t).map(NBTHelper::readVec3).toList(), NBTHelper.readVec3(nbt.getList("StartDir", Tag.TAG_DOUBLE)), NBTHelper.readVec3(nbt.getList("EndDir", Tag.TAG_DOUBLE)), tubeBlock.getTubeMaxAngle(), tubeBlock.getTubeSegmentLength(), tubeBlock.getTubeSegmentRadius());
    };

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        if (spline == null || clientPacket) return;
        ListTag pointsTag = new ListTag();
        spline.getControlPoints().forEach(p -> pointsTag.add(NBTHelper.writeVec3(p)));
        nbt.put("StartDir", NBTHelper.writeVec3(spline.getStartTangent()));
        nbt.put("EndDir", NBTHelper.writeVec3(spline.getEndTangent()));
    };

    @Override
    public BehaviourType<TubeBehaviour> getType() {
        return TYPE;
    };
    
};
