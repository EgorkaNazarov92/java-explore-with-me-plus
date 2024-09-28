package ewm.event.controller.priv;

import ewm.event.EventService;
import ewm.event.dto.*;
import ewm.event.validate.EventValidate;
import ewm.request.dto.ParticipationRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
					  @PathVariable Long id,
					  HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		String uri = request.getRequestURI();
		return service.getEventById(userId, id, ip, uri);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	EventDto createEvent(@PathVariable Long userId,
						 @Valid @RequestBody CreateEventDto event) {
		EventValidate.eventDateValidate(event, log);
		return service.createEvent(userId, event);
	}

	@PatchMapping("/{eventId}")
	EventDto updateEvent(@PathVariable Long userId,
						 @PathVariable Long eventId,
						 @Valid @RequestBody UpdateEventDto event) {
		EventValidate.updateEventDateValidate(event, log);
		EventValidate.textLengthValidate(event, log);
		return service.updateEvent(userId, event, eventId);
	}

	@PatchMapping("/{eventId}/requests")
	EventRequestStatusUpdateResultDto updateStatusRequest(@PathVariable Long userId,
														  @PathVariable Long eventId,
														  @RequestBody EventRequestStatusUpdateRequestDto dto) {
		return service.updateStatusRequest(userId, eventId, dto);
	}

	@GetMapping("/{eventId}/requests")
	ResponseEntity<List<ParticipationRequestDto>> getEventParticipants(
			@PathVariable Long userId,
			@PathVariable Long eventId) {
		List<ParticipationRequestDto> requests = service.getEventParticipants(userId, eventId);
		return ResponseEntity.ok(requests);
	}

}
