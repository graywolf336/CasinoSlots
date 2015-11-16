package com.craftyn.casinoslots.actions.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.Type;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * The TpTo action.
 *
 * Usage:
 * <ul>
 * <li>tpto x,y,z</li>
 * <li>tpto world,x,y,z,yaw,pitch</li>
 * <li>tpto world x y z</li>
 * <li>tpto world x y z yaw pitch</li>
 * </ul>
 *
 * @author graywolf336
 * @since 2.6.0
 * @version 1.0.0
 */
public class TpToAction extends Action {
    private String name = "TpTo";
    private Location loc;
    private boolean changeWorld = false;

    public TpToAction(CasinoSlots plugin, Type type, String... args) throws ActionLoadingException {
        super(plugin, type, args);
        String exceptionMsg = "The arguments for the '" + this.getName() + "' action for " + type.getName() + " are not valid.";

        switch (args.length) {
            case 1:
                String[] coord = args[0].split("\\,");

                switch (coord.length) {
                    case 3:
                        //x,y,z
                        try {
                            changeWorld = true;
                            loc = new Location(plugin.getServer().getWorlds().get(0), Double.parseDouble(coord[0]), Double.parseDouble(coord[1]), Double.parseDouble(coord[2]));
                        } catch (Exception e) {
                            throw new ActionLoadingException(exceptionMsg);
                        }
                        break;
                    case 6:
                        //world,x,y,z,yaw,pitch
                        if (plugin.getServer().getWorld(coord[0]) == null)
                            throw new ActionLoadingException(exceptionMsg + " (invalid world: " + coord[0] + ")");

                        try {
                            loc = new Location(plugin.getServer().getWorld(coord[0]), Double.parseDouble(coord[1]), Double.parseDouble(coord[2]), Double.parseDouble(coord[3]), Float.parseFloat(coord[4]), Float.parseFloat(coord[5]));
                        } catch (Exception e) {
                            throw new ActionLoadingException(exceptionMsg + " (number didn't parse right?)");
                        }
                        break;
                    default:
                        throw new ActionLoadingException(exceptionMsg);
                }
                break;
            case 4:
                //world x y z
                if (plugin.getServer().getWorld(args[0]) == null)
                    throw new ActionLoadingException(exceptionMsg + " (invalid world: " + args[0] + ")");

                try {
                    loc = new Location(plugin.getServer().getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                } catch (Exception e) {
                    throw new ActionLoadingException(exceptionMsg + " (number didn't parse right?)");
                }
                break;
            case 6:
                //world x y z yaw pitch
                if (plugin.getServer().getWorld(args[0]) == null)
                    throw new ActionLoadingException(exceptionMsg + " (invalid world: " + args[0] + ")");

                try {
                    loc = new Location(plugin.getServer().getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
                } catch (Exception e) {
                    throw new ActionLoadingException(exceptionMsg + " (number didn't parse right?)");
                }
                break;
            default:
                throw new ActionLoadingException(exceptionMsg);
        }
    }

    public boolean isValid() {
        return loc != null;
    }

    public boolean execute(Type type, Reward reward, Player player) {
        if (changeWorld)
            loc.setWorld(player.getWorld());
        player.teleport(loc);
        return true;
    }

    public String getName() {
        return this.name;
    }
}
