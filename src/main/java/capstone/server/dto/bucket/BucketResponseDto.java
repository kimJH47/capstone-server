package capstone.server.dto.bucket;

import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.domain.bucket.SubBucket;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Builder
@AllArgsConstructor
@Getter
public class BucketResponseDto {


    private Long id;
    private String content;
    @NotNull
    private BucketStatus bucketStatus;
    @NotNull
    private BucketPrivacyStatus bucketPrivacyStatus;

    private List<SubBucketResponseDto> subBucketList;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime uploadTime;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime modifiedTime;


    public static BucketResponseDto create(Bucket bucket) {
        List<SubBucket> subBucketList = bucket.getSubBucketList();
        //세부목표 dto 변환
        List<SubBucketResponseDto> dtoList = subBucketList.stream()
                                                          .map(subBucket -> SubBucketResponseDto.builder()
                                                                                                .id(subBucket.getId())
                                                                                                .content(subBucket.getContent())
                                                                                                .subBucketStatus(subBucket.getSubBucketStatus())
                                                                                                .uploadTime(subBucket.getUploadTime())
                                                                                                .modifiedTime(subBucket.getModifiedTime())
                                                                                                .build())
                                                          .collect(Collectors.toList());

        return BucketResponseDto.builder()
                                .id(bucket.getId())
                                .content(bucket.getContent())
                                .bucketPrivacyStatus(bucket.getBucketPrivacyStatus())
                                .bucketStatus(bucket.getBucketStatus())
                                .modifiedTime(bucket.getModifiedTime())
                                .uploadTime(bucket.getUploadTime())
                                .subBucketList(dtoList)
                                .build();
    }

    public void changeSubBucketList(List<SubBucketResponseDto> list) {
        this.subBucketList = list;
    }
    @Override
    public String toString() {
        return "BucketResponseDto{" +
                "content='" + content + '\'' +
                ", bucketStatus=" + bucketStatus +
                ", bucketPrivacyStatus=" + bucketPrivacyStatus +
                ", uploadTime=" + uploadTime +
                ", modifiedTime=" + modifiedTime +
                '}';
    }
}
