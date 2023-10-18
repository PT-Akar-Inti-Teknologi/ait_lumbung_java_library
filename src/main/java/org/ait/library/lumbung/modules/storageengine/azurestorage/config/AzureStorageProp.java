package org.ait.library.lumbung.modules.storageengine.azurestorage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("azure")
public class AzureStorageProp {

  private String endpoint;
  private String blobContainer;
  private String clientId;
  private String clientSecret;
  private String tenantId;
  private String sasToken;
}
