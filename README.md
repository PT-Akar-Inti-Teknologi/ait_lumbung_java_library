# **Lumbung**
by AIT

author: Bintang AIT

Lumbung adalah sebuah libary utility yang digunakan untuk hal-hal yang berkaitan dengan Upload dan download File.

### **Type Storage:**
1. Local Storage
2. Google Cloud Storage
3. AWS S3
4. Alibaba OSS
5. Azure Blob Storage
6. Hitachi Cloud Platform Storage

## **Maven:**

## **Gradle:**

## **How To Used:**

###Requirement
- Java 11
- Spring framework

####Get Started

tambahkan ***@EnableConfigurationProperties*** pada main class project anda

contoh:

```java

@SpringBootApplication
@EnableConfigurationProperties
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

}

```

untuk menggunakan library ini cukup menambahkan **_FileStorageService_** pada service yang membutuhkan upload file.

berikut contoh cara penggunaan library lumbung:

```java

public class TestController {

  private static String DIR = "/data-upload";

  @Autowired
  private FileStorageService fileStorageService;

  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(@RequestParam("attachment") MultipartFile file) {
    return ResponseEntity.ok(fileStorageService.uploadFile(null, file, DIR));
  }

  @GetMapping("/get-public-url/{id}")
  public ResponseEntity<String> getPublicUrlFile(@PathVariable String id){
    return ResponseEntity.ok(fileStorageService.getPublicUrlFile(id,DIR));
  }

  @GetMapping("/download/{id}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String id){

    HttpHeaders headers = new HttpHeaders();
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");

    ByteArrayResource resource = fileStorageService.downloadFile(id,DIR);

    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(resource.contentLength())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteFile(@PathVariable String id){
    fileStorageService.deleteFile(id,DIR);
    return ResponseEntity.ok("Sukses");
  }
}
```

bisa dilihat contoh di atas, terdapat 4 functuin yang terdapat pada **_FileStorageService_**, yaitu:
- String uploadFile(String fileId, MultipartFile file, String fileDir)
- String getPublicUrlFile(String fileId, String fileDir)
- ByteArrayResource downloadFile(String fileId, String fileDir)
- void deleteFile(String fileId, String fileDir)

|variable    |tipe data| kegunaan |
|------------|---------|----------|
|`fileId`|String| merupakan id yang diberikan oleh main project untuk memberikan nama file yang akan diupload, jika di isi null maka fileId akan digenerated oleh library Lumbung.|
|`fileDir`|String| merupakan direktori untuk meletakkan file pada storage|
|`file`|MultipartFile| Merupakan file yang akan di upload|

dan fungsi masing-masing dari method itu, yaitu:

|method|return|kegunaan|
|------|------|--------|
|`uploadFile`|`String`|Method untuk melakukan upload file, dan return berupa `fileId`|
|`getPublicUrlFile`|`String`|Method untuk mendapatkan link url yang dapat diakses public, dan return berpupa `url`. *note: beberapa storage tidak support fungsi ini*|
|`downloadFile`|`ByteArrayResource`|Method untuk melakukan download file, dan return berupa file yang dipake controller untuk mendownload filenya|
|`deleteFile`|`void`|Method untuk menghapus file yang ada pada storage|

### **> Local Storage <**
### **> Google Cloud Storage <**
### **> AWS S3 <**
### **> Alibaba OSS <**