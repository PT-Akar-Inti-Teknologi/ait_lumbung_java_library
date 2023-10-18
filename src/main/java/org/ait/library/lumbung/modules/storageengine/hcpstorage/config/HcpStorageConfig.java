package org.ait.library.lumbung.modules.storageengine.hcpstorage.config;


import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ait.library.lumbung.config.GlobalConfig;
import org.ait.library.lumbung.modules.storageengine.hcpstorage.client.HcpClient;
import org.ait.library.lumbung.modules.storageengine.hcpstorage.service.HcpStorageService;
import org.ait.library.lumbung.shared.enums.PlatformEnum;
import org.ait.library.lumbung.shared.serviceskelenton.EngineStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HcpStorageConfig {

  private final HcpStorageProp hcpStorageProp;

  private final GlobalConfig config;

  private HcpClient createHcpClient() {
    if (config.getPlatform() == PlatformEnum.HCP) {
      return Feign.builder()
          .encoder(new JacksonEncoder())
          .decoder(new JacksonDecoder())
          .client(new OkHttpClient(new okhttp3.OkHttpClient.Builder()
              .callTimeout(Duration.ofMinutes(2))
              .build()))
          .logger(new Slf4jLogger())
          .logLevel(Logger.Level.FULL)
          .target(HcpClient.class, hcpStorageProp.getEndpoint());
    }
    return null;
  }

  @Bean
  @ConditionalOnProperty(prefix = "filestorage",name = "platform",havingValue = "HCP")
  public EngineStorageService hcpStorageService(HcpStorageProp hcpStorageProp) {
    if (config.getPlatform() == PlatformEnum.HCP) {
      log.info("HcpStorageService Created");
      return new HcpStorageService(createHcpClient(), hcpStorageProp);
    }
    return new HcpStorageService(null, null);
  }
}
