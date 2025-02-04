package com.petrolpark.team.packet;

import java.util.function.Supplier;

import com.petrolpark.network.packet.C2SPacket;
import com.petrolpark.team.ITeam;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public abstract class BindTeamPacket extends C2SPacket {

    public final CompoundTag teamTag;

    public BindTeamPacket(FriendlyByteBuf buffer) {
        teamTag = buffer.readNbt();
    };

    public <T extends ITeam<? super T>> BindTeamPacket(T team) {
        this.teamTag = ITeam.write(team);
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeNbt(teamTag);
    };

    public abstract <T extends ITeam<? super T>> void handle(T team, NetworkEvent.Context context);

    @Override
    public final boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> handle(context));
        return true;
    };

    @SuppressWarnings("unchecked")
    private final <T extends ITeam<? super T>> void handle(NetworkEvent.Context context) {
        T team = (T)ITeam.read(teamTag, context.getSender().level());
        if (team.isMember(context.getSender())) handle(team, context);
    };

    @FunctionalInterface
    public static interface Factory {
        public <T extends ITeam<? super T>> BindTeamPacket create(T team);
    };
    
};
