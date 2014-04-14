package fr.oversimple.cryptmypass.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import fr.oversimple.cryptmypass.R;
import fr.oversimple.cryptmypass.adapter.PasswordReferenceListViewAdapter;
import fr.oversimple.cryptmypass.bdd.PasswordReferenceBDD;
import fr.oversimple.cryptmypass.model.PasswordReference;
import fr.oversimple.cryptmypass.model.PasswordReferenceCipher;

public class MainActivity extends Activity {

	private String password;
	private PasswordReferenceBDD pBdd;

	public void addPassword(View v) {
		Intent intent = new Intent(this, AddpasswordActivity.class);
		intent.putExtra("PASSWORD", password);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ListView passwordListView = (ListView) findViewById(R.id.passwordReferencesListView);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String datas = extras.getString("PASSWORD");
			if (datas != null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				password = datas;
			}
		}

		pBdd = new PasswordReferenceBDD(this);
		pBdd.open();
		passwordListView.setAdapter(new PasswordReferenceListViewAdapter(this,
				0, pBdd.getAllPasswords(), password));
		pBdd.close();

		passwordListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pBdd.open();
				PasswordReferenceCipher cipher = new PasswordReferenceCipher();
				PasswordReference reference = pBdd.getAllPasswords().get(
						position);
				pBdd.close();
				reference = cipher.decipher(reference, password);

				Intent intent = new Intent(MainActivity.this,
						AddpasswordActivity.class);
				intent.putExtra("PASSWORD", password);
				intent.putExtra("DESCRIPTION", reference.getDescription());
				intent.putExtra("LOGIN", reference.getLogin());
				intent.putExtra("PASSWORD_TEXT", reference.getPassword());
				intent.putExtra("ID", reference.getId());
				startActivity(intent);
				finish();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_change_password:
			Intent intent = new Intent(this, ChangePasswordActivity.class);
			intent.putExtra("PASSWORD", password);
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
