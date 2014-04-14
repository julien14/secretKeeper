package fr.oversimple.cryptmypass.bdd;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.oversimple.cryptmypass.model.PasswordReference;

public class PasswordReferenceBDD {

	private static final int VERSION_BDD = 1;
	private static final String NAME_BDD = "password.db";

	private SQLiteDatabase bdd;
	private PasswordDatabase passwordDB;

	public PasswordReferenceBDD(Context context) {
		passwordDB = new PasswordDatabase(context, NAME_BDD, null, VERSION_BDD);
	}

	public void close() {
		bdd.close();
	}

	public long createHash(String hash) {
		// Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		// on lui ajoute une valeur associé à une clé (qui est le nom de la
		// colonne dans laquelle on veut mettre la valeur)
		values.put(PasswordDatabase.COL_HASH, hash);
		// on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(PasswordDatabase.TABLE_HASH, null, values);
	}

	// Cette méthode permet de convertir un cursor en un PasswordReference
	private PasswordReference cursorToPr(Cursor c) {
		// si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0) {
			return null;
		}

		PasswordReference reference = new PasswordReference(
				c.getInt(PasswordDatabase.NUM_COL_ID),
				c.getString(PasswordDatabase.NUM_COL_DESCRIPTION),
				c.getString(PasswordDatabase.NUM_COL_LOGIN),
				c.getString(PasswordDatabase.NUM_COL_PASSWORD));

		// On retourne le PasswordReference
		return reference;
	}

	public List<PasswordReference> getAllPasswords() {
		Cursor c = bdd.query(PasswordDatabase.TABLE_PASSWORD, new String[] {
				PasswordDatabase.COL_ID, PasswordDatabase.COL_DESCRIPTION,
				PasswordDatabase.COL_LOGIN, PasswordDatabase.COL_PASSWORD, },
				null, null, null, null, null);

		List<PasswordReference> passwords = new ArrayList<PasswordReference>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			passwords.add(cursorToPr(c));
			c.moveToNext();
		}
		c.close();
		return passwords;
	}

	public SQLiteDatabase getBDD() {
		return bdd;
	}

	public String getHash() {
		Cursor c = bdd.query(PasswordDatabase.TABLE_HASH, new String[] {
				PasswordDatabase.COL_ID, PasswordDatabase.COL_HASH }, null,
				null, null, null, null);
		c.moveToFirst();
		String hash = c.getString(PasswordDatabase.NUM_COL_HASH);
		c.close();
		return hash;
	}

	public long insertPassword(PasswordReference reference) {
		// Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		// on lui ajoute une valeur associé à une clé (qui est le nom de la
		// colonne dans laquelle on veut mettre la valeur)
		values.put(PasswordDatabase.COL_DESCRIPTION, reference.getDescription());
		values.put(PasswordDatabase.COL_LOGIN, reference.getLogin());
		values.put(PasswordDatabase.COL_PASSWORD, reference.getPassword());
		// on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(PasswordDatabase.TABLE_PASSWORD, null, values);
	}

	public void open() {
		bdd = passwordDB.getWritableDatabase();
	}

	public int removePasswordWithID(int id) {
		// Suppression d'un PasswordReference de la BDD grâce à l'ID
		return bdd.delete(PasswordDatabase.TABLE_PASSWORD,
				PasswordDatabase.COL_ID + " = " + id, null);
	}

	public int updateHash(String hash) {
		ContentValues values = new ContentValues();
		values.put(PasswordDatabase.COL_HASH, hash);

		return bdd.update(PasswordDatabase.TABLE_HASH, values,
				PasswordDatabase.COL_ID + " = " + 0, null);
	}

	public int updatePassword(PasswordReference reference) {
		// La mise à jour d'un PasswordReference dans la BDD fonctionne plus ou
		// moins comme
		// une insertion
		// il faut simple préciser quelle PasswordReference on doit mettre à
		// jour grâce à
		// l'ID
		ContentValues values = new ContentValues();
		values.put(PasswordDatabase.COL_DESCRIPTION, reference.getDescription());
		values.put(PasswordDatabase.COL_LOGIN, reference.getLogin());
		values.put(PasswordDatabase.COL_PASSWORD, reference.getPassword());
		return bdd.update(PasswordDatabase.TABLE_PASSWORD, values,
				PasswordDatabase.COL_ID + " = " + reference.getId(), null);
	}

}
