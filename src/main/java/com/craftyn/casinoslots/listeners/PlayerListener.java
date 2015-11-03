package com.craftyn.casinoslots.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;
import com.craftyn.casinoslots.slot.Type;
import com.craftyn.casinoslots.slot.game.Game;
import com.craftyn.casinoslots.util.PermissionUtil;

public class PlayerListener implements Listener {
    private CasinoSlots plugin;

    public PlayerListener(CasinoSlots plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void notifyOpsOfUpdate(PlayerJoinEvent event) {
        if(plugin.getConfig().getBoolean("options.update-checking.enabled")) {
            if(event.getPlayer().isOp() && plugin.getUpdate().isAvailable()) {
                event.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "An update for Jail is available: " + plugin.getUpdate().getNewVersion());
                event.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + plugin.getUpdate().getFileUrl());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Check if plugin is enabled
        if(plugin.isEnabled()) {
            Block b = event.getClickedBlock();
            if(b == null) return;

            Player player = event.getPlayer();

            if(event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.getSlotData().isCreatingSlots(player.getName())) {
                // Creating slots
                BlockFace face = event.getBlockFace();

                if(face != BlockFace.DOWN && face != BlockFace.UP) {
                    if(plugin.useTowny) {
                        if(!plugin.getTownyChecks().checkSlotsTowny(b, face, player.getName())) {
                            SlotMachine slot = plugin.getSlotData().getCreatingSlot(player.getName());
                            plugin.getSlotData().toggleCreatingSlots(player.getName(), slot);
                            plugin.sendMessage(player, plugin.getConfigData().noOwnership);
                            return;
                        }
                    }

                    if(plugin.useWorldGuard) {
                        if(!plugin.getWorldGuard().canBuild(player, b)) {
                            SlotMachine slot = plugin.getSlotData().getCreatingSlot(player.getName());
                            plugin.getSlotData().toggleCreatingSlots(player.getName(), slot);
                            player.sendMessage(ChatColor.DARK_RED + "You don't have permission for this area.");
                            return;
                        }
                    }

                    SlotMachine slot = plugin.getSlotData().getCreatingSlot(player.getName());
                    plugin.getSlotData().createReel(player, face, slot);
                    plugin.getSlotData().toggleCreatingSlots(player.getName(), slot);
                    plugin.getSlotData().togglePlacingController(player.getName(), slot);
                    plugin.sendMessage(player, "Punch a block to serve as the controller for this slot machine.");
                    event.setCancelled(true);
                    return;
                }else {
                    plugin.sendMessage(player, "Only sides of blocks are valid targets for this operation.");
                    return;
                }
            }else if(event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.getSlotData().isPlacingController(player.getName())) {
                // Placing controller

                if(plugin.useTowny) {
                    if(!plugin.getTownyChecks().checkSingleTowny(b, player.getName())) {
                        SlotMachine slot = plugin.getSlotData().getPlacingSlot(player.getName());
                        plugin.getSlotData().togglePlacingController(player.getName(), slot);
                        plugin.sendMessage(player, plugin.getConfigData().noOwnership);
                        return;
                    }
                }

                if(plugin.useWorldGuard) {
                    if(!plugin.getWorldGuard().canBuild(player, b)) {
                        SlotMachine slot = plugin.getSlotData().getPlacingSlot(player.getName());
                        plugin.getSlotData().togglePlacingController(player.getName(), slot);
                        player.sendMessage(ChatColor.DARK_RED + "You don't have permission for this area.");
                        return;
                    }
                }

                SlotMachine slot = plugin.getSlotData().getPlacingSlot(player.getName());
                slot.setController(b);
                plugin.getSlotData().togglePlacingController(player.getName(), slot);
                plugin.getSlotData().addSlot(slot);
                plugin.getSlotData().saveSlot(slot);
                plugin.sendMessage(player, "Slot machine set up successfully!");
                event.setCancelled(true);
                return;
            }else if(event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.getSlotData().isPunchingSign(player.getName())) {
                //setting the sign

                if(plugin.useTowny) {
                    if(!plugin.getTownyChecks().checkSingleTowny(b, player.getName())) {
                        SlotMachine slot = plugin.getSlotData().getSignPunchingSlot(player.getName());
                        plugin.getSlotData().togglePunchingSign(player.getName(), slot);
                        plugin.sendMessage(player, plugin.getConfigData().noOwnership);
                        return;
                    }
                }

                if(plugin.useWorldGuard) {
                    if(!plugin.getWorldGuard().canBuild(player, b)) {
                        SlotMachine slot = plugin.getSlotData().getSignPunchingSlot(player.getName());
                        plugin.getSlotData().togglePunchingSign(player.getName(), slot);
                        player.sendMessage(ChatColor.DARK_RED + "You don't have permission for this area.");
                        return;
                    }
                }

                if (b.getType().equals(Material.WALL_SIGN) || b.getType().equals(Material.SIGN_POST)) {
                    SlotMachine slot = plugin.getSlotData().getSignPunchingSlot(player.getName());

                    Sign sign = (Sign) b.getState();
                    sign.setLine(0, "The Last");
                    sign.setLine(1, "Winner:");
                    sign.update(true);

                    slot.setSign(b);

                    plugin.getSlotData().saveSlot(slot);

                    plugin.sendMessage(player, "Successfully stored the location of the sign!");

                    plugin.getSlotData().togglePunchingSign(player.getName(), slot);
                    event.setCancelled(true);
                }else {
                    plugin.sendMessage(player, "Please make sure you are punching a sign on the wall or sign standing up. Try again.");
                    event.setCancelled(true);
                    return;
                }
            }
            
            SlotMachine slot = null;
            for(SlotMachine s : plugin.getSlotData().getSlots()) {
                if(b.equals(s.getController())) {
                    slot = s;
                    break;
                }
            }
            
            if(slot == null) return;//No slot machine found here

            //Checks if the type given is null or not and informs the player and the console.
            if (typeIsNull(player, slot.getType())) return;

            Type type = slot.getType();
            
            // Left click event
            if(event.getAction() == Action.LEFT_CLICK_BLOCK) {

                //  Player has permission
                if(PermissionUtil.canUse(player, type)) {

                    // Slot is not busy
                    if(!slot.isBusy()) {

                        // See if the slot is an item game
                        if(slot.isItem()) {
                            // Get the information about the item cost
                            int itemID, itemAmt;
                            itemID = slot.getItem();
                            itemAmt = slot.getItemAmount();

                            Material itemMat = Material.getMaterial(itemID);
                            ItemStack cost = new ItemStack(itemMat, itemAmt);

                            if(player.getInventory().contains(itemID, itemAmt)) {
                                player.getInventory().removeItem(cost);

                                //Let's go!
                                Game game = new Game(slot, player, plugin);
                                game.play();
                                return;
                            }else {
                                if (itemAmt == 1) {
                                    plugin.sendMessage(player, "Sorry, you need to have at least " + itemAmt + " " + itemMat.toString().toLowerCase() + " in your inventory to play.");
                                }else {
                                    plugin.sendMessage(player, "Sorry, you need to have at least " + itemAmt + " " + itemMat.toString().toLowerCase() + "s in your inventory to play.");
                                }
                                return;
                            }
                        }else {
                            if(!type.getItemCost().equalsIgnoreCase("0")) {
                                String[] item = type.getItemCost().split("\\,");
                                int id, amt;
                                byte data;
                                Material itemMat;
                                ItemStack cost;

                                switch(item.length) {
                                    case 1:
                                        plugin.severe("Type " + type.getName() + " has an incorrect itemCost, please fix!");
                                        player.sendMessage("Please inform the administrator that this slot machine has an incorrect configuration, thanks.");
                                        return;
                                    case 2:
                                        try {
                                            id = Integer.parseInt(item[0]);
                                            amt = Integer.parseInt(item[1]);
                                        }catch (NumberFormatException e) {
                                            plugin.severe("Type " + type.getName() + " has an incorrect itemCost, please fix.");
                                            player.sendMessage("Please inform the administrator that this slot machine has an incorrect configuration, thanks.");
                                            return;
                                        }

                                        itemMat = new ItemStack(id).getType();
                                        cost = new ItemStack(itemMat, amt);

                                        if(player.getInventory().contains(id, amt)) {
                                            player.getInventory().removeItem(cost);
                                            break;
                                        }else {
                                            if (amt == 1) {
                                                plugin.sendMessage(player, "Sorry, you need to have at least " + amt + " " + itemMat.toString().toLowerCase() + " in your inventory to play.");
                                            }else {
                                                plugin.sendMessage(player, "Sorry, you need to have at least " + amt + " " + itemMat.toString().toLowerCase() + "s in your inventory to play.");
                                            } return;
                                        }
                                    case 3:
                                        try {
                                            id = Integer.parseInt(item[0]);
                                            data = Byte.parseByte(item[1]);
                                            amt = Integer.parseInt(item[2]);
                                        }catch (NumberFormatException e) {
                                            plugin.severe("Type " + type.getName() + " has an incorrect itemCost, please fix.");
                                            player.sendMessage("Please inform the administrator that this slot machine has an incorrect configuration, thanks.");
                                            return;
                                        }

                                        itemMat = new ItemStack(id).getType();
                                        cost = new ItemStack(itemMat, amt);
                                        cost.getData().setData(data);

                                        if(player.getInventory().contains(id, amt)) {
                                            player.getInventory().removeItem(cost);
                                            break;
                                        }else {
                                            if (amt == 1) {
                                                plugin.sendMessage(player, "Sorry, you need to have at least " + amt + " " + itemMat.toString().toLowerCase() + " in your inventory to play.");
                                            }else {
                                                plugin.sendMessage(player, "Sorry, you need to have at least " + amt + " " + itemMat.toString().toLowerCase() + "s in your inventory to play.");
                                            } return;
                                        }
                                    default:
                                        plugin.severe("Type " + type.getName() + " has an incorrect itemCost, please fix!");
                                        player.sendMessage("Please inform the administrator that this slot machine has an incorrect configuration, thanks.");
                                        return;
                                }

                                chargeAndPlay(type, slot, player);
                            }else {
                                chargeAndPlay(type, slot, player);
                            }
                        }
                    } else {// Slot is busy
                        plugin.sendMessage(player, type.getMessages().get("inUse"));
                        return;
                    }
                } else {// Player does not have type permission
                    plugin.sendMessage(player, type.getMessages().get("noPermission"));
                    return;
                }
            }// End Left click

            // Right click event
            else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(PermissionUtil.isOwner(player, slot) || PermissionUtil.isAdmin(player)) {
                    if (slot.isManaged()) {
                        if(slot.getFunds() >= plugin.getTypeData().getMaxPrize(slot.getType())) {
                            slot.setEnabled(true);
                        }else {
                            slot.setEnabled(false);
                        }
                    }
                    plugin.sendMessage(player, slot.getName() +":");
                    plugin.sendMessage(player, "    Type: " + slot.getType().getName());
                    plugin.sendMessage(player, "    Owner: " + slot.getOwner());
                    plugin.sendMessage(player, "    Managed: " + slot.isManaged().toString());
                    if(slot.isManaged()) {
                        if (slot.isEnabled()) {
                            plugin.sendMessage(player, "    Enabled: " + slot.isEnabled().toString());
                        }else {
                            plugin.sendMessage(player, "    Enabled: " + ChatColor.RED + slot.isEnabled().toString());
                        }
                        plugin.sendMessage(player, "    Funds: " + slot.getFunds() + " " + plugin.getEconomy().currencyNamePlural());
                        plugin.sendMessage(player, "    Funds required: " + plugin.getTypeData().getMaxPrize(slot.getType()));
                    }
                    plugin.sendMessage(player, "    Item: " + slot.isItem().toString());
                    if(slot.isItem()) {
                        plugin.sendMessage(player, "        itemID: " + slot.getItem());
                        plugin.sendMessage(player, "        itemAmount: " + slot.getItemAmount());
                    }
                }

                //Player isn't the owner of the slot, so display the help
                else {
                    //Get the amount of help messages
                    int helpCount = type.getHelpMessages().size();
                    List<String> message = type.getHelpMessages();

                    //initiate the varible for the loop
                    int counter = 0;

                    //Start the loop for the HelpMessages
                    while (counter < helpCount) {
                        if (counter == 0) {
                            plugin.sendMessage(player, message.get(counter));
                        }else {
                            plugin.sendMessage(player, "   " + message.get(counter));
                        }
                        counter++;
                    }
                }
            }
        }
    }

    /**
     * Checks if the type passed is null or not, returns true if so and tells the player and console.
     * 
     * @param player	The player who is attempting to play this type.
     * @param type		The type that is being checked.
     * @return			True if the type is null or false if the type isn't null
     */
    private boolean typeIsNull (Player player, Type type) {
        // Check to see if the type is valid, if it's not then display an error to both the player and the console.
        if (type == null) {
            plugin.sendMessage(player, "Sorry, that seems to be a messed up CasinoSlot, please contact your server administrator.");
            plugin.error("There is an incorrect type of Casino in your server somewhere, ask " + player.getDisplayName() + " which one they just tried to play.");
            return true;
        }else {
            return false;
        }

    }

    /**
     * Charges the player money and starts the rolling of the slot.
     * 
     * @param type The type of the slot the player is wanting to play.
     * @param slot The slot the player is wanting to roll.
     * @param player The player wanting to play!
     */
    private void chargeAndPlay(Type type, SlotMachine slot, Player player) {
        // Player has enough money
        Double cost = type.getCost();
        if(plugin.getEconomy().has(player.getName(), cost)) {
            //Let's go!
            Game game = new Game(slot, player, plugin);
            game.play();
            return;
        }else {// Player does not have enough money
            plugin.sendMessage(player, type.getMessages().get("noFunds"));
            return;
        }
    }
}