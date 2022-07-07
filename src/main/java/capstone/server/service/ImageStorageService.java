//package capstone.server.service;
//
//
//import capstone.server.commons.S3Uploader;
//import capstone.server.domain.bucket.Bucket;
//import capstone.server.domain.bucket.BucketImage;
//import capstone.server.repository.ProfileImageRepository;
//import capstone.server.repository.bucket.BucketImageRepository;
//import capstone.server.repository.bucket.BucketRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class    ImageStorageService {
//
//    private final BucketImageRepository bucketImageRepository;
//    private final ProfileImageRepository profileImageRepository;
//    private final BucketRepository bucketRepository;
//    private final S3Uploader s3Uploader;
//    //private final Path dirLocation;
//
///*    @Autowired
//    public ImageStorageService(BucketImageRepository bucketImageRepository, ProfileImageRepository profileImageRepository,FileUploadProperties fileUploadProperties) {
//        this.bucketImageRepository = bucketImageRepository;
//        this.profileImageRepository = profileImageRepository;
//        this.dirLocation = Paths.get(fileUploadProperties.getUploadDir())
//                                .toAbsolutePath()
//                                .normalize();
//    }*/
//
///*    @Transactional
//    public String profileImageSave(MultipartFile file) {
//
//        String fileName = file.getName();
//        Path location = this.dirLocation.resolve(fileName);
//        return null;
//
//    }*/
//
//    @Transactional
//    public List<String> BucketImageUploadToS3(Long saveBucketId, List<MultipartFile> multipartFiles) throws IOException {
//        List<String> ImageUrls = s3Uploader.uploadToS3(multipartFiles, "static");
//        Bucket bucket = bucketRepository.findById(saveBucketId)
//                                        .orElseThrow(() -> new IllegalArgumentException("테이블에 버킷이없습니다"));
//        LocalDateTime now = LocalDateTime.now();
//        for (String imageUrl : ImageUrls) {
//            BucketImage bucketImage = BucketImage.builder()
//                                           .bucket(bucket)
//                                           .uploadTime(now)
//                                           .location(imageUrl)
//                                           .build();
//            bucketImageRepository.save(bucketImage);
//        }
//
//
//        return ImageUrls;
//
//    }
//
//
//}
