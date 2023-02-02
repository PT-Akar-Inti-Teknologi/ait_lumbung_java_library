package org.ait.library.lumbung.modules.storageengine.awss3storage.service;

import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ait.library.lumbung.modules.storageengine.awss3storage.config.AwsS3Prop;
import org.ait.library.lumbung.shared.enums.PlatformEnum;
import org.ait.library.lumbung.shared.exception.CannotDownloadFileException;
import org.ait.library.lumbung.shared.exception.CannotUploadFileException;
import org.ait.library.lumbung.shared.exception.StorageFileNotFoundException;
import org.ait.library.lumbung.shared.serviceskelenton.EngineStorageAbstract;
import org.ait.library.lumbung.shared.utils.FileUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@RequiredArgsConstructor
public class AwsS3StorageService extends EngineStorageAbstract {

  private final S3Client s3Client;

  private final AwsS3Prop awsS3Prop;

  private final S3Presigner s3Presigner;

  @Override
  protected PlatformEnum getPlatform() {
    return PlatformEnum.S3;
  }

  @Override
  public String uploadFile(String fileId, MultipartFile file, String fileDir) {
    log.info("AWS S3 Upload");
    log.info(
        String.format("File Upload: id => %s file => %s", fileId, file.getOriginalFilename()));

    fileId = generateFileId(fileId, file);

    PutObjectRequest request = createObjectRequest(FileUtils.getBasePath(fileDir) + fileId);

    try {
      s3Client.putObject(request,
          RequestBody.fromInputStream(file.getInputStream(), file.getInputStream().available()));
    } catch (IOException e) {
      throw new CannotUploadFileException(e.getMessage());
    }

    return fileId;
  }

  public PutObjectRequest createObjectRequest(String fileId) {
    return PutObjectRequest.builder()
        .bucket(awsS3Prop.getBucketName())
        .key(fileId)
        .build();
  }

  @Override
  public ByteArrayResource downloadFile(String fileId, String fileDir)
      throws StorageFileNotFoundException {

    log.info("AWS S3 Download");

    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(awsS3Prop.getBucketName())
        .key(FileUtils.getBasePath(fileDir) + fileId)
        .build();

    ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);

    if (response == null) {
      throw new StorageFileNotFoundException();
    }

    try {
      return new ByteArrayResource(response.readAllBytes());
    } catch (IOException e) {
      throw new CannotDownloadFileException(e.getMessage());
    }
  }

  @Override
  public String getPublicUrl(String fileId, String fileDir) throws StorageFileNotFoundException {
    log.info("AWS Get Public URL");

    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(awsS3Prop.getBucketName())
        .key(FileUtils.getBasePath(fileDir) + fileId)
        .build();

    GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(awsS3Prop.getPresignUrlDuration()))
        .getObjectRequest(request)
        .build();

    PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
    String theUrl = presignedGetObjectRequest.url().toString();
    log.info("Presigned URL: " + theUrl);

    return theUrl;
  }

  @Override
  public void deleteFile(String fileId, String fileDir) throws StorageFileNotFoundException {
    log.info("AWS Delete URL");

    DeleteObjectRequest request = DeleteObjectRequest.builder()
        .bucket(awsS3Prop.getBucketName())
        .key(FileUtils.getBasePath(fileDir) + fileId)
        .build();

    s3Client.deleteObject(request);
  }
}
