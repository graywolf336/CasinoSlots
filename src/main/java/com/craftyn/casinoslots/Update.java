package com.craftyn.casinoslots;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Update {
    private CasinoSlots plugin;

    // The project's unique ID
    private static final int projectID = 37985;

    // Static information for querying the API
    private static final String BUKKIT_API_QUERY = "https://api.curseforge.com/servermods/files?projectIds=" + projectID;
    private static final String CI_DEV_API_QUERY = "https://ci.craftyn.com/job/CasinoSlots/lastSuccessfulBuild/api/json";
    private boolean needed = false;

    // The url for the new file and file version
    private String fileUrl = "", version = "";

    public Update(CasinoSlots plugin) {
        this.plugin = plugin;
    }

    public void query() {
        String channel = plugin.getConfig().getString("options.update-checking.channel");
        URL url = null;

        try {
            url = channel.equalsIgnoreCase("dev") ? new URL(CI_DEV_API_QUERY) : new URL(BUKKIT_API_QUERY);
        } catch (MalformedURLException e) {
            plugin.getLogger().warning("The url for checking for an update was malformed, please open a ticket about this.");
            return;
        }

        try {
            // Open a connection and query the project
            URLConnection conn = url.openConnection();

            // Add the user-agent to identify the program
            conn.addRequestProperty("User-Agent", "CasinoSlots Update Checker");

            // Read the response of the query
            // The response will be in a JSON format, so only reading one line is necessary.
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();

            if(channel.equalsIgnoreCase("dev")) {
                // Parse the object from the response from the CI server
                JSONObject obj = (JSONObject) JSONValue.parse(response);
                // Get the latest build number
                long number = Long.parseLong(obj.get("number").toString());
                // Get the current running version
                String[] ver = plugin.getDescription().getVersion().split("-b");
                // Let them know they're on a custom version when on build #0.
                if(ver[ver.length - 1].equalsIgnoreCase("0")) {
                    plugin.getLogger().info("You are using a custom version, you can disable update checking.");
                }

                // Parse the current build number to a number
                long curr = Long.parseLong(ver[ver.length - 1]);
                plugin.debug(number + " verus " + curr);
                // Check if the build on the CI server is higher than the one we're running
                needed = number > curr;

                // Alert the console that an update is needed, if it is needed
                if(needed) {
                    this.version = obj.get("fullDisplayName").toString();
                    this.fileUrl = obj.get("url").toString();
                    plugin.getLogger().info("New development version of CasinoSlots is available: " + this.version);
                    plugin.getLogger().info(this.fileUrl);
                }
            }else {
                // Parse the array of files from the query's response
                JSONArray array = (JSONArray) JSONValue.parse(response);

                // Make sure there are results returned
                if (array.size() > 0) {
                    // Get the newest file's details
                    JSONObject latest = (JSONObject) array.get(array.size() - 1);

                    // Split the numbers into their own separate items
                    String remoteVer = ((String) latest.get("name")).split(" ")[1];
                    String currentVer = plugin.getDescription().getVersion().split("-")[0];

                    plugin.debug(remoteVer + " verus " + currentVer);
                    this.needed = this.versionCompare(remoteVer, currentVer) > 0;

                    if(needed) {
                        this.version = latest.get("name").toString();
                        this.fileUrl = latest.get("fileUrl").toString();
                        plugin.getLogger().info("New stable version of CasinoSlots is available: " + this.version);
                        plugin.getLogger().info(this.fileUrl);
                    }
                }
            }
        } catch (IOException e) {
            // There was an error reading the query
            e.printStackTrace();
            plugin.getLogger().severe("There was an error checking for an update, please see the above stacktrace for details before reporting.");
            return;
        }
    }

    /**
     * Compares two version strings.
     * 
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     * 
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     * 
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     *         The result is a positive integer if str1 is _numerically_ greater than str2.
     *         The result is zero if the strings are _numerically_ equal.
     */
    private Integer versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }

        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            return Integer.signum(Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i])));
        }

        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        else {
            return Integer.signum(vals1.length - vals2.length);
        }
    }

    /**
     * Returns true if there is an update needed, false if not.
     * 
     * @return Whether an update is available
     */
    public boolean isAvailable() {
        return this.needed;
    }

    /**
     * Returns the new version.
     * 
     * @return The string name of the new version
     */
    public String getNewVersion() {
        return this.version;
    }

    /**
     * Returns the new file url.
     * 
     * @return New file's url
     */
    public String getFileUrl() {
        return this.fileUrl;
    }
}
