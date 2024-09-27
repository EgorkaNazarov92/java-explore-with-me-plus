package ewm.compilation.dto;

import ewm.event.dto.EventShortDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    @NotNull
    private Long id;

    @NotNull
    private Boolean pinned;

    @NotNull
    private String title;

    private List<EventShortDto> events;
}
