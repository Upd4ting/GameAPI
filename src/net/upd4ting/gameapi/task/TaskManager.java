package net.upd4ting.gameapi.task;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import net.upd4ting.gameapi.GameAPI;

public class TaskManager extends BukkitRunnable {
	
	private static final List<Task> tasks = new ArrayList<>();
	
	public TaskManager() {
		this.runTaskTimer(GameAPI.getInstance(), 0, 1);
	}

	@Override
	public void run() {
		for (Task t : new ArrayList<>(tasks))
			if (t.isElapsed())
				t.processTick();
	}
	
	public static void runTask(String name, Runnable runnable, Integer tick) {
		tasks.add(new Task(name, tick, runnable));
	}
	
	public static Task getTask(String name) {
		for (Task t : tasks)
			if (t.getName().equals(name))
				return t;
		return null;
	}
	
	public static void cancelTask(String name) {
		cancelTask(getTask(name));
	}
	
	public static void cancelTask(Task t) {
		tasks.remove(t);
	}
}
