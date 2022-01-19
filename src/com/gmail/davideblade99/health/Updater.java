package com.gmail.davideblade99.health;

import com.gmail.davideblade99.health.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.IllegalPluginAccessException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

final class Updater {

    public interface ResponseHandler {

        /**
         * Called when the updater finds a new version.
         *
         * @param newVersion - the new version
         */
        void onUpdateFound(final String newVersion);
    }

    private final Health plugin;

    Updater(final Health instance) {
        this.plugin = instance;
    }

    void checkForUpdate(final ResponseHandler responseHandler) {
        new Thread(() -> {
            try {
                final HttpURLConnection con = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=99340").openConnection();
                final String newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();

                if (isNewerVersion(newVersion)) {
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        if (newVersion.contains(" "))
                            responseHandler.onUpdateFound(newVersion.split(" ")[0]);
                        else
                            responseHandler.onUpdateFound(newVersion);
                    });
                }
            }
            catch (final IOException ignored) {
                MessageUtil.sendConsoleMessage("&cCould not contact Spigot to check for updates.");
            }
            catch (final IllegalPluginAccessException ignored) {
                // Plugin not enabled
            }
            catch (final Exception e) {
                e.printStackTrace();
                MessageUtil.sendConsoleMessage("&cUnable to check for updates: unhandled exception.");
            }
        }).start();
    }

    /**
     * Compare the version found with the plugin's version
     */
    private boolean isNewerVersion(final String versionOnSpigot) {
        return !plugin.getDescription().getVersion().equals(versionOnSpigot);
    }
}