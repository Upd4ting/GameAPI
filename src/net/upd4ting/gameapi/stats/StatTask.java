package net.upd4ting.gameapi.stats;

import net.upd4ting.gameapi.task.TaskManager;

public final class StatTask implements Runnable {
	private static final String TASK_NAME = "StatTask";
	private static StatTask instance;
	
	private StatTask() {}
	
	@Override
	public void run() {
		Stat.save();
	}
	
	public void start() {
		TaskManager.runTask(TASK_NAME, this, 20 * 30);
	}
	
	public void stop() {
		TaskManager.cancelTask(TASK_NAME);
	}
	
	public static StatTask getInstance() {
		if (instance == null)
			instance = new StatTask();
		return instance;
	}
}
