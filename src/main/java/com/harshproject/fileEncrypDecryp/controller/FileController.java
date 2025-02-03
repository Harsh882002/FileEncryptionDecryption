package com.harshproject.fileEncrypDecryp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import com.harshproject.fileEncrypDecryp.model.FileModel;
import com.harshproject.fileEncrypDecryp.service.FileEncryptionServices;
import com.harshproject.fileEncrypDecryp.service.FileService;

@RestController
@RequestMapping("/file-encryption")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileEncryptionServices fileEncryptionServices;

    @GetMapping
    public List<FileModel> getAllFiles() {
        return fileService.getAllFiles();
    }

    @PostMapping
    public FileModel uploadFile(@RequestBody FileModel file) {
        return fileService.saveFile(file);
    }

    @PostMapping("/encrypt")
    public String encryptFile(@RequestParam("file") MultipartFile file) {

        try {
            
            // Convert MultipartFile to File
            File inputFile = convertMultipartFileToFile(file);
            File outputFile = new File(inputFile.getParent(), "encrypted_" + inputFile.getName());

            // Encryt the file
            String encryptionKey = fileEncryptionServices.encryptFile(inputFile.getAbsolutePath(),
                    outputFile.getAbsolutePath());

            return "File encrypted succeddfully! Encryption key: " + encryptionKey;
        } catch (Exception e) {
            return "Error encrypting file: " + e.getMessage();
        }

    }

    @PostMapping("/decrypt")
    public String decryptFile(@RequestParam("file") MultipartFile file, @RequestParam("key") String key){
        try{
            //convert multipartFile to File
            File encryptedFile = convertMultipartFileToFile(file);
            File decryptedFile = new File(encryptedFile.getParent(),"decrypted_" + encryptedFile.getName());

            //Decrypt the file
            fileEncryptionServices.decryptFile(encryptedFile.getAbsolutePath(), decryptedFile.getAbsolutePath(), key);

            return "File Decrypted Successfully";
        }catch(Exception e){
            return "Error decrypting file: " + e.getMessage();
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws Exception{
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try(
            InputStream is = file.getInputStream();
             FileOutputStream fos = new FileOutputStream(convFile)){
                
                byte[] buffer = new byte[1024];
                int bytesRead;
                while((bytesRead = is.read(buffer)) != -1){
                    fos.write(buffer,0,bytesRead);
                }
            }
        
        return convFile;
    }
}
