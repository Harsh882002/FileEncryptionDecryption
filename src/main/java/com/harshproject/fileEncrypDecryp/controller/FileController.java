package com.harshproject.fileEncrypDecryp.controller;

import org.springframework.core.io.Resource;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

import com.harshproject.fileEncrypDecryp.model.FileModel;
import com.harshproject.fileEncrypDecryp.service.FileEncryptionServices;
 
@RestController
@RequestMapping("/file-encryption")
@CrossOrigin
public class FileController {

    @Autowired
    private FileEncryptionServices fileEncryptionServices;

    

   @PostMapping("/encrypt")
public ResponseEntity<Resource> encryptFile(@RequestPart("file") MultipartFile file) {
    try {
        // Convert MultipartFile to File
        File inputFile = convertMultipartFileToFile(file);
        File outputFile = new File(inputFile.getParent(), "encrypted_" + inputFile.getName());

        // Encrypt the file
        String encryptionKey = fileEncryptionServices.encryptFile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        System.out.println("Generated Encryption Key " +  encryptionKey);

        // Return the encrypted file as a downloadable resource
        Path path = outputFile.toPath();
        Resource resource = new UrlResource(path.toUri());

         System.out.println("Generated Encryption Key " +  encryptionKey);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + outputFile.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Encryption-Key", encryptionKey)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Encryption-Key")
                .body(resource);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}


    @PostMapping("/decrypt")
public ResponseEntity<byte[]> decryptFile(@RequestParam("file") MultipartFile file, @RequestParam("key") String key) {
    try {
        // Convert MultipartFile to File
        File encryptedFile = convertMultipartFileToFile(file);
        File decryptedFile = new File(encryptedFile.getParent(), "decrypted_" + encryptedFile.getName().replace(".enc", ".pdf"));

        // Decrypt the file
        fileEncryptionServices.decryptFile(encryptedFile.getAbsolutePath(), decryptedFile.getAbsolutePath(), key);

        // Convert decrypted file to byte array
        byte[] fileContent = Files.readAllBytes(decryptedFile.toPath());

        // Return the decrypted file with correct headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(decryptedFile.getName())
                .build());

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(("Error decrypting file: " + e.getMessage()).getBytes());
    }
}


    private File convertMultipartFileToFile(MultipartFile file) throws Exception {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (InputStream is = file.getInputStream();
             FileOutputStream fos = new FileOutputStream(convFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return convFile;
    }
}
