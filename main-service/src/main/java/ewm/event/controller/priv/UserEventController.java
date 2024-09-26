package ewm.event.controller.priv;

import ewm.event.EventService;
import ewm.event.dto.CreateEventDto;
import ewm.event.dto.EventDto;
import ewm.event.dto.UpdateEventDto;
import ewm.event.validate.EventValidate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class UserEventController {
	private final EventService service;

	@GetMapping
	List<EventDto> getEvents(@PathVariable Long userId,
							 @RequestParam(name = "from", defaultValue = "0") Integer from,
							 @RequestParam(name = "size", defaultValue = "10") Integer size) {
		return service.getEvents(userId, from, size);
	}

	@GetMapping("/{id}")
	EventDto getEvent(@PathVariable Long userId,
					  @PathVariable Long id) {
		return service.getEventById(userId, id);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	EventDto createEvent(@PathVariable Long userId,
						 @Valid @RequestBody CreateEventDto event) {
		EventValidate.EventDateValidate(event, log);
		return service.createEvent(userId, event);
	}

	@PatchMapping("/{eventId}")
	EventDto updateEvent(@PathVariable Long userId,
						 @PathVariable Long eventId,
						 @Valid @RequestBody UpdateEventDto event) {
		return service.updateEvent(userId, event, eventId);
	}


}
