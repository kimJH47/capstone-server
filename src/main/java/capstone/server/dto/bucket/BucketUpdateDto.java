package capstone.server.dto.bucket;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

public class BucketUpdateDto {

    private Long id;
    private String content;
    @JsonSerialize(using = ToStringSerializer.class)
    private capstone.server.domain.challenge.JoinStatus JoinStatus;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime updateTime;

}
