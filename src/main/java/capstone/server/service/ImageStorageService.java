package capstone.server.service;


import capstone.server.commons.S3Uploader;
import capstone.server.repository.ProfileImageRepository;
import capstone.server.repository.bucket.BucketImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageStorageService {

    private final BucketImageRepository bucketImageRepository;
    private final ProfileImageRepository profileImageRepository;
    private final S3Uploader s3Uploader;
    //private final Path dirLocation;

/*    @Autowired
    public ImageStorageService(BucketImageRepository bucketImageRepository, ProfileImageRepository profileImageRepository,FileUploadProperties fileUploadProperties) {
        this.bucketImageRepository = bucketImageRepository;
        this.profileImageRepository = profileImageRepository;
        this.dirLocation = Paths.get(fileUploadProperties.getUploadDir())
                                .toAbsolutePath()
                                .normalize();
    }*/

/*    @Transactional
    public String profileImageSave(MultipartFile file) {

        String fileName = file.getName();
        Path location = this.dirLocation.resolve(fileName);
        return null;

    }*/

    @Transactional
    public String ImageUploadtoS3(MultipartFile multipartFile) throws IOException {
        String aStatic = s3Uploader.upload(multipartFile, "static");
        return aStatic;

    }


}
