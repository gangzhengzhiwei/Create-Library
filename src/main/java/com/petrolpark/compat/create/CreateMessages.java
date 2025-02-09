package com.petrolpark.compat.create;

import com.petrolpark.network.PetrolparkMessages;
import com.petrolpark.tube.BuildTubePacket;

public class CreateMessages {
  
    public static final void register() {
        PetrolparkMessages.addC2SPacket(BuildTubePacket.class, BuildTubePacket::new);
    };
};
