package cinema;

import java.util.UUID;

record ReservationRequest(UUID reservationId, UUID clientId) implements ReservationEvent {
}
