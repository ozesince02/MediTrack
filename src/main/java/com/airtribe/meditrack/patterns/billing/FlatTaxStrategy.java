package com.airtribe.meditrack.patterns.billing;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.util.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Applies a flat tax rate to the base amount.
 */
public class FlatTaxStrategy implements BillingStrategy {
    private final BigDecimal taxRate;

    public FlatTaxStrategy() {
        this(Constants.DEFAULT_TAX_RATE);
    }

    public FlatTaxStrategy(BigDecimal taxRate) {
        this.taxRate = Validator.requireNonNegative(taxRate, "taxRate");
    }

    @Override
    public BigDecimal computeTotal(BigDecimal baseAmount) {
        BigDecimal base = Validator.requireNonNegative(baseAmount, "baseAmount");
        BigDecimal tax = base.multiply(taxRate);
        return base.add(tax).setScale(2, RoundingMode.HALF_UP);
    }
}


