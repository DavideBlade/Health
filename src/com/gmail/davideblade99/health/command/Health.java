/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package com.gmail.davideblade99.health.command;

import com.gmail.davideblade99.health.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public final class Health extends CommandFramework {

    public Health(final com.gmail.davideblade99.health.Health instance) {
        super(instance);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 0) {
            final String[] message = {
                    "&8------------[&cHealth&8]------------",
                    "&8Developer: &cDavideBlade",
                    "&8Version: &c" + plugin.getDescription().getVersion(),
                    "&8Help: &c/health help"
            };
            MessageUtil.sendChatMessage(sender, message);
            return true;
        }


        if (args[0].equalsIgnoreCase("help")) {
            final String[] message = {
                    "&8--------[&cHealth commands&8]--------",
                    "&8/health - &cPlugin information.",
                    "&8/healme - &cRegenerate all hearts.",
                    "&8/feedme - &cRegenerate all cutlets.",
                    "&8/hearts <hearts> [player] - &cSet health to the specified number of hearts.",
                    "&8/cutlets <cutlets> [player] - &cSet satiety to the specified number of cutlets.",
                    "&8/regen [player] - &cSet regen effect for 8 seconds."
            };
            MessageUtil.sendChatMessage(sender, message);
        } else
            MessageUtil.sendChatMessage(sender, "&cUnknow sub-command \"" + args[0] + "\". Use /health help.");
        return true;
    }
}