package ewm.event.controller;

import ewm.event.EventService;
import ewm.event.dto.EventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class UserEventController {
    private final EventService service;

    @GetMapping
    List<EventDto> getEvents(@PathVariable Long userId) {
        return service.getEvents(userId);
    }

}
