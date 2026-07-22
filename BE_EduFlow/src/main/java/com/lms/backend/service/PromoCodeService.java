package com.lms.backend.service;

public interface PromoCodeService {

    /**
     * Validates the promo code against the order amount and returns the
     * final discounted amount. Throws InvalidPromoCodeException if the code
     * is invalid, expired, over its usage limit, or the order amount does
     * not meet the minimum required.
     */
    double calculateDiscountedAmount(String code, double originalAmount);

    /**
     * Marks the promo code as used (increments usedCount). Should be called
     * only after a successful payment.
     */
    void markCodeAsUsed(String code);
}
