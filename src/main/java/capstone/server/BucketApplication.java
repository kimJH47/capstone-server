package capstone.server;

import capstone.server.config.FileUploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(FileUploadProperties.class)
@SpringBootApplication
public class BucketApplication {


	public static void main(String[] args) {

		SpringApplication.run(BucketApplication.class, args);

	}

}
