package capstone.server.domain;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime uploadTime;
    @LastModifiedDate
    private LocalDateTime modifiedTime;
}
