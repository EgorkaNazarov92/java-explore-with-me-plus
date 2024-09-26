package ewm.request.controller;

import ewm.request.dto.ParticipationRequestDto;
import ewm.request.mapper.ParticipationRequestMapper;
import ewm.request.model.ParticipationRequest;
import ewm.request.service.ParticipationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class ParticipationRequestController {

    private final ParticipationRequestService participationRequestService;
    private final ParticipationRequestMapper participationRequestMapper = ParticipationRequestMapper.INSTANCE;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> addParticipationRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId) {
        ParticipationRequest newRequest = participationRequestService.addParticipationRequest(userId, eventId);
        ParticipationRequestDto requestDto = participationRequestMapper.participationRequestToParticipationRequestDto(newRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(requestDto);
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getUserRequests(
            @PathVariable Long userId) {
        List<ParticipationRequest> requests = participationRequestService.getUserRequests(userId);
        List<ParticipationRequestDto> requestDtos = requests.stream()
                .map(participationRequestMapper::participationRequestToParticipationRequestDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestDtos);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        ParticipationRequest canceledRequest = participationRequestService.cancelRequest(userId, requestId);
        ParticipationRequestDto requestDto = participationRequestMapper.participationRequestToParticipationRequestDto(canceledRequest);
        return ResponseEntity.ok(requestDto);
    }
}
