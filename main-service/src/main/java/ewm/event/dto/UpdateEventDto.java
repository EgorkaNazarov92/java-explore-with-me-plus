package ewm.event.dto;

import ewm.event.EventService;
import ewm.event.constant.StateAction;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateEventDto {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
    private StateAction stateAction;
}
