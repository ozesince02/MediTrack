package com.airtribe.meditrack.test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.PatientProfile;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.patterns.billing.BillFactory;
import com.airtribe.meditrack.patterns.billing.FlatTaxStrategy;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.AIHelper;
import com.airtribe.meditrack.util.DataStore;

/**
 * Manual test runner (no JUnit).
 */
public class TestRunner {
    public static void main(String[] args) throws CloneNotSupportedException {
        testValidation();
        testDeepClone();
        testPatientCrudActions();
        testDoctorCrudActions();
        testAppointmentCrudActions();
        testAppointmentNotFound();
        testBillingStrategyFactory();
        testGenerateBillFromAppointmentService();
        testAIRecommendation();
        testPatientSearchOverloads();
        System.out.println("\nALL TESTS PASSED");
    }

    private static void testValidation() {
        boolean threw = false;
        try {
            Patient ignored = new Patient("John", -1, "999", new PatientProfile("Addr"));
            throw new AssertionError("Expected InvalidDataException but object was created: " + ignored.getId());
        } catch (InvalidDataException e) {
            threw = true;
        }
        assertTrue(threw, "Expected InvalidDataException for invalid age");
    }

    private static void testDeepClone() throws CloneNotSupportedException {
        Patient p1 = new Patient("A", 30, "111", new PatientProfile("Addr1", List.of("dust")));
        Patient p2 = (Patient) p1.clone();
        assertTrue(p1 != p2, "Clone should return a different instance");
        assertTrue(p1.getProfile() != p2.getProfile(), "Nested profile must be deep-cloned");
        assertEquals(p1.getProfile().getAddress(), p2.getProfile().getAddress(), "Address should match");
    }

    /**
     * Covers the same patient operations the CLI exposes:
     * add, view-by-id, update, delete, list.
     */
    private static void testPatientCrudActions() {
        PatientService patientService = new PatientService(new DataStore<>());

        Patient p1 = new Patient("Neha", 22, "999", new PatientProfile("Addr"));
        patientService.add(p1);
        assertTrue(patientService.getById(p1.getId()).isPresent(), "CLI view patient by id should work");
        assertEquals(1, patientService.listAll().size(), "CLI list patients should work");

        // Update (mutate entity and upsert)
        Patient loaded = patientService.getById(p1.getId()).orElseThrow();
        loaded.setPhone("777");
        loaded.getProfile().setAddress("Addr2");
        patientService.add(loaded);
        Patient updated = patientService.getById(p1.getId()).orElseThrow();
        assertEquals("777", updated.getPhone(), "CLI update patient phone should persist");
        assertEquals("Addr2", updated.getProfile().getAddress(), "CLI update patient address should persist");

        // Delete
        assertTrue(patientService.removeById(p1.getId()).isPresent(), "CLI delete patient should remove");
        assertTrue(patientService.getById(p1.getId()).isEmpty(), "Deleted patient should not be found");
    }

    /**
     * Covers the same doctor operations the CLI exposes:
     * add, view-by-id, update, delete, list.
     */
    private static void testDoctorCrudActions() {
        DoctorService doctorService = new DoctorService(new DataStore<>());

        Doctor d1 = new Doctor("Doc1", 40, "111", Specialization.GENERAL_PHYSICIAN, new BigDecimal("500"));
        doctorService.add(d1);
        assertTrue(doctorService.getById(d1.getId()).isPresent(), "CLI view doctor by id should work");
        assertEquals(1, doctorService.listAll().size(), "CLI list doctors should work");

        Doctor loaded = doctorService.getById(d1.getId()).orElseThrow();
        loaded.setConsultationFee(new BigDecimal("650"));
        loaded.setSpecialization(Specialization.CARDIOLOGY);
        doctorService.add(loaded);

        Doctor updated = doctorService.getById(d1.getId()).orElseThrow();
        assertEquals(new BigDecimal("650"), updated.getConsultationFee(), "CLI update doctor fee should persist");
        assertEquals(Specialization.CARDIOLOGY, updated.getSpecialization(), "CLI update doctor specialization should persist");

        assertTrue(doctorService.removeById(d1.getId()).isPresent(), "CLI delete doctor should remove");
        assertTrue(doctorService.getById(d1.getId()).isEmpty(), "Deleted doctor should not be found");
    }

    /**
     * Covers appointment operations the CLI exposes:
     * create, cancel, list.
     */
    private static void testAppointmentCrudActions() {
        DoctorService doctorService = new DoctorService(new DataStore<>());
        PatientService patientService = new PatientService(new DataStore<>());
        AppointmentService appointmentService = new AppointmentService(new DataStore<>());

        Doctor doctor = new Doctor("Doc1", 40, "111", Specialization.GENERAL_PHYSICIAN, new BigDecimal("500"));
        doctorService.add(doctor);
        Patient patient = new Patient("Neha", 22, "999", new PatientProfile("Addr"));
        patientService.add(patient);

        Appointment created = appointmentService.create(doctor, patient, LocalDateTime.now().plusDays(1));
        assertEquals(AppointmentStatus.CONFIRMED, created.getStatus(), "CLI create appointment should confirm");
        assertEquals(1, appointmentService.listAll().size(), "CLI list appointments should work");
        assertEquals(1, appointmentService.listByDoctorId(doctor.getId()).size(), "List appointments by doctor should work");
        assertEquals(1, appointmentService.listByPatientId(patient.getId()).size(), "List appointments by patient should work");

        Appointment cancelled = appointmentService.cancel(created.getId());
        assertEquals(AppointmentStatus.CANCELLED, cancelled.getStatus(), "CLI cancel appointment should set CANCELLED");
    }

    private static void testAppointmentNotFound() {
        AppointmentService apptService = new AppointmentService(new DataStore<>());
        boolean threw = false;
        try {
            apptService.cancel("APT-404");
        } catch (AppointmentNotFoundException e) {
            threw = true;
        }
        assertTrue(threw, "Expected AppointmentNotFoundException");
    }

    private static void testBillingStrategyFactory() {
        var bill = BillFactory.createBillWithStrategy("APT-1", new BigDecimal("1000"), new FlatTaxStrategy(new BigDecimal("0.10")));
        assertEquals(new BigDecimal("1100.00"), bill.amountDue(), "Total should include tax via strategy");
    }

    private static void testGenerateBillFromAppointmentService() {
        AppointmentService appointmentService = new AppointmentService(new DataStore<>());
        Doctor doctor = new Doctor("Doc1", 40, "111", Specialization.GENERAL_PHYSICIAN, new BigDecimal("500"));
        Patient patient = new Patient("Neha", 22, "999", new PatientProfile("Addr"));
        Appointment appt = appointmentService.create(doctor, patient, LocalDateTime.now().plusDays(2));

        Bill bill = appointmentService.generateBill(appt.getId());
        assertTrue(bill.amountDue().compareTo(BigDecimal.ZERO) > 0, "Generate bill should return a payable amount");
        assertEquals(appt.getId(), bill.getAppointmentId(), "Bill should be tied to appointment id");
    }

    private static void testAIRecommendation() {
        var spec = AIHelper.recommendSpecialization(Arrays.asList("rash", "itch"));
        assertEquals(Specialization.DERMATOLOGY, spec, "Symptoms should map to dermatology");

        Doctor d1 = new Doctor("D1", "Doc1", 40, "1", Specialization.DERMATOLOGY, new BigDecimal("700"));
        Doctor d2 = new Doctor("D2", "Doc2", 40, "1", Specialization.DERMATOLOGY, new BigDecimal("500"));
        var suggested = AIHelper.suggestDoctor(List.of(d1, d2), Specialization.DERMATOLOGY);
        assertTrue(suggested.isPresent(), "Expected suggested doctor");
        assertEquals("D2", suggested.get().getId(), "Expected cheaper doctor");

        var slots = AIHelper.suggestSlots(LocalDate.now());
        assertTrue(!slots.isEmpty(), "Expected non-empty slots");
    }

    private static void testPatientSearchOverloads() {
        PatientService patientService = new PatientService(new DataStore<>());
        Patient p1 = new Patient("Neha", 22, "999", new PatientProfile("Addr"));
        Patient p2 = new Patient("Rahul", 22, "888", new PatientProfile("Addr"));
        patientService.add(p1);
        patientService.add(p2);

        assertTrue(patientService.searchPatient(p1.getId()).isPresent(), "Expected search by id");
        assertEquals(2, patientService.searchPatient(22).size(), "Expected search by age");
        assertEquals(1, patientService.searchPatient("neh".toCharArray()).size(), "Expected search by name");
    }

    private static void assertTrue(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + " (expected=" + expected + ", actual=" + actual + ")");
        }
    }
}


