package com.lms.backend.service.impl;

import com.lms.backend.exception.InvalidPromoCodeException;
import com.lms.backend.model.entity.PromoCode;
import com.lms.backend.repository.PromoCodeRepository;
import com.lms.backend.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @Override
    public double calculateDiscountedAmount(String code, double originalAmount) {
        PromoCode promoCode = getValidPromoCode(code, originalAmount);

        double discount;
        if (promoCode.getDiscountPercent() != null) {
            discount = originalAmount * (promoCode.getDiscountPercent() / 100.0);
            if (promoCode.getMaxDiscountAmount() != null) {
                discount = Math.min(discount, promoCode.getMaxDiscountAmount());
            }
        } else if (promoCode.getDiscountAmount() != null) {
            discount = promoCode.getDiscountAmount();
        } else {
            discount = 0;
        }

        double finalAmount = originalAmount - discount;
        return Math.max(finalAmount, 0);
    }

    @Override
    @Transactional
    public void markCodeAsUsed(String code) {
        promoCodeRepository.findByCodeIgnoreCase(code).ifPresent(promoCode -> {
            promoCode.setUsedCount(promoCode.getUsedCount() + 1);
            promoCodeRepository.save(promoCode);
        });
    }

    private PromoCode getValidPromoCode(String code, double originalAmount) {
        PromoCode promoCode = promoCodeRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new InvalidPromoCodeException("Mã giảm giá không tồn tại"));

        if (!promoCode.isActive()) {
            throw new InvalidPromoCodeException("Mã giảm giá đã bị ngừng sử dụng");
        }

        LocalDateTime now = LocalDateTime.now();
        if (promoCode.getStartDate() != null && now.isBefore(promoCode.getStartDate())) {
            throw new InvalidPromoCodeException("Mã giảm giá chưa đến thời gian áp dụng");
        }
        if (promoCode.getEndDate() != null && now.isAfter(promoCode.getEndDate())) {
            throw new InvalidPromoCodeException("Mã giảm giá đã hết hạn");
        }

        if (promoCode.getUsageLimit() != null && promoCode.getUsedCount() >= promoCode.getUsageLimit()) {
            throw new InvalidPromoCodeException("Số lần sử dụng vượt quá số lượng khuyến mại của chương trình");
        }

        if (promoCode.getMinOrderAmount() != null && originalAmount < promoCode.getMinOrderAmount()) {
            throw new InvalidPromoCodeException("Đơn hàng chưa đạt giá trị tối thiểu để áp dụng mã này");
        }

        return promoCode;
    }
}
