package ewm.event;

import ewm.category.model.Category;
import ewm.category.repository.CategoryRepository;
import ewm.error.exception.ConflictExceprion;
import ewm.error.exception.NotFoundException;
import ewm.error.exception.ValidationException;
import ewm.event.dto.AdminGetEventRequestDto;
import ewm.event.dto.CreateEventDto;
import ewm.event.dto.EventDto;
import ewm.event.dto.PublicGetEventRequestDto;
import ewm.event.dto.UpdateEventDto;
import ewm.event.mapper.EventMapper;
import ewm.event.model.Event;
import ewm.event.model.EventState;
import ewm.event.model.StateAction;
import ewm.user.model.User;
import ewm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private static final String EVENT_NOT_FOUND_MESSAGE = "Event not found";

    @Override
    public List<EventDto> getEvents(Long userId, Integer from, Integer size) {
        getUser(userId);
        Pageable pageable = PageRequest.of(from, size);
        return repository.findByInitiatorId(userId, pageable).stream()
                .map(this::eventToDto)
                .toList();
    }

    @Override
    public EventDto getEventById(Long userId, Long id) {
        getUser(userId);
        Optional<Event> event = repository.findByIdAndInitiatorId(id, userId);
        if (event.isEmpty()) {
            throw new NotFoundException(EVENT_NOT_FOUND_MESSAGE);
        }
        return eventToDto(event.get());
    }

    @Override
    public EventDto createEvent(Long userId, CreateEventDto eventDto) {
        User user = getUser(userId);
        Category category = getCategory(eventDto.getCategory());
        Event event = EventMapper.mapCreateDtoToEvent(eventDto);
        event.setInitiator(user);
        event.setCategory(category);
        event.setState(EventState.PENDING);
        Event newEvent = repository.save(event);
        return eventToDto(newEvent);
    }

    @Override
    public EventDto updateEvent(Long userId, UpdateEventDto eventDto, Long eventId) {
        getUser(userId);
        Optional<Event> eventOptional = repository.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new NotFoundException(EVENT_NOT_FOUND_MESSAGE);
        }
        Event foundEvent = eventOptional.get();
        if (foundEvent.getState() == EventState.PUBLISHED) {
            throw new ConflictExceprion("Нельзя изменять изменять сообщение, которое опубликовано");
        }
        updateEventFields(eventDto, foundEvent);
        Event saved = repository.save(foundEvent);
        return eventToDto(saved);
    }

    @Override
    public List<EventDto> publicGetEvents(PublicGetEventRequestDto requestParams) {
        List<Event> events = repository.findEventsPublic(
                requestParams.getText(),
                requestParams.getCategories(),
                requestParams.getPaid(),
                requestParams.getRangeStart(),
                requestParams.getRangeEnd(),
                EventState.PUBLISHED,
                requestParams.getOnlyAvailable(),
                PageRequest.of(requestParams.getFrom() / requestParams.getSize()
                        , requestParams.getSize())
        );
        return EventMapper.mapToEventDto(events);
    }

    @Override
    public EventDto publicGetEvent(Long id) {
        return EventMapper.mapEventToEventDto(getEvent(id));
    }

    @Override
    public List<EventDto> adminGetEvents(AdminGetEventRequestDto requestParams) {
        List<Event> events = repository.findEventsByAdmin(
                requestParams.getUsers(),
                requestParams.getStates(),
                requestParams.getCategories(),
                requestParams.getRangeStart(),
                requestParams.getRangeEnd(),
                PageRequest.of(requestParams.getFrom() / requestParams.getSize()
                        , requestParams.getSize())
        );
        return EventMapper.mapToEventDto(events);
    }

    @Override
    public EventDto adminChangeEvent(Long eventId, UpdateEventDto eventDto) {
        Event event = getEvent(eventId);
        //checkEventForUpdate(event, eventDto.getStateAction());
        Event updatedEvent = repository.save(prepareEventForUpdate(event, eventDto));
        EventDto result = EventMapper.mapEventToEventDto(updatedEvent);
        return result;
    }

    private EventDto eventToDto(Event event) {
        return EventMapper.mapEventToEventDto(event);
    }

    private Event getEvent(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND_MESSAGE));
    }

    private void checkEventForUpdate(Event event, StateAction action) {
        checkEventDate(event.getEventDate());
        if (action.equals(StateAction.PUBLISH_EVENT)
                && !event.getState().equals(EventState.PENDING))
            throw new ConflictExceprion("Опубликовать событие можно в статусе PENDING, а статус = "
                    + event.getState());
        if (action.equals(StateAction.CANCEL_REVIEW)
                && event.getState().equals(EventState.PUBLISHED))
            throw new ConflictExceprion("Отменить событие можно только в статусе PUBLISHED, а статус = "
                    + event.getState());
    }

    private Event prepareEventForUpdate(Event event, UpdateEventDto updateEventDto) {
        if (updateEventDto.getAnnotation() != null)
            event.setAnnotation(updateEventDto.getAnnotation());
        if (updateEventDto.getDescription() != null)
            event.setDescription(updateEventDto.getDescription());
        if (updateEventDto.getEventDate() != null) {
            checkEventDate(updateEventDto.getEventDate());
            event.setEventDate(updateEventDto.getEventDate());
        }
        if (updateEventDto.getPaid() != null)
            event.setPaid(updateEventDto.getPaid());
        if (updateEventDto.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        if (updateEventDto.getTitle() != null)
            event.setTitle(updateEventDto.getTitle());
        switch (updateEventDto.getStateAction()) {
            case PUBLISH_EVENT:
                event.setState(EventState.PUBLISHED);
                break;
            case CANCEL_REVIEW:
                event.setState(EventState.CANCELED);
                break;
            case SEND_TO_REVIEW:
                event.setState(EventState.PENDING);
                break;
        }
        return event;
    }

    private void checkEventDate(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now().plusHours(1)))
            throw new ConflictExceprion("Дата начала события меньше чем час " + dateTime);
    }

    private User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return user.get();
    }

    private Category getCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new NotFoundException("Категория не найдена");
        }
        return category.get();
    }


    private void updateEventFields(UpdateEventDto eventDto, Event foundEvent) {
        if (eventDto.getCategory() != null) {
            Category category = getCategory(eventDto.getCategory());
            foundEvent.setCategory(category);
        }

        if (eventDto.getAnnotation() != null && !eventDto.getAnnotation().isBlank()) {
            foundEvent.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null && !eventDto.getDescription().isBlank()) {
            foundEvent.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConflictExceprion("Дата начала события не может быть раньше чем через 2 часа");
            }
            foundEvent.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getPaid() != null) {
            foundEvent.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            if (eventDto.getParticipantLimit() < 0) {
                throw new ValidationException("Participant limit cannot be negative");
            }
            foundEvent.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() != null) {
            foundEvent.setRequestModeration(eventDto.getRequestModeration());
        }
        if (eventDto.getTitle() != null && !eventDto.getTitle().isBlank()) {
            foundEvent.setTitle(eventDto.getTitle());
        }
        if (eventDto.getLocation() != null) {
            if (eventDto.getLocation().getLat() != null) {
                foundEvent.setLat(eventDto.getLocation().getLat());
            }
            if (eventDto.getLocation().getLon() != null) {
                foundEvent.setLon(eventDto.getLocation().getLon());
            }
        }

        if (eventDto.getStateAction() != null) {
            switch (eventDto.getStateAction()) {
                case CANCEL_REVIEW -> foundEvent.setState(EventState.CANCELED);
                case PUBLISH_EVENT -> foundEvent.setState(EventState.PUBLISHED);
                case SEND_TO_REVIEW -> foundEvent.setState(EventState.PENDING);
            }
        }
    }
}
