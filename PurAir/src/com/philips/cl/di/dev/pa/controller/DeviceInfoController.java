package com.philips.cl.di.dev.pa.controller;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.dto.DeviceInfoDto;
import com.philips.cl.di.dev.pa.utils.DBHelper;

public class DeviceInfoController {
	
	public static final String TAG = "Database";
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
		Log.i(TAG, "Usn: " + deviceInfoDto.getUsn()
				+ ", CppId: " + deviceInfoDto.getCppId()
				+ ", BootId: " + deviceInfoDto.getBootId()
				+ ", Key: " + deviceInfoDto.getDeviceKey());
		db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(AppConstants.AIRPUR_USN, deviceInfoDto.getUsn());
		values.put(AppConstants.AIRPUR_CPP_ID, deviceInfoDto.getCppId());
		values.put(AppConstants.AIRPUR_BOOT_ID, deviceInfoDto.getBootId());
		values.put(AppConstants.AIRPUR_KEY, deviceInfoDto.getDeviceKey());
		
		long rowId = db.insert(AppConstants.AIRPUR_INFO_TABLE, null, values);
		
		closeDb();
		
		return rowId;
	}
	/**
	 * 
	 */
	public void closeDb() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}
	/**
	 * 
	 * @param usn
	 * @return
	 */
	public boolean isUsnExistsInTable(String usn) {
		boolean isExist = false;
		
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(AppConstants.AIRPUR_INFO_TABLE, 
				new String[] {AppConstants.AIRPUR_USN}, 
				AppConstants.AIRPUR_USN + "= ?", new String[]{usn}, null, null, null);
		
		if (cursor != null && cursor.getCount() == 0) {
			isExist = true;
		}else {
			isExist = false;
		}
		closeDb();
		return isExist;
	}
	/**
	 * 
	 * @return
	 */
	public List<DeviceInfoDto> getAllDeviceInfo() {
		List<DeviceInfoDto> deviceInfoList = new ArrayList<DeviceInfoDto>();
		
		db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.query(AppConstants.AIRPUR_INFO_TABLE, 
				null, null, null, null, null, null);
		
		if (cursor != null && cursor.getCount() == 0) {
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
				deviceInfoDto.setDeviceKey(
						cursor.getString(cursor.getColumnIndex(AppConstants.AIRPUR_KEY)));
				
				deviceInfoList.add(deviceInfoDto);
			} while(cursor.moveToNext());
		}
		
		closeDb();
		return deviceInfoList;
	}
	/**
	 * 
	 * @param id
	 * @param bootId
	 * @param devKey
	 * @return
	 */
	public long updateDeviceInfo(int id, int bootId, String devKey) {
		
		db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(AppConstants.AIRPUR_BOOT_ID, bootId);
		values.put(AppConstants.AIRPUR_KEY, devKey);
		
		long rowId = db.update(AppConstants.AIRPUR_INFO_TABLE, 
				values, AppConstants.ID + "= ?", new String[] {String.valueOf(id)});
		
		closeDb();
		
		return rowId;
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public long deleteDeviceInfo(int id) {
		db = dbHelper.getWritableDatabase();
		
		long rowId = db.delete(AppConstants.AIRPUR_INFO_TABLE, 
				AppConstants.ID + "= ?", new String[] {String.valueOf(id)});
		
		closeDb();
		
		return rowId;
		
	}

}
