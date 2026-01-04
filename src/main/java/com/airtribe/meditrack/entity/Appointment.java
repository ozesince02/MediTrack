package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;

import com.airtribe.meditrack.util.DateUtil;
import com.airtribe.meditrack.util.Validator;

/**
 * Appointment entity.
 */
public class Appointment extends MedicalEntity implements Cloneable {
    private Doctor doctor;
    private Patient patient;
    private LocalDateTime scheduledAt;
    private AppointmentStatus status;

    public Appointment(String id, Doctor doctor, Patient patient, LocalDateTime scheduledAt, AppointmentStatus status) {
        super(id);
        
        this.doctor = Validator.requireNonNull(doctor, "doctor");
        this.patient = Validator.requireNonNull(patient, "patient");
        this.scheduledAt = Validator.requireNonNull(scheduledAt, "scheduledAt");
        this.status = Validator.requireNonNull(status, "status");
    }

    public Appointment(Doctor doctor, Patient patient, LocalDateTime scheduledAt) {
        super("APT", true);
        
        this.doctor = Validator.requireNonNull(doctor, "doctor");
        this.patient = Validator.requireNonNull(patient, "patient");
        this.scheduledAt = Validator.requireNonNull(scheduledAt, "scheduledAt");
        this.status = AppointmentStatus.PENDING;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = Validator.requireNonNull(doctor, "doctor");
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = Validator.requireNonNull(patient, "patient");
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = Validator.requireNonNull(scheduledAt, "scheduledAt");
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = Validator.requireNonNull(status, "status");
    }

    public void cancel() {
        this.status = AppointmentStatus.CANCELLED;
    }

    @Override
    public Appointment clone() throws CloneNotSupportedException {
        Appointment copy = (Appointment) super.clone();
        // Deep copy nested objects: doctor and patient
        // Doctor is treated as immutable enough for this demo, but patient is deep-cloned per requirements.
        copy.doctor = this.doctor;
        copy.patient = this.patient == null ? null : (Patient) this.patient.clone();
        copy.scheduledAt = this.scheduledAt;
        copy.status = this.status;
        return copy;
    }

    @Override
    protected String details() {
        return "doctorId=" + (doctor == null ? "" : doctor.getId())
                + ", patientId=" + (patient == null ? "" : patient.getId())
                + ", scheduledAt=" + (scheduledAt == null ? "" : DateUtil.formatDateTime(scheduledAt))
                + ", status=" + status;
    }
}


