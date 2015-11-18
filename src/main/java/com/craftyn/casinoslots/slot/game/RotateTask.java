package com.craftyn.casinoslots.slot.game;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.block.Block;

import com.craftyn.casinoslots.classes.ReelBlock;
import com.craftyn.casinoslots.enums.SlotMachineColumnType;

public class RotateTask implements Runnable {
    private Game game;
    private SlotMachineColumnType column;
    private Random generator;

    // Task for rotating one column
    public RotateTask(Game game, SlotMachineColumnType column) {
        this.game = game;
        this.column = column;
        this.generator = new Random();
    }

    // The task itself
    public void run() {
        rotateColumn();
    }

    // Rotates one column one block
    private void rotateColumn() {
        ArrayList<Block> blocks = game.getSlot().getBlocks();

        ArrayList<String> last = new ArrayList<String>();
        last.add(blocks.get(column.getFirstRow()).getTypeId() + ":" + blocks.get(column.getFirstRow()).getData());
        last.add(blocks.get(column.getSecondRow()).getTypeId() + ":" + blocks.get(column.getSecondRow()).getData());

        //Get the id and split it
        ReelBlock block = getNext();

        // Prevent silly-looking duplicate blocks
        while(block.toString().equalsIgnoreCase(last.get(0))) {
            block = getNext();
        }

        // First block
        blocks.get(column.getFirstRow()).setTypeIdAndData(block.getBlockData().getItemTypeId(), block.getBlockData().getData(), false);

        // Second block
        int c2ID = 1;
        byte c2Byte = 0;
        String[] column2 = last.get(0).split("\\:");
        if (column2.length == 2) {
            c2ID = Integer.parseInt(column2[0]);
            c2Byte = Byte.parseByte(column2[1]);
        }else {
            c2ID = Integer.parseInt(column2[0]);
        }
        blocks.get(column.getSecondRow()).setTypeIdAndData(c2ID, c2Byte, false);

        // Third block
        int c3ID = 1;
        byte c3Byte = 0;
        String[] column3 = last.get(1).split("\\:");
        if (column3.length == 2) {
            c3ID = Integer.parseInt(column3[0]);
            c3Byte = Byte.parseByte(column3[1]);
        }else {
            c3ID = Integer.parseInt(column3[0]);
        }
        blocks.get(column.getThirdRow()).setTypeIdAndData(c3ID, c3Byte, false);

    }

    // Gets the next block in the reel
    private ReelBlock getNext() {
        int id = generator.nextInt(game.getType().getReel().size());
        return game.getType().getReel().get(id);
    }
}