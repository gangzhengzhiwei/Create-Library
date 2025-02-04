package com.petrolpark.team.data;

import static com.petrolpark.Petrolpark.REGISTRATE;

import com.petrolpark.shop.ShopsTeamData;
import com.tterrag.registrate.util.entry.RegistryEntry;

public class TeamDataTypes {

    public static final RegistryEntry<ITeamDataType<?>>

    SHOPS = REGISTRATE.teamDataType("shops", ShopsTeamData.Type::new);
    
    public static final void register() {};
};
