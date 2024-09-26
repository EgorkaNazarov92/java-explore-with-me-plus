package ewm.request.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {

    private Long id;
    private Long requester;
    private Long event;
    private String status;
    private LocalDateTime created;
}
