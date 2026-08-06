package com.lms.backend.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Converts the VND price stored by EduFlow to the integer amount required by
 * VNPay. VNPay represents a 1 VND payment as 100 in {@code vnp_Amount}.
 */
public final class VnPayAmountUtil {

    public static final long MINIMUM_PAYMENT_VND = 5_000L;
    public static final long MAXIMUM_PAYMENT_VND = 9_999_999_999L;

    private VnPayAmountUtil() {
    }

    public static long toVnd(double amountVnd) {
        if (!Double.isFinite(amountVnd) || amountVnd <= 0) {
            throw new IllegalArgumentException("Payment amount must be a positive VND value");
        }

        return BigDecimal.valueOf(amountVnd)
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
    }

    public static long toGatewayAmount(double amountVnd) {
        return toGatewayAmount(toVnd(amountVnd));
    }

    public static long toGatewayAmount(long amountVnd) {
        if (amountVnd < MINIMUM_PAYMENT_VND) {
            throw new IllegalArgumentException(
                    "Payment amount must be at least 5,000 VND");
        }
        if (amountVnd > MAXIMUM_PAYMENT_VND) {
            throw new IllegalArgumentException(
                    "Payment amount exceeds the VNPay limit");
        }
        return Math.multiplyExact(amountVnd, 100L);
    }

    public static long fromGatewayAmount(long gatewayAmount) {
        if (gatewayAmount <= 0 || gatewayAmount % 100L != 0) {
            throw new IllegalArgumentException("Invalid VNPay amount");
        }
        return gatewayAmount / 100L;
    }
}
