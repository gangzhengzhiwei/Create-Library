package com.petrolpark.team;

import java.util.List;
import java.util.ArrayList;

import com.google.common.collect.ImmutableList;
import com.petrolpark.Petrolpark;
import com.petrolpark.team.ITeam.ITeamType;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Fired to gather {@link ITeam}s of which this Player {@link ITeam#isMember(Player) is a part}.
 */
public class GatherTeamsEvent extends PlayerEvent {

    protected final List<ITeam<?>> teams;

    public GatherTeamsEvent(Player player) {
        super(player);
        teams = new ArrayList<>(2);

        // Built-in Teams
        player.getCapability(SinglePlayerTeam.CAPABILITY).ifPresent(this::addTeam);
        Team team = player.getTeam();
        if (team != null) Petrolpark.SCOREBOARD_TEAMS.get(team.getName()).ifPresent(this::addTeam);
    };

    public List<ITeam<?>> getTeamsUnmodifiable() {
        return ImmutableList.copyOf(teams);
    };

    public boolean addTeam(ITeam<?> team) {
        return teams.add(team);
    };

    public boolean remove(ITeam<?> team) {
        return teams.remove(team);
    };

    public boolean remove(ITeamType<?> teamType) {
        return teams.removeIf(team -> team.getType() == teamType);
    };
    
    @Override
    public boolean isCancelable() {
        return false;
    };
    
};
