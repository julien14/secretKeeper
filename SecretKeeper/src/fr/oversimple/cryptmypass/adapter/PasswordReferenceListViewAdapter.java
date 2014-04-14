package fr.oversimple.cryptmypass.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.oversimple.cryptmypass.R;
import fr.oversimple.cryptmypass.model.PasswordReference;
import fr.oversimple.cryptmypass.model.PasswordReferenceCipher;

public class PasswordReferenceListViewAdapter extends
		ArrayAdapter<PasswordReference> {

	private List<PasswordReference> references;
	private String password;

	public PasswordReferenceListViewAdapter(Context context, int resource,
			List<PasswordReference> objects, String password) {
		super(context, resource, objects);
		references = objects;
		this.password = password;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.password_referenceritem_layout, null);
		}

		PasswordReference reference = references.get(position);

		TextView descriptionTextView = (TextView) v
				.findViewById(R.id.description);
		TextView loginTextView = (TextView) v.findViewById(R.id.login);
		TextView passwordTextView = (TextView) v.findViewById(R.id.password);

		PasswordReferenceCipher cipher = new PasswordReferenceCipher();
		reference = cipher.decipher(reference, password);
		descriptionTextView.setText(reference.getDescription());
		loginTextView.setText(reference.getLogin());
		passwordTextView.setText("********");

		return v;
	}

}
