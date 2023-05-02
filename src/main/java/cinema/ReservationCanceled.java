package cinema;

import java.time.Instant;
import java.util.UUID;

record ReservationCanceled(UUID reservationId, Instant canceledAt) implements ReservationEvent {
}
