package ewm.event.mapper;

import ewm.event.dto.CreateEventDto;
import ewm.event.dto.EventDto;
import ewm.event.dto.LocationDto;
import ewm.event.model.Event;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class EventMapper {
//    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);
//
//    EventDto mapEventToEventDto(Event event);
//    @Mapping(target="lat", source="location.lat")
////    @Mapping(target = "lon", source="location.lon")
////    @Mapping(target = "eventDate", source = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
////    @Mapping(target = "categoryId", source = "category")
//    Event matCreateEventDtoToEvent (CreateEventDto dto);

    public static Event mapCreateDtoToEvent(CreateEventDto dto){
        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(dto.getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dto.getEventDate(),formatter);
        event.setEventDate(dateTime);
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setTitle(dto.getTitle());
        event.setCategory_id(dto.getCategory());
        event.setLat(dto.getLocation().getLat());
        event.setLon(dto.getLocation().getLon());
        return event;
    }

    public static EventDto mapEventToEventDto(Event event){
        EventDto dto = EventDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .eventDate(event.getEventDate())
                .annotation(event.getAnnotation())
                .paid(event.getPaid())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .state(event.getState())
                .participantLimit(event.getParticipantLimit())
                .location(LocationDto.builder().lat(event.getLat()).lon(event.getLon()).build())
                .build();
        return dto;
    }
}
