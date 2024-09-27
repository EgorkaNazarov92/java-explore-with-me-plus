package ewm.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserShortDto {

    private Long id;

    @NotBlank
    @Size(min = 2, max = 250, message = "Имя должно быть от 2 до 250 символов")
    private String name;
}
