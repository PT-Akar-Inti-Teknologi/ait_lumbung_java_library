package org.ait.library.lumbung.modules.storageengine.googlestorage.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ait.library.lumbung.config.GlobalConfig;
import org.ait.library.lumbung.modules.storageengine.googlestorage.service.GoogleStorageService;
import org.ait.library.lumbung.shared.enums.PlatformEnum;
import org.ait.library.lumbung.shared.serviceskelenton.EngineStorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GoogleStorageConfig {
  private final GlobalConfig config;
  private final GoogleStorageProp googleStorageProp;
  private final ApplicationContext context;

  private Storage googleStorageOption() {
    if (config.getPlatform() == PlatformEnum.GCS) {
      try {
        InputStream inputStream =
            new ClassPathResource(googleStorageProp.getConfigFile()).getInputStream();
        return StorageOptions.newBuilder().setProjectId(googleStorageProp.getProjectId())
            .setCredentials(GoogleCredentials.fromStream(inputStream)).build().getService();
      } catch (IOException e) {
        log.error("Failed initialize google cloud component");
        log.error(e.getMessage());
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
      }
    }
    return null;
  }

  @Bean
  @ConditionalOnProperty(prefix = "filestorage",name = "platform",havingValue = "GCS")
  public EngineStorageService googleStorageService(GoogleStorageProp googleStorageProp) {
    if (config.getPlatform() == PlatformEnum.GCS) {
      log.info("GoogleStorageService Created");
      return new GoogleStorageService(googleStorageOption(), googleStorageProp);
    }
    return new GoogleStorageService(null, null);
  }
}
