package org.ait.library.lumbung.modules.storageengine.hcpstorage.config;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.xml.bind.DatatypeConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "hcp")
public class HcpStorageProp {

  private String endpoint;
  private String username;
  private String password;

  public String getHCPAuthorization() throws NoSuchAlgorithmException {
    return "HCP " + this.toBase64Characters() + ":" + this.toMD5Characters();
  }

  private String toBase64Characters() {
    return Base64.getEncoder().encodeToString(username.getBytes());
  }

  private String toMD5Characters() throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(password.getBytes());
    byte[] digest = md.digest();
    return DatatypeConverter
        .printHexBinary(digest);
  }
}
