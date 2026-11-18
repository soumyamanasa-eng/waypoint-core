package waypoint.model;

import java.time.Instant;
import java.util.Objects;

/** ENTITY. Invariants:
-   I1  entry is always present
-   I2  exit is null, or not before entry
-   I3  fare is null, or not negative
-   I4  fare exists only if exit exists
-   I5  a closed ticket never reopens
 */
public final class Ticket {
    private final TicketId id;
    private final SpotId spotId;
    private final Instant entry;
    private Instant exit;   // mutable: set once on close
    private Money fare;     // mutable: set once on close

    private Ticket(TicketId id, SpotId spotId, Instant entry) {
        this.id = Objects.requireNonNull(id, "TicketId cannot be null");
        this.spotId = Objects.requireNonNull(spotId, "SpotId cannot be null");
        this.entry = Objects.requireNonNull(entry, "Entry instant cannot be null");
    }

    public static Ticket open(TicketId id, SpotId spotId, Instant entry) {
        return new Ticket(id, spotId, entry);
    }

    public TicketId id() {
        return id;
    }

    public SpotId spotId() {
        return spotId;
    }

    public Instant entry() {
        return entry;
    }

    public Instant exit() {
        return exit;
    }

    public Money fare() {
        return fare;
    }

    public boolean isOpen() {
        return exit == null;
    }

    public Money close(Instant at, FarePolicy policy) {
        if (!isOpen()) {
            throw new TicketAlreadyClosed("Ticket " + id.value() + " is already closed");
        }
        Objects.requireNonNull(at, "Exit instant cannot be null");
        Objects.requireNonNull(policy, "FarePolicy cannot be null");
        if (at.isBefore(entry)) {
            throw new IllegalArgumentException("Exit instant cannot be before entry instant");
        }

        Money computedFare = Objects.requireNonNull(policy.fareFor(entry, at, spotId), "Fare cannot be null");
        if (computedFare.isNegative()) {
            throw new IllegalArgumentException("Fare cannot be negative: " + computedFare);
        }

        this.exit = at;
        this.fare = computedFare;
        return computedFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket other)) return false;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class TicketAlreadyClosed extends RuntimeException {
    public TicketAlreadyClosed(String message) {
        super(message);
    }
}