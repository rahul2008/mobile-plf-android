package com.philips.cl.di.dev.pa.cppdatabase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CppDatabaseAdapter {
	
	private final String TAG = CppDatabaseAdapter.class.getSimpleName();
	private Context context;
	private SQLiteDatabase db;
	
	/**
	 * 
	 * @param context
	 */
	public CppDatabaseAdapter(Context context) {
		this.context = context;
	}
	
	/**
	 * 
	 * @return
	 */
	private String dbName = "AirDB.sqlite";
	public void open() {
		//new CppDBHelper(context);
		try {
			copyDataBase();
			db = SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(dbName), null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	} 
	
	private void copyDataBase() throws IOException {

		if (!context.getDatabasePath(dbName).exists()) {
			context.getDatabasePath(dbName).getParentFile().mkdirs();
			Log.i(TAG, "Database not exist");
			OutputStream o = null;
			InputStream in = null;
			try {
				try {
					in = context.getAssets().open(dbName);
				} catch (final Exception e) {
					//new CppDBHelper(context);
					return;
				}
				o = new FileOutputStream(context.getDatabasePath(dbName));
				final byte buffer[] = new byte[4096];
				int len;
				while ((len = in.read(buffer)) != -1) {
					o.write(buffer, 0, len);
				}
				o.flush();
			} catch (final Exception e) {
				return;
			} finally {
				if (o != null) {
					try {
						o.close();
					} catch (final IOException e) {
					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (final IOException e) {
					}
				}
			}
		}
	}
	
	public CppDatabaseModel getCppInfo(String lastfiveRegId) {
		CppDatabaseModel cppInfo = null;
		if (db != null) {
			String selection = "reg_id LIKE '%" + lastfiveRegId +"'";
			Cursor cursor = db.query("KeysTable", null, selection, null, null, null, null);
			
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				cppInfo = new CppDatabaseModel(); 
				
				cppInfo.setMacId(cursor.getString(cursor.getColumnIndex("mac_add")));
				cppInfo.setEuId(cursor.getString(cursor.getColumnIndex("euid")));
				cppInfo.setSetNc(cursor.getString(cursor.getColumnIndex("set_nc")));
				cppInfo.setCtn(cursor.getString(cursor.getColumnIndex("ctn")));
				cppInfo.setPrivateKey(cursor.getString(cursor.getColumnIndex("private_key")));
				cppInfo.setSysKey(cursor.getString(cursor.getColumnIndex("sys_key")));
				cppInfo.setRegId(cursor.getString(cursor.getColumnIndex("reg_id")));
				cppInfo.setDistribution(cursor.getString(cursor.getColumnIndex("distribution")));
				
			}
		}
		Log.i(TAG, "getCppInfo " + cppInfo);
		return cppInfo;
	}

}
