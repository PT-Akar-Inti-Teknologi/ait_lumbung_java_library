package org.ait.library.lumbung.modules.storageengine.localstorage.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ait.library.lumbung.config.GlobalConfig;
import org.ait.library.lumbung.modules.storageengine.localstorage.service.LocalStorageService;
import org.ait.library.lumbung.shared.enums.PlatformEnum;
import org.ait.library.lumbung.shared.serviceskelenton.EngineStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LocalStorageConfig {

  private final GlobalConfig config;

  @Bean
  @ConditionalOnClass(EngineStorageService.class)
  public EngineStorageService localStorageService(LocalStorageProp localStorageProp) {
    if (config.getPlatform() == PlatformEnum.LOCAL) {
      log.info("LocalStorageService Created");
      return new LocalStorageService(localStorageProp);
    }
    return new LocalStorageService(null);
  }
}
