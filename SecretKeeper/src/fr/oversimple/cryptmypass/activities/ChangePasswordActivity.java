package fr.oversimple.cryptmypass.activities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import fr.oversimple.cryptmypass.R;
import fr.oversimple.cryptmypass.bdd.PasswordReferenceBDD;
import fr.oversimple.cryptmypass.model.PasswordReference;
import fr.oversimple.cryptmypass.model.PasswordReferenceCipher;

public class ChangePasswordActivity extends Activity {

	private String password;
	private PasswordReferenceBDD pBdd;

	public void changePassword(View v) {
		EditText password1EditText = (EditText) findViewById(R.id.password1);
		EditText password2EditText = (EditText) findViewById(R.id.password2);

		String password1 = password1EditText.getText().toString();
		String password2 = password2EditText.getText().toString();

		if (password1.equals(password2)) {
			String password = password1 + LoginActivity.SALT_TEST;
			MessageDigest sha1 = null;
			try {
				sha1 = MessageDigest.getInstance("SHA");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			sha1.update(password.getBytes());
			// Updating the password hash in memory
			byte[] hash = sha1.digest();
			pBdd.open();

			pBdd.updateHash(Base64.encodeToString(hash, Base64.DEFAULT));
			// Transcoding the passwords
			List<PasswordReference> references = pBdd.getAllPasswords();

			Iterator<PasswordReference> it = references.iterator();
			PasswordReferenceCipher cipher = new PasswordReferenceCipher();

			while (it.hasNext()) {
				PasswordReference pr = it.next();
				pr = cipher.decipher(pr, this.password);
				pBdd.updatePassword(cipher.cipher(pr, password1));
			}

			pBdd.close();

			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("PASSWORD", password1);
			startActivity(intent);
			finish();
		} else {
			Toast.makeText(this, R.string.different_passwords,
					Toast.LENGTH_SHORT).show();
			password1EditText.setText("");
			password2EditText.setText("");
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("PASSWORD", password);
		startActivity(intent);

		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String datas = extras.getString("PASSWORD");
			if (datas != null) {
				password = datas;
			}
		}

		pBdd = new PasswordReferenceBDD(this);

	}

}
