package com.petrolpark.team;

import java.util.List;

import com.petrolpark.network.PetrolparkMessages;
import com.petrolpark.team.packet.BindTeamPacket;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SelectTeamScreen extends Screen {

    public final BindTeamPacket.Factory packetFactory;

    protected final List<ITeam<?>> selectableTeams;

    protected ITeam<?> selectedTeam;

    protected SelectTeamScreen(Component title, List<ITeam<?>> selectableTeams, BindTeamPacket.Factory packetFactory) {
        super(title);
        this.selectableTeams = selectableTeams;
        this.packetFactory = packetFactory;
    };

    @SuppressWarnings("unchecked")
    public <T extends ITeam<? super T>> T getSelectedTeam() {
        return (T)selectedTeam;
    };

    @Override
    public void onClose() {
        super.onClose();
        PetrolparkMessages.sendToServer(packetFactory.create(getSelectedTeam()));
    };
    
};
