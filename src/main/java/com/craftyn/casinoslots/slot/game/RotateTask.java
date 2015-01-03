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
    public void run() {
        rotateColumn(i);
    }

    // Rotates one column one block
    private void rotateColumn(Integer column) {

        ArrayList<Block> blocks = game.getSlot().getBlocks();

        ArrayList<String> last = new ArrayList<String>();
        last.add(blocks.get(column+6).getTypeId() + ":" + blocks.get(column+6).getData());
        last.add(blocks.get(column+3).getTypeId() + ":" + blocks.get(column+3).getData());

        //Get the id and split it
        int s1 = 1;
        byte s2 = 0;
        String id = getNext();

        // Prevent silly-looking duplicate blocks
        while(id.equalsIgnoreCase(last.get(0))) {
            id = getNext();
        }

        //Since the id is not the same (see above) we can go ahead and split it up
        String[] mSplit = id.split("\\:");
        if (mSplit.length == 2) {
            s1 = Integer.parseInt(mSplit[0]);
            s2 = Byte.parseByte(mSplit[1]);
        }else {
            s1 = Integer.parseInt(mSplit[0]);
        }

        // First column
        blocks.get(column+6).setTypeIdAndData(s1, s2, false);

        // Second Column
        int c2ID = 1;
        byte c2Byte = 0;
        String[] column2 = last.get(0).split("\\:");
        if (column2.length == 2) {
            c2ID = Integer.parseInt(column2[0]);
            c2Byte = Byte.parseByte(column2[1]);
        }else {
            c2ID = Integer.parseInt(column2[0]);
        }
        blocks.get(column+3).setTypeIdAndData(c2ID, c2Byte, false);

        // Third Column
        int c3ID = 1;
        byte c3Byte = 0;
        String[] column3 = last.get(1).split("\\:");
        if (column3.length == 2) {
            c3ID = Integer.parseInt(column3[0]);
            c3Byte = Byte.parseByte(column3[1]);
        }else {
            c3ID = Integer.parseInt(column3[0]);
        }
        blocks.get(column).setTypeIdAndData(c3ID, c3Byte, false);

    }

    // Gets the next block in the reel
    private String getNext() {

        ArrayList<String> reel = game.getType().getReel();

        Random generator = new Random();
        int id = generator.nextInt(reel.size());

        String nextID = reel.get(id);
        String[] idSplit = nextID.split("\\:");

        if (idSplit.length == 2) {
            return nextID;
        }else {
            String newID;
            newID = Integer.parseInt(idSplit[0]) + ":0";

            return newID;
        }
    }
}