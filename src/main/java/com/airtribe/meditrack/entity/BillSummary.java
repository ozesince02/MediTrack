package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.Validator;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Immutable, thread-safe bill summary.
 */
public final class BillSummary {
    private final String billId;
    private final String appointmentId;
    private final BigDecimal baseAmount;
    private final BigDecimal taxAmount;
    private final BigDecimal totalAmount;
    private final String currency;

    public BillSummary(String billId,
                       String appointmentId,
                       BigDecimal baseAmount,
                       BigDecimal taxAmount,
                       BigDecimal totalAmount,
                       String currency) {
        this.billId = Validator.requireNonBlank(billId, "billId");
        this.appointmentId = Validator.requireNonBlank(appointmentId, "appointmentId");
        this.baseAmount = Validator.requireNonNegative(baseAmount, "baseAmount");
        this.taxAmount = Validator.requireNonNegative(taxAmount, "taxAmount");
        this.totalAmount = Validator.requireNonNegative(totalAmount, "totalAmount");
        this.currency = Validator.requireNonBlank(currency, "currency");
    }

    public String getBillId() {
        return billId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillSummary that = (BillSummary) o;
        return billId.equals(that.billId)
                && appointmentId.equals(that.appointmentId)
                && baseAmount.equals(that.baseAmount)
                && taxAmount.equals(that.taxAmount)
                && totalAmount.equals(that.totalAmount)
                && currency.equals(that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId, appointmentId, baseAmount, taxAmount, totalAmount, currency);
    }

    @Override
    public String toString() {
        return "BillSummary{billId='" + billId + '\''
                + ", appointmentId='" + appointmentId + '\''
                + ", baseAmount=" + baseAmount
                + ", taxAmount=" + taxAmount
                + ", totalAmount=" + totalAmount
                + ", currency='" + currency + '\''
                + '}';
    }
}


