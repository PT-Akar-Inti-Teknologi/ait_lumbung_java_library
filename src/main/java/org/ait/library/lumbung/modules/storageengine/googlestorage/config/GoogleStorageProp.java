package org.ait.library.lumbung.modules.storageengine.googlestorage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "googlestorage")
public class GoogleStorageProp {
  private String configFile;
  private String projectId;
  private String bucketId;
  private String dirName;
}
