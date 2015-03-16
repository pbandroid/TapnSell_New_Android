package com.convertingoffers.tapnsell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {

	private String DB_PATH ;
	private Context mContext;
	private SQLiteDatabase sqlDB;
	private static String DB_NAME = "demo.sqlite";
	private static int DB_VERSION = 9;

	@SuppressLint("SdCardPath") public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mContext = context;
		DB_PATH="/data/data/"+mContext.getPackageName()+"/databases/";
	}

	public void createDatabase() {
		boolean dbExist = checkDatabase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {
				copyDataBase();
			} catch (Exception e) {
				//Log.i("Error ", "" + e.toString());
				throw new Error("Error copying database");
			}
		}
	}
	public boolean isOpen(){
		if (sqlDB != null)
		return sqlDB.isOpen();
		else 
		return false;
	}
	private void copyDataBase() throws IOException {
		InputStream is = mContext.getAssets().open(DB_NAME);
	
		String outFileName = DB_PATH + DB_NAME;
		OutputStream out = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		is.close();
		out.flush();
		out.close();
	}

	private boolean checkDatabase() {
		File dbFile = new File(DB_PATH + DB_NAME);
		return dbFile.exists();
	}

	public SQLiteDatabase openDatabase() {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		sqlDB = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		return sqlDB;
	}

	public synchronized void close() {
		if (sqlDB != null) {
			sqlDB.close();
			super.close();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Log.e("inside==", "onupgrade");

		try {
			copyDataBase();
		} catch (IOException e) {
			//Log.i("Error ", "" + e.toString());
			throw new Error("Error copying database");
		}
	}
}
