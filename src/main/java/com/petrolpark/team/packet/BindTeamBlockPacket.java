package com.petrolpark.team.packet;

import com.petrolpark.team.ITeam;
import com.petrolpark.team.ITeamBoundBlockEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkEvent.Context;

public class BindTeamBlockPacket extends BindTeamPacket {

    public final BlockHitResult hit;

    public BindTeamPacket.Factory forHit(BlockHitResult hit) {
        return new Factory(hit);
    };

    public <T extends ITeam<? super T>> BindTeamBlockPacket(T team, BlockHitResult hit) {
        super(team);
        this.hit = hit;
    };

    public BindTeamBlockPacket(FriendlyByteBuf buffer) {
        super(buffer);
        hit = buffer.readBlockHitResult();
    };
    
    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        super.toBytes(buffer);
        buffer.writeBlockHitResult(hit);
    };

    @Override
    public <T extends ITeam<? super T>> void handle(T team, Context context) {
        BlockEntity be = context.getSender().level().getBlockEntity(hit.getBlockPos());
        if (be instanceof ITeamBoundBlockEntity tbbe) tbbe.bind(team, context.getSender(), hit);
    };

    private static class Factory implements BindTeamPacket.Factory {

        public final BlockHitResult hit;

        public Factory(BlockHitResult hit) {
            this.hit = hit;
        };

        @Override
        public <T extends ITeam<? super T>> BindTeamPacket create(T team) {
            return new BindTeamBlockPacket(team, hit);
        };
    };
    
};
