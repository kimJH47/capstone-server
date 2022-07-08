package capstone.server.domain;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime uploadTime;
    @LastModifiedDate
    private LocalDateTime modifiedTime;
}
