package ewm;

import ewm.stats.StatsClient;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class MainApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);
		StatsClient statsClient = context.getBean(StatsClient.class);
	}
}
