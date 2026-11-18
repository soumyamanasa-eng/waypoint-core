package waypoint.model;

import java.util.Objects;

/** VALUE OBJECT. TODO: validate and normalise so an invalid instance cannot exist. */
public record Registration(String value) {
    // Format: two letters, two digits, one or two letters, four digits. e.g. MH12AB1234
    public Registration {
        Objects.requireNonNull(value, "Registration cannot be null");
        value = value.trim().toUpperCase();
        if (!value.matches("^[A-Z]{2}\\d{2}[A-Z]{1,2}\\d{4}$")) {
            throw new IllegalArgumentException("Invalid registration format: " + value);
        }
    }
}