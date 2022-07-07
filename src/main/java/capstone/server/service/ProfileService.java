package capstone.server.service;


import capstone.server.domain.Profile;
import capstone.server.domain.image.ProfileImage;
import capstone.server.dto.ProfileSaveRequestDto;
import capstone.server.repository.ProfileImageRepository;
import capstone.server.repository.ProfileRepository;
import capstone.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileImageRepository profileImageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void save(ProfileSaveRequestDto requestDto) {
        //이미지 처리는 aws S3 해서 따로 서비스구현, 프로필 서비스는 프로필에 관련된 서비스만 처리
        ProfileImage.builder();

        Profile profile = Profile.builder()
                                 .age(requestDto.getAge())
                                 .build();
        profileRepository.save(profile);

    }

}
