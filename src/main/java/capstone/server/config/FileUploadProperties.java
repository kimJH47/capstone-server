package capstone.server.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties {
    private String uploadDir;

}
