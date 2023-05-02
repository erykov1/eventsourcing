package cinema

import spock.lang.Specification

import java.time.Instant

class ReservationStateSpec extends Specification {

  UUID clientId = UUID.fromString("a9f6edf2-e851-11ed-a05b-0242ac120003")
  UUID reservationId = UUID.fromString("a9f6f14e-e851-11ed-a05b-0242ac120003")
  Ticket ticket = new Ticket(
      UUID.fromString("a9f6f2ca-e851-11ed-a05b-0242ac120003"), 25.0
  )
  Instant time = Instant.now()

  def "Should have pending status when client send request for reservation"() {
    when: "client: $clientId send reservation request $reservationId"
      List<ReservationEvent> events = List.of(
          new ReservationRequest(reservationId, clientId)
      )
    then: "reservation request: $reservationId has pending status"
      Reservation reservation = Reservation.getReservationFrom(events)
      reservation.getId() == reservationId
      reservation.getClientId() == clientId
      reservation.status.toString() == "PENDING"
  }

  def "Should have pending status when client add another reservation request"() {
    when: "client: $clientId has one reservation request $reservationId and add another ticket"
      List<ReservationEvent> events = List.of(
          new ReservationRequest(reservationId, clientId),
          new TicketAddedToReservation(reservationId, ticket)
      )
    then: "client: $clientId has two reservation request with pending status"
      Reservation reservation = Reservation.getReservationFrom(events)
      reservation.getId() == reservationId
      reservation.getClientId() == clientId
      reservation.status.toString() == "PENDING"
  }

  def "Should have pending status when client remove one ticket"() {
    when: "client: $clientId has one reservation request $reservationId and add another ticket then remove it"
      List<ReservationEvent> events = List.of(
          new ReservationRequest(reservationId, clientId),
          new TicketAddedToReservation(reservationId, ticket),
          new TicketRemovedFromReservation(reservationId, ticket)
      )
    then: "client: $clientId has reservation request with pending status"
      Reservation reservation = Reservation.getReservationFrom(events)
      reservation.getId() == reservationId
      reservation.getClientId() == clientId
      reservation.status.toString() == "PENDING"

  }

  def "Should have canceled status when client decides to cancel reservation"() {
    when: "client: $clientId has one reservation request $reservationId and then cancels it"
      List<ReservationEvent> events = List.of(
          new ReservationRequest(reservationId, clientId),
          new ReservationCanceled(reservationId, time)
      )
    then: "reservation: $reservationId has status canceled"
      Reservation reservation = Reservation.getReservationFrom(events)
      reservation.getId() == reservationId
      reservation.getClientId() == clientId
      reservation.getCanceledAt() == time
      reservation.status.toString() == "CANCELED"
  }

  def "Should have canceled status when client added another ticket and decides to cancel reservation"() {
    when: "client: $clientId has one reservation request $reservationId and then cancels it"
    List<ReservationEvent> events = List.of(
        new ReservationRequest(reservationId, clientId),
        new TicketAddedToReservation(reservationId, ticket),
        new ReservationCanceled(reservationId, time)
    )
    then: "reservation: $reservationId has status canceled"
    Reservation reservation = Reservation.getReservationFrom(events)
    reservation.getId() == reservationId
    reservation.getClientId() == clientId
    reservation.getCanceledAt() == time
    reservation.status.toString() == "CANCELED"
  }

  def "Should have canceled status when client added another ticket then remove it and decides to cancel reservation"() {
    when: "client: $clientId has one reservation request $reservationId and then cancels it"
    List<ReservationEvent> events = List.of(
        new ReservationRequest(reservationId, clientId),
        new TicketAddedToReservation(reservationId, ticket),
        new TicketRemovedFromReservation(reservationId, ticket),
        new ReservationCanceled(reservationId, time)
    )
    then: "reservation: $reservationId has status canceled"
      Reservation reservation = Reservation.getReservationFrom(events)
      reservation.getId() == reservationId
      reservation.getClientId() == clientId
      reservation.getCanceledAt() == time
      reservation.status.toString() == "CANCELED"
  }

  def "Should have confirmed status when reservation is accepted"() {
    when: "client: $clientId has his reservation request $reservationId accepted"
      List<ReservationEvent> events = List.of(
          new ReservationRequest(reservationId, clientId),
          new ReservationConfirmed(reservationId, time)
      )
    then: "reservation $reservationId has status confirmed"
      Reservation reservation = Reservation.getReservationFrom(events)
      reservation.getId() == reservationId
      reservation.getClientId() == clientId
      reservation.getConfirmedAt() == time
      reservation.status.toString() == "CONFIRMED"
  }

  def "Should have confirmed status when client add another ticket and then reservation is accepted"() {
    when: "client: $clientId has his reservation with added another ticket then request $reservationId is accepted"
    List<ReservationEvent> events = List.of(
        new ReservationRequest(reservationId, clientId),
        new TicketAddedToReservation(reservationId, ticket),
        new ReservationConfirmed(reservationId, time)
    )
    then: "reservation $reservationId has status confirmed"
    Reservation reservation = Reservation.getReservationFrom(events)
    reservation.getId() == reservationId
    reservation.getClientId() == clientId
    reservation.getConfirmedAt() == time
    reservation.status.toString() == "CONFIRMED"
  }

  def "Should have confirmed status when client added another ticket then remove it and then reservation is accepted"() {
    when: "client: $clientId has one reservation request $reservationId and then cancels it"
    List<ReservationEvent> events = List.of(
        new ReservationRequest(reservationId, clientId),
        new TicketAddedToReservation(reservationId, ticket),
        new TicketRemovedFromReservation(reservationId, ticket),
        new ReservationConfirmed(reservationId, time)
    )
    then: "reservation: $reservationId has status canceled"
    Reservation reservation = Reservation.getReservationFrom(events)
    reservation.getId() == reservationId
    reservation.getClientId() == clientId
    reservation.getConfirmedAt() == time
    reservation.status.toString() == "CONFIRMED"
  }
}
