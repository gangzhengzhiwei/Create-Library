package com.petrolpark.contamination;

import java.util.Map;
import java.util.Set;

import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;

@EventBusSubscriber
public class IntrinsicContaminants {

    protected static final Map<Object, Set<Contaminant>> INTRINSIC_CONTAMINANTS = new HashMap<>();
    protected static final Map<Object, Set<Contaminant>> SHOWN_IF_ABSENT_CONTAMINANTS = new HashMap<>();

    public static void clear() {
        INTRINSIC_CONTAMINANTS.clear();
        SHOWN_IF_ABSENT_CONTAMINANTS.clear();  
    };

    protected static <OBJECT> Set<Contaminant> get(Contamination<OBJECT, ?> contamination) {
        return INTRINSIC_CONTAMINANTS.computeIfAbsent(contamination.getType(), obj -> contamination.getContaminable().getIntrinsicContaminants(contamination.getType()));
    };

    protected static <OBJECT> Set<Contaminant> getShownIfAbsent(Contamination<OBJECT, ?> contamination) {
        return SHOWN_IF_ABSENT_CONTAMINANTS.computeIfAbsent(contamination.getType(), obj -> contamination.getContaminable().getShownIfAbsentContaminants(contamination.getType()));
    };

    @SubscribeEvent
    public static final void onTagsUpdated(TagsUpdatedEvent event) {
        clear();
    };
    
};
