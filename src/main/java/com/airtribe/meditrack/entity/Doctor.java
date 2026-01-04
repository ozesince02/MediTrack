package com.airtribe.meditrack.entity;

import java.math.BigDecimal;

import com.airtribe.meditrack.util.Validator;

/**
 * Doctor entity.
 */
public class Doctor extends Person {
    private Specialization specialization;
    private BigDecimal consultationFee;

    public Doctor(String id,
                  String name,
                  int age,
                  String phone,
                  Specialization specialization,
                  BigDecimal consultationFee) {
        super(id, name, age, phone);
        
        this.specialization = Validator.requireNonNull(specialization, "specialization");
        this.consultationFee = Validator.requireNonNegative(consultationFee, "consultationFee");
    }

    public Doctor(String name,
                  int age,
                  String phone,
                  Specialization specialization,
                  BigDecimal consultationFee) {
        super("DOC", true, name, age, phone);
        
        this.specialization = Validator.requireNonNull(specialization, "specialization");
        this.consultationFee = Validator.requireNonNegative(consultationFee, "consultationFee");
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = Validator.requireNonNull(specialization, "specialization");
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = Validator.requireNonNegative(consultationFee, "consultationFee");
    }

    @Override
    protected String details() {
        return super.details() + ", specialization=" + specialization + ", fee=" + consultationFee;
    }
}


