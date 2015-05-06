package com.philips.cl.di.dev.pa.database;

import static com.philips.cl.di.dev.pa.database.NetworkNodeDatabaseHelper.KEY_BOOT_ID;
import static com.philips.cl.di.dev.pa.database.NetworkNodeDatabaseHelper.KEY_CPP_ID;
import static com.philips.cl.di.dev.pa.database.NetworkNodeDatabaseHelper.KEY_DEVICE_NAME;
import static com.philips.cl.di.dev.pa.database.NetworkNodeDatabaseHelper.KEY_ENCRYPTION_KEY;
import static com.philips.cl.di.dev.pa.database.NetworkNodeDatabaseHelper.KEY_IP_ADDRESS;
import static com.philips.cl.di.dev.pa.database.NetworkNodeDatabaseHelper.KEY_IS_PAIRED;
import static com.philips.cl.di.dev.pa.database.NetworkNodeDatabaseHelper.KEY_LASTKNOWN_NETWORK;
import static com.philips.cl.di.dev.pa.database.NetworkNodeDatabaseHelper.KEY_LAST_PAIRED;
import static com.philips.cl.di.dev.pa.database.NetworkNodeDatabaseHelper.KEY_MODEL_NAME;
import static com.philips.cl.di.dev.pa.database.NetworkNodeDatabaseHelper.TABLE_NETWORK_NODE;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;

public class NetworkNodeDatabase  {
	
	private SQLiteDatabase db;
	private NetworkNodeDatabaseHelper dbHelper;
	
	public NetworkNodeDatabase() {
		dbHelper = new NetworkNodeDatabaseHelper(PurAirApplication.getAppContext());
	}

	public List<NetworkNode> getAll() {
		List<NetworkNode> result = new ArrayList<NetworkNode>();
		
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
		
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			
			do {
				String eui64 = cursor.getString(cursor.getColumnIndex(KEY_CPP_ID));
				long bootId = cursor.getLong(cursor.getColumnIndex(KEY_BOOT_ID));
				String encryptionKey = cursor.getString(cursor.getColumnIndex(KEY_ENCRYPTION_KEY));
				String name = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_NAME));
				String lastKnownNetwork = cursor.getString(cursor.getColumnIndex(KEY_LASTKNOWN_NETWORK));
				int pairedStatus = cursor.getInt(cursor.getColumnIndex(KEY_IS_PAIRED));
				long lastPairedTime = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_LAST_PAIRED));
				String ipAddress = cursor.getString(cursor.getColumnIndex(KEY_IP_ADDRESS));
				String modelName = cursor.getString(cursor.getColumnIndex(KEY_MODEL_NAME));
				
				NetworkNode networkNode = new NetworkNode();
				networkNode.setCppId(eui64);
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
		
		return result;
	}

	public void save(NetworkNode object) {
//		if (alreadyExists(object)) {
//			update(object);
//		} else {
//			insert(object);
//		}
	}
	
	private void insert(NetworkNode networkNode) {
		
	}
	
	private void update(NetworkNode networkNode) {
		
	}
	
}
