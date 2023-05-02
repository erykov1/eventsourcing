package cinema;

import java.util.UUID;

record TicketAddedToReservation(UUID reservationId, Ticket ticket) implements ReservationEvent {
}
