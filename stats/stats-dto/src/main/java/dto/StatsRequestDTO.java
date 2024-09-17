package dto;

import lombok.Data;
import validation.Create;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class StatsRequestDTO {

    @NotNull(groups = Create.class)
    private String start;

    @NotNull(groups = Create.class)
    private String end;

    private List<String> uris;

    private Boolean unique = false;
}
