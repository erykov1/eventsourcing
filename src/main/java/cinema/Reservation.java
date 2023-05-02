package cinema;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
class Reservation {

  enum ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELED
  }

  UUID id;
  UUID clientId;
  ReservationStatus status;
  final Collection<Ticket> tickets = new ArrayList<>();
  Instant confirmedAt;
  Instant canceledAt;

  private void apply(ReservationEvent event) {
    switch (event) {
      case ReservationCanceled canceled -> apply(canceled);
      case ReservationConfirmed confirmed -> apply(confirmed);
      case ReservationRequest pending -> apply(pending);
      case TicketAddedToReservation added -> apply(added);
      case TicketRemovedFromReservation removed -> apply(removed);
      default -> throw new IllegalStateException("Unexpected value: " + event);
    }
  }

  private void apply(ReservationRequest event) {
    this.id = event.reservationId();
    this.clientId = event.clientId();
    this.status = ReservationStatus.PENDING;
  }

  private void apply(ReservationCanceled event) {
    this.status = ReservationStatus.CANCELED;
    this.canceledAt = event.canceledAt();
  }

  private void apply(ReservationConfirmed event) {
    this.status = ReservationStatus.CONFIRMED;
    this.confirmedAt = event.confirmedAt();
  }

  private void apply(TicketAddedToReservation event) {
    this.tickets.add(event.ticket());
  }

  private void apply(TicketRemovedFromReservation event) {
    this.tickets
        .removeIf(ticket -> ticket.ticketId().equals(event.ticket().ticketId()));
  }

  static Reservation getReservationFrom(List<ReservationEvent> events) {

    Reservation reservation = new Reservation();
    for(ReservationEvent event: events) {
      reservation.apply(event);
    }

    return reservation;
  }
}
