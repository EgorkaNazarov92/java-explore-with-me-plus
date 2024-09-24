package ewm.stats;

import ewm.client.BaseClient;
import ewm.dto.EndpointHitDTO;
import ewm.dto.StatsRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class StatsClient extends BaseClient {

	@Autowired
	public StatsClient(@Value("${stats.server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(
				builder
						.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
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
