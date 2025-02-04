package com.petrolpark.shop;

import java.util.Map;
import java.util.HashMap;

import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.team.data.ITeamDataType;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ShopsTeamData extends HashMap<Shop, ShopsTeamData.Entry> {

    public Entry getOrCreate(Shop shop) {
        return computeIfAbsent(shop, s -> defaultEntry());
    };

    public void grantXP(Shop shop, int amount) {
        getOrCreate(shop).xp += amount;
    };

    protected Entry defaultEntry() {
        return new Entry("", 0);
    };

    protected class Entry {
        public int xp;
        public String customName;

        public Entry(String customName, int xp) {
            this.customName = customName;
            this.xp = xp;
        };
    };
    
    public static class Type implements ITeamDataType<ShopsTeamData> {

        @Override
        public ShopsTeamData getBlankInstance() {
            return new ShopsTeamData();
        };

        @Override
        public boolean isBlank(ShopsTeamData data) {
            return data.isEmpty();
        };

        @Override
        public ShopsTeamData load(Level level, CompoundTag tag) {
            Registry<Shop> registry = level.registryAccess().registryOrThrow(PetrolparkRegistries.Keys.SHOP);
            ShopsTeamData data = getBlankInstance();
            for (String key : tag.getAllKeys()) {
                Shop shop = registry.get(new ResourceLocation(key));
                if (shop != null && tag.contains(key, Tag.TAG_COMPOUND)) {
                    CompoundTag shopTag = tag.getCompound(key);
                    String customName = shopTag.getString("Name");
                    int xp = shopTag.getInt("XP");
                    if (xp != 0 && !customName.isEmpty()) data.put(shop, data.new Entry(customName, xp));
                };
            };
            return data;
        };

        @Override
        public CompoundTag save(Level level, ShopsTeamData data) {
            Registry<Shop> registry = level.registryAccess().registryOrThrow(PetrolparkRegistries.Keys.SHOP);
            CompoundTag tag = new CompoundTag();
            for (Map.Entry<Shop, Entry> entry : data.entrySet()) {
                CompoundTag shopTag = new CompoundTag();
                if (!entry.getValue().customName.isEmpty()) shopTag.putString("Name", entry.getValue().customName);
                if (entry.getValue().xp != 0) shopTag.putInt("XP", entry.getValue().xp);
                tag.put(registry.getKey(entry.getKey()).toString(), shopTag);
            };
            return tag;
        };

    };
};
