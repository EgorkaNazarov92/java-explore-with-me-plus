package ewm.event.dto;

import ewm.category.dto.CategoryDto;
import ewm.user.dto.UserShortDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@Validated
public class EventShortDto {

    private Long id;

    @NotBlank(message = "Краткое описание события не может быть пустым")
    @Size(min=20, max = 2000, message = "Краткое описание не может быть меньше 20 или больше 2000 символов")
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;

    @NotNull(message = "Время события должно быть указано")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;

    @NotBlank(message = "Заголовок события не может быть пустым")
    @Size(min=3, max = 120, message = "Заголовок не может быть меньше 3 или больше 120 символов")
    private String title;
    private Long views;
}
