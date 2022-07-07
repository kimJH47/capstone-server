package capstone.server.dto.bucket;


import capstone.server.domain.bucket.SubBucketStatus;
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
public class SubBucketUpdateDto {

    @NotNull
    private Long SubBucketId;
    private SubBucketStatus subBucketStatus;
    @NotNull
    private String content;
    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime modifiedTime;
}
