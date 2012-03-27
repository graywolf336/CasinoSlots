package com.craftyn.casinoslots.slot.game;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.block.Block;

public class RotateTask implements Runnable {
	
	private Game game;
	private Integer i;
	
	// Task for rotating one column
	public RotateTask(Game game, Integer i) {
	
		this.game = game;
		this.i = i;
		
	}
	
	// The task itself
	@Override
	public void run() {
		rotateColumn(i);	
	}
	
	// Rotates one column one block
	private void rotateColumn(Integer column) {
		
		ArrayList<Block> blocks = game.getSlot().getBlocks();
		
		ArrayList<Integer> last = new ArrayList<Integer>();
		last.add(blocks.get(column+6).getTypeId());
		last.add(blocks.get(column+3).getTypeId());
		
		// Prevent silly-looking duplicate blocks
		int id = getNext();
		while(id == last.get(0)) {
			id = getNext();
		}
		
		blocks.get(column+6).setTypeId(id);
		blocks.get(column+3).setTypeId(last.get(0));
		blocks.get(column).setTypeId(last.get(1));
		
	}
	
	// Gets the next block in the reel
	private Integer getNext() {
		
		ArrayList<Integer> reel = game.getType().getReel();
		
		Random generator = new Random();
		int id = generator.nextInt(reel.size());
		return reel.get(id);
		
	}

}