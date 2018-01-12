package com.eloancn.framework.cipher;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
01 算法/模式/填充                                                                 16字节加密后数据长度                                        不满16字节加密后长度
02 AES/CBC/NoPadding             16                          不支持
03 AES/CBC/PKCS5Padding          32                          16
04 AES/CBC/ISO10126Padding       32                          16
05 AES/CFB/NoPadding             16                          原始数据长度
06 AES/CFB/PKCS5Padding          32                          16
07 AES/CFB/ISO10126Padding       32                          16
08 AES/ECB/NoPadding             16                          不支持
09 AES/ECB/PKCS5Padding          32                          16
10 AES/ECB/ISO10126Padding       32                          16
11 AES/OFB/NoPadding             16                          原始数据长度
12 AES/OFB/PKCS5Padding          32                          16
13 AES/OFB/ISO10126Padding       32                          16
14 AES/PCBC/NoPadding            16                          不支持
15 AES/PCBC/PKCS5Padding         32                          16
16 AES/PCBC/ISO10126Padding      32                          16
 
 
 
CryptoJS supports the following padding schemes:
 
    Pkcs7 (the default)
    Iso97971
    AnsiX923
    Iso10126
    ZeroPadding
    NoPadding
*/
public class AESUtils {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(AESUtils.class);
	 
    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param key 加密密码
     * @param md5Key 是否对key进行md5加密
     * @param iv 加密向量
     * @return 加密后的字节数据
     */
    public static byte[] encrypt(byte[] content, byte[] key, boolean md5Key, byte[] iv) {
        try {
            if (md5Key) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                key = md.digest(key);
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); //"算法/模式/补码方式"
            IvParameterSpec ivps = new IvParameterSpec(iv);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivps);
            return cipher.doFinal(content);
        } catch (Exception ex) {
            logger.error("encrypt error\n",ex);
        }
        return null;
    }
    public static String encryptString(String content, String key, boolean md5Key) {
    	return  Base64.encodeBase64String(encrypt(content.getBytes(),key.getBytes(),false,key.getBytes()));
    }
     
    public static byte[] decrypt(byte[] content, byte[] key, boolean md5Key, byte[] iv) {
        try {
            if (md5Key) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                key = md.digest(key);
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); //"算法/模式/补码方式"
            IvParameterSpec ivps = new IvParameterSpec(iv);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivps);
            return cipher.doFinal(content);
        } catch (Exception ex) {
            logger.error("decrypt error\n",ex);
        }
        return null;
    }
    public static String decryptString(String content, String key, boolean md5Key) throws UnsupportedEncodingException {
    	return new String(decrypt(Base64.decodeBase64(content.getBytes("UTF-8")), key.getBytes(), false, key.getBytes()));
    }

}
