package com.airtribe.meditrack.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.interfaces.Payable;
import com.airtribe.meditrack.util.Validator;

/**
 * Bill for an appointment or medical service.
 */
public class Bill extends MedicalEntity implements Payable {
    private final String appointmentId;
    private BigDecimal baseAmount;
    private BigDecimal taxRate;

    public Bill(String id, String appointmentId, BigDecimal baseAmount, BigDecimal taxRate) {
        super(id);
        this.appointmentId = Validator.requireNonBlank(appointmentId, "appointmentId");
        
        this.baseAmount = Validator.requireNonNegative(baseAmount, "baseAmount");
        this.taxRate = Validator.requireNonNegative(taxRate, "taxRate");
    }

    public Bill(String appointmentId, BigDecimal baseAmount) {
        super("BILL", true);
        this.appointmentId = Validator.requireNonBlank(appointmentId, "appointmentId");
        
        this.baseAmount = Validator.requireNonNegative(baseAmount, "baseAmount");
        this.taxRate = Validator.requireNonNegative(Constants.DEFAULT_TAX_RATE, "taxRate");
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = Validator.requireNonNegative(baseAmount, "baseAmount");
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = Validator.requireNonNegative(taxRate, "taxRate");
    }

    public BigDecimal taxAmount() {
        return baseAmount.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal amountDue() {
        return baseAmount.add(taxAmount()).setScale(2, RoundingMode.HALF_UP);
    }

    public BillSummary toSummary() {
        return new BillSummary(getId(), appointmentId, baseAmount, taxAmount(), amountDue(), Constants.DEFAULT_CURRENCY);
    }

    @Override
    protected String details() {
        return "appointmentId=" + appointmentId + ", baseAmount=" + baseAmount + ", taxRate=" + taxRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bill)) return false;
        Bill bill = (Bill) o;
        return getId().equals(bill.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}


