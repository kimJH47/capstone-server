package capstone.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("capstone.server.oauth.config.properties")
public class BucketApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yml,";
//			+ "classpath:application-aws.yml";

	public static void main(String[] args) {

		SpringApplication.run(BucketApplication.class, args);


	}

}
