package com.seam.util;

import java.util.regex.Pattern;

public final class PiiUtils {
    private static final Pattern RESIDENT_REG_NO = Pattern.compile("\\b\\d{6}[- ]?\\d{7}\\b");
    private static final Pattern ACCOUNT_NO = Pattern.compile("\\b\\d{4}[- ]?\\d{4}[- ]?\\d{4}\\b");
    private static final Pattern PHONE_NO = Pattern.compile("\\b(\\+\\d{1,3}[- ]?)?\\d{2,4}[- ]?\\d{3,4}[- ]?\\d{4}\\b");

    public static boolean containsPii(String text) {
        if (text == null) return false;
        return RESIDENT_REG_NO.matcher(text).find() || ACCOUNT_NO.matcher(text).find() || PHONE_NO.matcher(text).find();
    }

    public static String maskPii(String text) {
        if (text == null) return null;
        String masked = RESIDENT_REG_NO.matcher(text).replaceAll("[REDACTED]");
        masked = ACCOUNT_NO.matcher(masked).replaceAll("[REDACTED]");
        masked = PHONE_NO.matcher(masked).replaceAll("[REDACTED]");
        return masked;
    }
}
