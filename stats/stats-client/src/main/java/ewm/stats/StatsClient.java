package ewm.stats;

import dto.EndpointHitDTO;
import dto.StatsRequestDTO;
import ewm.client.BaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class StatsClient extends BaseClient {

	@Autowired
	public StatsClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(
				builder
						.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
						.requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
						.build()
		);
	}

	public ResponseEntity<Object> saveHit(EndpointHitDTO requestDto) {
		return post("/hit", requestDto);
	}

	public ResponseEntity<Object> getStats(StatsRequestDTO headersDto) {
		return get("/stats", headersDto);
	}
}
