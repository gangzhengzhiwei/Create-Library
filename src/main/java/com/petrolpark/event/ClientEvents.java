package com.petrolpark.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.petrolpark.PetrolparkClient;
import com.petrolpark.contamination.Contaminant;
import com.petrolpark.contamination.ItemContamination;
import com.petrolpark.tube.ClientTubePlacementHandler;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() == null) return; // Don't populate the Intrinsics map before the world has been loaded, as the Tags have not been loaded
        ItemContamination contamination = ItemContamination.get(event.getItemStack());
        contamination.streamShownContaminants().map(Contaminant::getNameColored).forEach(event.getToolTip()::add);
        contamination.streamShownAbsentContaminants().map(Contaminant::getAbsentNameColored).forEach(event.getToolTip()::add);
    };

    @SubscribeEvent
    public static void onTick(ClientTickEvent event) {
        if (!isGameActive()) return;
        PetrolparkClient.OUTLINER.tickOutlines();
    };

    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != Stage.AFTER_PARTICLES) return;

		PoseStack ms = event.getPoseStack();
		ms.pushPose();
		SuperRenderTypeBuffer buffer = SuperRenderTypeBuffer.getInstance();
		float partialTicks = AnimationTickHolder.getPartialTicks();
        Minecraft mc = Minecraft.getInstance();
		Vec3 camera = mc.gameRenderer.getMainCamera().getPosition();

        PetrolparkClient.OUTLINER.renderOutlines(ms, buffer, camera, partialTicks);

        buffer.draw();
		RenderSystem.enableCull();
        ms.popPose();
    };
    
    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
		public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
			event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "tube_info", ClientTubePlacementHandler.OVERLAY);
		};
    };

    protected static boolean isGameActive() {
        Minecraft mc = Minecraft.getInstance();
		return !(mc.level == null || mc.player == null);
	};
};
