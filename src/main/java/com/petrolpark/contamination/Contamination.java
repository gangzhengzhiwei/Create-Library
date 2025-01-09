package com.petrolpark.contamination;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
    
    protected final SortedSet<Contaminant> extrinsicOrphanContaminants = new TreeSet<>(Contaminant::compareTo);
    protected final Set<Contaminant> extrinsicContaminants = new HashSet<>();

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
        if (!extrinsicContaminants.add(contaminant)) return false;
        extrinsicOrphanContaminants.removeAll(contaminant.getChildren());
        extrinsicOrphanContaminants.add(contaminant);
        extrinsicContaminants.addAll(contaminant.getChildren());
        save();
        return true;
    };

    public final boolean contaminateAll(Stream<Contaminant> contaminantsStream) {
        boolean changed = !contaminantsStream
            .dropWhile(IntrinsicContaminants.get(this)::contains) // Don't include intrinsic Contaminants
            .filter(extrinsicContaminants::add) // Only include Contaminants whose (parents) are not already here
            .map(contaminant -> {
                extrinsicOrphanContaminants.removeAll(contaminant.getChildren()); // Children of this Contaminant are no longer orphans
                extrinsicOrphanContaminants.add(contaminant); // Add all reminaing Contaminants (they don't have existing parents)
                extrinsicContaminants.addAll(contaminant.getChildren());
                return contaminant;
            }).toList().isEmpty(); // Need to collect in a List to ensure the map is executed for every element
        if (changed) save();
        return changed;
    };

    /**
     * Remove a Contaminant and any {@link Contaminant#getChildren() children} it has that don't belong to another parent.
     * If the Contaminant has any parents in this Contamination, it will not be removed.
     * @param contaminant
     * @return Whether this Contamination changed
     * @see Contamination#decontaminateOnly(Contaminant) Don't remove children
     */
    public final boolean decontaminate(Contaminant contaminant) {
        if (IntrinsicContaminants.get(this).contains(contaminant)) return false;
        if (!extrinsicOrphanContaminants.remove(contaminant)) return false;
        extrinsicContaminants.remove(contaminant);
        for (Contaminant child : contaminant.getChildren()) {
            if (Collections.disjoint(extrinsicContaminants, child.getParents())) extrinsicContaminants.remove(child);
        };
        save();
        return true;
    };

    /**
     * Remove a Contaminant, but not any of its children.
     * If the Contaminant has any parents in this Contamination, it will not be removed.
     * @param contaminant
     * @return Whether this contaminant changed
     */
    public final boolean decontaminateOnly(Contaminant contaminant) {
        if (IntrinsicContaminants.get(this).contains(contaminant)) return false;
        if (!extrinsicOrphanContaminants.remove(contaminant)) return false;
        extrinsicContaminants.remove(contaminant);
        for (Contaminant child : contaminant.getChildren()) {
            if (Collections.disjoint(extrinsicContaminants, child.getParents())) extrinsicOrphanContaminants.add(child);
        };
        save();
        return true;
    };

    public final boolean fullyDecontaminate() {
        if (extrinsicOrphanContaminants.isEmpty()) return false;
        extrinsicOrphanContaminants.clear();
        extrinsicContaminants.clear();
        save();
        return true;
    };

    public ListTag writeToNBT() {
        ListTag tag = new ListTag();
        extrinsicOrphanContaminants.forEach(c -> tag.add(StringTag.valueOf(c.getLocation().toString())));
        return tag;
    };
};
