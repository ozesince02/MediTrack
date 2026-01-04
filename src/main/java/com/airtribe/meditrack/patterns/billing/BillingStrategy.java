package com.airtribe.meditrack.patterns.billing;

import java.math.BigDecimal;

/**
 * Strategy pattern for billing computations.
 */
public interface BillingStrategy {
    BigDecimal computeTotal(BigDecimal baseAmount);

    default String name() {
        return getClass().getSimpleName();
    }
}


