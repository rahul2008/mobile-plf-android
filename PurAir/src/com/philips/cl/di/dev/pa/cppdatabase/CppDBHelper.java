package com.philips.cl.di.dev.pa.cppdatabase;

import com.philips.cl.di.dev.pa.constants.AppConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CppDBHelper extends SQLiteOpenHelper {
	
	
	public CppDBHelper(Context context) {
		super(context, "AirDB.sqlite", null, AppConstants.DB_VERS);
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
