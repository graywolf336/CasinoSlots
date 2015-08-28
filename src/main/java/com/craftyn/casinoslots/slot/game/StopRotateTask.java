package com.craftyn.casinoslots.slot.game;

import com.craftyn.casinoslots.util.Util;

public class StopRotateTask implements Runnable {
    private Game game;
    private Integer task;

    // Task for stopping a reel
    public StopRotateTask(Game game, Integer task) {
        this.game = game;
        this.task = task;
    }

    // The task itself
    public void run() {
        game.scheduler.cancelTask(task);
        Util.playGameSound(game.getSlot().getController().getLocation());
    }
}
