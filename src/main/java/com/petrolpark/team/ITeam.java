package com.petrolpark.team;

import java.util.stream.Stream;

import com.petrolpark.Petrolpark;
import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.team.data.ITeamDataType;
import com.petrolpark.util.NBTHelper;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ITeam<T extends ITeam<? super T>> {

    public static ITeam<?> read(CompoundTag tag, Level level) {
        ITeamType<?> type = NBTHelper.readRegistryObject(tag, "Type", PetrolparkRegistries.Keys.TEAM_TYPE);
        if (type == null) {
            Petrolpark.LOGGER.warn("Unknown Team Type: "+tag.getString("Type"));
            return NoTeam.INSTANCE;
        };
        return type.read(tag, level);
    };

    public static <T extends ITeam<? super T>> CompoundTag write(T team) {
        CompoundTag tag = new CompoundTag();
        NBTHelper.writeRegistryObject(tag, "Type", PetrolparkRegistries.Keys.TEAM_TYPE, team.getType());
        team.getType().write(team, tag);
        return tag;
    };

    public ITeamType<T> getType();

    public boolean isMember(Player player);
    
    public Stream<String> streamMemberUsernames(Level level);

    /**
     * If called, it is assumed that {@link ITeam#isMember(Player)} has already passed.
     * @param player
     * @return Whether this Player can manage this Team
     */
    public boolean isAdmin(Player player);

    public Component getName(Level level);

    public <DATA> DATA getTeamData(ITeamDataType<? super DATA> dataType);

    public void setChanged(Level level, ITeamDataType<?> dataType);

    @OnlyIn(Dist.CLIENT)
    public void renderIcon(GuiGraphics graphics);

    public static interface ITeamType<T extends ITeam<? super T>> {

        public T read(CompoundTag tag, Level level);

        public void write(T team, CompoundTag tag);
    };
};
