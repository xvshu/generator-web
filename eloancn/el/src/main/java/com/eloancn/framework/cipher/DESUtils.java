package com.eloancn.framework.cipher;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESUtils {

	public DESUtils() {
	}

	private static Key key;
	private static String KEY_STR;

	static {
		KEY_STR = "eloan_yzld";
		try {
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(KEY_STR.getBytes());
			generator.init(secureRandom);
			key = generator.generateKey();
			generator = null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getEncryptString(String str) {
		if (str == null)
			return null;
		BASE64Encoder base64en = new BASE64Encoder();
		try {
			byte strBytes[] = str.getBytes("UTF8");
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(1, key);
			byte encryptStrBytes[] = cipher.doFinal(strBytes);
			return base64en.encode(encryptStrBytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getDecryptString(String str) {
		if (str == null)
			return null;
		BASE64Decoder base64De = new BASE64Decoder();
		try {
			byte strBytes[] = base64De.decodeBuffer(str);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(2, key);
			byte decryptStrBytes[] = cipher.doFinal(strBytes);
			return new String(decryptStrBytes, "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	public static void main(String a[]){
		//System.out.print(DESUtils.getDecryptString("WW7+0Io7LwXuIugFfpH4cdHN09NYoIh0"));
		System.out.print(DESUtils.getEncryptString("4239349"));
	}
}
