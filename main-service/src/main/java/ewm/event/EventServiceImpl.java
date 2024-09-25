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
        Optional<User> user =userRepository.findById(userId);
        if(user.isEmpty()){
            throw new NotFoundException("User not found");
        }
        Pageable pageable = PageRequest.of(from, size);
        return repository.findByInitiatorId(userId,pageable).stream()
                .map(x-> eventToDto(x, user.get()))
                .toList();
    }

    @Override
    public EventDto getEventById(Long userId, Long id) {
        Optional<User> user =userRepository.findById(userId);
        if(user.isEmpty()){
            throw new NotFoundException("User not found");
        }
        Optional<Event> event = repository.findByIdAndInitiatorId(id,userId);
        if(event.isEmpty()){
            throw new NotFoundException("Event not found");
        }
        return eventToDto(event.get(), user.get());
    }

    @Override
    public EventDto createEvent(Long userId, CreateEventDto eventDto) {
        Optional<User> user =userRepository.findById(userId);
        if(user.isEmpty()){
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

    private EventDto eventToDto(Event event, User user){
        EventDto dto = EventMapper.mapEventToEventDto(event);
        dto.setInitiator(UserMapper.mapToUserDto(user));
        Optional<Category> category = categoryRepository.findById(event.getCategory_id());
        if(category.isPresent()){
            dto.setCategory(CategoryMapper.INSTANCE.categoryToCategoryDto(category.get()));
        }

        return dto;
    }
}
