package com.zzy.admin.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.security.KeyFactory;

/**
 * RSA和AES加解密工具类
 * <p>
 * RSA (非对称加密): 用于加密小数据（如AES密钥）、数字签名。速度慢。
 * AES (对称加密): 用于加密大量数据。速度快。
 * <p>
 * 推荐用法 (混合加密):
 * 1. 后端生成RSA密钥对（公钥+私钥）。
 * 2. 前端请求后端获取RSA公钥。
 * 3. 前端生成一个随机的AES密钥。
 * 4. 前端用AES密钥加密业务数据。
 * 5. 前端用RSA公钥加密AES密钥。
 * 6. 将RSA加密后的AES密钥和AES加密后的数据一起发送给后端。
 * 7. 后端用RSA私钥解密AES密钥。
 * 8. 后端用解密后的AES密钥解密业务数据。
 */
public class CryptoUtils {
    // RSA算法标识
    private static final String RSA_ALGORITHM = "RSA";
    // AES算法标识 (CBC模式，PKCS5填充)
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    // AES密钥算法
    private static final String AES_KEY_ALGORITHM = "AES";

    /**
     * 私有构造函数，防止实例化
     */
    private CryptoUtils() {
    }

    // =================================================================================
    // RSA (非对称加密) 相关方法
    // =================================================================================

    /**
     * 生成RSA密钥对
     *
     * @param keySize 密钥大小 (推荐 2048) 1024 2048 4096
     * @return KeyPair 密钥对对象
     * @throws NoSuchAlgorithmException 如果请求的加密算法不可用
     */
    public static KeyPair generateRsaKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * RSA公钥加密
     *
     * @param data      待加密的明文数据
     * @param publicKey RSA公钥
     * @return Base64编码的加密后字符串
     * @throws Exception 加密过程中可能发生异常
     */
    public static String rsaEncrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * RSA私钥解密
     *
     * @param encryptedData Base64编码的加密字符串
     * @param privateKey    RSA私钥
     * @return 解密后的明文数据
     * @throws Exception 解密过程中可能发生异常
     */
    public static String rsaDecrypt(String encryptedData, PrivateKey privateKey) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // =================================================================================
    // AES (对称加密) 相关方法
    // =================================================================================

    /**
     * 生成AES密钥
     *
     * @param keySize 密钥大小 (推荐 128, 192, 256)
     * @return SecretKey AES密钥对象
     * @throws NoSuchAlgorithmException 如果请求的加密算法不可用
     */
    public static SecretKey generateAesKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_KEY_ALGORITHM);
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
    }

    /**
     * AES加密
     *
     * @param data      待加密的明文数据
     * @param secretKey AES密钥
     * @param iv        初始化向量 (16字节)
     * @return Base64编码的加密后字符串
     * @throws Exception 加密过程中可能发生异常
     */
    public static String aesEncrypt(String data, SecretKey secretKey, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * AES解密
     *
     * @param encryptedData Base64编码的加密字符串
     * @param secretKey     AES密钥
     * @param iv            初始化向量 (16字节)
     * @return 解密后的明文数据
     * @throws Exception 解密过程中可能发生异常
     */
    public static String aesDecrypt(String encryptedData, SecretKey secretKey, IvParameterSpec iv) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 生成一个随机的16字节初始化向量 (IV)
     *
     * @return IvParameterSpec IV对象
     */
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    // =================================================================================
    // 密钥转换和存储相关方法
    // =================================================================================

    /**
     * 将公钥对象转换为Base64编码的字符串 (方便传输)
     *
     * @param publicKey 公钥对象
     * @return Base64字符串
     */
    public static String publicKeyToString(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 将私钥对象转换为Base64编码的字符串 (方便存储)
     *
     * @param privateKey 私钥对象
     * @return Base64字符串
     */
    public static String privateKeyToString(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 将AES密钥对象转换为Base64编码的字符串 (方便传输和存储)
     *
     * @param secretKey AES密钥对象
     * @return Base64字符串
     */
    public static String aesKeyToString(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 将Base64编码的公钥字符串转换回PublicKey对象
     *
     * @param publicKeyString Base64编码的公钥字符串
     * @return PublicKey对象
     * @throws Exception 转换异常
     */
    public static PublicKey stringToPublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将Base64编码的私钥字符串转换回PrivateKey对象
     *
     * @param privateKeyString Base64编码的私钥字符串
     * @return PrivateKey对象
     * @throws Exception 转换异常
     */
    public static PrivateKey stringToPrivateKey(String privateKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 将Base64编码的AES密钥字符串转换回SecretKey对象
     *
     * @param secretKeyString Base64编码的AES密钥字符串
     * @return SecretKey对象
     */
    public static SecretKey stringToAesKey(String secretKeyString) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyString);
        return new SecretKeySpec(keyBytes, AES_KEY_ALGORITHM);
    }

    // =================================================================================
    // Main方法 - 使用示例
    // =================================================================================
    public static void main(String[] args) {
        try {
            // ==================== RSA示例 ====================
            System.out.println("=============== RSA示例 ===============");
            // 1. 生成RSA密钥对
            KeyPair rsaKeyPair = generateRsaKeyPair(2048);
            PublicKey rsaPublicKey = rsaKeyPair.getPublic();
            PrivateKey rsaPrivateKey = rsaKeyPair.getPrivate();

            // 2. 将密钥转换为字符串（方便查看和传输）
            String rsaPublicKeyStr = publicKeyToString(rsaPublicKey);
            String rsaPrivateKeyStr = privateKeyToString(rsaPrivateKey);
            System.out.println("RSA公钥 (Base64): " + rsaPublicKeyStr);
            System.out.println("RSA私钥 (Base64): " + rsaPrivateKeyStr);

            // 3. RSA加密
            String originalText = "这是一段需要RSA加密的敏感信息";
            String rsaEncryptedText = rsaEncrypt(originalText, rsaPublicKey);
            System.out.println("RSA加密后: " + rsaEncryptedText);

            // 4. RSA解密
            String rsaDecryptedText = rsaDecrypt(rsaEncryptedText, rsaPrivateKey);
            System.out.println("RSA解密后: " + rsaDecryptedText);
            System.out.println("RSA加解密是否成功: " + originalText.equals(rsaDecryptedText));

            // ==================== AES示例 ====================
            System.out.println("\n=============== AES示例 ===============");
            // 1. 生成AES密钥
            SecretKey aesKey = generateAesKey(256);
            String aesKeyStr = aesKeyToString(aesKey);
            System.out.println("AES密钥 (Base64): " + aesKeyStr);

            // 2. 生成初始化向量IV
            IvParameterSpec iv = generateIv();
            String ivStr = Base64.getEncoder().encodeToString(iv.getIV());
            System.out.println("AES初始化向量 (Base64): " + ivStr);


            // 3. AES加密
            String largeText = "这是一段比较长的数据，适合使用AES进行加密处理，因为AES速度更快。";
            String aesEncryptedText = aesEncrypt(largeText, aesKey, iv);
            System.out.println("AES加密后: " + aesEncryptedText);

            // 4. AES解密
            String aesDecryptedText = aesDecrypt(aesEncryptedText, aesKey, iv);
            System.out.println("AES解密后: " + aesDecryptedText);
            System.out.println("AES加解密是否成功: " + largeText.equals(aesDecryptedText));

            // ==================== 混合加密示例 ====================
            System.out.println("\n=============== 混合加密示例 ===============");
            String businessData = "{\"userId\": 123, \"orderId\": \"SN20240728\", \"amount\": 999.99}";
            System.out.println("原始业务数据: " + businessData);

            // 1. 生成临时的AES密钥和IV
            SecretKey sessionAesKey = generateAesKey(256);
            IvParameterSpec sessionIv = generateIv();

            // 2. 使用AES加密业务数据
            String encryptedBusinessData = aesEncrypt(businessData, sessionAesKey, sessionIv);
            System.out.println("AES加密后的业务数据: " + encryptedBusinessData);

            // 3. 使用RSA公钥加密AES密钥
            String aesKeyString = aesKeyToString(sessionAesKey);
            String encryptedAesKey = rsaEncrypt(aesKeyString, rsaPublicKey);
            System.out.println("RSA加密后的AES密钥: " + encryptedAesKey);

            // 4. [网络传输] 将 encryptedAesKey, encryptedBusinessData, 和 IV 发送给后端...

            // 5. [后端接收] 后端使用RSA私钥解密AES密钥
            String decryptedAesKeyString = rsaDecrypt(encryptedAesKey, rsaPrivateKey);
            SecretKey restoredAesKey = stringToAesKey(decryptedAesKeyString);
            System.out.println("RSA解密后的AES密钥与原始AES密钥是否一致: " + aesKeyString.equals(decryptedAesKeyString));


            // 6. 后端使用解密后的AES密钥解密业务数据
            String decryptedBusinessData = aesDecrypt(encryptedBusinessData, restoredAesKey, sessionIv);
            System.out.println("AES解密后的业务数据: " + decryptedBusinessData);
            System.out.println("混合加密流程是否成功: " + businessData.equals(decryptedBusinessData));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
