package ewm.request.service;

import ewm.request.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequest addParticipationRequest(Long userId, Long eventId);

    List<ParticipationRequest> getUserRequests(Long userId);

    ParticipationRequest cancelRequest(Long userId, Long requestId);
}
