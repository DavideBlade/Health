/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package com.gmail.davideblade99.health.command;

import com.gmail.davideblade99.health.Health;
import com.gmail.davideblade99.health.PlayerManager;
import com.gmail.davideblade99.health.util.MathUtil;
import com.gmail.davideblade99.health.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command that allows you to set the maximum number of hearts of a player
 */
public final class MaxHearts extends CommandFramework {

    public MaxHearts(final Health instance) {
        super(instance);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 0) {
            MessageUtil.sendChatMessage(sender, plugin.getMessage("Max hearts usage"));
            return true;
        }
        if (MathUtil.isNotNumeric(args[0])) {
            MessageUtil.sendChatMessage(sender, plugin.getMessage("Only number"));
            return true;
        }

        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (!player.hasPermission("health.maxhearts")) {
                MessageUtil.sendChatMessage(player, plugin.getMessage("No permission"));
                return true;
            }

            if (args.length == 1) {
                if (player.getGameMode() != GameMode.SURVIVAL) {
                    MessageUtil.sendChatMessage(player, plugin.getMessage("Required survival"));
                    return true;
                }

                final int hearts = (int) (Double.parseDouble(args[0]) * 2);
                if (hearts >= 1 && hearts <= 512) {
                    player.setMaxHealth(hearts);

                    MessageUtil.sendChatMessage(player, plugin.getMessage("Max hearts successful").replace("%number", Integer.toString(hearts / 2)));
                } else
                    MessageUtil.sendChatMessage(player, plugin.getMessage("Number between 1-512"));

            } else {
                if (!player.hasPermission("health.maxhearts.others")) {
                    MessageUtil.sendChatMessage(player, plugin.getMessage("No permission"));
                    return true;
                }
                final Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    MessageUtil.sendChatMessage(player, plugin.getMessage("Player not found").replace("%player", args[1]));
                    return true;
                }
                if (target.getGameMode() != GameMode.SURVIVAL) {
                    MessageUtil.sendChatMessage(player, plugin.getMessage("Player not in survival").replace("%player", target.getName()));
                    return true;
                }

                final int hearts = (int) (Double.parseDouble(args[0]) * 2);
                if (hearts >= 1 && hearts <= 512) {
                    target.setMaxHealth(hearts);

                    MessageUtil.sendChatMessage(player, plugin.getMessage("Max hearts others successful").replace("%number", Integer.toString(hearts / 2)).replace("%player", target.getName()));
                    MessageUtil.sendChatMessage(target, plugin.getMessage("Max hearts by other").replace("%number", Integer.toString(hearts / 2)).replace("%player", sender.getName() + ""));
                } else
                    MessageUtil.sendChatMessage(player, plugin.getMessage("Number between 1-512"));
            }
        } else {
            if (args.length == 1)
                MessageUtil.sendChatMessage(sender, plugin.getMessage("Select player"));
            else {
                final Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    MessageUtil.sendChatMessage(sender, plugin.getMessage("Player not found").replace("%player", args[1]));
                    return true;
                }
                if (target.getGameMode() != GameMode.SURVIVAL) {
                    MessageUtil.sendChatMessage(sender, plugin.getMessage("Player not in survival").replace("%player", target.getName()));
                    return true;
                }

                final int hearts = (int) (Double.parseDouble(args[0]) * 2);
                if (hearts >= 1 && hearts <= 512) { // Minecraft supports a maximum of 1024 health (512 hearts)
                    target.setMaxHealth(hearts);

                    MessageUtil.sendChatMessage(sender, plugin.getMessage("Max hearts others successful").replace("%number", Integer.toString(hearts / 2)).replace("%player", target.getName()));
                    MessageUtil.sendChatMessage(target, plugin.getMessage("Max hearts by other").replace("%number", Integer.toString(hearts / 2)).replace("%player", "console"));
                } else
                    MessageUtil.sendChatMessage(sender, plugin.getMessage("Number between 1-512"));
            }
        }
        return true;
    }
}
