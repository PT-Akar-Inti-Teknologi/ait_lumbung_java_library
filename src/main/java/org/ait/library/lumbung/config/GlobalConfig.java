package org.ait.library.lumbung.config;

import lombok.Getter;
import lombok.Setter;
import org.ait.library.lumbung.shared.enums.PlatformEnum;

@Setter
@Getter
public class GlobalConfig {
  private PlatformEnum platform;
}
