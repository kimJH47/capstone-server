package capstone.server.dto.bucket;

import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BucketSaveRequestDto {


    @NotNull
    private Long userId;
    @Size(max = 30, message = "버킷 타이틀을 알맞게 작성 해주세요")
    private String content;
    private BucketStatus bucketStatus;
    @NotNull
    private BucketPrivacyStatus bucketPrivacyStatus;
    private List<SubBucketSaveRequestDto> subBucketSaveRequestDtoList;

    public Bucket toEntity() {
        return Bucket.builder()
                     .content(this.getContent())
                     .bucketStatus(this.getBucketStatus())
                     .bucketPrivacyStatus(this.getBucketPrivacyStatus())
                     .subBucketList(new ArrayList<>())
                     .build();
    }
}
