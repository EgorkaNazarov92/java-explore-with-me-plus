package dto;

import javax.validation.constraints.NotNull;

public class EndpointHitRequestDto extends EndpointHitDTO {

    @NotNull
    private Long id;

}
