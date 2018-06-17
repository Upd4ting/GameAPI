package net.upd4ting.gameapi.signs;

import net.upd4ting.gameapi.task.TaskManager;

/**
 * Task that will keep updated each line of each sign
 * This class is instanced by {@link SignTask#getInstance()}
 * This method assume to create only one instance of this task
 * @author Upd4ting
 *
 */
public final class SignTask implements Runnable {
	
	private static final String TASK_NAME = "SignTask";
	private static SignTask instance;
	
	private SignTask() {} // Non instanciable class
	
	@Override
	public void run() {
		for (Sign sign : Sign.getSigns()) {
			sign.update();
		}
	}
	
	public void start() {
		TaskManager.runTask(TASK_NAME, this, 20);
	}
	
	public void stop() {
		TaskManager.cancelTask(TASK_NAME);
	}
	
	public static SignTask getInstance() {
		if (instance == null)
			return new SignTask();
		return instance;
	}
}
