package ewm.event;

import ewm.category.mapper.CategoryMapper;
import ewm.category.model.Category;
import ewm.category.repository.CategoryRepository;
import ewm.error.exception.ConflictExceprion;
import ewm.error.exception.NotFoundException;
import ewm.event.dto.AdminGetEventRequestDto;
import ewm.event.dto.CreateEventDto;
import ewm.event.dto.EventDto;
import ewm.event.dto.UpdateEventDto;
import ewm.event.mapper.EventMapper;
import ewm.event.model.Event;
import ewm.event.model.EventState;
import ewm.event.model.StateAction;
import ewm.user.mapper.UserMapper;
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

	@Override
	public List<EventDto> getEvents(Long userId, Integer from, Integer size) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			throw new NotFoundException("User not found");
		}
		Pageable pageable = PageRequest.of(from, size);
		return repository.findByInitiatorId(userId, pageable).stream()
				.map(x -> eventToDto(x, user.get()))
				.toList();
	}

	@Override
	public EventDto getEventById(Long userId, Long id) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			throw new NotFoundException("User not found");
		}
		Optional<Event> event = repository.findByIdAndInitiatorId(id, userId);
		if (event.isEmpty()) {
			throw new NotFoundException("Event not found");
		}
		return eventToDto(event.get(), user.get());
	}

	@Override
	public EventDto createEvent(Long userId, CreateEventDto eventDto) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			throw new NotFoundException("User not found");
		}
		Event event = EventMapper.mapCreateDtoToEvent(eventDto);
		event.setInitiatorId(userId);
		Event newEvent = repository.save(event);

		return eventToDto(event, user.get());
	}

	@Override
	public EventDto updateEvent(Long userId, UpdateEventDto eventDto) {
		return null;
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
		checkEventForUpdate(event, eventDto.getStateAction());
		Event updatedEvent = repository.save(prepareEventForUpdate(event, eventDto));
		EventDto result = EventMapper.mapEventToEventDto(updatedEvent);
		return result;
	}

	private EventDto eventToDto(Event event, User user) {
		EventDto dto = EventMapper.mapEventToEventDto(event);
		dto.setInitiator(UserMapper.mapToUserDto(user));
		Optional<Category> category = categoryRepository.findById(event.getCategory_id());
		if (category.isPresent()) {
			dto.setCategory(CategoryMapper.INSTANCE.categoryToCategoryDto(category.get()));
		}

		return dto;
	}

	private Event getEvent(Long eventId) {
		return repository.findById(eventId)
				.orElseThrow(() -> new NotFoundException("Event not found"));
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
				event.setState(EventState.PUBLISHED.toString());
				break;
			case CANCEL_REVIEW:
				event.setState(EventState.CANCELED.toString());
				break;
			case SEND_TO_REVIEW:
				event.setState(EventState.PENDING.toString());
				break;
		}
		return event;
	}

	private void checkEventDate(LocalDateTime dateTime) {
		if (dateTime.isAfter(LocalDateTime.now().plusHours(1)))
			throw new ConflictExceprion("Дата начала события меньше чем час" + dateTime);
	}
}
