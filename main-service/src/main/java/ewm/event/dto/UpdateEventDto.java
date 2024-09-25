package ewm.event.dto;

import ewm.event.model.StateAction;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateEventDto {
	protected String annotation;
	protected Long category;
	protected String description;
	protected LocalDateTime eventDate;
	protected LocationDto location;
	protected Boolean paid;
	protected Integer participantLimit;
	protected Boolean requestModeration;
	protected StateAction stateAction;
	protected String title;
}
