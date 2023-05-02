package cinema;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
class CinemaEventService {

  private final List<ReservationEvent> eventStore;

  void appendEvents(List<ReservationEvent> event) throws Exception {
    if(event.isEmpty() || event.contains(null)) {
      throw new IllegalArgumentException("event cannot be null or empty");
    }
    try{
       eventStore.addAll(event);
    } catch(Exception e) {
      throw new Exception(e);
    }
  }

  List<ReservationEvent> findAllEvents() {
    return new ArrayList<>(eventStore);
  }
}
