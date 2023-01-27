package org.ait.library.lumbung.modules.storageengine.hcpstorage.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface HcpClient {

  @GetMapping(value = "/rest/{directory}/{filename}",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  ResponseEntity<byte[]> downloadFile(
      @RequestHeader(value = "Authorization") String authorization,
      @PathVariable("directory") String directory,
      @PathVariable("filename") String fileName);


  @PutMapping(value = "/rest/{directory}/{filename}",
      produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  ResponseEntity<JsonNode> uploadFile(
      @RequestHeader(value = "Authorization") String authorization,
      @PathVariable("directory") String directory,
      @PathVariable("filename") String fileName,
      @RequestBody byte[] byteFile);

  @DeleteMapping(value = "rest/{directory}/{filename}",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  ResponseEntity<JsonNode> deleteFile(
      @RequestHeader(value = "Authorization") String authorization,
      @PathVariable("directory") String directory,
      @PathVariable("filename") String fileName);

}
