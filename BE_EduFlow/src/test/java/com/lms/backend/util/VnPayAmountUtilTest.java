package com.lms.backend.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VnPayAmountUtilTest {

    @Test
    void convertsDisplayedVndPriceToVnPayAmount() {
        assertEquals(490_000L, VnPayAmountUtil.toVnd(490_000.0));
        assertEquals(49_000_000L, VnPayAmountUtil.toGatewayAmount(490_000.0));
    }

    @Test
    void roundsDiscountedPriceOnceBeforeSendingItToVnPay() {
        assertEquals(470_001L, VnPayAmountUtil.toVnd(470_000.5));
        assertEquals(47_000_100L, VnPayAmountUtil.toGatewayAmount(470_000.5));
    }

    @Test
    void rejectsAmountsThatVnPayCannotAcceptInsteadOfSilentlyChangingThePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> VnPayAmountUtil.toGatewayAmount(4_999.0));
    }

    @Test
    void restoresTheExactVndAmountReturnedByVnPay() {
        assertEquals(490_000L,
                VnPayAmountUtil.fromGatewayAmount(49_000_000L));
        assertThrows(IllegalArgumentException.class,
                () -> VnPayAmountUtil.fromGatewayAmount(49_000_001L));
    }
}
