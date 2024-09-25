package ewm.event.dto;

import ewm.category.dto.CategoryDto;
import ewm.user.dto.UserDto;
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
    private Integer participantLimit;
    private LocalDateTime pablishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Long views;
    private CategoryDto category;
    private UserDto initiator;
    private LocationDto location;

}
