package com.craftyn.casinoslots.slot.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.craftyn.casinoslots.classes.OldSlotMachine;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.enums.Settings;
import com.craftyn.casinoslots.util.Stat;
import com.craftyn.casinoslots.util.Util;

public class ResultsTask implements Runnable {
    private Game game;

    // Deploys rewards after game is finished
    public ResultsTask(Game game) {
        this.game = game;
    }

    public void run() {

        SlotType type = game.getType();
        Player player = game.getPlayer();
        String name = type.getName();
        double cost = type.getCost();
        double won = 0.0;

        ArrayList<String> results = getResults();

        if (!results.isEmpty()) {
            OldSlotMachine slot = game.getSlot();

            if (!(slot.getSign() == null)) {
                Block b = slot.getSign();
                if (b.getType().equals(Material.WALL_SIGN) || b.getType().equals(Material.SIGN_POST)) {
                    Sign sign = (Sign) b.getState();
                    sign.setLine(3, player.getDisplayName());
                    sign.update(true);
                } else {
                    game.getPlugin().error("The block stored for the sign is NOT a sign, please remove it.");
                }
            }
            
            won += type.sendRewards(results, player);
            
            //Take the money from the slot machine, if it is managed
            if (slot.isManaged()) {
                if (won < 0) {
                    slot.deposit(Math.abs(won));
                } else {
                    slot.withdraw(won);
                }
            }
        }

        // No win
        else {
            game.getPlugin().sendMessage(player, type.getMessages().getLost());
        }

        // Register statistics
        if (Settings.SLOTS_TRACK_STATS.asBoolean()) {
            Stat stat;

            //Already have some stats for this type
            if (game.getPlugin().getStatData().isStat(name)) {
                stat = game.getPlugin().getStatData().getStat(name);
                if (!results.isEmpty()) {
                    stat.addWon(won, cost);
                    game.getPlugin().getStatData().addStat(stat);
                } else {
                    stat.addLost(won, cost);
                    game.getPlugin().getStatData().addStat(stat);
                }
            } else {
                game.getPlugin().debug("The player has won an amount of: " + won);
                game.getPlugin().debug("The player has lost an amount of: " + cost);
                if (!results.isEmpty()) {
                    stat = new Stat(name, 1, 1, 0, won, cost);
                    game.getPlugin().getStatData().addStat(stat);
                } else {
                    stat = new Stat(name, 1, 0, 1, won, cost);
                    game.getPlugin().getStatData().addStat(stat);
                }
            }
            game.getPlugin().getConfigData().saveStats();
        }

        // All done
        game.getSlot().toggleBusy();
    }

    // Gets the results
    private ArrayList<String> getResults() {
        ArrayList<String> results = new ArrayList<String>();
        ArrayList<Block> blocks = game.getSlot().getBlocks();

        // checks horizontal matches
        for (int i = 0; i < 5; i++) {
            ArrayList<String> currentId = new ArrayList<String>();
            List<Block> current = null;

            if (i < 3) {
                int start = 0 + 3 * i;
                int end = 3 + 3 * i;
                current = blocks.subList(start, end);
            } else {
                //diagonals
                if (Settings.SLOTS_ALLOW_DIAGONAL_WINNINGS.asBoolean()) {
                    current = new ArrayList<Block>();
                    for (int j = 0; j < 3; j++) {
                        if (i == 3) {
                            current.add(blocks.get(j * 4));
                        } else {
                            current.add(blocks.get(2 + 2 * j));
                        }
                    }
                } else {
                    // Break loop if diagonals are disabled
                    break;
                }
            }

            for (Block b : current) {
                currentId.add(b.getTypeId() + ":" + b.getData());
            }

            // Check for matches, deploy rewards
            Set<String> currentSet = new HashSet<String>(currentId);
            if (currentSet.size() == 1) {
                results.add(current.get(0).getTypeId() + ":" + current.get(0).getData());
            }
        }

        //Play some sounds on rewards!
        game.getPlugin().getServer().getScheduler().runTaskLater(game.getPlugin(), new Runnable() {
            public void run() {
                Util.playNoteBlockSound(game.getSlot().getController().getLocation(), Instrument.PIANO, new Note((byte) 1, Tone.C, false));
            }
        }, 15);

        return results;
    }
}
