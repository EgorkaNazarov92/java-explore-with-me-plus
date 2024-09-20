package ewm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewStatsDTO {

    private String app;

    private String uri;

    private Long hits;
}
