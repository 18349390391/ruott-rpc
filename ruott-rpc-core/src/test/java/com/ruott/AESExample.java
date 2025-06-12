package com.ruott;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESExample {
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;

    // 生成 AES 密钥
    public static SecretKey generateKey(int keySize) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
    }

    // 生成随机 IV
    public static byte[] generateIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // AES-GCM 加密
    public static String encrypt(String plaintext, SecretKey key) throws Exception {
        byte[] iv = generateIV();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

        // 合并 IV 和密文
        byte[] encryptedData = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, encryptedData, 0, iv.length);
        System.arraycopy(ciphertext, 0, encryptedData, iv.length, ciphertext.length);

        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // AES-GCM 解密
    public static String decrypt(String ciphertext, SecretKey key) throws Exception {
        byte[] decodedCiphertext = Base64.getDecoder().decode(ciphertext);
        
        // 分离 IV 和密文
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] encryptedData = new byte[decodedCiphertext.length - GCM_IV_LENGTH];
        System.arraycopy(decodedCiphertext, 0, iv, 0, iv.length);
        System.arraycopy(decodedCiphertext, iv.length, encryptedData, 0, encryptedData.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        
        return new String(decryptedData);
    }

    public static void main(String[] args) {
        try {
            // 生成 256 位 AES 密钥
            SecretKey key = generateKey(256);
            String originalText = "Hello, AES-GCM Encryption!";
            
            // 加密
            String encryptedText = encrypt(originalText, key);
            System.out.println("加密后: " + encryptedText);
            
            // 解密
            String decryptedText = decrypt(encryptedText, key);
            System.out.println("解密后: " + decryptedText);
            
            // 验证
            System.out.println("验证结果: " + originalText.equals(decryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
