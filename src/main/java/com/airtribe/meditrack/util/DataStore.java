package com.airtribe.meditrack.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Generic in-memory data store.
 *
 * <p>Backed by a {@link Map} for O(1) lookups by id.</p>
 */
public class DataStore<T> implements Iterable<T> {
    private final Map<String, T> byId = new LinkedHashMap<>();

    public void upsert(String id, T entity) {
        String key = Validator.requireNonBlank(id, "id");
        T value = Validator.requireNonNull(entity, "entity");
        byId.put(key, value);
    }

    public Optional<T> get(String id) {
        String key = Validator.requireNonBlank(id, "id");
        return Optional.ofNullable(byId.get(key));
    }

    public boolean contains(String id) {
        String key = Validator.requireNonBlank(id, "id");
        return byId.containsKey(key);
    }

    public Optional<T> remove(String id) {
        String key = Validator.requireNonBlank(id, "id");
        return Optional.ofNullable(byId.remove(key));
    }

    public int size() {
        return byId.size();
    }

    public List<T> listAll() {
        return Collections.unmodifiableList(new ArrayList<>(byId.values()));
    }

    public Stream<T> stream() {
        return byId.values().stream();
    }

    public List<T> findAll(Predicate<T> predicate) {
        Validator.requireNonNull(predicate, "predicate");
        List<T> out = new ArrayList<>();
        for (T t : byId.values()) {
            if (predicate.test(t)) {
                out.add(t);
            }
        }
        return out;
    }

    @Override
    public Iterator<T> iterator() {
        return byId.values().iterator();
    }
}


