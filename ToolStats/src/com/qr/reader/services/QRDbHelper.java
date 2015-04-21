package com.qr.reader.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author JRD Systems
 * 
 * A helper class to manage database creation.
 */

public class QRDbHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "toolstatsdb";

	public QRDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
			db.execSQL(Tables.ScanHistory.getCreateCommand());
			db.execSQL(Tables.ScanHistory.getUserCreateCommand());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
