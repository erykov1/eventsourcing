package cinema;

import java.time.Instant;
import java.util.UUID;

record ReservationConfirmed(UUID reservationId, Instant confirmedAt) implements ReservationEvent {
}
