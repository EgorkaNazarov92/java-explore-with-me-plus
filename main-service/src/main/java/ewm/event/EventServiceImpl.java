package ewm.event;

import ewm.category.mapper.CategoryMapper;
import ewm.category.model.Category;
import ewm.category.repository.CategoryRepository;
import ewm.error.exception.NotFoundException;
import ewm.event.dto.CreateEventDto;
import ewm.event.dto.EventDto;
import ewm.event.dto.UpdateEventDto;
import ewm.event.mapper.EventMapper;
import ewm.event.model.Event;
import ewm.user.mapper.UserMapper;
import ewm.user.model.User;
import ewm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements  EventService{

    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventDto> getEvents(Long userId, Integer from, Integer size) {
        User user = getUser(userId);
        Pageable pageable = PageRequest.of(from, size);
        return repository.findByInitiatorId(userId,pageable).stream()
                .map(x-> eventToDto(x, user))
                .toList();
    }

    @Override
    public EventDto getEventById(Long userId, Long id) {
        User user = getUser(userId);
        Optional<Event> event = repository.findByIdAndInitiatorId(id,userId);
        if(event.isEmpty()){
            throw new NotFoundException("Event not found");
        }
        return eventToDto(event.get(), user);
    }

    @Override
    public EventDto createEvent(Long userId, CreateEventDto eventDto) {
        User user = getUser(userId);
        Event event = EventMapper.mapCreateDtoToEvent(eventDto);
        event.setInitiatorId(userId);
        Event newEvent = repository.save(event);

        return eventToDto(event, user);
    }

    @Override
    public EventDto updateEvent(Long userId, UpdateEventDto eventDto, Long eventId) {
        User user = getUser(userId);
        Event event = EventMapper.mapUpdateDtoToEvent(eventDto);
        event.setId(eventId);
        Optional<Event> newEvent = repository.findById(eventId);
        if(newEvent.isEmpty()){
            throw new NotFoundException("Event not found");
        }

        if(event.getAnnotation() != null && !event.getAnnotation().isBlank()){
            newEvent.get().setAnnotation(event.getAnnotation());
        }
        if(event.getDescription() != null && !event.getDescription().isBlank()){
            newEvent.get().setDescription(event.getDescription());
        }
        if(event.getEventDate() != null){
            newEvent.get().setEventDate(event.getEventDate());
        }
        if(event.getPaid()!=null){
            newEvent.get().setPaid(event.getPaid());
        }
        if(event.getParticipantLimit() != null){
            newEvent.get().setParticipantLimit(event.getParticipantLimit());
        }
        if(event.getRequestModeration()!=null){
            newEvent.get().setRequestModeration(event.getRequestModeration());
        }
        if (event.getTitle() != null && !event.getTitle().isBlank()){
            newEvent.get().setTitle(event.getTitle());
        }
        if(event.getCategory_id()!=null){
            newEvent.get().setCategory_id(event.getCategory_id());
        }
        if(event.getLat() != null){
            newEvent.get().setLat(event.getLat());
        }
        if(event.getLon() != null){
            newEvent.get().setLon(event.getLon());
        }
        repository.save(newEvent.get());

        return eventToDto(newEvent.get(), user);
    }

    private EventDto eventToDto(Event event, User user){
        EventDto dto = EventMapper.mapEventToEventDto(event);
        dto.setInitiator(UserMapper.mapToUserDto(user));
        Optional<Category> category = categoryRepository.findById(event.getCategory_id());
        if(category.isPresent()){
            dto.setCategory(CategoryMapper.INSTANCE.categoryToCategoryDto(category.get()));
        }

        return dto;
    }

    private User getUser(Long userId){
        Optional<User> user =userRepository.findById(userId);
        if(user.isEmpty()){
            throw new NotFoundException("User not found");
        }
        return user.get();
    }
}
