package com.petrolpark.team;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

public interface ITeamBoundBlockEntity {
  
    public <T extends ITeam<? super T>> void bind(T team, Player player, BlockHitResult hit);
};
