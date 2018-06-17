package net.upd4ting.gameapi.task;

/**
 * Class to handle repeating task
 * @author Upd4ting
 *
 */
public class Task {
	
	private final String name;
	private final Long step;
	private final Runnable runnable;
	
	private Long lastStep;
	
	public Task(String name, Integer tick, Runnable runnable) {
		this.name = name;
		this.step = (long) tick * 50;
		this.runnable = runnable;
		this.lastStep = System.currentTimeMillis() - this.step;
	}
	
	public void processTick() {
		this.runnable.run();
		this.lastStep = System.currentTimeMillis();
	}
	
	
	Boolean isElapsed() { return System.currentTimeMillis() - lastStep >= step; }
	
	public String getName() { return name; }
}
