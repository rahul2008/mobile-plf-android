package com.philips.cl.di.dev.pa.purifier;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DatabaseHelper;

public class PurifierDatabase {

	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	/**
	 * 
	 * @param context
	 */
	public PurifierDatabase() {
		dbHelper = new DatabaseHelper(PurAirApplication.getAppContext());
	}
	/**
	 * 
	 * @param deviceInfoDto
	 * @return
	 */
	public long insertPurAirDevice(AirPurifier purifier) {
		long rowId = -1L;
		if (purifier == null) return rowId;

		if(purifier.getNetworkNode().getPairedState()!=NetworkNode.PAIRED_STATUS.PAIRED){
			purifier.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
		}
		
		ALog.i(ALog.DATABASE, "Insert into table Usn: " + purifier.getUsn()
				+ ", CppId: " + purifier.getNetworkNode().getCppId()
				+ ", BootId: " + purifier.getNetworkNode().getBootId()
				+ ", Name: " + purifier.getNetworkNode().getName()
				+ ", Key: " + purifier.getNetworkNode().getEncryptionKey());

		
		rowId = getRowIdOfPurifier(purifier);
		if (rowId == -1L) {
			ALog.i(ALog.DATABASE, "First time adding");
			try {
				db = dbHelper.getWritableDatabase();

				ContentValues values = new ContentValues();
				values.put(AppConstants.KEY_AIRPUR_USN, purifier.getUsn());
				values.put(AppConstants.KEY_AIRPUR_CPP_ID, purifier.getNetworkNode().getCppId());
				values.put(AppConstants.KEY_AIRPUR_DEVICE_NAME, purifier.getNetworkNode().getName());
				values.put(AppConstants.KEY_AIRPUR_BOOT_ID, purifier.getNetworkNode().getBootId());
				values.put(AppConstants.KEY_AIRPUR_LASTKNOWN_NETWORK, purifier.getNetworkNode().getHomeSsid());
				values.put(AppConstants.KEY_AIRPUR_KEY, purifier.getNetworkNode().getEncryptionKey());
				values.put(AppConstants.KEY_LATITUDE, purifier.getLatitude());
				values.put(AppConstants.KEY_LONGITUDE, purifier.getLongitude());
				
				ALog.i(ALog.DATABASE, "ordinal value of"+ purifier.getNetworkNode().getPairedState() +"is: "+ purifier.getNetworkNode().getPairedState().ordinal());
				values.put(AppConstants.KEY_AIRPUR_IS_PAIRED, purifier.getNetworkNode().getPairedState().ordinal()); 
				rowId = db.insert(AppConstants.TABLE_AIRPUR_INFO, null, values);
			} catch (Exception e) {
				ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
			} finally {
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
	public List<AirPurifier> getAllPurifiers(ConnectionState state) {
		List<AirPurifier> purAirDevicesList = new ArrayList<AirPurifier>();
		ALog.i(ALog.DATABASE, "getAllDeviceInfo()");
		Cursor cursor = null;
		try {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(AppConstants.TABLE_AIRPUR_INFO, null,
					null, null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					String usn = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AIRPUR_USN));
					String eui64 = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AIRPUR_CPP_ID));
					String name = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AIRPUR_DEVICE_NAME));
					long bootId = cursor.getLong(cursor.getColumnIndex(AppConstants.KEY_AIRPUR_BOOT_ID));
					String lastKnownNetwork = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AIRPUR_LASTKNOWN_NETWORK));
					String encryptionKey = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AIRPUR_KEY));
					int pairedStatus = cursor.getInt(cursor.getColumnIndex(AppConstants.KEY_AIRPUR_IS_PAIRED));
					long lastPairedTime = cursor.getLong(cursor.getColumnIndexOrThrow(AppConstants.KEY_AIRPUR_LAST_PAIRED)) ;
					String latitude = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_LATITUDE));
					String longitude = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_LONGITUDE));

					AirPurifier purifier = new AirPurifier(eui64, usn, null, name, bootId, state);
					purifier.getNetworkNode().setHomeSsid(lastKnownNetwork);
					purifier.getNetworkNode().setEncryptionKey(encryptionKey);
					ALog.i(ALog.PAIRING, "Database- pairing status set to: "+ NetworkNode.getPairedStatusKey(pairedStatus));
					purifier.getNetworkNode().setPairedState(NetworkNode.getPairedStatusKey(pairedStatus));
					purifier.getNetworkNode().setLastPairedTime(lastPairedTime);
					purifier.setLatitude(latitude);
					purifier.setLongitude(longitude);

					ALog.i(ALog.DATABASE, "Loaded purifier: " + purifier);

					purAirDevicesList.add(purifier);
				} while (cursor.moveToNext());

			} else {
				ALog.i(ALog.DATABASE,"Empty device table");
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
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
	public long updatePurifier(long rowId, AirPurifier purifier) {
		long newRowId = -1;
		if(purifier==null) return newRowId;
		ALog.i(ALog.DATABASE, "Updating purifier: " + purifier);
		
		if(purifier.getNetworkNode().getPairedState()!=NetworkNode.PAIRED_STATUS.PAIRED){
			purifier.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
		}
		
		try {
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(AppConstants.KEY_AIRPUR_DEVICE_NAME, purifier.getNetworkNode().getName());
			values.put(AppConstants.KEY_AIRPUR_BOOT_ID, purifier.getNetworkNode().getBootId());
			values.put(AppConstants.KEY_AIRPUR_LASTKNOWN_NETWORK, purifier.getNetworkNode().getHomeSsid());
			values.put(AppConstants.KEY_AIRPUR_KEY, purifier.getNetworkNode().getEncryptionKey());
			values.put(AppConstants.KEY_LATITUDE, purifier.getLatitude());
			values.put(AppConstants.KEY_LONGITUDE, purifier.getLongitude());
			values.put(AppConstants.KEY_AIRPUR_IS_PAIRED, purifier.getNetworkNode().getPairedState().ordinal());
			if(purifier.getNetworkNode().getPairedState()==NetworkNode.PAIRED_STATUS.NOT_PAIRED || purifier.getNetworkNode().getPairedState()==NetworkNode.PAIRED_STATUS.UNPAIRED)
			{
				values.put(AppConstants.KEY_AIRPUR_LAST_PAIRED, -1);
			}
			
			newRowId = db.update(AppConstants.TABLE_AIRPUR_INFO, 
					values, AppConstants.KEY_ID + "= ?", new String[] {String.valueOf(rowId)});
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to update row " +"Error: " + e.getMessage());
		} finally {
			closeDb();
		}
		return newRowId;
	}
	
	public long updatePurifierUsingUsn(AirPurifier purifier) {
		ALog.i(ALog.DATABASE, "Updating purifier: " + purifier);
		long newRowId = -1;
		
		if (purifier == null || purifier.getUsn() == null) return newRowId;

		if(purifier.getNetworkNode().getPairedState()!=NetworkNode.PAIRED_STATUS.PAIRED){
			purifier.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
		}

		purifier.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
		try {
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(AppConstants.KEY_AIRPUR_DEVICE_NAME, purifier.getNetworkNode().getName());
			values.put(AppConstants.KEY_AIRPUR_BOOT_ID, purifier.getNetworkNode().getBootId());
			values.put(AppConstants.KEY_AIRPUR_LASTKNOWN_NETWORK, purifier.getNetworkNode().getHomeSsid());
			values.put(AppConstants.KEY_AIRPUR_KEY, purifier.getNetworkNode().getEncryptionKey());
			values.put(AppConstants.KEY_LATITUDE, purifier.getLatitude());
			values.put(AppConstants.KEY_LONGITUDE, purifier.getLongitude());
			values.put(AppConstants.KEY_AIRPUR_IS_PAIRED, purifier.getNetworkNode().getPairedState().ordinal());
			if(purifier.getNetworkNode().getPairedState()==NetworkNode.PAIRED_STATUS.NOT_PAIRED || purifier.getNetworkNode().getPairedState()==NetworkNode.PAIRED_STATUS.UNPAIRED)
			{
				values.put(AppConstants.KEY_AIRPUR_LAST_PAIRED, -1);
			}
			
			newRowId = db.update(AppConstants.TABLE_AIRPUR_INFO, 
					values, AppConstants.KEY_AIRPUR_USN + "= ?", new String[] {purifier.getUsn()});
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to update row " +"Error: " + e.getMessage());
		} finally {
			closeDb();
		}
		return newRowId;
	}
	
	public long updateGeoLocation(AirPurifier purifier) {
		ALog.i(ALog.DATABASE, "Updating purifier: " + purifier);
		long newRowId = -1;
		
		if (purifier == null || purifier.getUsn() == null) return newRowId;
		try {
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(AppConstants.KEY_LATITUDE, purifier.getLatitude());
			values.put(AppConstants.KEY_LONGITUDE, purifier.getLongitude());
			
			newRowId = db.update(AppConstants.TABLE_AIRPUR_INFO, 
					values, AppConstants.KEY_AIRPUR_USN + "= ?", new String[] {purifier.getUsn()});
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to update row with location co-ordinate " +"Error: " + e.getMessage());
		} finally {
			closeDb();
		}
		return newRowId;
	}

	public int deletePurifier(String usn) {
		ALog.i(ALog.DATABASE, "Deleting purifier usn: " + usn);
		int effectedRowId = -1;
		if (usn == null) return effectedRowId;
		try {
			db = dbHelper.getWritableDatabase();

			effectedRowId = db.delete(AppConstants.TABLE_AIRPUR_INFO, 
					AppConstants.KEY_AIRPUR_USN + "= ?", new String[]{usn});
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to delete row "+"Error: " + e.getMessage());
		} finally {
			closeDb();
		}

		return effectedRowId;
	}

	/**
	 * Method updatePairingStatus.
	 * @param purifierEui64 String
	 * @param isPaired int
	 * @param lastPaired long
	 * @return long
	 */
	public long updatePairingStatus(AirPurifier purifier, NetworkNode.PAIRED_STATUS status) {
		ALog.i(ALog.DATABASE, "Updating pairing status: " + purifier);
		long newRowId = -1;
		try {
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(AppConstants.KEY_AIRPUR_IS_PAIRED, status.ordinal());
			if(status==NetworkNode.PAIRED_STATUS.PAIRED){
			values.put(AppConstants.KEY_AIRPUR_LAST_PAIRED, purifier.getNetworkNode().getLastPairedTime());
			}
			newRowId = db.update(AppConstants.TABLE_AIRPUR_INFO, 
					values, AppConstants.KEY_AIRPUR_CPP_ID + "= ?", new String[] {String.valueOf(purifier.getNetworkNode().getCppId())});
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to update row " +"Error: " + e.getMessage());
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
	public long getPurifierLastPairedOn(AirPurifier purifier) {
		ALog.i(ALog.DATABASE, "Getting purifier last Paired on: " + purifier);
		long lastPaired = -1;
		if (purifier == null) return -1;

		Cursor cursor = null;
		try {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(AppConstants.TABLE_AIRPUR_INFO, 
					new String[] {AppConstants.KEY_AIRPUR_LAST_PAIRED}, 
					AppConstants.KEY_AIRPUR_CPP_ID + "= ?", new String[]{purifier.getNetworkNode().getCppId()}, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToNext();
				lastPaired = cursor.getLong(cursor.getColumnIndex(AppConstants.KEY_AIRPUR_LAST_PAIRED));
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to get Last paired on: " +"Error: " + e.getMessage());
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
		} finally {
			closeCursor(cursor);
			closeDb();
		}
		return lastPaired;
	}

	private long getRowIdOfPurifier(AirPurifier purifier) {
		long id = -1;
		if (purifier == null) return -1;

		Cursor cursor = null;
		try {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(AppConstants.TABLE_AIRPUR_INFO, 
					new String[] {AppConstants.KEY_ID, AppConstants.KEY_AIRPUR_USN}, 
					AppConstants.KEY_AIRPUR_USN + "= ?", new String[]{purifier.getUsn()}, null, null, null);

			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToNext();
				id = cursor.getLong(cursor.getColumnIndex(AppConstants.KEY_ID));
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
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
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
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
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
		}

	}

}
