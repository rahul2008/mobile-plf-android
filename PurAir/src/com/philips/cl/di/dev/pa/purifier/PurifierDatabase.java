package com.philips.cl.di.dev.pa.purifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.PurifierDBHelper;

public class PurifierDatabase {

	private SQLiteDatabase db;
	private PurifierDBHelper dbHelper;
	/**
	 * 
	 * @param context
	 */
	public PurifierDatabase() {
		dbHelper = new PurifierDBHelper(PurAirApplication.getAppContext());
	}
	/**
	 * 
	 * @param deviceInfoDto
	 * @return
	 */
	public long insertPurAirDevice(PurAirDevice purifier) {

		ALog.i(ALog.DATABASE, "Insert into table Usn: " + purifier.getUsn()
				+ ", CppId: " + purifier.getEui64()
				+ ", BootId: " + purifier.getBootId()
				+ ", Name: " + purifier.getName()
				+ ", Key: " + purifier.getEncryptionKey());

		long rowId = -1L;

		long id = getRowIdOfPurifier(purifier);
		Cursor cursor = null;
		if (id == -1L) {
			ALog.i(ALog.DATABASE, "First time adding");
			try {
				db = dbHelper.getWritableDatabase();

				ContentValues values = new ContentValues();
				values.put(AppConstants.AIRPUR_USN, purifier.getUsn());
				values.put(AppConstants.AIRPUR_CPP_ID, purifier.getEui64());
				values.put(AppConstants.AIRPUR_BOOT_ID, purifier.getBootId());
				values.put(AppConstants.AIRPUR_DEVICE_NAME, purifier.getName());
				values.put(AppConstants.AIRPUR_KEY, purifier.getEncryptionKey());
				values.put(AppConstants.IS_PAIRED, purifier.isPaired() ? 1 : 0);

				rowId = db.insert(AppConstants.AIRPUR_INFO_TABLE, null, values);
			} catch (Exception e) {
				ALog.e(ALog.DATABASE, e.getMessage());
			} finally {
				closeCursor(cursor);
				closeDb();
			}
		} else {
			updatePurifier(rowId, purifier);
		}
		return rowId;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<PurAirDevice> getAllPurifiers(ConnectionState state) {
		List<PurAirDevice> purAirDevicesList = new ArrayList<PurAirDevice>();
		ALog.i(ALog.DATABASE, "getAllDeviceInfo()");
		Cursor cursor = null;
		try {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(AppConstants.AIRPUR_INFO_TABLE, null,
					null, null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					String usn = cursor.getString(cursor.getColumnIndex(AppConstants.AIRPUR_USN));
					String eui64 = cursor.getString(cursor.getColumnIndex(AppConstants.AIRPUR_CPP_ID));
					long bootId = cursor.getLong(cursor.getColumnIndex(AppConstants.AIRPUR_BOOT_ID));
					String name = cursor.getString(cursor.getColumnIndex(AppConstants.AIRPUR_DEVICE_NAME));
					String encryptionKey = cursor.getString(cursor.getColumnIndex(AppConstants.AIRPUR_KEY));
					boolean isPaired = cursor.getInt(cursor.getColumnIndex(AppConstants.IS_PAIRED)) == 1;
					
					PurAirDevice purifier = new PurAirDevice(eui64, usn, null, name, bootId, state);
					purifier.setEncryptionKey(encryptionKey);
					purifier.setPairing(isPaired);

					ALog.i(ALog.DATABASE, "Loaded purifier: " + purifier);

					purAirDevicesList.add(purifier);
				} while (cursor.moveToNext());

			} else {
				ALog.i(ALog.DATABASE,"Empty device table");
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, e.getMessage());
		} finally {
			closeCursor(cursor);
			closeDb();
		}

		return purAirDevicesList;
	}
	/**
	 * 
	 * @param id
	 * @param bootId
	 * @param devKey
	 * @return
	 */
	public long updatePurifier(long rowId, PurAirDevice purifier) {
		ALog.i(ALog.DATABASE, "Updating purifier: " + purifier);
		long newRowId = -1;
		try {
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(AppConstants.AIRPUR_BOOT_ID, purifier.getBootId());
			values.put(AppConstants.AIRPUR_KEY, purifier.getEncryptionKey());
			values.put(AppConstants.AIRPUR_DEVICE_NAME, purifier.getName());
			values.put(AppConstants.IS_PAIRED, purifier.isPaired() ? 1 : 0);

			newRowId = db.update(AppConstants.AIRPUR_INFO_TABLE, 
					values, AppConstants.ID + "= ?", new String[] {String.valueOf(rowId)});
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to update row " +e.getMessage());
		} finally {
			closeDb();
		}
		return newRowId;
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public long deletePurifier(PurAirDevice purifier) {
		ALog.i(ALog.DATABASE, "Deleting purifier: " + purifier);
		long rowId = getRowIdOfPurifier(purifier);
		if (rowId <= 0) return -1;
		
		long newRowId = -1;
		try {
			db = dbHelper.getWritableDatabase();

			newRowId = db.delete(AppConstants.AIRPUR_INFO_TABLE, 
					AppConstants.ID + "= ?", new String[] {String.valueOf(rowId)});
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to delete row "+e.getMessage());
		} finally {
			closeDb();
		}

		return newRowId;

	}

	/**
	 * Method updatePairingStatus.
	 * @param purifierEui64 String
	 * @param isPaired int
	 * @param lastPaired long
	 * @return long
	 */
	public long updatePairingStatus(PurAirDevice purifier) {
		ALog.i(ALog.DATABASE, "Updating pairing status: " + purifier);
		long newRowId = -1;
		try {
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(AppConstants.IS_PAIRED, 1);
			values.put(AppConstants.LAST_PAIRED, new Date().getTime());

			newRowId = db.update(AppConstants.AIRPUR_INFO_TABLE, 
					values, AppConstants.AIRPUR_CPP_ID + "= ?", new String[] {String.valueOf(purifier.getEui64())});
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to update row " +e.getMessage());
		} finally {
			closeDb();
		}
		return newRowId;
	}

	/**
	 * Method getPurifierLastPairedOn.
	 * @param purifierEui64 String
	 * @return long
	 */
	public long getPurifierLastPairedOn(PurAirDevice purifier) {
		ALog.i(ALog.DATABASE, "Getting purifier last Paired on: " + purifier);
		long lastPaired = -1;
		if (purifier == null) return -1;
		
		Cursor cursor = null;
		try {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(AppConstants.AIRPUR_INFO_TABLE, 
					new String[] {AppConstants.LAST_PAIRED}, 
					AppConstants.AIRPUR_CPP_ID + "= ?", new String[]{purifier.getEui64()}, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToNext();
				lastPaired = cursor.getLong(cursor.getColumnIndex(AppConstants.LAST_PAIRED));
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to get Last paired on: " +e.getMessage());
			ALog.e(ALog.DATABASE, e.getMessage());
		} finally {
			closeCursor(cursor);
			closeDb();
		}
		return lastPaired;
	}
	
	private long getRowIdOfPurifier(PurAirDevice purifier) {
		long id = -1;
		if (purifier == null) return -1;

		Cursor cursor = null;
		try {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(AppConstants.AIRPUR_INFO_TABLE, 
					new String[] {AppConstants.ID, AppConstants.AIRPUR_USN}, 
					AppConstants.AIRPUR_USN + "= ?", new String[]{purifier.getUsn()}, null, null, null);

			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToNext();
				id = cursor.getLong(cursor.getColumnIndex(AppConstants.ID));
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, e.getMessage());
		} finally {
			closeCursor(cursor);
			closeDb();
		}
		return id;
	}
	
	/**
	 * 
	 */
	public void closeDb() {
		try {
			if (db != null && db.isOpen()) {
				db.close();
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, e.getMessage());
		}
	}
	/**
	 * Close cursor
	 */
	private void closeCursor(Cursor c) {
		try {
			if (c != null && !c.isClosed() ) {
				c.close();
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, e.getMessage());
		}

	}
	
}
