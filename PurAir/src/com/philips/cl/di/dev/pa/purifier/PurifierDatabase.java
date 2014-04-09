package com.philips.cl.di.dev.pa.purifier;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.PurifierDetailDto;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.PurifierDBHelper;

public class PurifierDatabase {
	
	private SQLiteDatabase db;
	private PurifierDBHelper dbHelper;
	/**
	 * 
	 * @param context
	 */
	public PurifierDatabase(Context context) {
		dbHelper = new PurifierDBHelper(context);
	}
	/**
	 * 
	 * @param deviceInfoDto
	 * @return
	 */
	public long insertPurifierDetail(PurifierDetailDto deviceInfoDto) {
		
		ALog.i(ALog.DATABASE, "Insert into table Usn: " + deviceInfoDto.getUsn()
				+ ", CppId: " + deviceInfoDto.getCppId()
				+ ", BootId: " + deviceInfoDto.getBootId()
				+ ", Name: " + deviceInfoDto.getDeviceName()
				+ ", Key: " + deviceInfoDto.getDeviceKey());
		
		long rowId = -1L;
		
		long id = getRowIdOfPurifier(deviceInfoDto.getUsn());
		
		Cursor cursor = null;
		if (id == -1L) {
			ALog.i(ALog.DATABASE, "First time adding");
			try {
				db = dbHelper.getWritableDatabase();
				
				ContentValues values = new ContentValues();
				values.put(AppConstants.AIRPUR_USN, deviceInfoDto.getUsn());
				values.put(AppConstants.AIRPUR_CPP_ID, deviceInfoDto.getCppId());
				values.put(AppConstants.AIRPUR_BOOT_ID, deviceInfoDto.getBootId());
				values.put(AppConstants.AIRPUR_DEVICE_NAME, deviceInfoDto.getDeviceName());
				values.put(AppConstants.AIRPUR_KEY, deviceInfoDto.getDeviceKey());
				
				rowId = db.insert(AppConstants.AIRPUR_INFO_TABLE, null, values);
			} catch (Exception e) {
				ALog.e(ALog.DATABASE, e.getMessage());
			} finally {
				closeCursor(cursor);
				closeDb();
			}
		} else {
			updatePurifierDetail(id, deviceInfoDto.getBootId(), deviceInfoDto.getDeviceKey(), deviceInfoDto.getDeviceName());
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
	public List<PurifierDetailDto> getAllPurifierDetail() {
		List<PurifierDetailDto> deviceInfoDtoList = new ArrayList<PurifierDetailDto>();
		ALog.i(ALog.DATABASE, "getAllDeviceInfo()");
		Cursor cursor = null;
		try {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(AppConstants.AIRPUR_INFO_TABLE, null,
					null, null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					PurifierDetailDto deviceInfoDto = new PurifierDetailDto();
					
					deviceInfoDto.setId(
							cursor.getLong(cursor.getColumnIndex(AppConstants.ID)));
					deviceInfoDto.setUsn(
							cursor.getString(cursor.getColumnIndex(AppConstants.AIRPUR_USN)));
					deviceInfoDto.setCppId(
							cursor.getString(cursor.getColumnIndex(AppConstants.AIRPUR_CPP_ID)));
					deviceInfoDto.setBootId(
							cursor.getLong(cursor.getColumnIndex(AppConstants.AIRPUR_BOOT_ID)));
					deviceInfoDto.setDeviceName(
							cursor.getString(cursor.getColumnIndex(AppConstants.AIRPUR_DEVICE_NAME)));
					deviceInfoDto.setDeviceKey(
							cursor.getString(cursor.getColumnIndex(AppConstants.AIRPUR_KEY)));
	
					ALog.i(ALog.DATABASE, "Database device details: " + deviceInfoDto.getUsn()
							+ ", CppId: " + deviceInfoDto.getCppId()
							+ ", BootId: " + deviceInfoDto.getBootId()
							+ ", ID: " + deviceInfoDto.getId()
							+ ", Name: " + deviceInfoDto.getDeviceName()
							+ ", Key: " + deviceInfoDto.getDeviceKey());
					
					deviceInfoDtoList.add(deviceInfoDto);
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
		
		return deviceInfoDtoList;
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

}
