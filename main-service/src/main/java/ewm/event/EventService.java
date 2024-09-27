package ewm.event;

import ewm.event.dto.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface EventService {
	List<EventDto> getEvents(Long userId, Integer from, Integer size);

	EventDto getEventById(Long userId, Long Id, String ip, String uri);

	EventDto createEvent(Long userId, CreateEventDto eventDto);

	List<EventDto> adminGetEvents(AdminGetEventRequestDto requestParams);

	EventDto adminChangeEvent(Long eventId, UpdateEventDto eventDto);

	EventDto updateEvent(Long userId, UpdateEventDto eventDto, Long eventId);

	List<EventDto> publicGetEvents(PublicGetEventRequestDto requestParams, HttpServletRequest request);

	EventDto publicGetEvent(Long id, HttpServletRequest request);
}
