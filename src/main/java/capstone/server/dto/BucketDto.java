package capstone.server.dto;

import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BucketSaveRequestDto {

    private Long id;
    private User user;
    private String content;
    private BucketStatus bucketStatus;
    private BucketPrivacyStatus bucketPrivacyStatus;
    private LocalDateTime uploadTime;
    private LocalDateTime modifiedTime;

    public BucketSaveRequestDto(String content,) {

    }
}
