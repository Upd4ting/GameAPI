package net.upd4ting.gameapi.task;

import net.upd4ting.gameapi.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle a task that have a time of life
 * @author Upd4ting
 *
 */
public abstract class Timer implements Runnable {

	private static List<Timer> timers = new ArrayList<>();

	protected final Game game;
	protected final String taskName;
	protected Integer time;
	protected Integer initialTime;
	
	public Timer(Game game, String taskName, Integer time) {
		this.game = game;
		this.taskName = taskName;
		this.time = time;
		this.initialTime = time;
	}
	
	@Override
	public void run() {
		if (game.isInGame() && isActive()) {
			onSec();
			time--;
		}
	}
	
	public abstract void onSec();
	
	public void start() {
		timers.add(this);
		TaskManager.runTask(taskName, this, 20);
	}
	
	public void stop() {
		timers.remove(this);
		TaskManager.cancelTask(taskName);
	}

	public void reset() {
		this.time = this.initialTime;
	}
	
	public Integer getTime() { return time; }
	public String getDisplayTime() { return String.format("%02d:%02d", time / 60, time % 60); }
	public Boolean isActive() { return time >= 0; }

	public static Timer getTimer(String name) {
		for (Timer t : timers)
			if (t.taskName.equals(name))
				return t;
		return null;
	}
}
