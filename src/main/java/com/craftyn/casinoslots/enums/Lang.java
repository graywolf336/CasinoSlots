package com.craftyn.casinoslots.enums;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
    /** The message sent whenever the sender/player doesn't have permission. */
    NOPERMISSION("general"),
    /** The message sent whenever the sender/player supplies a number format that is incorrect. */
    NUMBERFORMATINCORRECT("general"),
    /** The message sent whenever something is done that needs a player but doesn't have it. */
    PLAYERCONTEXTREQUIRED("general"),
    /** The message sent to the sender when the plugin data has been reloaded. */
    PLUGINRELOADED("general"),
    /** The message sent to the sender of a command when the plugin didn't start correct. */
    PLUGINNOTLOADED("general"),
    /** The message sent whenever someone does a command we don't know. */
    UNKNOWNCOMMAND("general");

    private String                   section, name, path;
    private static YamlConfiguration lang;

    Lang(String section) {
        this.section = section;
        this.name = toString().toLowerCase();
        this.path = "language." + this.section + "." + this.name;
    }

    Lang(String section, String name) {
        this.section = section;
        this.name = name;
        this.path = "language." + this.section + "." + this.name;
    }

    /**
     * Sets the {@link YamlConfiguration} instance to use.
     *
     * @param file
     *            of the language to use.
     */
    public static void setFile(YamlConfiguration file) {
        lang = file;
    }

    /** Gets the {@link YamlConfiguration} instance. */
    public static YamlConfiguration getFile() {
        return lang;
    }

    /** Writes any new language settings to the language file in storage. */
    public static boolean writeNewLanguage(YamlConfiguration newLang) {
        boolean anything = false;

        for(Lang l : values()) {
            if(!lang.contains(l.path)) {
                lang.set(l.path, newLang.getString(l.path));
                anything = true;
            }
        }

        return anything;
    }

    /** Returns the message in the language, no variables are replaced. */
    public String get() {
        return get(new String[] {});
    }

    /** Returns the message in the language, no variables are replaced. */
    public String get(Lang langString) {
        return get(new String[] { langString.get() });
    }

    /**
     * Returns the message in the language, with the provided variables being replaced.
     *
     * @param variables
     *            All the variables to replace, in order from 0 to however many.
     * @return The message as a colorful message or an empty message if that
     *         isn't defined in the language file.
     */
    public String get(String... variables) {
        String message = lang.getString(path);

        if (message == null) return "";

        for (int i = 0; i < variables.length; i++) {
            message = message.replaceAll("%" + i + "%", variables[i]);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
