package dto;

import javax.validation.constraints.NotNull;

public class EndpointHitResponseDto extends EndpointHitDTO {

    @NotNull
    private Long id;

}
