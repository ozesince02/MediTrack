package com.airtribe.meditrack.patterns.billing;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.util.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Applies a discount first, then tax.
 */
public class SeniorDiscountThenTaxStrategy implements BillingStrategy {
    private final BigDecimal discountRate;
    private final BigDecimal taxRate;

    public SeniorDiscountThenTaxStrategy() {
        this(new BigDecimal("0.10"), Constants.DEFAULT_TAX_RATE);
    }

    public SeniorDiscountThenTaxStrategy(BigDecimal discountRate, BigDecimal taxRate) {
        this.discountRate = Validator.requireNonNegative(discountRate, "discountRate");
        this.taxRate = Validator.requireNonNegative(taxRate, "taxRate");
    }

    @Override
    public BigDecimal computeTotal(BigDecimal baseAmount) {
        BigDecimal base = Validator.requireNonNegative(baseAmount, "baseAmount");
        BigDecimal discounted = base.subtract(base.multiply(discountRate));
        BigDecimal tax = discounted.multiply(taxRate);
        return discounted.add(tax).setScale(2, RoundingMode.HALF_UP);
    }
}


