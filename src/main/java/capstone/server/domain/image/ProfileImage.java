package capstone.server.domain.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImage {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "profile_image_id")
    private Long id;
    private String location;
    private LocalDateTime uploadDate;



}