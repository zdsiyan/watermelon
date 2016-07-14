package com.github.watermelon.util;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.codec.Base64;

import java.security.MessageDigest;
import java.util.Random;

public class PasswordEncoderUtil implements PasswordEncoder {
	// ~ Static fields/initializers
	// =====================================================================================

	private static final int SHA_LENGTH = 20;
	private static final int MD5_LENGTH = 16;
	private static final String SSHA = "SSHA";
	private static final String SHA = "SHA";
	private static final String MD5 = "MD5";
	private static final String SMD5 = "SMD5";
	private static final String SSHA_PREFIX = "{SSHA}";
	private static final String SHA_PREFIX = "{SHA}";
	private static final String MD5_PREFIX = "{MD5}";
	private static final String SMD5_PREFIX = "{SMD5}";

	private static final String SALT_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

	// ~ Instance fields
	// ================================================================================================
	private boolean forceLowerCasePrefix;
	private String encodeAlgorithm = "plaintext";

	// ~ Constructors
	// ===================================================================================================
	public PasswordEncoderUtil() {
	}

	// ~ Methods
	// ========================================================================================================
	public String encodePassword(String rawPass, Object salt) {
		if (encodeAlgorithm.equalsIgnoreCase(MD5)) {
			return encodeMD5Password(rawPass);
		}
		if (encodeAlgorithm.equalsIgnoreCase(SMD5)) {
			return encodeSMD5Password(rawPass);
		}
		if (encodeAlgorithm.equalsIgnoreCase(SHA)) {
			return encodeSHAPassword(rawPass);
		}
		if (encodeAlgorithm.equalsIgnoreCase(SSHA)) {
			return encodeSSHAPassword(rawPass);
		}
		return rawPass;
	}

	public String encodePassword(String rawPass) {
		return encodePassword(rawPass, null);
	}

	private String encodeMD5Password(String rawPass) {
		String prefix = forceLowerCasePrefix ? MD5_PREFIX.toLowerCase()
				: MD5_PREFIX;
		return prefix + encodePassword(rawPass, null, MD5);
	}

	private String encodeSMD5Password(String rawPass) {
		String prefix = forceLowerCasePrefix ? SMD5_PREFIX.toLowerCase()
				: SMD5_PREFIX;
		return prefix + encodePassword(rawPass, generateSalt(), MD5);
	}

	private String encodeSHAPassword(String rawPass) {
		String prefix = forceLowerCasePrefix ? SHA_PREFIX.toLowerCase()
				: SHA_PREFIX;
		return prefix + encodePassword(rawPass, null, SHA);
	}

	private String encodeSSHAPassword(String rawPass) {
		String prefix = forceLowerCasePrefix ? SSHA_PREFIX.toLowerCase()
				: SSHA_PREFIX;
		return prefix + encodePassword(rawPass, generateSalt(), SHA);
	}

	private String encodePassword(String rawPass, byte[] salt,
			String encodeAlgorithm) {
		MessageDigest messageDigest = getMessageDigest(encodeAlgorithm);
		messageDigest.update(rawPass.getBytes());
		String encPass;
		if (salt != null) {
			messageDigest.update(salt);
			byte[] hash = combineHashAndSalt(messageDigest.digest(), salt);
			encPass = new String(Base64.encode(hash));
		} else {
			encPass = new String(Base64.encode(messageDigest.digest()));
		}
		return encPass;
	}

	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		if (encPass.toUpperCase().startsWith(MD5_PREFIX))
			return isMD5PasswordValid(encPass, rawPass);
		if (encPass.toUpperCase().startsWith(SMD5_PREFIX))
			return isSMD5PasswordValid(encPass, rawPass);
		if (encPass.toUpperCase().startsWith(SHA_PREFIX))
			return isSHAPasswordValid(encPass, rawPass);
		if (encPass.toUpperCase().startsWith(SSHA_PREFIX))
			return isSSHAPasswordValid(encPass, rawPass);
		return isPlaintextPasswordValid(encPass, rawPass);
	}

	public boolean isPasswordValid(String encPass, String rawPass) {
		return isPasswordValid(encPass, rawPass, null);
	}

	private boolean isPlaintextPasswordValid(String encPass, String rawPass) {
		String pass1 = encPass + "";
		return pass1.equals(rawPass);
	}

	private boolean isMD5PasswordValid(String encPass, String rawPass) {
		String encPassWithoutPrefix = encPass.substring(MD5_PREFIX.length());
		return encodePassword(rawPass, null, MD5).equals(encPassWithoutPrefix);
	}

	private boolean isSMD5PasswordValid(String encPass, String rawPass) {
		String encPassWithoutPrefix = encPass.substring(SMD5_PREFIX.length());
		byte[] salt = extractSalt(encPassWithoutPrefix, MD5_LENGTH);
		return encodePassword(rawPass, salt, MD5).equals(encPassWithoutPrefix);
	}

	private boolean isSHAPasswordValid(String encPass, String rawPass) {
		String encPassWithoutPrefix = encPass.substring(SHA_PREFIX.length());
		return encodePassword(rawPass, null, SHA).equals(encPassWithoutPrefix);
	}

	private boolean isSSHAPasswordValid(String encPass, String rawPass) {
		String encPassWithoutPrefix = encPass.substring(SSHA_PREFIX.length());
		byte[] salt = extractSalt(encPassWithoutPrefix, SHA_LENGTH);
		return encodePassword(rawPass, salt, SHA).equals(encPassWithoutPrefix);
	}

	public void setForceLowerCasePrefix(boolean forceLowerCasePrefix) {
		this.forceLowerCasePrefix = forceLowerCasePrefix;
	}

	private byte[] combineHashAndSalt(byte[] hash, byte[] salt) {
		if (salt == null) {
			return hash;
		}
		byte[] hashAndSalt = new byte[hash.length + salt.length];
		System.arraycopy(hash, 0, hashAndSalt, 0, hash.length);
		System.arraycopy(salt, 0, hashAndSalt, hash.length, salt.length);
		return hashAndSalt;
	}

	private MessageDigest getMessageDigest(String algorithm) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance(algorithm);
		} catch (java.security.NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm ["
					+ algorithm + "]");
		}
		return messageDigest;
	}

	private byte[] extractSalt(String encPass, int hashLength) {
		byte[] hashAndSalt = Base64.decode(encPass.getBytes());
		int saltLength = hashAndSalt.length - hashLength;
		byte[] salt = new byte[saltLength];
		System.arraycopy(hashAndSalt, hashLength, salt, 0, saltLength);
		return salt;
	}

	private byte[] generateSalt() {
		StringBuffer salt = new StringBuffer();
		Random randgen = new Random();
		while (salt.length() < 4) {
			int index = (int) (randgen.nextFloat() * SALT_CHARS.length());
			salt.append(SALT_CHARS.substring(index, index + 1));
		}
		return salt.toString().getBytes();
	}

	public void setEncodeAlgorithm(String encodeAlgorithm) {
		this.encodeAlgorithm = encodeAlgorithm;
	}

	/**
	 * 生成随即密码
	 * 
	 * @param pwd_len
	 *            生成的密码的总长度
	 * @return 密码的字符串
	 */
	public static String genRandomNum(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}

		return pwd.toString();
	}

}