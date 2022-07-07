package capstone.server.dto.bucket;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class BucketContentUpdateDto {

    private Long BucketId;
    @NotNull
    private String content;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime updateTime;

    public BucketContentUpdateDto() {
    }
}
