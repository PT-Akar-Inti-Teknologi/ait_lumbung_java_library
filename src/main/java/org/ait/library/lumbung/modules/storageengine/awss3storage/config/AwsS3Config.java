package org.ait.library.lumbung.modules.storageengine.awss3storage.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ait.library.lumbung.config.GlobalConfig;
import org.ait.library.lumbung.modules.storageengine.awss3storage.service.AwsS3StorageService;
import org.ait.library.lumbung.shared.enums.PlatformEnum;
import org.ait.library.lumbung.shared.serviceskelenton.EngineStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AwsS3Config {
  private final GlobalConfig config;
  private final AwsS3Prop awsS3Prop;


  private S3Client buildS3Client() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(awsS3Prop.getAccessKey(),
        awsS3Prop.getAwsSecretKey());
    final StaticCredentialsProvider credentialsProvider =
        StaticCredentialsProvider.create(credentials);

    Region region = Region.of(awsS3Prop.getAwsRegion());

    return S3Client.builder()
        .credentialsProvider(credentialsProvider)
        .region(region)
        .build();
  }

  @Bean
  @ConditionalOnProperty(prefix = "filestorage",name = "platform",havingValue = "S3")
  public EngineStorageService awsS3StorageService(AwsS3Prop awsS3Prop) {
    if (config.getPlatform() == PlatformEnum.S3) {
      log.info("AwsS3StorageService Created");
      return new AwsS3StorageService(buildS3Client(), awsS3Prop);
    }
    return new AwsS3StorageService(null, null);
  }
}
