package com.airtribe.meditrack.patterns.billing;

import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.util.Validator;

import java.math.BigDecimal;

/**
 * Factory pattern for bill creation.
 */
public final class BillFactory {
    private BillFactory() {}

    public static Bill createConsultationBill(String appointmentId, BigDecimal baseAmount) {
        return new Bill(Validator.requireNonBlank(appointmentId, "appointmentId"),
                Validator.requireNonNegative(baseAmount, "baseAmount"));
    }

    public static Bill createBillWithStrategy(String appointmentId, BigDecimal baseAmount, BillingStrategy strategy) {
        Validator.requireNonNull(strategy, "strategy");
        BigDecimal total = strategy.computeTotal(baseAmount);
        // Strategy returns the final payable amount. To avoid double-taxing by Bill, set Bill's taxRate to 0.
        Bill bill = new Bill(Validator.requireNonBlank(appointmentId, "appointmentId"), total);
        bill.setTaxRate(BigDecimal.ZERO);
        return bill;
    }
}


