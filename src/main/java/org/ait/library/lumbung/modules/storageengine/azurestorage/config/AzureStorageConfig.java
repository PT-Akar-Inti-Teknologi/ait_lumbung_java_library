package org.ait.library.lumbung.modules.storageengine.azurestorage.config;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ait.library.lumbung.config.GlobalConfig;
import org.ait.library.lumbung.modules.storageengine.azurestorage.service.AzureStorageService;
import org.ait.library.lumbung.shared.enums.PlatformEnum;
import org.ait.library.lumbung.shared.serviceskelenton.EngineStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AzureStorageConfig {
  private final GlobalConfig config;
  private final AzureStorageProp azureStorageProp;

  private BlobContainerClient createBlobServiceClient() {
    ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
        .clientId(azureStorageProp.getClientId())
        .clientSecret(azureStorageProp.getClientSecret())
        .tenantId(azureStorageProp.getTenantId())
        .build();

    return new BlobServiceClientBuilder()
        .connectionString(azureStorageProp.getEndpoint())
        .credential(clientSecretCredential)
        .buildClient()
        .createBlobContainer(azureStorageProp.getBlobContainer());
  }

  @Bean
  @ConditionalOnClass(EngineStorageService.class)
  public EngineStorageService azureStorageService() {
    if (config.getPlatform() == PlatformEnum.AZURE) {
      log.info("AzureStorageService Created");
      return new AzureStorageService(createBlobServiceClient());
    }
    return new AzureStorageService(null);
  }
}
