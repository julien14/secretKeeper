package crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class Encryptor {

	private static final String IV = "Bar12345Bar12345";

	public String decrypt(String password, String salt, String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));

			SecretKeySpec skeySpec = new SecretKeySpec(
					diversify(password, salt), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.decode(encrypted,
					Base64.DEFAULT));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private byte[] diversify(String password, String salt) {
		password = password + salt;
		byte[] key = new byte[16];
		MessageDigest sha1 = null;
		try {
			sha1 = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		sha1.update(password.getBytes());
		byte[] hash = sha1.digest();
		System.arraycopy(hash, 0, key, 0, 16);
		return key;
	}

	public String encrypt(String password, String salt, String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));

			SecretKeySpec skeySpec = new SecretKeySpec(
					diversify(password, salt), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(value.getBytes());
			return Base64.encodeToString(encrypted, Base64.DEFAULT);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
