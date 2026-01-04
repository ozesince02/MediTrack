package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.DateUtil;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base abstract class for common entity behavior (abstraction).
 *
 * <p>Template Method: {@link #describe()} has a fixed skeleton and delegates details to subclasses.</p>
 */
public abstract class MedicalEntity {
    private final String id;
    private final LocalDateTime createdAt;

    protected MedicalEntity(String id) {
        this.id = Validator.requireNonBlank(id, "id");
        this.createdAt = LocalDateTime.now();
    }

    protected MedicalEntity(String prefix, boolean autoId) {
        this(autoId ? IdGenerator.getInstance().nextId(prefix) : Validator.requireNonBlank(prefix, "id"));
    }

    public final String getId() {
        return id;
    }

    public final LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public final String describe() {
        return getType() + " { id=" + id + ", createdAt=" + DateUtil.formatDateTime(createdAt) + ", details=" + details() + " }";
    }

    protected String getType() {
        return getClass().getSimpleName();
    }

    protected abstract String details();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalEntity that = (MedicalEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


