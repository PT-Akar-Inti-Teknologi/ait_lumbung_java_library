package org.ait.library.lumbung.modules.storageengine.azurestorage.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class AzureStorageService extends EngineStorageAbstract {

  private final BlobContainerClient blobContainerClient;

  @Override
  protected PlatformEnum getPlatform() {
    return PlatformEnum.AZURE;
  }

  @Override
  public String uploadFile(String fileId, MultipartFile file, String fileDir) {
    log.info("Azure Blob Storage Upload");
    log.info(
        String.format("File Upload: id => %s file => %s", fileId, file.getOriginalFilename()));

    fileId = generateFileId(fileId, file);

    BlobClient blobClient =
        blobContainerClient.getBlobClient(FileUtils.getBasePath(fileDir) + fileId);

    try {
      blobClient.upload(file.getInputStream());
    } catch (IOException e) {
      throw new CannotUploadFileException(e.getMessage());
    }

    return fileDir;
  }

  @Override
  public ByteArrayResource downloadFile(String fileId, String fileDir)
      throws StorageFileNotFoundException {
    log.info("Azure Blob Storage Download");

    BlobClient blobClient =
        blobContainerClient.getBlobClient(FileUtils.getBasePath(fileDir) + fileId);

    if (blobClient.exists() == Boolean.TRUE) {
      try {
        return new ByteArrayResource(blobClient.downloadContent().toBytes());
      } catch (Exception e) {
        throw new CannotDownloadFileException(e.getMessage());
      }
    }

    throw new StorageFileNotFoundException();
  }

  @Override
  public String getPublicUrl(String fileId, String fileDir) throws StorageFileNotFoundException {
    BlobClient blobClient =
        blobContainerClient.getBlobClient(FileUtils.getBasePath(fileDir) + fileId);


    if (blobClient.exists() == Boolean.TRUE) {
      return blobClient.getBlobUrl();
    }

    throw new StorageFileNotFoundException();
  }

  @Override
  public void deleteFile(String fileId, String fileDir) throws StorageFileNotFoundException {
    BlobClient blobClient =
        blobContainerClient.getBlobClient(FileUtils.getBasePath(fileDir) + fileId);

    if (!blobClient.deleteIfExists()) {
      throw new StorageFileNotFoundException();
    }
  }
}
