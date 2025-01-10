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

/**
 * A specific instance of a contaminable object, with the specific Contaminants that object posseses.
 */
public abstract class Contamination<OBJECT, OBJECT_STACK> {

    public static Optional<Contamination<?, ?>> get(Object object) {
        return Contaminables.streamContaminables().map(c -> c.getContamination(object)).filter(Objects::nonNull).findFirst().map(c -> (Contamination<?, ?>)c);
    };

    protected final OBJECT_STACK stack;
    
    /**
     * Extrinsic {@link Contaminant}s that do not have a parent (if one exists) in this Contamination.
     */
    protected final SortedSet<Contaminant> orphanContaminants = new TreeSet<>(Contaminant::compareTo);
    /**
     * All extrinsic {@link Contaminant}s, whether added themselves or by parental proxy.
     */
    protected final Set<Contaminant> contaminants = new HashSet<>();

    protected Contamination(OBJECT_STACK stack) {
        this.stack = stack;
    };

    public abstract Contaminable<OBJECT, OBJECT_STACK> getContaminable();
    
    public abstract OBJECT getType();

    public abstract double getAmount();

    public abstract void save();

    public final boolean has(Contaminant contaminant) {
        return IntrinsicContaminants.get(this).contains(contaminant) || contaminants.contains(contaminant);
    };

    public final boolean hasAnyContaminant() {
        return !IntrinsicContaminants.get(this).isEmpty() || hasAnyExtrinsicContaminant();
    };

    public final boolean hasAnyExtrinsicContaminant() {
        return !contaminants.isEmpty();
    };

    public final Stream<Contaminant> streamAllContaminants() {
        return Stream.concat(IntrinsicContaminants.get(this).stream(), contaminants.stream());
    };

    public final boolean contaminate(Contaminant contaminant) {
        if (IntrinsicContaminants.get(this).contains(contaminant)) return false;
        if (!contaminants.add(contaminant)) return false;
        orphanContaminants.removeAll(contaminant.getChildren());
        orphanContaminants.add(contaminant);
        contaminants.addAll(contaminant.getChildren());
        save();
        return true;
    };

    /**
     * Add several Contaminants, and 
     * @param contaminantsStream
     * @return
     */
    public final boolean contaminateAll(Stream<Contaminant> contaminantsStream) {
        boolean changed = !contaminantsStream
            .dropWhile(IntrinsicContaminants.get(this)::contains) // Don't include intrinsic Contaminants
            .filter(contaminants::add) // Only include Contaminants whose (parents) are not already here
            .map(contaminant -> {
                orphanContaminants.removeAll(contaminant.getChildren()); // Children of this Contaminant are no longer orphans
                orphanContaminants.add(contaminant); // Add all reminaing Contaminants (they don't have existing parents)
                contaminants.addAll(contaminant.getChildren());
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
        if (!orphanContaminants.remove(contaminant)) return false;
        contaminants.remove(contaminant);
        for (Contaminant child : contaminant.getChildren()) {
            if (Collections.disjoint(contaminants, child.getParents())) contaminants.remove(child);
        };
        save();
        return true;
    };

    /**
     * Remove a Contaminant, but not any of its children.
     * If the Contaminant has any parents in this Contamination, it will not be removed.
     * @param contaminant
     * @return Whether this Contamination changed (the Contaminant was removed)
     * @see Contamination#decontaminate(Contaminant) Remove all children
     */
    public final boolean decontaminateOnly(Contaminant contaminant) {
        if (IntrinsicContaminants.get(this).contains(contaminant)) return false;
        if (!orphanContaminants.remove(contaminant)) return false;
        contaminants.remove(contaminant);
        for (Contaminant child : contaminant.getChildren()) {
            if (Collections.disjoint(contaminants, child.getParents())) orphanContaminants.add(child);
        };
        save();
        return true;
    };

    /**
     * Remove all extrinsic Contaminants.
     * @return Whether this Contamination changed (whether it had any extrinsic Contaminants)
     */
    public final boolean fullyDecontaminate() {
        if (orphanContaminants.isEmpty()) return false;
        orphanContaminants.clear();
        contaminants.clear();
        save();
        return true;
    };

    public ListTag writeToNBT() {
        ListTag tag = new ListTag();
        orphanContaminants.forEach(c -> tag.add(StringTag.valueOf(c.getLocation().toString())));
        return tag;
    };
};
