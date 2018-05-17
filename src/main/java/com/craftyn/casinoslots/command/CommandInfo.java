package com.craftyn.casinoslots.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Contains a definition of all the annotations {@link Command commands} should have, if a {@link Command command} doesn't have any then it isn't registered.
 * 
 * <p>
 * 
 * This helps make loading and registering and verifying commands a lot
 * easier, makes sure that before we really process a command that it
 * is structured correctly and all the right information is passed. If
 * the command takes a variety of options, then that command obviously
 * has to handle it. We just make sure that the minimum amount of
 * arguments is met and same with the maximum amount, if there is a max.
 * We also check if the commands needs a player or not, if so and the
 * sender is something other than a player we send a message stating that
 * a player context is required. The pattern is just used to match up
 * the command used to it's registered value, in regex form. We check
 * the permission stated and determine if the sender has it or not, if
 * they don't then we send a message stating they don't have permission
 * for that command. Finally we have the usage string, which is sent
 * when the sender of the command sends an incorrectly formatted
 * command. The order of checking is as defined in {@link CommandHandler#handleCommand(org.bukkit.command.CommandSender, String, String[]) CommandHandler.handleCommand}.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 *
 */
@Retention (RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    /**
     * Gets the maximum amount of arguments required, -1 if no maximum
     * (ex: Jailing someone with a reason or editing a reason).
     * 
     * @return The maximum number of arguments required, -1 if no maximum.
     */
    public int maxArgs();

    /**
     * Gets the minimum amount of arguments required.
     * 
     * @return The minimum number of arguments required.
     */
    public int minimumArgs();

    /**
     * Whether the command needs a player context or not.
     * 
     * @return True if requires a player, false if not.
     */
    public boolean needsPlayer();

    /**
     * A regex pattern that allows for alternatives to
     * the command (ex: /casino or /cs).
     * 
     * @return The regex pattern to match.
     */
    public String pattern();

    /**
     * Gets the permission required to execute this command.
     * 
     * @return The permission required.
     */
    public String permission();

    /**
     * Gets the usage message for this command.
     * 
     * @return The usage message.
     */
    public String usage();
}
