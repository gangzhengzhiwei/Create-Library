package com.petrolpark.util;

import java.util.Objects;

public class Pair<F, S> {

	protected F first;
	protected S second;

	protected Pair(F first, S second) {
		this.first = first;
		this.second = second;
	};

	public static <F, S> Pair<F, S> of(F first, S second) {
		return new Pair<>(first, second);
	};

	public F getFirst() {
		return first;
	};

	public S getSecond() {
		return second;
	};

	public void setFirst(F first) {
		this.first = first;
	};

	public void setSecond(S second) {
		this.second = second;
	};

	public Pair<F, S> copy() {
		return Pair.of(first, second);
	};

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) return true;
		if (obj instanceof final Pair other) return Objects.equals(first, other.first) && Objects.equals(second, other.second);
		return false;
	};

	@Override
	public int hashCode() {
		return (nullHash(first) * 31) ^ nullHash(second);
	};

	private int nullHash(Object o) {
		return o == null ? 0 : o.hashCode();
	};

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	};

	public Pair<S, F> swap() {
		return Pair.of(second, first);
	};

};

