package capstone.server.domain.sns;


import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;
    private String content;
    private LocalDateTime uploadTime;

    @ManyToOne
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;





}

