package com.craftyn.casinoslots.slot.game;

import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.Sound;

public class StopRotateTask implements Runnable {
	
	private Game game;
	private Integer task;
	
	// Task for stopping one reel
	public StopRotateTask(Game game, Integer task) {
		this.game = game;
		this.task = task;
	}
	
	// The task itself
	public void run() {
		
		Location location = game.getSlot().getController().getLocation();
		game.scheduler.cancelTask(task);
		game.getPlayer().playNote(location, Instrument.PIANO, new Note((byte) 0, Tone.C, false));
		if(game.getPlugin().enableSounds)
			game.getPlayer().getWorld().playSound(game.getPlayer().getLocation(), Sound.NOTE_PIANO, 100F, 0.75F);
	}

}