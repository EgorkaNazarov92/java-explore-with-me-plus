package ewm.request.service;

import ewm.error.exception.ConflictException;
import ewm.error.exception.NotFoundException;
import ewm.event.EventRepository;
import ewm.event.model.Event;
import ewm.event.model.EventState;
import ewm.request.model.ParticipationRequest;
import ewm.request.repository.RequestRepository;
import ewm.user.model.User;
import ewm.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    public ParticipationRequest addParticipationRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id=" + eventId + " не найдено"));
        Long countConfirmedRequest = (long) requestRepository.findByEvent(eventId).size();
        event.setConfirmedRequests(countConfirmedRequest);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Инициатор не может участвовать в своём событии");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Нельзя подать заявку на неопубликованое событие");
        }

        if (event.getParticipantLimit() != 0 &&
                event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Лимит участников на событие исчерпан");
        }

        if (requestRepository.existsByRequesterAndEvent(userId, eventId)) {
            throw new ConflictException("Повторный запрос на участие не разрешен");
        }
        String status = "PENDING";
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            status = "CONFIRMED";
        }

        ParticipationRequest newRequest = ParticipationRequest.builder()
                .requester(userId)
                .event(eventId)
                .status(status)
                .created(event.getCreatedOn())
                .build();

        ParticipationRequest savedRequest = requestRepository.save(newRequest);
        if (event.getRequestModeration()) {
            eventRepository.incrementConfirmedRequest(eventId);
        }
        return savedRequest;
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

        request.setStatus("CANCELED");
        return requestRepository.save(request);
    }
}
