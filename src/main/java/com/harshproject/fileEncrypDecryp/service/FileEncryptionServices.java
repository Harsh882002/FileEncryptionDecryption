package com.harshproject.fileEncrypDecryp.service;

import java.io.File;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.harshproject.fileEncrypDecryp.utils.FileEncryptionUtil;

@Service
public class FileEncryptionServices {

    public String encryptFile(String inputFilePath, String outputFilePath) throws Exception{
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        

        //Generate AES Key
        SecretKey secretKey = FileEncryptionUtil.generateKey();

        //Encrypt File
        FileEncryptionUtil.encryptFile(inputFile,outputFile,secretKey);

        //Return key for decryption
        return FileEncryptionUtil.keyToString(secretKey);

    }

    public void decryptFile(String encryptedFilePath, String decryptedFilePath, String keyString) throws Exception{
        File encryptedFile = new File(encryptedFilePath);
        File decryptedFile = new File(decryptedFilePath);

        //convert string back to secretKey
        SecretKey secretKey = FileEncryptionUtil.stringToKey(keyString);

        //Decrypt file
        FileEncryptionUtil.decrypttFile(encryptedFile, decryptedFile, secretKey);
    }
    
}
