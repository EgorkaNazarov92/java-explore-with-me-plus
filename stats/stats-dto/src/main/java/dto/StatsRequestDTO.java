package dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class StatsRequestDTO {

    @NotNull
    private String start;

    @NotNull
    private String end;

    private List<String> uris;

    private Boolean unique = false;
}
