package org.ait.library.lumbung.modules.storageengine.alibabaossstorage.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ait.library.lumbung.modules.storageengine.alibabaossstorage.config.AlibabaOssProp;
import org.ait.library.lumbung.shared.enums.PlatformEnum;
import org.ait.library.lumbung.shared.exception.CannotDownloadFileException;
import org.ait.library.lumbung.shared.exception.CannotUploadFileException;
import org.ait.library.lumbung.shared.exception.StorageFileNotFoundException;
import org.ait.library.lumbung.shared.serviceskelenton.EngineStorageAbstract;
import org.ait.library.lumbung.shared.utils.FileUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
public class AlibabaStorageService extends EngineStorageAbstract {

  private final AlibabaOssProp alibabaOssProp;

  private final OSS ossClient;

  @Override
  protected PlatformEnum getPlatform() {
    return PlatformEnum.OSS;
  }

  @Override
  public String uploadFile(String fileId, MultipartFile file, String fileDir) {
    log.info("Alibaba OSS Upload File");
    log.info(
        String.format("File Upload: id => %s file => %s", fileId, file.getOriginalFilename()));

    fileId = generateFileId(fileId, file);

    try {
      ossClient.putObject(alibabaOssProp.getBucketName(), FileUtils.getBasePath(fileDir) + fileId,
          new ByteArrayInputStream(file.getBytes()));
    } catch (OSSException oe) {
      log.error("Caught an OSSException, which means your request made it to OSS, "
          + "but was rejected with an error response for some reason.");
      log.error("Error Message: " + oe.getErrorMessage());
      log.error("Error Code:       " + oe.getErrorCode());
      log.error("Request ID:      " + oe.getRequestId());
      log.error("Host ID:           " + oe.getHostId());
      throw new CannotUploadFileException(oe.getMessage());
    } catch (Exception e) {
      throw new CannotUploadFileException(e.getMessage());
    }

    return fileId;
  }

  @Override
  public ByteArrayResource downloadFile(String fileId, String fileDir)
      throws StorageFileNotFoundException {
    log.info("Alibaba OSS Download: " + fileId);

    OSSObject ossObject = ossClient
        .getObject(alibabaOssProp.getBucketName(), FileUtils.getBasePath(fileDir) + fileId);
    try {
      return new ByteArrayResource(ossObject.getObjectContent().readAllBytes());
    } catch (IOException e) {
      throw new CannotDownloadFileException(e.getMessage());
    }
  }

  @Override
  public String getPublicUrl(String fileId, String fileDir) throws StorageFileNotFoundException {
    log.info("Alibaba OSS Get Public URL: " + fileId);

    URL url = ossClient.generatePresignedUrl(alibabaOssProp.getBucketName(),
        FileUtils.getBasePath(fileDir) + fileId,
        new Date(System.currentTimeMillis() +
            Duration.ofHours(1).toMillis()));
    return url.toString();
  }

  @Override
  public void deleteFile(String fileId, String fileDir) throws StorageFileNotFoundException {
    log.info("Alibaba OSS Delete: " + fileId);

    ossClient.deleteObject(alibabaOssProp.getBucketName(), FileUtils.getBasePath(fileDir) + fileId);
  }
}
