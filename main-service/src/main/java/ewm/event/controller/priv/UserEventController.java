package ewm.event.controller.priv;

import ewm.event.EventService;
import ewm.event.dto.CreateEventDto;
import ewm.event.dto.EventDto;
import ewm.event.dto.UpdateEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

	@PostMapping
	EventDto createEvent(@PathVariable Long userId,
						 @RequestBody CreateEventDto event) {
		return service.createEvent(userId, event);
	}

	@PatchMapping("/{id}")
	EventDto updateEvent (@PathVariable Long userId,
						  @PathVariable Long id,
						  @RequestBody UpdateEventDto event){
		return service.updateEvent(userId,event, id);
	}


}
