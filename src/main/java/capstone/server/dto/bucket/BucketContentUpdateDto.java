package capstone.server.dto.bucket;


import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class BucketContentUpdateDto {

    private Long BucketId;
    @NotNull
    private String content;

}
