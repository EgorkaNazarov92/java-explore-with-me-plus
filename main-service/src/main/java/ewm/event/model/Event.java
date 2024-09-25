package ewm.event.model;

import ewm.category.model.Category;
import ewm.event.dto.LocationDto;
import ewm.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;

@Table(name = "events")
//@SecondaryTable(name = "categories")
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

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
//
//    @Column(name = "user_id")
//    private Long initiatorId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User initiator;

    private Double lat;
    private Double lon;
}
