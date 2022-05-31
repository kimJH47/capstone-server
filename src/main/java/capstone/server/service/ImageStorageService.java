package capstone.server.service;


import capstone.server.repository.ProfileImageRepository;
import capstone.server.repository.bucket.BucketImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageStorageService {

    private final BucketImageRepository bucketImageRepository;
    private final ProfileImageRepository profileImageRepository;


    @Transactional
    public String profileImageSave() {


    }


}
