/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package com.gmail.davideblade99.health.command;

import com.gmail.davideblade99.health.Health;
import com.gmail.davideblade99.health.PlayerManager;
import com.gmail.davideblade99.health.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Regen extends CommandFramework {

    public Regen(final Health instance) {
        super(instance);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.sendChatMessage(sender, plugin.getMessage("Command only for player"));
            return true;
        }

        final Player player = (Player) sender;
        if (!player.hasPermission("health.regen")) {
            MessageUtil.sendChatMessage(player, plugin.getMessage("No permission"));
            return true;
        }
        if (!PlayerManager.isCooldownExpired(player)) {
            final long remainingTime = PlayerManager.getRemainingTime(player);
            final String word = remainingTime == 1 ? plugin.getMessage("Second") : plugin.getMessage("Seconds");

            MessageUtil.sendChatMessage(player, plugin.getMessage("Cooldown not finished").replace("%time", Long.toString(remainingTime)).replace("%type", word));
            return true;
        }

        if (args.length == 0) {
            if (player.getGameMode() != GameMode.SURVIVAL) {
                MessageUtil.sendChatMessage(player, plugin.getMessage("Required survival"));
                return true;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 2, false, false), true);
            MessageUtil.sendChatMessage(player, plugin.getMessage("Regen successful"));
            PlayerManager.addCooldown(player);
        } else {
            if (!player.hasPermission("health.regen.others")) {
                MessageUtil.sendChatMessage(player, plugin.getMessage("No permission"));
                return true;
            }

            final Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                MessageUtil.sendChatMessage(player, plugin.getMessage("Player not found").replace("%player", args[0]));
                return true;
            }
            if (target.getGameMode() != GameMode.SURVIVAL) {
                MessageUtil.sendChatMessage(player, plugin.getMessage("Player not in survival").replace("%player", target.getName()));
                return true;
            }

            target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 2, false, false), true);

            MessageUtil.sendChatMessage(player, plugin.getMessage("Regen others successful").replace("%player", target.getName()));
            MessageUtil.sendChatMessage(target, plugin.getMessage("Regen by other").replace("%player", sender.getName()));

            PlayerManager.addCooldown(player);
        }
        return true;
    }
}