package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.Validator;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CRUD and search operations for doctors.
 */
public class DoctorService {
    private final DataStore<Doctor> store;

    public DoctorService(DataStore<Doctor> store) {
        this.store = Validator.requireNonNull(store, "store");
    }

    public void add(Doctor doctor) {
        Validator.requireNonNull(doctor, "doctor");
        store.upsert(doctor.getId(), doctor);
    }

    public Optional<Doctor> getById(String id) {
        return store.get(id);
    }

    public List<Doctor> listAll() {
        return store.listAll();
    }

    public Optional<Doctor> removeById(String id) {
        return store.remove(id);
    }

    public List<Doctor> findBySpecialization(Specialization specialization) {
        Validator.requireNonNull(specialization, "specialization");
        return store.stream()
                .filter(d -> d.getSpecialization() == specialization)
                .collect(Collectors.toList());
    }

    public List<Doctor> searchByNameContains(String query) {
        String q = Validator.requireNonBlank(query, "query").toLowerCase();
        return store.stream()
                .filter(d -> d.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Doctor> sortByFeeAscending() {
        return store.stream()
                .sorted(Comparator.comparing(Doctor::getConsultationFee))
                .collect(Collectors.toList());
    }
}


