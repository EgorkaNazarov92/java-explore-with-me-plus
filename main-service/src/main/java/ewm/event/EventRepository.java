package ewm.event;

import ewm.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface EventRepository  extends JpaRepository<Event, Long> {
    List<Event> findByInitiatorId(Long userId, Pageable pageable);
    Optional<Event> findByIdAndInitiatorId(Long id, long initiatorId);
}
