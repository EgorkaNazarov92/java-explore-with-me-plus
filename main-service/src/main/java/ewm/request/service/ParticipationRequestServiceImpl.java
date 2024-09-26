package ewm.request.service;

import ewm.error.exception.ConflictException;
import ewm.error.exception.NotFoundException;
import ewm.request.model.ParticipationRequest;
import ewm.request.repository.RequestRepository;
import ewm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public ParticipationRequest addParticipationRequest(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));

        eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id=" + eventId + " не найдено"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Инициатор не может участвовать в своём событии");
        }

        if (event.getParticipantLimit() != 0 &&
                event.getConfirmedRequests().size() >= event.getParticipantLimit()) {
            throw new ConflictException("Лимит участников на событие исчерпан");
        }

        if (requestRepository.existsByRequesterAndEvent(userId, eventId)) {
            throw new ConflictException("Повторный запрос на участие не разрешен");
        }

        ParticipationRequest newRequest = ParticipationRequest.builder()
                .requester(userId)
                .event(eventId)
                .status(event.isRequestModeration() ? "PENDING" : "CONFIRMED")
                .created(LocalDateTime.now())
                .build();

        return requestRepository.save(newRequest);
    }

    public List<ParticipationRequest> getUserRequests(Long userId) {
        return requestRepository.findByRequester(userId);
    }

    public ParticipationRequest cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с id=" + requestId + " не найден"));

        if (!request.getRequester().equals(userId)) {
            throw new ConflictException("Пользователь не может отменить чужой запрос");
        }

        request.setStatus("CANCELLED");
        return requestRepository.save(request);
    }
}
