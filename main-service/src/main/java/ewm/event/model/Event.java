package ewm.event.model;

import ewm.event.dto.LocationDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;

@Table(name = "events")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @Column(name = "create_on")
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime pablishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    @Column(name = "category_id")
    private Long category_id;
    @Column(name = "user_id")
    private Long initiatorId;
    private Double lat;
    private Double lon;
}
