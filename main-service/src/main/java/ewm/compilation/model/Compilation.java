package ewm.compilation.model;


import ewm.event.model.Event;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean pinned;

    @Column(nullable = false, length = 50)
    private String title;

    @ManyToMany
    private List<Event> events;
}
