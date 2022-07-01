package capstone.server.dto.bucket;

import capstone.server.domain.bucket.BucketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BucketStatusUpdateDto {

    private Long BucketId;
    @NotNull
    private BucketStatus status;
}
