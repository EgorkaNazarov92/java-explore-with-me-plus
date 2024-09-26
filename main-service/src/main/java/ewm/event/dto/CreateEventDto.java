package ewm.event.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@Validated
public class CreateEventDto {
    @NotBlank(message = "Краткое описание события не может быть пустым")
    private String annotation;
    @NotNull(message = "Категория должна быть указана")
    private Long category;
    @NotBlank(message = "Полное описание события не может быть пустым")
    private String description;
    @NotNull(message = "Время события должно быть указано")
    private String eventDate;
    @NotNull(message = "Место проведения события должно быть указано")
    private LocationDto location;
    @Builder.Default
    private Boolean paid = false;
    private Integer participantLimit;
    @Builder.Default
    private Boolean requestModeration = true;
    @NotBlank(message = "Заголовок события не может быть пустым")
    private String title;
}
