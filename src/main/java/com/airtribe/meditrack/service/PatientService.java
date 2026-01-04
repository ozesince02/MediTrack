package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.Validator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CRUD and search operations for patients.
 *
 * <p>Demonstrates overloading via {@code searchPatient(...)} methods.</p>
 */
public class PatientService {
    private final DataStore<Patient> store;

    public PatientService(DataStore<Patient> store) {
        this.store = Validator.requireNonNull(store, "store");
    }

    public void add(Patient patient) {
        Validator.requireNonNull(patient, "patient");
        store.upsert(patient.getId(), patient);
    }

    public Optional<Patient> getById(String id) {
        return store.get(id);
    }

    public List<Patient> listAll() {
        return store.listAll();
    }

    public Optional<Patient> removeById(String id) {
        return store.remove(id);
    }

    // Overloaded searches
    public Optional<Patient> searchPatient(String id) {
        return getById(id);
    }

    /**
     * Overload: search by patient name.
     *
     * <p>Using {@code char[]} makes this a distinct overload from {@code searchPatient(String id)}.</p>
     */
    public List<Patient> searchPatient(char[] name) {
        Validator.requireNonNull(name, "name");
        String q = new String(name).trim().toLowerCase();
        if (q.isEmpty()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return store.stream()
                .filter(p -> p.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Patient> searchPatient(int age) {
        Validator.requireRangeInclusive(age, 0, 130, "age");
        return store.stream()
                .filter(p -> p.getAge() == age)
                .collect(Collectors.toList());
    }
}


