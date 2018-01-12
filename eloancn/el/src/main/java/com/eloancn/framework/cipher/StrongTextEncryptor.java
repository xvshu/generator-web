package com.eloancn.framework.cipher;

public class StrongTextEncryptor implements Encryptor{

	public StrongTextEncryptor(){
		
	}
	private org.jasypt.util.text.StrongTextEncryptor ste= new org.jasypt.util.text.StrongTextEncryptor();
	
	@Override
	public void setPassword(String password) {
		ste.setPassword(password);
	}

	@Override
	public String encrypt(String message) {
		return ste.encrypt(message);
	}

	@Override
	public String decrypt(String encryptedMessage) {
		return ste.decrypt(encryptedMessage);
	}

}
