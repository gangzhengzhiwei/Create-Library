package com.petrolpark.shop;

import com.petrolpark.team.ITeamDataType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class ShopsTeamData {
    
    public static class Type implements ITeamDataType<ShopsTeamData> {

        @Override
        public ShopsTeamData getBlankInstance() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getBlankInstance'");
        };

        @Override
        public boolean isBlank(ShopsTeamData data) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'isBlank'");
        };

        @Override
        public ShopsTeamData load(Level level, CompoundTag tag) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'load'");
        };

        @Override
        public CompoundTag save(Level level, ShopsTeamData data) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'save'");
        };

    };
};
