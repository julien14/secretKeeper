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

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	public void register(View v) {
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
			byte[] hash = sha1.digest();
			PasswordReferenceBDD pBdd = new PasswordReferenceBDD(this);
			pBdd.open();
			pBdd.createHash(Base64.encodeToString(hash, Base64.DEFAULT));
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

}
