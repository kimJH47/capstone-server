package capstone.server.dto.bucket;

import capstone.server.domain.bucket.BucketStatus;
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
public class BucketStatusUpdateDto {
    private Long BucketId;
    @NotNull
    private BucketStatus status;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime updateTime;

    public BucketStatusUpdateDto() {
    }
}
