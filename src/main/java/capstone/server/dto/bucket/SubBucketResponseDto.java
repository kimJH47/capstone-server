package capstone.server.dto.bucket;


import capstone.server.domain.bucket.SubBucketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class SubBucketResponseDto {


    private String content;
    private SubBucketStatus subBucketStatus;
    private LocalDateTime uploadTime;
    private LocalDateTime modifiedTime;


}
