package waypoint.model;

/** Distinct id types so they can never be swapped at a call site. */
record TicketId(String value) {
    public TicketId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TicketId cannot be null or blank");
        }
    }
}

record SpotId(String value) {
    public SpotId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SpotId cannot be null or blank");
        }
    }
}

record VehicleId(String value) {
    public VehicleId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("VehicleId cannot be null or blank");
        }
    }
}