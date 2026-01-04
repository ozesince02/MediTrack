package com.airtribe.meditrack;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.PatientProfile;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.AIHelper;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.DateUtil;
import com.airtribe.meditrack.util.Validator;

/**
 * Entry point for the MediTrack console application.
 *
 * <p>
 * Use {@code mvn -q exec:java} to run.</p>
 */
public class Main {

    public static void main(String[] args) {
        DoctorService doctorService = new DoctorService(new DataStore<>());
        PatientService patientService = new PatientService(new DataStore<>());
        AppointmentService appointmentService = new AppointmentService(new DataStore<>());

        // Seed a couple of doctors for quick testing.
        doctorService.add(new Doctor("Dr. Asha", 40, "9999999999", Specialization.GENERAL_PHYSICIAN, new BigDecimal("500")));
        doctorService.add(new Doctor("Dr. Raj", 45, "8888888888", Specialization.DERMATOLOGY, new BigDecimal("700")));

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n=== MediTrack Menu ===");
                System.out.println("1) Add Patient");
                System.out.println("2) Update Patient");
                System.out.println("3) Delete Patient");
                System.out.println("4) View Patient by ID");
                System.out.println("5) List Patients");

                System.out.println("6) Add Doctor");
                System.out.println("7) Update Doctor");
                System.out.println("8) Delete Doctor");
                System.out.println("9) View Doctor by ID");
                System.out.println("10) List Doctors");

                System.out.println("11) Create Appointment");
                System.out.println("12) Cancel Appointment");
                System.out.println("13) List Appointments");

                System.out.println("14) AI: Recommend Doctor");
                System.out.println("0) Exit");
                System.out.print("Choose: ");

                String choice = sc.nextLine().trim();
                if ("0".equals(choice)) {
                    System.out.println("Bye.");
                    return;
                }


                try {
                    switch (choice) {
                        case "1" ->
                            addPatient(sc, patientService);
                        case "2" ->
                            updatePatient(sc, patientService);
                        case "3" ->
                            deletePatient(sc, patientService);
                        case "4" ->
                            viewPatient(sc, patientService);
                        case "5" ->
                            patientService.listAll().forEach(p -> System.out.println(p.describe()));
                        case "6" ->
                            addDoctor(sc, doctorService);
                        case "7" ->
                            updateDoctor(sc, doctorService);
                        case "8" ->
                            deleteDoctor(sc, doctorService);
                        case "9" ->
                            viewDoctor(sc, doctorService);
                        case "10" ->
                            doctorService.listAll().forEach(d -> System.out.println(d.describe()));
                        case "11" ->
                            createAppointment(sc, doctorService, patientService, appointmentService);
                        case "12" ->
                            cancelAppointment(sc, appointmentService);
                        case "13" ->
                            appointmentService.listAll().forEach(a -> System.out.println(a.describe()));
                        case "14" ->
                            aiRecommend(sc, doctorService);
                        default ->
                            System.out.println("Invalid choice.");
                    }
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private static void addPatient(Scanner sc, PatientService patientService) {
        System.out.print("Patient name: ");
        String name = sc.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Phone: ");
        String phone = sc.nextLine();
        System.out.print("Address: ");
        String address = sc.nextLine();

        PatientProfile profile = new PatientProfile(address);
        Patient patient = new Patient(name, age, phone, profile);
        patientService.add(patient);
        System.out.println("Added: " + patient.getId());
    }

    private static void updatePatient(Scanner sc, PatientService patientService) {
        System.out.print("Patient ID to update: ");
        String id = Validator.requireNonBlank(sc.nextLine(), "patientId");
        Patient patient = patientService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + id));

        System.out.println("Press Enter to keep existing values.");
        System.out.print("Name [" + patient.getName() + "]: ");
        String name = sc.nextLine();
        if (!name.trim().isEmpty()) {
            patient.setName(name);
        }

        System.out.print("Age [" + patient.getAge() + "]: ");
        String ageStr = sc.nextLine();
        if (!ageStr.trim().isEmpty()) {
            patient.setAge(Integer.parseInt(ageStr.trim()));
        }

        System.out.print("Phone [" + patient.getPhone() + "]: ");
        String phone = sc.nextLine();
        if (!phone.trim().isEmpty()) {
            patient.setPhone(phone);
        }

        String currentAddr = patient.getProfile() == null ? "" : patient.getProfile().getAddress();
        System.out.print("Address [" + currentAddr + "]: ");
        String address = sc.nextLine();
        if (!address.trim().isEmpty()) {
            if (patient.getProfile() == null) {
                patient.setProfile(new PatientProfile(address)); 
            }else {
                patient.getProfile().setAddress(address);
            }
        }

        patientService.add(patient); // upsert
        System.out.println("Updated: " + patient.getId());
    }

    private static void deletePatient(Scanner sc, PatientService patientService) {
        System.out.print("Patient ID to delete: ");
        String id = Validator.requireNonBlank(sc.nextLine(), "patientId");
        var removed = patientService.removeById(id);
        if (removed.isPresent()) {
            System.out.println("Deleted: " + id); 
        }else {
            System.out.println("Patient not found: " + id);
        }
    }

    private static void viewPatient(Scanner sc, PatientService patientService) {
        System.out.print("Patient ID: ");
        String id = Validator.requireNonBlank(sc.nextLine(), "patientId");
        Patient patient = patientService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + id));
        System.out.println(patient.describe());
    }

    private static void addDoctor(Scanner sc, DoctorService doctorService) {
        System.out.print("Doctor name: ");
        String name = sc.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Phone: ");
        String phone = sc.nextLine();
        Specialization spec = readSpecialization(sc, null);
        System.out.print("Consultation fee: ");
        BigDecimal fee = new BigDecimal(sc.nextLine().trim());

        Doctor doctor = new Doctor(name, age, phone, spec, fee);
        doctorService.add(doctor);
        System.out.println("Added: " + doctor.getId());
    }

    private static void updateDoctor(Scanner sc, DoctorService doctorService) {
        System.out.print("Doctor ID to update: ");
        String id = Validator.requireNonBlank(sc.nextLine(), "doctorId");
        Doctor doctor = doctorService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + id));

        System.out.println("Press Enter to keep existing values.");
        System.out.print("Name [" + doctor.getName() + "]: ");
        String name = sc.nextLine();
        if (!name.trim().isEmpty()) {
            doctor.setName(name);
        }

        System.out.print("Age [" + doctor.getAge() + "]: ");
        String ageStr = sc.nextLine();
        if (!ageStr.trim().isEmpty()) {
            doctor.setAge(Integer.parseInt(ageStr.trim()));
        }

        System.out.print("Phone [" + doctor.getPhone() + "]: ");
        String phone = sc.nextLine();
        if (!phone.trim().isEmpty()) {
            doctor.setPhone(phone);
        }

        Specialization spec = readSpecialization(sc, doctor.getSpecialization());
        if (spec != null) {
            doctor.setSpecialization(spec);
        }

        System.out.print("Consultation fee [" + doctor.getConsultationFee() + "]: ");
        String feeStr = sc.nextLine();
        if (!feeStr.trim().isEmpty()) {
            doctor.setConsultationFee(new BigDecimal(feeStr.trim()));
        }

        doctorService.add(doctor); // upsert
        System.out.println("Updated: " + doctor.getId());
    }

    private static void deleteDoctor(Scanner sc, DoctorService doctorService) {
        System.out.print("Doctor ID to delete: ");
        String id = Validator.requireNonBlank(sc.nextLine(), "doctorId");
        var removed = doctorService.removeById(id);
        if (removed.isPresent()) {
            System.out.println("Deleted: " + id); 
        }else {
            System.out.println("Doctor not found: " + id);
        }
    }

    private static void viewDoctor(Scanner sc, DoctorService doctorService) {
        System.out.print("Doctor ID: ");
        String id = Validator.requireNonBlank(sc.nextLine(), "doctorId");
        Doctor doctor = doctorService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + id));
        System.out.println(doctor.describe());
    }

    private static void createAppointment(Scanner sc,
            DoctorService doctorService,
            PatientService patientService,
            AppointmentService appointmentService) {
        System.out.print("Doctor ID: ");
        String docId = sc.nextLine();
        Doctor doctor = doctorService.getById(Validator.requireNonBlank(docId, "doctorId"))
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + docId));

        System.out.print("Patient ID: ");
        String patId = sc.nextLine();
        Patient patient = patientService.getById(Validator.requireNonBlank(patId, "patientId"))
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patId));

        System.out.print("Scheduled At (yyyy-MM-dd HH:mm): ");
        String when = sc.nextLine();
        LocalDateTime scheduledAt = DateUtil.parseDateTime(when, "scheduledAt");

        var appt = appointmentService.create(doctor, patient, scheduledAt);
        System.out.println("Created appointment: " + appt.getId());
        System.out.println("Bill due: " + appointmentService.generateBill(appt.getId()).amountDue());
    }

    private static void cancelAppointment(Scanner sc, AppointmentService appointmentService) {
        System.out.print("Appointment ID: ");
        String id = sc.nextLine();
        var appt = appointmentService.cancel(id);
        System.out.println("Cancelled: " + appt.getId());
    }

    private static void aiRecommend(Scanner sc, DoctorService doctorService) {
        System.out.println("Enter symptoms (comma separated): ");
        String line = sc.nextLine();
        String[] parts = line.split(",");
        List<String> symptoms = new ArrayList<>();
        for (String p : parts) {
            if (p != null && !p.trim().isEmpty()) {
                symptoms.add(p.trim());
            }
        }

        Specialization spec = AIHelper.recommendSpecialization(symptoms);
        System.out.println("Recommended specialization: " + spec);

        Optional<Doctor> doc = AIHelper.suggestDoctor(doctorService.listAll(), spec);
        if (doc.isPresent()) {
            System.out.println("Suggested doctor: " + doc.get().describe());
            System.out.print("Show suggested slots for which date (yyyy-MM-dd): ");
            LocalDate date = DateUtil.parseDate(sc.nextLine(), "date");
            AIHelper.suggestSlots(date).stream().limit(8).forEach(d -> System.out.println(" - " + d));
        } else {
            System.out.println("No doctor available for " + spec);
        }
    }

    private static Specialization readSpecialization(Scanner sc, Specialization current) {
        System.out.println("Specialization" + (current == null ? "" : " [" + current + "]") + ":");
        Specialization[] all = Specialization.values();
        for (int i = 0; i < all.length; i++) {
            System.out.println((i + 1) + ") " + all[i]);
        }
        System.out.print("Choose (1-" + all.length + ")" + (current == null ? "" : " or Enter to keep") + ": ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) {
            return current;
        }
        int idx = Integer.parseInt(input);
        if (idx < 1 || idx > all.length) {
            throw new IllegalArgumentException("Invalid specialization choice");
        }
        return all[idx - 1];
    }
}
