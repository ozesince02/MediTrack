package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.Validator;

/**
 * Patient entity.
 *
 * <p>Implements {@link Cloneable} to demonstrate deep vs shallow copy.</p>
 */
public class Patient extends Person implements Cloneable {
    private PatientProfile profile;

    public Patient(String id, String name, int age, String phone, PatientProfile profile) {
        super(id, name, age, phone);
        
        this.profile = Validator.requireNonNull(profile, "profile");
    }

    public Patient(String name, int age, String phone, PatientProfile profile) {
        super("PAT", true, name, age, phone);
        
        this.profile = Validator.requireNonNull(profile, "profile");
    }

    public PatientProfile getProfile() {
        return profile;
    }

    public void setProfile(PatientProfile profile) {
        this.profile = Validator.requireNonNull(profile, "profile");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Patient copy = (Patient) super.clone(); // shallow copy of Person fields
        copy.profile = (this.profile == null) ? null : this.profile.clone(); // deep copy nested object
        return copy;
    }

    @Override
    protected String details() {
        return super.details() + ", address=" + (profile == null ? "" : profile.getAddress());
    }
}


