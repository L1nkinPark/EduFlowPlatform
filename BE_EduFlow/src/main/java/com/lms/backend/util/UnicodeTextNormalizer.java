package com.lms.backend.util;

import java.text.Normalizer;

public final class UnicodeTextNormalizer {

    private UnicodeTextNormalizer() {
    }

    public static String normalize(String value) {
        return value == null ? null : Normalizer.normalize(value, Normalizer.Form.NFC);
    }

    public static String normalizeAndTrim(String value) {
        String normalized = normalize(value);
        return normalized == null ? null : normalized.strip();
    }

    public static String normalizeAndTrimToNull(String value) {
        String normalized = normalizeAndTrim(value);
        return normalized == null || normalized.isEmpty() ? null : normalized;
    }
}
