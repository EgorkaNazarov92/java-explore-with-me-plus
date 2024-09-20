package ewm.stats.service;

import ewm.dto.EndpointHitDTO;
import ewm.dto.EndpointHitResponseDto;
import ewm.stats.model.Hit;
import ewm.stats.repository.HitRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

@SpringBootTest
@AllArgsConstructor(onConstructor_ = {@Autowired})
class HitServiceImplTest {
    @MockBean
    private HitRepository hitRepository;

    private HitService hitService;

    private static final Hit hit = Hit.builder()
            .id(1L)
            .ip("test")
            .timestamp(LocalDateTime.of(2024, 9, 20, 0, 0, 0))
            .app("test")
            .uri("test")
            .build();

    private static final EndpointHitDTO endpointHitDto = EndpointHitDTO.builder()
            .timestamp("2024-09-20 00:00:00")
            .app("test")
            .uri("test")
            .build();

    private static final EndpointHitResponseDto endpointHitResponseDto = EndpointHitResponseDto.builder()
            .id(1L)
            .timestamp("2024-09-20 00:00:00")
            .app("test")
            .uri("test")
            .ip("test")
            .build();

    @Test
    void create() {
        Mockito.when(hitRepository.save(Mockito.any())).thenReturn(hit);
        EndpointHitResponseDto responseDto = hitService.create(endpointHitDto);
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(endpointHitResponseDto.getApp(), responseDto.getApp());
        Assertions.assertEquals(endpointHitResponseDto.getUri(), responseDto.getUri());
        Assertions.assertEquals(endpointHitResponseDto.getIp(), responseDto.getIp());
        Assertions.assertEquals(endpointHitResponseDto.getId(), responseDto.getId());
    }
}
