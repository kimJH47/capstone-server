package capstone.server.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ProfileSaveRequestDto {

    private int age;
    private String NickName;
    private String profileImage;

}
