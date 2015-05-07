package com.philips.cl.di.dev.pa.database;

import static com.philips.cl.di.dev.pa.database.NetworkNodeDatabaseHelper.*;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode.PAIRED_STATUS;
import com.philips.cl.di.dev.pa.util.ALog;

public class NetworkNodeDatabase {
	
	private SQLiteDatabase db;
	private NetworkNodeDatabaseHelper dbHelper;
	
	public NetworkNodeDatabase() {
		dbHelper = new NetworkNodeDatabaseHelper(PurAirApplication.getAppContext());
	}

	public List<NetworkNode> getAll() {
		List<NetworkNode> result = new ArrayList<NetworkNode>();
		
		Cursor cursor = null;
		
		try {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
		
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				
				do {
					String cppId = cursor.getString(cursor.getColumnIndex(KEY_CPP_ID));
					long bootId = cursor.getLong(cursor.getColumnIndex(KEY_BOOT_ID));
					String encryptionKey = cursor.getString(cursor.getColumnIndex(KEY_ENCRYPTION_KEY));
					String name = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_NAME));
					String lastKnownNetwork = cursor.getString(cursor.getColumnIndex(KEY_LASTKNOWN_NETWORK));
					int pairedStatus = cursor.getInt(cursor.getColumnIndex(KEY_IS_PAIRED));
					long lastPairedTime = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_LAST_PAIRED));
					String ipAddress = cursor.getString(cursor.getColumnIndex(KEY_IP_ADDRESS));
					String modelName = cursor.getString(cursor.getColumnIndex(KEY_MODEL_NAME));
					
					NetworkNode networkNode = new NetworkNode();
					networkNode.setCppId(cppId);
					networkNode.setBootId(bootId);
					networkNode.setEncryptionKey(encryptionKey);
					networkNode.setName(name);
					networkNode.setHomeSsid(lastKnownNetwork);
					networkNode.setPairedState(NetworkNode.getPairedStatusKey(pairedStatus));
					networkNode.setLastPairedTime(lastPairedTime);
					networkNode.setIpAddress(ipAddress);
					networkNode.setModelName(modelName);
					
					result.add(networkNode);
				} while (cursor.moveToNext());
			} else {
				ALog.i(ALog.DATABASE, "Empty network node table");
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
		} finally {
			closeCursor(cursor);
			closeDatabase();
		}
		
		return result;
	}
	
	public long save(NetworkNode networkNode) {
		long rowId = -1L;
		
		if (networkNode == null) 
			return rowId;
		
		if (networkNode.getPairedState() != NetworkNode.PAIRED_STATUS.PAIRED)
			networkNode.setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
		
		try {
			db = dbHelper.getWritableDatabase();
			
			ContentValues values = new ContentValues();
//			values.put(KEY_ID, null); // http://stackoverflow.com/questions/17212781/how-to-skip-the-primary-key-when-inserting-contentvalues-in-sqlitedatabase
			values.put(KEY_CPP_ID, networkNode.getCppId());
			values.put(KEY_BOOT_ID, networkNode.getBootId());
			values.put(KEY_ENCRYPTION_KEY, networkNode.getEncryptionKey());
			values.put(KEY_DEVICE_NAME, networkNode.getName());
			values.put(KEY_LASTKNOWN_NETWORK, networkNode.getHomeSsid());
			values.put(KEY_IS_PAIRED, networkNode.getPairedState().ordinal());
			
			if (networkNode.getPairedState() == PAIRED_STATUS.PAIRED) {
				values.put(KEY_LAST_PAIRED, networkNode.getLastPairedTime());
			} else {
				values.put(KEY_LAST_PAIRED, -1L);
			}
			
			values.put(KEY_IP_ADDRESS, networkNode.getIpAddress());
			values.put(KEY_MODEL_NAME, networkNode.getModelName());
			
			rowId = db.insertWithOnConflict(TABLE_NETWORK_NODE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to save NetworkNode" + " ,Error: " + e.getMessage());
		} finally {
			closeDatabase();
		}
		
		return rowId;
	}
	
	public int delete(NetworkNode networkNode) {
		int rowsDeleted = db.delete(TABLE_NETWORK_NODE, KEY_CPP_ID + "= ?", new String[] { networkNode.getCppId() });
		return rowsDeleted;
	}
	
	private void closeDatabase() {
		try {
			if (db != null && db.isOpen()) {
				db.close();
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
		}
	}

	private void closeCursor(Cursor cursor) {
		try {
			if (cursor != null && !cursor.isClosed() ) {
				cursor.close();
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
		}
	}
	
}
