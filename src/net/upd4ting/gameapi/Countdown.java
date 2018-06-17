package net.upd4ting.gameapi;

import net.upd4ting.gameapi.customevents.GameCountdownEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.upd4ting.gameapi.nms.NMSHandler;
import net.upd4ting.gameapi.task.TaskManager;

public final class Countdown implements Runnable {
	
	private static final String TASK_NAME = "Countdown";
	
	private final Game game;
	private final String taskName;
	private boolean started;
	private boolean forced;
	private int sec;
	private int max;
	
	private Countdown(Game game) {
		this.taskName = TASK_NAME + GameManager.getGames().indexOf(game);
		this.game = game;
		this.started = false;
		this.forced = false;
		this.sec = game.getTimeToStart();
		this.max = game.getTimeToStart();
	}
	
	public void start(boolean forced) {
		this.sec = game.getTimeToStart();
		this.max = game.getTimeToStart();
		this.started = true;
		this.forced = forced;
		TaskManager.runTask(this.taskName, this, 20);
	}

	@Override
	public void run() {
		if (sec <= 0) {
			game.start();
			started = false;
			TaskManager.cancelTask(this.taskName);
			return;
		}
		
		// Si pas assez de joueur et partie pas forcée on stop
		if (game.getMinPlayerToStart() > game.getInsides().size() && !forced) {
			game.avertAll(GameAPI.getLangConfiguration().getCountdownErrorNotEnoughPlayer());
			started = false;
			sec = game.getTimeToStart();
			max = game.getTimeToStart();
			TaskManager.cancelTask(this.taskName);
			return;
		}
		
		if (game.getMaxPlayers() <= game.getInsides().size() && sec > game.getTimeToStartFull()) {
			sec = game.getTimeToStartFull();
			max = game.getTimeToStartFull();
		}
		
		if (sec % 60 == 0 || sec == 30 || sec <= 5) {
			game.avertAll(GameAPI.getLangConfiguration().getGameTimeStart(sec));
			game.playSoundToAll(NMSHandler.isBasicSound() ? "NOTE_STICKS" : "BLOCK_NOTE_PLING");
		}
		
		// Compteur dans la barre d'exp
		for (Player p : game.getInsides()) {
			p.setLevel(sec);
			p.setExp((float)sec / max);
		}

		GameCountdownEvent event = new GameCountdownEvent(game, sec);
		Bukkit.getPluginManager().callEvent(event);
		
		sec--;
	}

	public void reset() {
		this.forced = false;
		this.sec = game.getTimeToStart();
		this.max = game.getTimeToStart();
		this.started = false;
	}

	public int getSec() {
		return sec;
	}

	public boolean isStarted() {
		return started;
	}
	
	public boolean hasBeenForced() {
		return forced;
	}
	
	public static Countdown newInstance(Game game) {
		return new Countdown(game);
	}
}
