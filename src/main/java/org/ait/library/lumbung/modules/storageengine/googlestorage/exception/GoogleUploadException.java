package org.ait.library.lumbung.modules.storageengine.googlestorage.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleUploadException extends RuntimeException {
  public GoogleUploadException(String message) {
    super(message);
    log.error("Google Upload Failed: " + message);
  }
}
