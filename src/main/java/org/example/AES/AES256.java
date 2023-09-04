package org.example.AES;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AES256 {

    public static void main(String[] args) {
        String inputFile = ".\\src\\main\\java\\org\\example\\AES\\input.txt"; // 輸入文件的路徑
        String encryptedFile = ".\\src\\main\\java\\org\\example\\AES\\encrypted.txt"; // 加密後的文件路徑
        String decryptedFile = ".\\src\\main\\java\\org\\example\\AES\\decrypted.txt"; // 解密後的文件路徑
        String key = "your_secret_key."; // 16字節（128位）的AES密鑰

        try {
            // 使用16字節的AES密鑰
            byte[] keyBytes = Arrays.copyOf(key.getBytes(), 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

            // 加密文件
            encrypt(inputFile, encryptedFile, secretKeySpec);

            // 解密文件
            decrypt(encryptedFile, decryptedFile, secretKeySpec);

            System.out.println("文件加密和解密成功！");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IOException e) {
            e.printStackTrace();
        }
    }

    public static void encrypt(String inputFile, String outputFile, SecretKeySpec secretKeySpec)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            cipherOutputStream.write(buffer, 0, bytesRead);
        }
        cipherOutputStream.close();
        inputStream.close();
        outputStream.close();
    }

    public static void decrypt(String inputFile, String outputFile, SecretKeySpec secretKeySpec)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        cipherInputStream.close();
        inputStream.close();
        outputStream.close();
    }
}
