/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package com.gmail.davideblade99.health;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class PlayerManager {

    private final static Map<String, Long> COOLDOWN = new HashMap<>();
    private final static long COOLDOWN_DURATION = TimeUnit.SECONDS.toMillis(Health.getInstance().getConfig().getInt("Command cooldown"));

    private PlayerManager() {
        throw new IllegalAccessError();
    }

    public static void removePlayer(final Player player) {
        COOLDOWN.remove(player.getName());
    }

    public static void addCooldown(final Player player) {
        if (COOLDOWN_DURATION == 0)
            return;

        COOLDOWN.put(player.getName(), System.currentTimeMillis() + COOLDOWN_DURATION);
    }

    public static boolean isCooldownExpired(final Player player) {
        return System.currentTimeMillis() > getCooldownExpiration(player);
    }

    public static long getRemainingTime(final Player player) {
        return (getCooldownExpiration(player) / 1000 - System.currentTimeMillis() / 1000);
    }

    private static long getCooldownExpiration(final Player player) {
        return COOLDOWN.getOrDefault(player.getName(), 0L);
    }
}
