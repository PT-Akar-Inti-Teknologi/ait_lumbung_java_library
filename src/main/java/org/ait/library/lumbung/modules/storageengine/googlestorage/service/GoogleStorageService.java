package org.ait.library.lumbung.modules.storageengine.googlestorage.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ait.library.lumbung.modules.storageengine.googlestorage.config.GoogleStorageProp;
import org.ait.library.lumbung.modules.storageengine.googlestorage.exception.GoogleUploadException;
import org.ait.library.lumbung.shared.enums.PlatformEnum;
import org.ait.library.lumbung.shared.exception.StorageFileNotFoundException;
import org.ait.library.lumbung.shared.serviceskelenton.EngineStorageAbstract;
import org.ait.library.lumbung.shared.utils.FileUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Based on: https://cloud.google.com/storage/docs/samples/storage-upload-file
 **/

@Slf4j
@RequiredArgsConstructor
public class GoogleStorageService extends EngineStorageAbstract {

  private final Storage storage;

  private final GoogleStorageProp googleStorageProp;

  @Override
  protected PlatformEnum getPlatform() {
    return PlatformEnum.GCS;
  }

  private String getObjectName(String fileId, String fileDir) {
    return FileUtils.getBasePath(googleStorageProp.getDirName()) + FileUtils.getBasePath(fileDir) +
        fileId;
  }

  @Override
  public String uploadFile(String fileId, MultipartFile file, String fileDir) {
    log.info("Google Storage Upload File");
    log.info(
        String.format("File Upload: id => %s file => %s", fileId, file.getOriginalFilename()));

    fileId = generateFileId(fileId, file);

    Bucket bucket = storage.get(googleStorageProp.getBucketId(), Storage.BucketGetOption.fields());

    try {
      bucket
          .create(getObjectName(fileId, fileDir), file.getBytes(),
              file.getContentType());
    } catch (IOException e) {
      throw new GoogleUploadException(e.getMessage());
    }

    return fileId;
  }

  @Override
  public ByteArrayResource downloadFile(String fileId, String fileDir)
      throws StorageFileNotFoundException {
    log.info("Google Storage Download: " + fileId);
    Blob blob = storage.get(googleStorageProp.getBucketId(),
        getObjectName(fileId, fileDir));
    if (blob == null) {
      throw new StorageFileNotFoundException();
    }
    return new ByteArrayResource(blob.getContent());
  }

  @Override
  public String getPublicUrl(String fileId, String fileDir) throws StorageFileNotFoundException {
    log.info("Google Storage Get Public Link: " + fileId);
    Blob blob = storage.get(googleStorageProp.getBucketId(),
        getObjectName(fileId, fileDir));
    if (blob == null) {
      throw new StorageFileNotFoundException();
    }
    return blob.getMediaLink();
  }

  @Override
  public void deleteFile(String fileId, String fileDir) throws StorageFileNotFoundException {
    log.info("Google Storage Delete: " + fileId);

    String objectName = getObjectName(fileId, fileDir);
    Blob blob = storage.get(googleStorageProp.getBucketId(), objectName);
    if (blob == null) {
      throw new StorageFileNotFoundException();
    }

    Storage.BlobSourceOption precondition =
        Storage.BlobSourceOption.generationMatch(blob.getGeneration());

    storage.delete(googleStorageProp.getBucketId(), objectName, precondition);
  }
}
