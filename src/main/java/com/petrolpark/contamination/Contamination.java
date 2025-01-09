package com.petrolpark.contamination;

import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

public abstract class Contamination<OBJECT, OBJECT_STACK> {

    public static Optional<Contamination<?, ?>> get(Object object) {
        return Contaminables.streamContaminables().map(c -> c.getContamination(object)).filter(Objects::nonNull).findFirst().map(op -> (Contamination<?, ?>)op);
    };

    protected final OBJECT_STACK stack;
    
    protected final SortedSet<Contaminant> extrinsicContaminants = new TreeSet<>(Contaminant::compareTo);

    protected Contamination(OBJECT_STACK stack) {
        this.stack = stack;
    };

    public abstract Contaminable<OBJECT, OBJECT_STACK> getContaminable();
    
    public abstract OBJECT getType();

    public abstract double getAmount();

    public abstract void save();

    public final boolean has(Contaminant contaminant) {
        return IntrinsicContaminants.get(this).contains(contaminant) || extrinsicContaminants.contains(contaminant);
    };

    public final boolean hasAnyContaminant() {
        return !IntrinsicContaminants.get(this).isEmpty() || hasAnyExtrinsicContaminant();
    };

    public final boolean hasAnyExtrinsicContaminant() {
        return !extrinsicContaminants.isEmpty();
    };

    public final Stream<Contaminant> streamAllContaminants() {
        return Stream.concat(IntrinsicContaminants.get(this).stream(), extrinsicContaminants.stream());
    };

    public final boolean contaminate(Contaminant contaminant) {
        if (IntrinsicContaminants.get(this).contains(contaminant)) return false;
        boolean added = extrinsicContaminants.add(contaminant);
        if (added) save();
        return added;
    };

    public final boolean contaminateAll(Stream<Contaminant> contaminantsStream) {
        boolean changed = contaminantsStream.dropWhile(IntrinsicContaminants.get(this)::contains).map(extrinsicContaminants::add).filter(b -> b).findAny().isPresent();
        if (changed) save();
        return changed;
    };

    public final boolean decontaminate(Contaminant contaminant) {
        if (IntrinsicContaminants.get(this).contains(contaminant)) return false;
        boolean removed = extrinsicContaminants.remove(contaminant);
        if (removed) save();
        return removed;
    };

    public final boolean fullyDecontaminate() {
        if (extrinsicContaminants.isEmpty()) return false;
        extrinsicContaminants.clear();
        save();
        return true;
    };

    public ListTag writeToNBT() {
        ListTag tag = new ListTag();
        extrinsicContaminants.forEach(c -> tag.add(StringTag.valueOf(c.getLocation().toString())));
        return tag;
    };
};
