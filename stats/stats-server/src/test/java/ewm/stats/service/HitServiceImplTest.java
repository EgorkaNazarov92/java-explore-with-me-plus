package ewm.stats.service;

import ewm.dto.EndpointHitDTO;
import ewm.dto.EndpointHitResponseDto;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AllArgsConstructor(onConstructor_ = {@Autowired})
class HitServiceImplTest {
    private HitService hitService;

    private static final EndpointHitDTO endpointHitDto = EndpointHitDTO.builder()
            .timestamp("2024-09-20 00:00:00")
            .app("test")
            .uri("test")
            .ip("test")
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
        EndpointHitResponseDto responseDto = hitService.create(endpointHitDto);
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(endpointHitResponseDto.getApp(), responseDto.getApp());
        Assertions.assertEquals(endpointHitResponseDto.getUri(), responseDto.getUri());
        Assertions.assertEquals(endpointHitResponseDto.getIp(), responseDto.getIp());
        Assertions.assertEquals(endpointHitResponseDto.getId(), responseDto.getId());
    }
}
