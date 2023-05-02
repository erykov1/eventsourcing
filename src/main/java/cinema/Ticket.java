package cinema;

import java.util.UUID;

record Ticket(UUID ticketId, Double price) implements ReservationEvent {
}
