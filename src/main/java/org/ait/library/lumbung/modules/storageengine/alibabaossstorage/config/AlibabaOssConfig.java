package org.ait.library.lumbung.modules.storageengine.alibabaossstorage.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ait.library.lumbung.config.GlobalConfig;
import org.ait.library.lumbung.modules.storageengine.alibabaossstorage.service.AlibabaStorageService;
import org.ait.library.lumbung.shared.enums.PlatformEnum;
import org.ait.library.lumbung.shared.serviceskelenton.EngineStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AlibabaOssConfig {
  private final GlobalConfig config;
  private final AlibabaOssProp alibabaOssProp;

  private OSS buildClientOSS() {
    log.info("Aibaba OSS Client Created");
    return new OSSClientBuilder()
        .build(alibabaOssProp.getEndpoint(), alibabaOssProp.getAccessKeyId(),
            alibabaOssProp.getAccessKeySecret());
  }

  @Bean
  @ConditionalOnProperty(prefix = "filestorage",name = "platform",havingValue = "OSS")
  public EngineStorageService alibabaStorageService(AlibabaOssProp alibabaOssProp) {
    if (config.getPlatform() == PlatformEnum.OSS) {
      log.info("AlibabaStorageService Created");
      return new AlibabaStorageService(alibabaOssProp, buildClientOSS());
    }
    return new AlibabaStorageService(null, null);
  }
}
