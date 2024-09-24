package ewm.event.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventDto {
    private Long id;
    private String annotation;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private Boolean paid;
    private Long participantLimit;
    private LocalDateTime pablishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Long views;

//    private Location location;
    //User initiator
    //Category category
}
