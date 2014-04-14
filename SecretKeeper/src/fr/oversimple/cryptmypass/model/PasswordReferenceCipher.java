package fr.oversimple.cryptmypass.model;

import crypto.Encryptor;

public class PasswordReferenceCipher {

	private String SALT_DESC = "01";
	private String SALT_LOG = "02";
	private String SALT_PASS = "03";

	public PasswordReferenceCipher() {

	}

	public PasswordReference cipher(PasswordReference reference, String password) {

		Encryptor enc = new Encryptor();

		String description = enc.encrypt(password, SALT_DESC,
				reference.getDescription());
		String login = enc.encrypt(password, SALT_LOG, reference.getLogin());
		String passwordText = enc.encrypt(password, SALT_PASS,
				reference.getPassword());

		reference.setDescription(description);
		reference.setLogin(login);
		reference.setPassword(passwordText);

		return reference;
	}

	public PasswordReference decipher(PasswordReference reference,
			String password) {

		try {
			Encryptor enc = new Encryptor();

			String description = enc.decrypt(password, SALT_DESC,
					reference.getDescription());
			String login = enc
					.decrypt(password, SALT_LOG, reference.getLogin());
			String passwordText = enc.decrypt(password, SALT_PASS,
					reference.getPassword());

			reference.setDescription(description);
			reference.setLogin(login);
			reference.setPassword(passwordText);
		} catch (Exception e) {
			return reference;
		}
		return reference;
	}

}
