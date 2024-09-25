package ewm.event;

import ewm.event.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
	List<Event> findByInitiatorId(Long userId, Pageable pageable);

	Optional<Event> findByIdAndInitiatorId(Long id, long initiatorId);

	@Query("SELECT e FROM Event AS e " +
			"WHERE ((:users) IS NULL OR e.initiatorId IN :users) " +
			"AND ((:states) IS NULL OR e.state IN :states) " +
			"AND ((:categories) IS NULL OR e.category_id IN :categories) " +
			"AND ((cast(:rangeStart as timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
			"AND ((cast(:rangeEnd as timestamp) IS NULL OR e.eventDate <= :rangeEnd)))")
	List<Event> findEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
								  LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
