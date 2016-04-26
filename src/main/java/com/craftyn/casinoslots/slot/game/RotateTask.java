package com.craftyn.casinoslots.slot.game;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

import com.craftyn.casinoslots.classes.ReelBlock;
import com.craftyn.casinoslots.enums.SlotMachineColumnType;

public class RotateTask implements Runnable {
    private Game game;
    private SlotMachineColumnType column;
    private Random generator;
    private ArrayList<ReelBlock> reelBlocks;

    // Task for rotating one column
    public RotateTask(Game game, SlotMachineColumnType column) {
        this.game = game;
        this.column = column;
        this.generator = new Random();
        this.reelBlocks = new ArrayList<ReelBlock>();
        
        this.game.getType().getReel().stream().forEach(r -> {
            for(int i = 0; i < r.getFrequency(); i++) {
                reelBlocks.add(new ReelBlock(r.getMaterialData()));
            }
        });
    }

    // The task itself
    public void run() {
        rotateColumn();
    }

    // Rotates one column one block
    @SuppressWarnings("deprecation")
    private void rotateColumn() {
        ArrayList<Block> blocks = game.getSlot().getBlocks();

        ArrayList<ReelBlock> last = new ArrayList<ReelBlock>();
        last.add(ReelBlock.fromBlock(blocks.get(column.getFirstRow())));
        last.add(ReelBlock.fromBlock(blocks.get(column.getSecondRow())));

        //Get the id and split it
        ReelBlock block = getNext();

        // Prevent silly-looking duplicate blocks
        while(block.equals(last.get(0))) {
            game.getPlugin().debug(block.toString() + " is 'equal' to " + last.get(0).toString());
            block = getNext();
        }

        // First block
        blocks.get(column.getFirstRow()).setTypeIdAndData(block.getBlockData().getItemTypeId(), block.getBlockData().getData(), false);

        // Second block
        MaterialData secondBlock = last.get(0).getBlockData();
        blocks.get(column.getSecondRow()).setTypeIdAndData(secondBlock.getItemTypeId(), secondBlock.getData(), false);

        // Third block
        MaterialData thirdBlock = last.get(1).getBlockData();
        blocks.get(column.getThirdRow()).setTypeIdAndData(thirdBlock.getItemTypeId(), thirdBlock.getData(), false);
    }

    // Gets the next block in the reel
    private ReelBlock getNext() {
        return reelBlocks.get(generator.nextInt(reelBlocks.size()));
    }
}