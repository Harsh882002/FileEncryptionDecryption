package com.harshproject.fileEncrypDecryp.utils;

import java.io.*;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryptionUtil {

    private static final String AES_ALGORITHM = "AES";
    private static final int AES_KEY_SIZE = 256;

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(AES_KEY_SIZE, new SecureRandom());
        return keyGenerator.generateKey();
    }

    public static void encryptFile(File inputFile, File outputFile, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] fileContent = Files.readAllBytes(inputFile.toPath());
        byte[] encryptedBytes = cipher.doFinal(fileContent);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(encryptedBytes);
        }
    }

    public static void decrypttFile(File encryptedFile, File decryptedOutputFile, SecretKey secretKey) throws Exception{
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] encryptedBytes = Files.readAllBytes(encryptedFile.toPath());
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        try(FileOutputStream fos = new FileOutputStream(decryptedOutputFile)){
            fos.write(decryptedBytes);
        }
    }

    public static String keyToString(SecretKey secretKey){
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static SecretKey stringToKey(String keyString){
        byte[]  decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, AES_ALGORITHM);
    }

}
