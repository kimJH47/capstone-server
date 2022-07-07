package capstone.server.domain.bucket.reactions;


import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "heart")
@Entity
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bucket bucket;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Heart(User user,Bucket bucket){
        this.bucket = bucket;
        this.user = user;
    }
}
