package org.ait.library.lumbung.modules.storageengine.hcpstorage.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ait.library.lumbung.modules.storageengine.hcpstorage.client.HcpClient;
import org.ait.library.lumbung.modules.storageengine.hcpstorage.config.HcpStorageProp;
import org.ait.library.lumbung.shared.enums.PlatformEnum;
import org.ait.library.lumbung.shared.exception.CannotDownloadFileException;
import org.ait.library.lumbung.shared.exception.CannotUploadFileException;
import org.ait.library.lumbung.shared.exception.StorageFileNotFoundException;
import org.ait.library.lumbung.shared.serviceskelenton.EngineStorageAbstract;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
public class HcpStorageService extends EngineStorageAbstract {

  private final HcpClient hcpClient;

  private final HcpStorageProp hcpStorageProp;

  @Override
  protected PlatformEnum getPlatform() {
    return PlatformEnum.HCP;
  }

  @Override
  public String uploadFile(String fileId, MultipartFile file, String fileDir) {
    log.info("HCP Storage Upload File");
    log.info(
        String.format("File Upload: id => %s file => %s", fileId, file.getOriginalFilename()));

    fileId = generateFileId(fileId, file);

    try {
      hcpClient.uploadFile(hcpStorageProp.getHCPAuthorization(), fileDir, fileId, file.getBytes());
    } catch (NoSuchAlgorithmException | IOException e) {
      throw new CannotUploadFileException(e.getMessage());
    }

    return fileId;
  }

  @Override
  public ByteArrayResource downloadFile(String fileId, String fileDir)
      throws StorageFileNotFoundException {
    log.info("HCP Storage Download: " + fileId);


    try {
      ResponseEntity<byte[]> responseEntity =
          hcpClient.downloadFile(hcpStorageProp.getHCPAuthorization(), fileDir, fileId);

      return Optional.ofNullable(responseEntity.getBody()).map(ByteArrayResource::new)
          .orElseThrow(StorageFileNotFoundException::new);
    } catch (NoSuchAlgorithmException e) {
      throw new CannotDownloadFileException(e.getMessage());
    }
  }

  @Override
  public String getPublicUrl(String fileId, String fileDir) throws StorageFileNotFoundException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteFile(String fileId, String fileDir) throws StorageFileNotFoundException {
    log.info("HCP Storage Delete: " + fileId);

    try {
      ResponseEntity<JsonNode> responseEntity =
          hcpClient.deleteFile(hcpStorageProp.getHCPAuthorization(), fileDir, fileId);

      if (responseEntity.getBody() == null) {
        throw new StorageFileNotFoundException();
      }

    } catch (NoSuchAlgorithmException e) {
      throw new CannotDownloadFileException(e.getMessage());
    }
  }
}
