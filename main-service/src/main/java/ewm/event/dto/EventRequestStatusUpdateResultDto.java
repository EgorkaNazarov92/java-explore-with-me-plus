package ewm.event.dto;

import ewm.request.dto.ParticipationRequestDto;
import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateResultDto {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
