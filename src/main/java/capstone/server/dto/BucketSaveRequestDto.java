package capstone.server.dto;

import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class BucketSaveRequestDto {


    @NotNull
    private Long userId;
    @Size(max = 30, message = "버킷 타이틀을 알맞게 작성 해주세요")
    private String content;
    private BucketStatus bucketStatus;
    @NotNull
    private BucketPrivacyStatus bucketPrivacyStatus;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime uploadTime;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime modifiedTime;

    public Bucket toEntity() {
        return Bucket.builder()
                     .content(this.getContent())
                     .bucketStatus(this.getBucketStatus())
                     .bucketPrivacyStatus(this.getBucketPrivacyStatus())
                     .uploadTime(this.getUploadTime())
                     .modifiedTime(this.getModifiedTime())
                     .build();
    }





}
