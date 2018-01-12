package com.eloancn.framework.cipher;

public interface Encryptor {

	public void setPassword(String password);

    /**
     * Encrypts a message.
     * 
     * @param message the message to be encrypted.
     */
    public String encrypt(String message);

    
    /**
     * Decrypts a message.
     * 
     * @param encryptedMessage the message to be decrypted.
     */
    public String decrypt(String encryptedMessage);
    
}
