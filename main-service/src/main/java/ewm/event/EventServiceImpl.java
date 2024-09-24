package ewm.event;

import ewm.event.dto.CreateEventDto;
import ewm.event.dto.EventDto;
import ewm.event.dto.UpdateEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements  EventService{
    @Override
    public List<EventDto> getEvents(Long userId) {
        return List.of();
    }

    @Override
    public EventDto getEventById(Long userId, Long Id) {
        return null;
    }

    @Override
    public EventDto createEvent(Long userId, CreateEventDto eventDto) {
        return null;
    }

    @Override
    public EventDto updateEvent(Long userId, UpdateEventDto eventDto) {
        return null;
    }
}
