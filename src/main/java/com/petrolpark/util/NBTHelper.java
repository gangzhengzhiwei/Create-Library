package com.petrolpark.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.phys.Vec3;

public class NBTHelper {
    
    public static Vec3 readVec3(ListTag tag, BlockPos origin) {
        return Vec3.atLowerCornerOf(origin).add(tag.getDouble(0), tag.getDouble(1), tag.getDouble(2));
    };

    public static ListTag writeVec3(Vec3 vec, BlockPos origin) {
        vec = vec.subtract(Vec3.atLowerCornerOf(origin));
        ListTag tag = new ListTag();
        tag.add(DoubleTag.valueOf(vec.x()));
        tag.add(DoubleTag.valueOf(vec.y()));
        tag.add(DoubleTag.valueOf(vec.z()));
        return tag;
    };
};
