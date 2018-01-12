package com.eloancn.framework.cipher;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class TextDESEncryptor implements Encryptor {
	
	public TextDESEncryptor(){}

	private Key key;
	
	public void setPassword(String password) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(password.getBytes());
			generator.init(secureRandom);
			key = generator.generateKey();
			generator = null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String encrypt(String message) {
		if (message == null)
			return null;
		BASE64Encoder base64en = new BASE64Encoder();
		try {
			byte strBytes[] = message.getBytes("UTF8");
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(1, key);
			byte encryptStrBytes[] = cipher.doFinal(strBytes);
			return base64en.encode(encryptStrBytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String decrypt(String encryptedMessage) {
		if (encryptedMessage == null)
			return null;
		BASE64Decoder base64De = new BASE64Decoder();
		try {
			byte strBytes[] = base64De.decodeBuffer(encryptedMessage);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(2, key);
			byte decryptStrBytes[] = cipher.doFinal(strBytes);
			return new String(decryptStrBytes, "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedMessage;
	}

}
