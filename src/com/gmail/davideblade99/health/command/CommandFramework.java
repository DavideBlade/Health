/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package com.gmail.davideblade99.health.command;

import com.gmail.davideblade99.health.Health;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

abstract class CommandFramework implements CommandExecutor {

    final Health plugin;

    CommandFramework(final Health instance) {
        this.plugin = instance;
    }
}