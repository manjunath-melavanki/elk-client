package com.tu.elk.request.util;

import static java.lang.String.format;

/**
 * Utility class for the ELK client
 * @author  Manjunath Melavanki
 * date    17-11-2023
 */
public class Utils {
    private Utils() {
    }

    /**
     * Formatting the IllegalArgumentException
     * @param message
     * @param args
     * @return IllegalArgumentException
     */
    public static IllegalArgumentException newIAE(String message, Object... args) {
        return new IllegalArgumentException(format(message, args));
    }
}
