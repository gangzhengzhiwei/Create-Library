package com.petrolpark.contamination;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.PetrolparkRegistries;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.server.ServerLifecycleHooks;

public class Contaminant {

    public static final Codec<Contaminant> CODEC = ExtraCodecs.catchDecoderException(RecordCodecBuilder.create(instance -> 
        instance.group(
            Codec.doubleRange(0d, 1d).fieldOf("preservationProportion").forGetter(Contaminant::getPreservationProportion),
            Codec.intRange(0, 16777215).fieldOf("color").forGetter(Contaminant::getColor),
            Codec.intRange(0, 16777215).fieldOf("absentColor").forGetter(Contaminant::getAbsentColor)
        ).apply(instance, Contaminant::new)
    ));

    public static Contaminant get(ResourceLocation resourceLocation) {
        return PetrolparkRegistries.getDataRegistry(PetrolparkRegistries.Keys.CONTAMINANT).get(resourceLocation);
    };

    public static Contaminant getFromIntrinsicTag(TagKey<?> tagKey) {
        return getFromTag(tagKey, "intrinsic");
    };

    public static Contaminant getFromShowIfAbsentTag(TagKey<?> tagKey) {
        return getFromTag(tagKey, "show_if_absent");
    };

    public static Contaminant getFromTag(TagKey<?> tagKey, String pathSuffix) {
        ResourceLocation rl = tagKey.location();
        String[] path = rl.getPath().split("/");
        if (!path[0].equals("contaminant") || !path[2].equals(pathSuffix)) return null;
        return PetrolparkRegistries.getDataRegistry(PetrolparkRegistries.Keys.CONTAMINANT).get(new ResourceLocation(rl.getNamespace(), path[1]));
    };

    public final double preservationProportion;
    public final int color;
    public final int absentColor;

    protected ResourceLocation rl;
    protected String descriptionId;
    protected String absentDescriptionId;

    public Contaminant(double preservationProportion, int color, int absentColor) {
        this.preservationProportion = preservationProportion;
        this.color = color;
        this.absentColor = absentColor;
    };

    public double getPreservationProportion() {
        return preservationProportion;
    };

    public int getColor() {
        return color;
    };

    public int getAbsentColor() {
        return absentColor;
    };

    public boolean isPreserved(double proportion) {
        if (preservationProportion == 0d) return proportion > 0d;
        return proportion >= preservationProportion;
    };

    public ResourceLocation getLocation() {
        if (rl == null) rl = ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(PetrolparkRegistries.Keys.CONTAMINANT).getKey(this);
        return rl;
    };

    public int compareTo(Contaminant contaminant) {
        return getLocation().compareTo(contaminant.getLocation());
    };

    public Component getName() {
        if (descriptionId == null) descriptionId = Util.makeDescriptionId("contaminant", getLocation());
        return Component.translatable(descriptionId);
    };

    public Component getNameColored() {
        return getName().copy().withStyle(Style.EMPTY.withColor(color));
    };

    public Component getAbsentName() {
        if (absentDescriptionId == null) absentDescriptionId = Util.makeDescriptionId("contaminant", getLocation()) + ".absent";
        return Component.translatable(absentDescriptionId);
    };

    public Component getAbsentNameColored() {
        return getAbsentName().copy().withStyle(Style.EMPTY.withColor(absentColor));
    };
};
