package org.ait.library.lumbung.modules.selector.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "filestorage")
public class FilestorageProp {

  private String platform;

}
