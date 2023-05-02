package cinema

import com.eventstore.dbclient.EventStoreDBClient
import com.eventstore.dbclient.*
import spock.lang.Specification

import java.time.Instant

class CinemaEventServiceSpec extends Specification {

  UUID clientId = UUID.fromString("a9f6edf2-e851-11ed-a05b-0242ac120003")
  UUID reservationId = UUID.fromString("a9f6f14e-e851-11ed-a05b-0242ac120003")
  List<ReservationEvent> eventStore = new ArrayList<>()
  Ticket ticket = new Ticket(
      UUID.fromString("a9f6f2ca-e851-11ed-a05b-0242ac120003"), 25.0
  )
  Instant time = Instant.now()

  CinemaEventService service = new CinemaEventService(eventStore)

  def "should thrown exception when try to add nothing"() {
    when: "there is a try to pass nothing into list"
      service.appendEvents(null)
    then: "system thrown an exception"
      thrown(Exception)
  }

  def "should append store with all events"() {
    given: "client: $clientId has reservation $reservationId with given ticket"
      List<ReservationEvent> events = List.of(
          new ReservationRequest(reservationId, clientId),
          new TicketAddedToReservation(reservationId, ticket)
      )
    when: "client makes reservation request"
      service.appendEvents(events)
    then: "all events are in store"
      service.findAllEvents().size() == 2
      service.findAllEvents().containsAll(events)
  }

  def "should thrown exception when there is try to add null in events"() {
    given: "client: $clientId has reservation $reservationId with given ticket and there is null"
      List<ReservationEvent> events = new ArrayList<>(
          Arrays.asList(
              new ReservationRequest(reservationId, clientId),
              new TicketAddedToReservation(reservationId, ticket),
              null
          )
      )
    when: "client makes reservation request"
      service.appendEvents(events)
    then: "system thrown exception"
      thrown(Exception)
  }
}
