package com.petrolpark.team;

import java.util.stream.Stream;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ITeam<T extends ITeam<? super T>> {

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

    public <DT> DT getTeamData(ITeamDataType<? super DT> dataType);

    public void setChanged(Level level, ITeamDataType<?> dataType);

    @OnlyIn(Dist.CLIENT)
    public void renderIcon(GuiGraphics graphics);

    public static interface ITeamType<T extends ITeam<? super T>> {

        public T read(CompoundTag tag, Level level);

        public void write(T team, CompoundTag tag);
    };
};
