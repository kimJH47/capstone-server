package capstone.server.service;


import capstone.server.config.FileUploadProperties;
import capstone.server.repository.ProfileImageRepository;
import capstone.server.repository.bucket.BucketImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ImageStorageService {

    private final BucketImageRepository bucketImageRepository;
    private final ProfileImageRepository profileImageRepository;

    private final Path dirLocation;

    @Autowired
    public ImageStorageService(BucketImageRepository bucketImageRepository, ProfileImageRepository profileImageRepository,FileUploadProperties fileUploadProperties) {
        this.bucketImageRepository = bucketImageRepository;
        this.profileImageRepository = profileImageRepository;
        this.dirLocation = Paths.get(fileUploadProperties.getUploadDir())
                                .toAbsolutePath()
                                .normalize();
    }

    @Transactional
    public String profileImageSave(MultipartFile file) {

        String fileName = file.getName();
        Path location = this.dirLocation.resolve(fileName);
        return null;

    }


}
