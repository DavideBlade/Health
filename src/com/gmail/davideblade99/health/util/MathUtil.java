/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package com.gmail.davideblade99.health.util;

public final class MathUtil {

    private MathUtil() {
        throw new IllegalAccessError();
    }

    public static boolean isNotNumeric(final String string) {
        return !string.matches("\\d{1,100}");
    }
}