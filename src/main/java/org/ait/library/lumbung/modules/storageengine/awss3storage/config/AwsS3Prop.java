package org.ait.library.lumbung.modules.storageengine.awss3storage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "amazon")
public class AwsS3Prop {

  private String accessKey;
  private String awsRegion;
  private String awsSecretKey;
  private String bucketName;
  private int presignUrlDuration;

}
