/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package com.gmail.davideblade99.health.command;

import com.gmail.davideblade99.health.Health;
import com.gmail.davideblade99.health.PlayerManager;
import com.gmail.davideblade99.health.util.MessageUtil;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class Healme extends CommandFramework {

    public Healme(final Health instance) {
        super(instance);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.sendChatMessage(sender, plugin.getMessage("Command only for player"));
            return true;
        }

        final Player player = (Player) sender;
        if (!player.hasPermission("health.healme")) {
            MessageUtil.sendChatMessage(player, plugin.getMessage("No permission"));
            return true;
        }
        if (player.getGameMode() != GameMode.SURVIVAL) {
            MessageUtil.sendChatMessage(player, plugin.getMessage("Required survival"));
            return true;
        }
        if (!PlayerManager.isCooldownExpired(player)) {
            final long remainingTime = PlayerManager.getRemainingTime(player);
            final String word = remainingTime == 1 ? plugin.getMessage("Second") : plugin.getMessage("Seconds");

            MessageUtil.sendChatMessage(player, plugin.getMessage("Cooldown not finished").replace("%time", Long.toString(remainingTime)).replace("%type", word));
            return true;
        }

        player.setHealth(20);

        PlayerManager.addCooldown(player);

        MessageUtil.sendChatMessage(player, plugin.getMessage("Healme successful"));
        return true;
    }
}