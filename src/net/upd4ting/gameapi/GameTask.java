package net.upd4ting.gameapi;

import net.upd4ting.gameapi.task.TaskManager;

public final class GameTask implements Runnable {
	private static final String TASK_NAME = "GameTask";
	
	private final Game game;
	private final String taskName;
	
	private GameTask(Game game) {
		this.game = game;
		this.taskName = TASK_NAME + GameManager.getGames().indexOf(game);
	}
	
	public void start() {
		TaskManager.runTask(taskName, this, 20);
	}
	
	public void stop() {
		TaskManager.cancelTask(taskName);
	}
	
	@Override
	public void run() {
		game.setTime(game.getTime() + 1);
	}
	
	public static GameTask newInstance(Game game) {
		return new GameTask(game);
	}
}
