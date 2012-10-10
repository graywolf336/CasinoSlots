# CasinoSlots #

A majorly updated version of AnCasino started by Darazo

### Update: 2.5.3 ###
* Using the logger provided with Bukkit
* Added support for withdrawing money from a player's account, if the reward money is negative it withdraws the money from the player
* Added a check to see if the config exists or not with the config-version property. If the config-version does not equal 1.0, the config will copy over it's default options.
* Added config option to display the information when we kept a chunk loaded, this defaults to false.
* Fixed the chunk listener from testing the same chunk over and over.

### Update: 2.5.2 ###
* Fixed the stats.yml file being over wrote each restart/reload
* Fixed the stats.yml displaying incorrect data
* Fixed the slots.yml not being saved when something was done to it, mostly noticeable when using managed slots (thanks LlmDl for noticing this)
* Fixed the prefix, colors, and all that from not changing when doing a /casino reload
* Fixed an NPE with the 'give' action.
* Added support for the new color codes
* Added new stats, wins and losts. This way you can keep track of how many people won it and how many people lost.
* Added a debugging option, I would advise as to not use this...it tends to spam the console a lot.
* Simplified the permissions check
* Made reloading the config and what happens during that a little better.

### Update: 2.5.1 ###
* Made sure the stats.yml file is being saved upon disabling of the plugin

### Update: 2.5.1 ###
* Fixed the slots.yml file not saving when being changed

### Update: 2.5.0 ###
* Fixed the reels displaying duplicate blocks
* Fixed the stats.yml not being saved
* Re-added the reload command
* Added the ability to give out enchanted items as rewards.

### Update: 2.4.9 ###
* Added /casino set sign <slot>
** This makes it so  you can punch and sign and that sign will update with the latest winner
* Added /casino set type <slotname> <type>
** This allows you to change the type of a slot machine to something different
* Enhanced the way the PlayerListner is handled
* Made the plugin check the chunks for both the controller and one of the reel blocks
* Started working on getting managed slots able to be made with permissions
* Created a check when loading for the seemingly random NPE on getBlock, tells you what to do now