package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.patterns.billing.BillFactory;
import com.airtribe.meditrack.patterns.billing.BillingStrategy;
import com.airtribe.meditrack.patterns.billing.FlatTaxStrategy;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.Validator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Appointment operations: create, view, cancel.
 */
public class AppointmentService {
    private final DataStore<Appointment> store;

    public AppointmentService(DataStore<Appointment> store) {
        this.store = Validator.requireNonNull(store, "store");
    }

    public Appointment create(Doctor doctor, Patient patient, LocalDateTime scheduledAt) {
        Validator.requireNonNull(doctor, "doctor");
        Validator.requireNonNull(patient, "patient");
        Validator.requireNonNull(scheduledAt, "scheduledAt");

        Appointment appt = new Appointment(doctor, patient, scheduledAt);
        appt.setStatus(AppointmentStatus.CONFIRMED);
        store.upsert(appt.getId(), appt);
        return appt;
    }

    public Optional<Appointment> getById(String id) {
        return store.get(id);
    }

    public List<Appointment> listAll() {
        return store.listAll();
    }

    public List<Appointment> listByDoctorId(String doctorId) {
        String did = Validator.requireNonBlank(doctorId, "doctorId");
        return store.stream()
                .filter(a -> a.getDoctor() != null && did.equals(a.getDoctor().getId()))
                .collect(Collectors.toList());
    }

    public List<Appointment> listByPatientId(String patientId) {
        String pid = Validator.requireNonBlank(patientId, "patientId");
        return store.stream()
                .filter(a -> a.getPatient() != null && pid.equals(a.getPatient().getId()))
                .collect(Collectors.toList());
    }

    public Appointment cancel(String appointmentId) {
        String id = Validator.requireNonBlank(appointmentId, "appointmentId");
        Appointment appt = store.get(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found: " + id));
        appt.setStatus(AppointmentStatus.CANCELLED);
        return appt;
    }

    /**
     * Basic billing hook (will be replaced/extended by Factory + Strategy in the next phase).
     */
    public Bill generateBill(String appointmentId) {
        String id = Validator.requireNonBlank(appointmentId, "appointmentId");
        Appointment appt = store.get(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found: " + id));
        if (appt.getDoctor() == null) {
            throw new IllegalStateException("Appointment has no doctor: " + id);
        }
        BillingStrategy strategy = new FlatTaxStrategy();
        return BillFactory.createBillWithStrategy(id, appt.getDoctor().getConsultationFee(), strategy);
    }
}


