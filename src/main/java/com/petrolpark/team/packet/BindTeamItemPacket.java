package com.petrolpark.team.packet;

import com.petrolpark.team.ITeam;
import com.petrolpark.team.ITeamBoundItem;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;

public class BindTeamItemPacket extends BindTeamPacket {

    @SuppressWarnings("unchecked")
    public <T extends ITeam<? super T>> BindTeamItemPacket(ITeam<?> team) {
        super((T)team);
    };

    public BindTeamItemPacket(FriendlyByteBuf buffer) {
        super(buffer);
    };

    @Override
    public <T extends ITeam<? super T>> void handle(T team, Context context) {
        ItemStack heldStack = context.getSender().getMainHandItem();
        if (heldStack.getItem() instanceof ITeamBoundItem<?> bindableItem) bindableItem.bind(team, heldStack, context.getSender());
    };
    
};
