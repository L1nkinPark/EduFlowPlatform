package com.lms.backend.util;

import org.junit.jupiter.api.Test;

import java.text.Normalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnicodeTextNormalizerTest {

    @Test
    void composesVietnameseTextAndTrimsWhitespace() {
        String decomposed = Normalizer.normalize("  CHƯƠNG 2 KHỐI XỬ LÝ TRUNG TÂM  ", Normalizer.Form.NFD);

        String result = UnicodeTextNormalizer.normalizeAndTrim(decomposed);

        assertEquals("CHƯƠNG 2 KHỐI XỬ LÝ TRUNG TÂM", result);
        assertTrue(Normalizer.isNormalized(result, Normalizer.Form.NFC));
    }

    @Test
    void convertsBlankTextToNull() {
        assertNull(UnicodeTextNormalizer.normalizeAndTrimToNull("   "));
        assertNull(UnicodeTextNormalizer.normalizeAndTrimToNull(null));
    }
}
