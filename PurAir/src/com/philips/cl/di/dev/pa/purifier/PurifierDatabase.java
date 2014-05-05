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

		long id = getRowIdOfPurifier(purifier.getUsn());

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

				rowId = db.insert(AppConstants.AIRPUR_INFO_TABLE, null, values);
			} catch (Exception e) {
				ALog.e(ALog.DATABASE, e.getMessage());
			} finally {
				closeCursor(cursor);
				closeDb();
			}
		} else {
			updatePurifierDetail(id, purifier.getBootId(), purifier.getEncryptionKey(), purifier.getName());
		}
		return rowId;
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
	/**
	 * 
	 * @param usn
	 * @return
	 */
	private long getRowIdOfPurifier(String usn) {
		long id = -1;
		if (usn != null) {
			Cursor cursor = null;
			try {
				db = dbHelper.getReadableDatabase();
				cursor = db.query(AppConstants.AIRPUR_INFO_TABLE, 
						new String[] {AppConstants.ID, AppConstants.AIRPUR_USN}, 
						AppConstants.AIRPUR_USN + "= ?", new String[]{usn}, null, null, null);

				if (cursor != null && cursor.getCount() > 0) {
					ALog.i(ALog.DATABASE, "All exists cursor count: " + cursor.getCount());
					cursor.moveToNext();
					id = cursor.getLong(cursor.getColumnIndex(AppConstants.ID));
					ALog.i(ALog.DATABASE, "All exists");
				}
			} catch (Exception e) {
				ALog.e(ALog.DATABASE, e.getMessage());
			} finally {
				closeCursor(cursor);
				closeDb();
			}
		}	
		return id;
	}
	/**
	 * 
	 * @return
	 */
	public List<PurAirDevice> getAllPurifierDetail() {
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
					
					PurAirDevice purifier = new PurAirDevice(eui64, usn, null, name, bootId, ConnectionState.DISCONNECTED);
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
	public long updatePurifierDetail(long id, long bootId, String devKey, String purifierName) {
		ALog.i(ALog.DATABASE, "Update before id: " + id +", bootId: " 
				+ bootId + ", devKey: " + devKey +", purfier name: " + purifierName);
		long rowId = -1;
		try {
			ALog.i(ALog.DATABASE, "Update");
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(AppConstants.AIRPUR_BOOT_ID, bootId);
			values.put(AppConstants.AIRPUR_KEY, devKey);
			values.put(AppConstants.AIRPUR_DEVICE_NAME, purifierName);

			rowId = db.update(AppConstants.AIRPUR_INFO_TABLE, 
					values, AppConstants.ID + "= ?", new String[] {String.valueOf(id)});
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to update row " +e.getMessage());
		} finally {
			closeDb();
		}
		return rowId;
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public long deletePurifierDetail(int id) {
		long rowId = -1;
		try {
			db = dbHelper.getWritableDatabase();

			rowId = db.delete(AppConstants.AIRPUR_INFO_TABLE, 
					AppConstants.ID + "= ?", new String[] {String.valueOf(id)});
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to delete row "+e.getMessage());
		} finally {
			closeDb();
		}

		return rowId;

	}

	/**
	 * Method updatePairingStatus.
	 * @param purifierId String
	 * @param isPaired int
	 * @param lastPaired long
	 * @return long
	 */
	public long updatePairingStatus(String purifierId) {
		ALog.i(ALog.DATABASE, "purfier id: " + purifierId);
		long rowId = -1;
		try {
			ALog.i(ALog.DATABASE, "Update pairing status");
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(AppConstants.IS_PAIRED, 1);
			values.put(AppConstants.LAST_PAIRED, new Date().getTime());

			rowId = db.update(AppConstants.AIRPUR_INFO_TABLE, 
					values, AppConstants.AIRPUR_CPP_ID + "= ?", new String[] {String.valueOf(purifierId)});
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to update row " +e.getMessage());
		} finally {
			closeDb();
		}
		return rowId;
	}

	/**
	 * Method getPurifierLastPairedOn.
	 * @param purifierId String
	 * @return long
	 */
	public long getPurifierLastPairedOn(String purifierId) {
		long lastPaired = -1;
		if (purifierId != null) {
			Cursor cursor = null;
			try {
				db = dbHelper.getReadableDatabase();
				cursor = db.query(AppConstants.AIRPUR_INFO_TABLE, 
						new String[] {AppConstants.LAST_PAIRED}, 
						AppConstants.AIRPUR_CPP_ID + "= ?", new String[]{purifierId}, null, null, null);
				ALog.i(ALog.DATABASE, "All exists cursor count: " + cursor.getCount());
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToNext();
					lastPaired = cursor.getLong(cursor.getColumnIndex(AppConstants.LAST_PAIRED));
					ALog.i(ALog.DATABASE, "All exists");
				}
			} catch (Exception e) {
				ALog.e(ALog.DATABASE, e.getMessage());
			} finally {
				closeCursor(cursor);
				closeDb();
			}
		}	
		return lastPaired;
	}
}
