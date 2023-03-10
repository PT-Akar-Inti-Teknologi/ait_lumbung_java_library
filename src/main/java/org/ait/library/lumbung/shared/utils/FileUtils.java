package org.ait.library.lumbung.shared.utils;

import java.util.Random;
import org.ait.library.lumbung.shared.enums.PlatformEnum;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

  private static final Random random = new Random();

  private FileUtils() {
    throw new IllegalStateException("Utility class");
  }

  public static String generateFileId(PlatformEnum module, MultipartFile file) {
    return String
        .format("lumbung-%s-%d-%d.%s", module.name(), System.currentTimeMillis(), random.nextInt(5),
            FileUtils.getFileExtention(file));
  }

  public static boolean hasExtention(String fileId) {
    return fileId.contains(".");
  }

  public static String getFileExtention(MultipartFile file) {
    return FilenameUtils.getExtension(file.getOriginalFilename());
  }

  private static char lastChar(String string) {
    return string.charAt(string.length() - 1);
  }

  public static String getBasePath(String string) {
    if(string.startsWith("/")){
      string = string.replaceFirst("/","");
    }
    if (!StringUtils.hasLength(string)) {
      return "";
    }
    boolean lastCharIsSlash = '/' == lastChar(string);
    return lastCharIsSlash ?
        string :
        string + "/";

  }
}
