package fr.oversimple.cryptmypass.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PasswordDatabase extends SQLiteOpenHelper {

	public static final String TABLE_PASSWORD = "table_passwords";
	public static final String TABLE_HASH = "table_hash";
	public static final String COL_ID = "id";
	public static final int NUM_COL_ID = 0;
	public static final String COL_HASH = "hash";
	public static final int NUM_COL_HASH = 1;
	public static final String COL_DESCRIPTION = "description";
	public static final int NUM_COL_DESCRIPTION = 1;
	public static final String COL_LOGIN = "login";
	public static final int NUM_COL_LOGIN = 2;
	public static final String COL_PASSWORD = "password";
	public static final int NUM_COL_PASSWORD = 3;

	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_PASSWORD
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COL_DESCRIPTION + " TEXT NOT NULL, " + COL_LOGIN
			+ " TEXT NOT NULL, " + COL_PASSWORD + " TEXT NOT NULL);";

	private static final String CREATE_HASH = "CREATE TABLE " + TABLE_HASH
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_HASH
			+ " TEXT NOT NULL);";

	public PasswordDatabase(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BDD);
		db.execSQL(CREATE_HASH);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_PASSWORD + ";");
		db.execSQL("DROP TABLE " + TABLE_HASH + ";");
		onCreate(db);
	}

}
