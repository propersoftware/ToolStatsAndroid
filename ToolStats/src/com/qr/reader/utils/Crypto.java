package com.qr.reader.utils;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author JRD Systems
 * 
 * This class provides the functionality of encryption and decryption. 
 *
 */
public class Crypto {

	// Secret key
	private String secretKey = "Tool2012";
	private byte[] ivbyte = { 0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xab,
			(byte) 0xcd, (byte) 0xef };

	public static int MAX_KEY_LENGTH = DESKeySpec.DES_KEY_LEN;
	private static String ENCRYPTION_KEY_TYPE = "DES";
	private static String ENCRYPTION_ALGORITHM = "DES/CBC/PKCS5Padding";
	private final SecretKeySpec keySpec;

	public Crypto() {
		byte[] key = secretKey.getBytes();
		key = padKeyToLength(key, MAX_KEY_LENGTH);
		keySpec = new SecretKeySpec(key, ENCRYPTION_KEY_TYPE);
	}

	private byte[] padKeyToLength(byte[] key, int len) {
		byte[] newKey = new byte[len];
		System.arraycopy(key, 0, newKey, 0, Math.min(key.length, len));
		return newKey;
	}

	/**
	 * Method to encrypt the array of bytes.
	 * 
	 * @param unencrypted Array of bytes to be encrypted    
	 * @return Returns encrypted array of bytes
	 * @throws GeneralSecurityException
	 */
	public byte[] encrypt(byte[] unencrypted) throws GeneralSecurityException {
		return doCipher(unencrypted, Cipher.ENCRYPT_MODE);
	}

	/**
	 * Method to decrypt the array of bytes.
	 * 
	 * @param encrypted Array of bytes to be decrypted    
	 * @return Returns decrypted array of bytes
	 * @throws GeneralSecurityException
	 */
	public byte[] decrypt(byte[] encrypted) throws GeneralSecurityException {
		return doCipher(encrypted, Cipher.DECRYPT_MODE);
	}

	private byte[] doCipher(byte[] original, int mode)
			throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		IvParameterSpec iv = new IvParameterSpec(ivbyte);
		cipher.init(mode, keySpec, iv);
		return cipher.doFinal(original);
	}
}