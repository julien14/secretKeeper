package fr.oversimple.cryptmypass.activities;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import fr.oversimple.cryptmypass.R;
import fr.oversimple.cryptmypass.bdd.PasswordReferenceBDD;
import fr.oversimple.cryptmypass.model.PasswordReference;
import fr.oversimple.cryptmypass.model.PasswordReferenceCipher;

public class AddpasswordActivity extends Activity {

	private String password;
	private int id;
	private boolean update;
	private EditText descriptionEditText;
	private EditText loginEditText;
	private EditText passwordEditText;

	public void deletePassword(View v) {
		PasswordReferenceBDD pBdd = new PasswordReferenceBDD(this);
		pBdd.open();

		PasswordReferenceCipher cipher = new PasswordReferenceCipher();

		PasswordReference pr = new PasswordReference(id, descriptionEditText
				.getText().toString(), loginEditText.getText().toString(),
				passwordEditText.getText().toString());

		pr = cipher.cipher(pr, password);
		pBdd.removePasswordWithID(pr.getId());
		pBdd.close();

		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("PASSWORD", password);
		startActivity(intent);

		finish();
	}

	public void generatePassword(View v) {
		passwordEditText.setText(randomString(12));
	}
	
	public void cancel(View v) {
		onBackPressed();
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
		setContentView(R.layout.activity_addpassword);

		id = 99999;
		update = false;
		descriptionEditText = (EditText) findViewById(R.id.description);
		loginEditText = (EditText) findViewById(R.id.login);
		passwordEditText = (EditText) findViewById(R.id.password);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String datas = extras.getString("PASSWORD");
			if (datas != null) {
				password = datas;
			}
			datas = extras.getString("DESCRIPTION");
			if (null != datas) {
				descriptionEditText.setText(datas);
				id = extras.getInt("ID");
				update = true;
			}
			datas = extras.getString("LOGIN");
			if (null != datas) {
				loginEditText.setText(datas);
			}
			datas = extras.getString("PASSWORD_TEXT");
			if (null != datas) {
				passwordEditText.setText(datas);
			}
		}

		if (!update) {
			((Button) findViewById(R.id.delete_button)).setEnabled(false);
			((Button) findViewById(R.id.delete_button))
					.setVisibility(View.INVISIBLE);
		}

	}

	public void savePassword(View v) {
		PasswordReferenceBDD pBdd = new PasswordReferenceBDD(this);
		pBdd.open();

		PasswordReferenceCipher cipher = new PasswordReferenceCipher();

		PasswordReference pr = new PasswordReference(id, descriptionEditText
				.getText().toString(), loginEditText.getText().toString(),
				passwordEditText.getText().toString());

		pr = cipher.cipher(pr, password);

		if (update) {
			pBdd.updatePassword(pr);
		} else {
			pBdd.insertPassword(pr);
		}

		pBdd.close();

		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("PASSWORD", password);
		startActivity(intent);

		finish();
	}
	
	private String randomString(int length) {
		char[] chars = "AZERTYUIOPQSDFGHJKLMWXCVBNabcdefghijklmnopqrstuvwxyz0123456789/*-+&#{[-_)]}=*!:;.,?/%"
				.toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new SecureRandom();
		for (int i = 0; i < length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb.toString();
	}
}