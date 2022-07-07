package capstone.server.domain.bucket;

import capstone.server.domain.User;
import capstone.server.domain.bucket.reactions.Heart;
import capstone.server.dto.bucket.BucketUpdateDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "bucket",cascade = CascadeType.ALL)
    Set<Heart> hearts = new HashSet<>();

    private String content;

    @Enumerated(EnumType.STRING)
    private BucketStatus bucketStatus;
    @Enumerated(EnumType.STRING)
    private BucketPrivacyStatus bucketPrivacyStatus;

    private LocalDateTime uploadTime;
    private LocalDateTime modifiedTime;

    //조회편의성을 위해 양방향 구현
    @OneToMany(mappedBy = "id",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<SubBucket> subBucketList = new ArrayList<>();

    //setter
    public void changeUser(User user) {
        this.user = user;

    }
    public void addSubBucket(SubBucket subBucket) {
        subBucketList.add(subBucket);
        subBucket.changeBucket(this);
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
