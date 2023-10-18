package org.ait.library.lumbung.modules.storageengine.firebasestorage.service;

import lombok.RequiredArgsConstructor;
import org.ait.library.lumbung.shared.enums.PlatformEnum;
import org.ait.library.lumbung.shared.exception.StorageFileNotFoundException;
import org.ait.library.lumbung.shared.serviceskelenton.EngineStorageAbstract;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FirebaseStorageService extends EngineStorageAbstract {

  @Override
  protected PlatformEnum getPlatform() {
    return PlatformEnum.FIREBASE;
  }

  @Override
  public String uploadFile(String fileId, MultipartFile file, String fileDir) {
    return null;
  }

  @Override
  public ByteArrayResource downloadFile(String fileId, String fileDir)
      throws StorageFileNotFoundException {
    return null;
  }

  @Override
  public String getPublicUrl(String fileId, String fileDir) throws StorageFileNotFoundException {
    return null;
  }

  @Override
  public void deleteFile(String fileId, String fileDir) throws StorageFileNotFoundException {

  }
}
