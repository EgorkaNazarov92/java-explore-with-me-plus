package ewm.request.repository;

import ewm.request.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findByRequester(Long userId);

    boolean existsByRequesterAndEvent(Long userId, Long eventId);
}
