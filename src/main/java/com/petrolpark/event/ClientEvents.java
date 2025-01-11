package com.petrolpark.event;

import com.petrolpark.contamination.Contaminant;
import com.petrolpark.contamination.IContamination;
import com.petrolpark.contamination.ItemContamination;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() == null) return; // Don't populate the Intrinsics map before the world has been loaded, as the Tags have not been loaded
        IContamination<?, ?> contamination = ItemContamination.get(event.getItemStack());
        contamination.streamShownContaminants().map(Contaminant::getNameColored).forEach(event.getToolTip()::add);
        contamination.streamShownAbsentContaminants().map(Contaminant::getAbsentNameColored).forEach(event.getToolTip()::add);
    };

    public static boolean isGameActive() {
        Minecraft mc = Minecraft.getInstance();
		return !(mc.level == null || mc.player == null);
	};
};
