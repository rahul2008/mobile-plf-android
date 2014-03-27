package com.philips.cl.di.dev.pa.controller;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.dto.DeviceInfoDto;
import com.philips.cl.di.dev.pa.utils.ALog;
import com.philips.cl.di.dev.pa.utils.DBHelper;

public class DeviceInfoController {
	
	private SQLiteDatabase db;
	private DBHelper dbHelper;
	/**
	 * 
	 * @param context
	 */
	public DeviceInfoController(Context context) {
		dbHelper = new DBHelper(context);
	}
	/**
	 * 
	 * @param deviceInfoDto
	 * @return
	 */
	public long insertDeviceInfo(DeviceInfoDto deviceInfoDto) {
		
		ALog.i(ALog.DATABASE, "Insert into table Usn: " + deviceInfoDto.getUsn()
				+ ", CppId: " + deviceInfoDto.getCppId()
				+ ", BootId: " + deviceInfoDto.getBootId()
				+ ", Name: " + deviceInfoDto.getDeviceName()
				+ ", Key: " + deviceInfoDto.getDeviceKey());
		
		long rowId = -1L;
		
		long id = getIdUsnExistsInTable(deviceInfoDto.getUsn());
		
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
				// NOP
				e.printStackTrace();
			} finally {
				closeCursor(cursor);
				closeDb();
			}
		} else {
			updateDeviceInfo(id, deviceInfoDto.getBootId(), deviceInfoDto.getDeviceKey(), deviceInfoDto.getDeviceName());
		}
		//getDeviceInfo();
		
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
			// NOP
			e.printStackTrace();
		}
	}
	/**
	 * Close cursor
	 */
	public void closeCursor(Cursor c) {
		try {
			if (c != null && !c.isClosed() ) {
				c.close();
			}
		} catch (Exception e) {
			// NOP
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * @param usn
	 * @return
	 */
	public long getIdUsnExistsInTable(String usn) {
		long id = -1;
		if (usn != null) {
			Cursor cursor = null;
			try {
				db = dbHelper.getReadableDatabase();
				cursor = db.query(AppConstants.AIRPUR_INFO_TABLE, 
						new String[] {AppConstants.ID, AppConstants.AIRPUR_USN}, 
						AppConstants.AIRPUR_USN + "= ?", new String[]{usn}, null, null, null);
				ALog.i(ALog.DATABASE, "All exists cursor count: " + cursor.getCount());
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToNext();
					id = cursor.getLong(cursor.getColumnIndex(AppConstants.ID));
					ALog.i(ALog.DATABASE, "All exists");
				}
			} catch (Exception e) {
				// NOP
				e.printStackTrace();
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
	public List<DeviceInfoDto> getAllDeviceInfo() {
		List<DeviceInfoDto> deviceInfoDtoList = new ArrayList<DeviceInfoDto>();
		ALog.i(ALog.DATABASE, "getAllDeviceInfo()");
		Cursor cursor = null;
		try {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(AppConstants.AIRPUR_INFO_TABLE, null,
					null, null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					DeviceInfoDto deviceInfoDto = new DeviceInfoDto();
					
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
			// NOP
			e.printStackTrace();
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
	public long updateDeviceInfo(long id, long bootId, String devKey, String purifierName) {
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
			// NOP
			e.printStackTrace();
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
	public long deleteDeviceInfo(int id) {
		long rowId = -1;
		try {
			db = dbHelper.getWritableDatabase();
			
			rowId = db.delete(AppConstants.AIRPUR_INFO_TABLE, 
					AppConstants.ID + "= ?", new String[] {String.valueOf(id)});
		} catch (Exception e) {
			// NOP
			e.printStackTrace();
		} finally {
			closeDb();
		}
		
		return rowId;
		
	}

}
