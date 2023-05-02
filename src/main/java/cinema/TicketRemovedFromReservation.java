package cinema;

import java.util.UUID;

record TicketRemovedFromReservation(UUID reservationId, Ticket ticket) implements ReservationEvent {
}
