package com.philips.cl.di.dev.pa.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;

public class NewPurifierDatabase {
	
	private SQLiteDatabase db;
	private NetworkNodeDatabase networkNodeDatabase;
	
	public long insert(AirPurifier airPurifier) {
		long rowId = -1L;
		
		long networkObjectId = networkNodeDatabase.insert(airPurifier.getNetworkNode());
		
		ContentValues values = new ContentValues();
		values.put("usn", airPurifier.getUsn());
		values.put("latitude", airPurifier.getLatitude());
		values.put("longitude", airPurifier.getLongitude());
		values.put("networkNodeId", networkObjectId);
		
		rowId = db.insert("airpurifier", null, values);
		
		return rowId;
	}

}
