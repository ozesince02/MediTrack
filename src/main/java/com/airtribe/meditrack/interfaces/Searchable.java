package com.airtribe.meditrack.interfaces;

/**
 * Marker + helper interface for searchable entities.
 */
public interface Searchable {
    /**
     * Default helper to normalize user input for searching.
     */
    default String normalizeQuery(String q) {
        return q == null ? "" : q.trim().toLowerCase();
    }
}


