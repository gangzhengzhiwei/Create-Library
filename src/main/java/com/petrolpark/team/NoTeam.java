package com.petrolpark.team;

import java.util.stream.Stream;

import com.petrolpark.team.data.ITeamDataType;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class NoTeam implements ITeam<NoTeam> {

    public static final NoTeam INSTANCE = new NoTeam();

    @Override
    public ITeamType<NoTeam> getType() {
        return TeamTypes.NONE.get();
    };

    @Override
    public boolean isMember(Player player) {
        return false;
    };

    @Override
    public Stream<String> streamMemberUsernames(Level level) {
        return Stream.empty();
    };

    @Override
    public boolean isAdmin(Player player) {
        return false;
    };

    @Override
    public Component getName(Level level) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    };

    @Override
    @SuppressWarnings("unchecked")
    public <DT> DT getTeamData(ITeamDataType<? super DT> dataType) {
        return (DT)dataType.getBlankInstance();
    };

    @Override
    public void setChanged(Level level, ITeamDataType<?> dataType) {};

    @Override
    public void renderIcon(GuiGraphics graphics) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renderIcon'");
    };

    public static class Type implements ITeamType<NoTeam> {

        @Override
        public NoTeam read(CompoundTag tag, Level level) {
            return NoTeam.INSTANCE;
        };

        @Override
        public void write(NoTeam team, CompoundTag tag) {};

    };
    
};
