package dto;

import lombok.Data;
import validation.Create;
import validation.Update;

import javax.validation.constraints.NotBlank;

@Data
public class EndpointHitDTO {

    @NotBlank(groups = {Create.class, Update.class})
    private String app;

    @NotBlank(groups = {Create.class, Update.class})
    private String uri;

    @NotBlank(groups = {Create.class, Update.class})
    private String ip;

    @NotBlank(groups = {Create.class, Update.class})
    private String timestamp;
}
