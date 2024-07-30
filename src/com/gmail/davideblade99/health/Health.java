/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package com.gmail.davideblade99.health;

import com.gmail.davideblade99.health.command.*;
import com.gmail.davideblade99.health.listener.PlayerQuit;
import com.gmail.davideblade99.health.util.MessageUtil;
import com.gmail.davideblade99.health.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.NumberConversions;

import java.io.File;

public final class Health extends JavaPlugin {

    private final static String[] SUPPORTED_VERSIONS = {"1.8", "1.9", "1.10", "1.11", "1.12", "1.13", "1.14", "1.15", "1.16", "1.17", "1.18", "1.19", "1.20", "1.21"};
    private final static FileConfiguration MESSAGES = new YamlConfiguration();

    private static Health instance;

    @Override
    public void onEnable() {
        instance = this;

        final String pluginVersion = getDescription().getVersion();
        new Updater(this).checkForUpdate(newVersion -> {
            final String currentVersion = pluginVersion.contains(" ") ? pluginVersion.split(" ")[0] : pluginVersion;

            MessageUtil.sendConsoleMessage("&bFound a new version: " + newVersion + " (Yours: v" + currentVersion + ")");
            MessageUtil.sendConsoleMessage("&bDownload it on spigot:");
            MessageUtil.sendConsoleMessage("&bspigotmc.org/resources/99340");
        });

        if (!checkVersion()) {
            MessageUtil.sendConsoleMessage("&cThis version of Health only supports the following versions:" + String.join(", ", SUPPORTED_VERSIONS));
            disablePlugin();
            return;
        }

        createConfig();
        try {
            loadMessages();
        } catch (final InvalidConfigurationException ignored) {
            disablePlugin();
            return;
        }

        registerListeners();
        registerCommands();

        MessageUtil.sendConsoleMessage("&bHealth has been enabled! (Version: " + pluginVersion + ")", false);
    }

    @Override
    public void onDisable() {
        MessageUtil.sendConsoleMessage("&bHealth has been disabled! (Version: " + getDescription().getVersion() + ")", false);
    }

    public String getMessage(final String path) {
        return MESSAGES.getString(path);
    }

    private void disablePlugin() {
        setEnabled(false);
    }

    /**
     * @return true if the Minecraft server version is supported, otherwise false
     */
    private boolean checkVersion() {
        final String serverVersion = Bukkit.getVersion();

        for (String version : SUPPORTED_VERSIONS)
            if (serverVersion.contains(version))
                return true;

        return false;
    }

    /**
     * Create config file
     */
    private void createConfig() {
        final File configFile = FileUtil.CONFIG_FILE;
        if (!configFile.exists())
            FileUtil.copyFile("config.yml", configFile);
    }

    private void loadMessages() throws InvalidConfigurationException {
        final String extension = getConfig().getString("Locale");
        final File messagesFile = new File(getDataFolder() + "/Messages", "messages_" + extension + ".yml");

        if (!messagesFile.exists())
            FileUtil.copyFile("messages_" + extension + ".yml", messagesFile);

        try {
            MESSAGES.load(messagesFile);
        } catch (final Exception ignore) {
            MessageUtil.sendConsoleMessage("&cFailed to load messages_" + extension + ".yml.");

            throw new InvalidConfigurationException();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
    }

    private void registerCommands() {
        getCommand("Feedme").setExecutor(new Feedme(this));
        getCommand("Healme").setExecutor(new Healme(this));
        getCommand("Cutlets").setExecutor(new Cutlets(this));
        getCommand("Health").setExecutor(new com.gmail.davideblade99.health.command.Health(this));
        getCommand("Hearts").setExecutor(new Hearts(this));
        getCommand("Regen").setExecutor(new Regen(this));
        getCommand("MaxHearts").setExecutor(new MaxHearts(this));
    }

    public static Health getInstance() {
        return instance;
    }
}