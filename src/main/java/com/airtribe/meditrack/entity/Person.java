package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.util.Validator;

/**
 * Base class for people in the system (inheritance base).
 */
public class Person extends MedicalEntity implements Searchable {
    private String name;
    private int age;
    private String phone;

    public Person(String id, String name, int age, String phone) {
        super(id);
        
        this.name = Validator.requireNonBlank(name, "name");
        this.age = Validator.requireRangeInclusive(age, 0, 130, "age");
        this.phone = Validator.requireNonBlank(phone, "phone");
    }

    public Person(String prefix, boolean autoId, String name, int age, String phone) {
        super(prefix, autoId);
        
        this.name = Validator.requireNonBlank(name, "name");
        this.age = Validator.requireRangeInclusive(age, 0, 130, "age");
        this.phone = Validator.requireNonBlank(phone, "phone");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Validator.requireNonBlank(name, "name");
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = Validator.requireRangeInclusive(age, 0, 130, "age");
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = Validator.requireNonBlank(phone, "phone");
    }

    @Override
    protected String details() {
        return "name=" + name + ", age=" + age + ", phone=" + phone;
    }
}


