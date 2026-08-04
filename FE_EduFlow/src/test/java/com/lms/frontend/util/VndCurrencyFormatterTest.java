package com.lms.frontend.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VndCurrencyFormatterTest {

    private final VndCurrencyFormatter formatter = new VndCurrencyFormatter();

    @Test
    void formatsVietnameseDongWithDotGroupingAndSymbol() {
        assertEquals("309.000 ₫", formatter.format(309_000));
        assertEquals("1.234.568 ₫", formatter.format(new BigDecimal("1234567.5")));
        assertEquals("0 ₫", formatter.format(null));
    }
}
