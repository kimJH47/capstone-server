package capstone.server.domain.bucket;

import capstone.server.domain.User;
import capstone.server.dto.bucket.BucketUpdateDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Bucket {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "bucket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String content;

    @Enumerated(EnumType.STRING)
    private BucketStatus bucketStatus;
    @Enumerated(EnumType.STRING)
    private BucketPrivacyStatus bucketPrivacyStatus;

    private LocalDateTime uploadTime;
    private LocalDateTime modifiedTime;


    //단방향 , setter 사용x

    //setter
    public void changeUser(User user) {
        this.user = user;

    }

    public void changeStatus(BucketStatus bucketStatus) {
        this.bucketStatus = bucketStatus;
    }
    public void changeContent(String content) {
        this.content = content;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public void update(BucketUpdateDto updateDto) {
        this.content = updateDto.getContent();
        this.modifiedTime = updateDto.getModifiedTime();
        this.bucketStatus = updateDto.getBucketStatus();
        this.bucketPrivacyStatus = updateDto.getBucketPrivacyStatus();
    }
}
