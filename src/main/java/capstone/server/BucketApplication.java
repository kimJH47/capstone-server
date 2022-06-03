package capstone.server;

import capstone.server.config.FileUploadProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(FileUploadProperties.class)
@SpringBootApplication
public class BucketApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yml,"
			+ "classpath:application-aws.yml";

	public static void main(String[] args) {

		//SpringApplication.run(BucketApplication.class, args);
		new SpringApplicationBuilder(BucketApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);

	}

}
