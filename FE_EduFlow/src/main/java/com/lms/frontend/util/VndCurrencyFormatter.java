package com.lms.frontend.util;

import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Component("vndCurrency")
public class VndCurrencyFormatter {

    private static final Locale VIETNAM = Locale.forLanguageTag("vi-VN");

    public String format(Number amount) {
        DecimalFormat formatter = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(VIETNAM));
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        return formatter.format(amount == null ? 0 : amount) + " ₫";
    }
}
