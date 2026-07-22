package com.lms.backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "promo_codes")
public class PromoCode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    // Percentage discount, e.g. 10 = 10%. If null, fixedAmount is used instead.
    @Column(name = "discount_percent")
    private Double discountPercent;

    // Fixed amount discount in VND. Used when discountPercent is null.
    @Column(name = "discount_amount")
    private Double discountAmount;

    // Maximum discount amount in VND when discountPercent is used (cap).
    @Column(name = "max_discount_amount")
    private Double maxDiscountAmount;

    @Column(name = "min_order_amount")
    private Double minOrderAmount;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    // Maximum total number of times this code can be used. Null = unlimited.
    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "used_count", nullable = false)
    private int usedCount = 0;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}
