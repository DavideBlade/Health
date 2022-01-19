/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package com.gmail.davideblade99.health.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class MessageUtil {

    private final static char COLOR_CHAR = '&';
    private final static String PREFIX = "&8[Health] ";

    private MessageUtil() {
        throw new IllegalAccessError();
    }

    public static void sendChatMessage(final CommandSender receiver, String message, final boolean prefix) {
        if (message == null || message.isEmpty())
            return;

        if (prefix)
            message = PREFIX + message;

        receiver.sendMessage(ChatColor.translateAlternateColorCodes(COLOR_CHAR, message));
    }

    public static void sendChatMessage(final CommandSender receiver, final String message) {
        sendChatMessage(receiver, message, false);
    }

    public static void sendChatMessage(final CommandSender receiver, final String[] messages) {
        for (String message : messages)
            sendChatMessage(receiver, message);
    }

    public static void sendConsoleMessage(final String message, final boolean prefix) {
        sendChatMessage(Bukkit.getConsoleSender(), message, prefix);
    }

    public static void sendConsoleMessage(final String message) {
        sendConsoleMessage(message, true);
    }

    /**
     * Returns the singular or plural form of a noun depending on the amount
     *
     * @param amount   The amount of the noun
     * @param singular The singular form of the noun
     * @param plural   The plural form of the noun
     * @return The correctly formed noun
     */
    public static String plural(final long amount, final String singular, final String plural) {
        return amount == 1 ? singular : plural;
    }
}