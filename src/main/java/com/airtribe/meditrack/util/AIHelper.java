package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Rule-based helper for simple \"AI\" features:
 * - specialization recommendation from symptoms
 * - doctor suggestion
 * - appointment slot suggestions
 */
public final class AIHelper {
    private AIHelper() {}

    /**
     * Recommend a specialization based on symptom keywords.
     */
    public static Specialization recommendSpecialization(List<String> symptoms) {
        Validator.requireNonNull(symptoms, "symptoms");

        Map<Specialization, Integer> score = new HashMap<>();
        for (Specialization s : Specialization.values()) {
            score.put(s, 0);
        }

        for (String raw : symptoms) {
            if (raw == null) continue;
            String s = raw.trim().toLowerCase(Locale.ROOT);
            if (s.isEmpty()) continue;

            if (containsAny(s, "fever", "cold", "cough", "flu", "body ache")) bump(score, Specialization.GENERAL_PHYSICIAN, 2);
            if (containsAny(s, "rash", "acne", "itch", "eczema", "psoriasis")) bump(score, Specialization.DERMATOLOGY, 3);
            if (containsAny(s, "chest pain", "palpitation", "bp", "blood pressure", "hypertension")) bump(score, Specialization.CARDIOLOGY, 3);
            if (containsAny(s, "joint pain", "knee", "back pain", "fracture", "sprain")) bump(score, Specialization.ORTHOPEDICS, 3);
            if (containsAny(s, "child", "pediatric", "newborn", "vaccination")) bump(score, Specialization.PEDIATRICS, 3);
            if (containsAny(s, "headache", "migraine", "seizure", "numbness", "stroke")) bump(score, Specialization.NEUROLOGY, 3);
            if (containsAny(s, "ear", "throat", "nose", "sinus", "tonsil")) bump(score, Specialization.ENT, 3);
        }

        return score.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Specialization.GENERAL_PHYSICIAN);
    }

    /**
     * Suggest the best doctor based on specialization match and lowest fee (simple heuristic).
     */
    public static Optional<Doctor> suggestDoctor(List<Doctor> doctors, Specialization specialization) {
        Validator.requireNonNull(doctors, "doctors");
        Validator.requireNonNull(specialization, "specialization");

        return doctors.stream()
                .filter(Objects::nonNull)
                .filter(d -> d.getSpecialization() == specialization)
                .min(Comparator.comparing(Doctor::getConsultationFee));
    }

    /**
     * Suggest appointment slots on a given date (default: every 30 mins from 10:00 to 16:00).
     */
    public static List<LocalDateTime> suggestSlots(LocalDate date) {
        Validator.requireNonNull(date, "date");
        List<LocalDateTime> slots = new ArrayList<>();

        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(16, 0);

        LocalTime t = start;
        while (!t.isAfter(end)) {
            slots.add(LocalDateTime.of(date, t));
            t = t.plusMinutes(30);
        }

        return slots;
    }

    private static boolean containsAny(String s, String... needles) {
        for (String n : needles) {
            if (s.contains(n)) return true;
        }
        return false;
    }

    private static void bump(Map<Specialization, Integer> score, Specialization s, int inc) {
        score.put(s, score.getOrDefault(s, 0) + inc);
    }
}


