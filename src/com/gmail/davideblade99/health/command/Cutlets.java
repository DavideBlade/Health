/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package com.gmail.davideblade99.health.command;

import com.gmail.davideblade99.health.Health;
import com.gmail.davideblade99.health.PlayerManager;
import com.gmail.davideblade99.health.util.MessageUtil;
import com.gmail.davideblade99.health.util.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class Cutlets extends CommandFramework {

    public Cutlets(final Health instance) {
        super(instance);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 0) {
            MessageUtil.sendChatMessage(sender, plugin.getMessage("Cutlets usage"));
            return true;
        }
        if (MathUtil.isNotNumeric(args[0])) {
            MessageUtil.sendChatMessage(sender, plugin.getMessage("Only number"));
            return true;
        }

        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (!player.hasPermission("health.cutlets")) {
                MessageUtil.sendChatMessage(player, plugin.getMessage("No permission"));
                return true;
            }
            if (!PlayerManager.isCooldownExpired(player)) {
                final long remainingTime = PlayerManager.getRemainingTime(player);
                final String word = MessageUtil.plural(remainingTime, plugin.getMessage("Second"), plugin.getMessage("Seconds"));

                MessageUtil.sendChatMessage(player, plugin.getMessage("Cooldown not finished").replace("%time", Long.toString(remainingTime)).replace("%type", word));
                return true;
            }


            if (args.length == 1) {
                if (player.getGameMode() != GameMode.SURVIVAL) {
                    MessageUtil.sendChatMessage(player, plugin.getMessage("Required survival"));
                    return true;
                }

                final int foods = (int) (Double.parseDouble(args[0]) * 2);
                if (foods >= 1 && foods <= 20) {
                    player.setFoodLevel(foods);

                    MessageUtil.sendChatMessage(player, plugin.getMessage("Cutlets successful").replace("%number", Integer.toString(foods / 2)));

                    PlayerManager.addCooldown(player);
                } else
                    MessageUtil.sendChatMessage(player, plugin.getMessage("Number between 1-10"));
            } else {
                if (!player.hasPermission("health.cutlets.others")) {
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

                final int foods = (int) (Double.parseDouble(args[0]) * 2);
                if (foods >= 1 && foods <= 20) {
                    target.setFoodLevel(foods);

                    MessageUtil.sendChatMessage(player, plugin.getMessage("Cutlets others successful").replace("%number", Integer.toString(foods / 2)).replace("%player", target.getName()));
                    MessageUtil.sendChatMessage(target, plugin.getMessage("Cutlets by other").replace("%number", Integer.toString(foods / 2)).replace("%player", sender.getName() + ""));

                    PlayerManager.addCooldown(player);
                } else
                    MessageUtil.sendChatMessage(player, plugin.getMessage("Number between 1-10"));
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

                final int foods = (int) (Double.parseDouble(args[0]) * 2);
                if (foods >= 1 && foods <= 20) {
                    MessageUtil.sendChatMessage(sender, plugin.getMessage("Cutlets others successful").replace("%number", Integer.toString(foods / 2)).replace("%player", target.getName()));
                    target.setHealth(foods);
                    MessageUtil.sendChatMessage(target, plugin.getMessage("Cutlets by other").replace("%number", Integer.toString(foods / 2)).replace("%player", "console"));
                } else
                    MessageUtil.sendChatMessage(sender, plugin.getMessage("Number between 1-10"));
            }
        }
        return true;
    }
}