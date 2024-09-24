package ewm.event;

import ewm.event.dto.CreateEventDto;
import ewm.event.dto.EventDto;
import ewm.event.dto.UpdateEventDto;

import java.util.List;

public interface EventService {
    List<EventDto> getEvents(Long userId);

    EventDto getEventById(Long userId, Long Id);

    EventDto createEvent(Long userId, CreateEventDto eventDto);

    EventDto updateEvent(Long userId, UpdateEventDto eventDto);

}
