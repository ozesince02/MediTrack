package com.airtribe.meditrack.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.airtribe.meditrack.util.Validator;

/**
 * Nested object to demonstrate deep cloning in {@link Patient}.
 */
public class PatientProfile implements Cloneable {
    private String address;
    private final List<String> allergies = new ArrayList<>();

    public PatientProfile(String address, List<String> allergies) {
        
        this.address = Validator.requireNonBlank(address, "address");
        if (allergies != null) {
            this.allergies.addAll(allergies);
        }
    }

    public PatientProfile(String address) {
        this(address, null);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = Validator.requireNonBlank(address, "address");
    }

    public List<String> getAllergies() {
        return Collections.unmodifiableList(allergies);
    }

    public void addAllergy(String allergy) {
        allergies.add(Validator.requireNonBlank(allergy, "allergy"));
    }

    @Override
    public PatientProfile clone() throws CloneNotSupportedException {
        PatientProfile copy = (PatientProfile) super.clone();
        // Deep copy the list
        copy.allergies.clear();
        copy.allergies.addAll(this.allergies);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientProfile that = (PatientProfile) o;
        return Objects.equals(address, that.address) && Objects.equals(allergies, that.allergies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, allergies);
    }
}


