package com.petrolpark.team.scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.petrolpark.network.PetrolparkMessages;
import com.petrolpark.team.ITeamDataType;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class ScoreboardTeamManager {

    protected final Map<PlayerTeam, ScoreboardTeam> teams = new HashMap<>();

    protected Scoreboard scoreboard;
    
    protected ScoreboardTeamSavedData savedData;

    public Optional<ScoreboardTeam> get(String teamName) {
        if (scoreboard == null) DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::findScoreboardClient);
        if (scoreboard == null) return Optional.empty();
        PlayerTeam team = scoreboard.getPlayerTeam(teamName);
        if (team == null) return Optional.empty();
        return Optional.of(teams.computeIfAbsent(team, ScoreboardTeam::new));
    };

    public void dataChanged(Level level, ScoreboardTeam team, ITeamDataType<?> dataType) {
        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> PetrolparkMessages.sendToAllClients(new ScoreboardTeamDataChangedPacket(level, team, dataType)));
        if (savedData != null) savedData.setDirty();
    };

    public void setData(Level level, String teamName, ITeamDataType<?> dataType, CompoundTag dataTag) {
        get(teamName).ifPresent(team -> team.loadTeamData(level, dataTag, dataType));
    };

    public void playerLogin(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			loadSavedData(serverPlayer.getServer());
			for (ScoreboardTeam team : teams.values()) {
                team.streamNonBlankTeamData().forEach(dt -> PetrolparkMessages.sendToClient(new ScoreboardTeamDataChangedPacket(serverPlayer.level(), team, dt), serverPlayer));
            };
		}
	};

	public void playerLogout(Player player) {};

	public void levelLoaded(LevelAccessor level) {
		MinecraftServer server = level.getServer();
		if (server == null || server.overworld() != level) return;
        teams.clear();
        scoreboard = null;
		savedData = null;
		loadSavedData(server);
	};

	private void loadSavedData(MinecraftServer server) {
		if (savedData != null) return;
        scoreboard = server.getScoreboard();
		savedData = server.overworld()
            .getDataStorage()
            .computeIfAbsent(tag -> load(server.overworld(), tag), () -> new ScoreboardTeamSavedData(server.overworld()), "petrolpark_teams");
	};

    private void findScoreboardClient() {
        Minecraft mc = Minecraft.getInstance();
        scoreboard = mc.level.getScoreboard();
    };

    public class ScoreboardTeamSavedData extends SavedData {

        protected final Level overworld;

        public ScoreboardTeamSavedData(Level overworld) {
            this.overworld = overworld;
        };

        @Override
        public CompoundTag save(CompoundTag tag) {
            for (ScoreboardTeam team : teams.values()) {
                tag.put(team.team.getName(), team.saveTeamData(overworld));
            };
            return tag;
        };
    
    };

    protected ScoreboardTeamSavedData load(Level overworld, CompoundTag tag) {
        ScoreboardTeamSavedData savedData = new ScoreboardTeamSavedData(overworld);

        for (String key : tag.getAllKeys()) {
            if (!tag.contains(key, Tag.TAG_COMPOUND)) continue;
            get(key).ifPresent(team -> team.loadTeamData(overworld, tag.getCompound(key)));
        };

        return savedData;
    };
};
