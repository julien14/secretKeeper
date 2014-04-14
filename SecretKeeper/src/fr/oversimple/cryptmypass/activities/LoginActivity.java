package fr.oversimple.cryptmypass.activities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import fr.oversimple.cryptmypass.R;
import fr.oversimple.cryptmypass.bdd.PasswordReferenceBDD;

public class LoginActivity extends Activity {

	public final static String SALT_TEST = "00"; //$NON-NLS-1$
	public final static String KEY_HASH = "SHA1"; //$NON-NLS-1$
	private String sha1;

	public void authenticate(View v) {
		EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		String password = passwordEditText.getText().toString();

		String passwordValidation = password + SALT_TEST;
		MessageDigest sha1 = null;
		try {
			sha1 = MessageDigest.getInstance("SHA"); //$NON-NLS-1$
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		sha1.update(passwordValidation.getBytes());
		byte[] hash = sha1.digest();

		if (!this.sha1.equals(Base64.encodeToString(hash, Base64.DEFAULT))) {
			Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_SHORT).show(); //$NON-NLS-1$
			passwordEditText.setText(""); //$NON-NLS-1$
		} else {
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("PASSWORD", password); //$NON-NLS-1$
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		PasswordReferenceBDD pBdd = new PasswordReferenceBDD(this);
		pBdd.open();
		try {
			sha1 = pBdd.getHash();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == sha1) {
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			finish();
		}
	}

}
