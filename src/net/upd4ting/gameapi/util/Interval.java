package net.upd4ting.gameapi.util;

import java.util.Random;

public class Interval<T> {
	private Random random;
	private T min;
	private T max;
	
	public Interval(T min, T max) {
		this.min = min;
		this.max = max;
		this.random = new Random();
	}
	
	public Integer getAsRandomInt() {
		return random.nextInt((Integer) max  - (Integer) min + 1) + (Integer) min;
	}
	
	public Double getAsRandomDouble() {
		return (Double) min + random.nextDouble() * ((Double) max - (Double) min);
	}
}