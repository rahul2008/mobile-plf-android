package com.philips.platform.appinfra.contentloader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.philips.platform.appinfra.contentloader.model.ContentItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310238114 on 11/8/2016.
 */
public class ContentDatabaseHandler extends SQLiteOpenHelper {
    // Database name
    private static final String DATABASE_NAME = "AppInfraContentDB";
    // Database version
    private static final int DATABASE_VERSION = 1;

    //Table Name
    private static final String CONTENT_TABLE = "ContentTable";
    //Column Name
    private static final String KEY_ID = "contentID";
    private static final String KEY_SERVICE_ID = "serviceID";
    private static final String KEY_TAG_IDS = "tagIDs";
    private static final String KEY_EXPIRE_TIMESTAMP = "expire_at";
    private static final String KEY_RAW_CONTENT = "rawData";
    private static final String KEY_VERSION_NUMBER = "versionNumber";

    //table name
    private static final String CONTENT_LOADER_TABLE = "ContentLoaderTable";


    public ContentDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTENT_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTENT_TABLE + "("
                + KEY_ID + " TEXT ,"
                + KEY_SERVICE_ID + " TEXT,"
                + KEY_RAW_CONTENT + " TEXT,"
                + KEY_TAG_IDS + " TEXT,"
                + KEY_VERSION_NUMBER + " DATETIME,"
                + " PRIMARY KEY (" + KEY_ID + " , " + KEY_SERVICE_ID + ") )";

        sqLiteDatabase.execSQL(CREATE_CONTENT_TABLE);
        Log.d("first run", "" + CONTENT_TABLE + "DB CREATED");

        String CREATE_CONTENT_LOADER_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTENT_LOADER_TABLE + "("
                + KEY_SERVICE_ID + " TEXT PRIMARY KEY,"
                + KEY_EXPIRE_TIMESTAMP + " DATETIME"
                + ")";
        sqLiteDatabase.execSQL(CREATE_CONTENT_LOADER_TABLE);
        Log.d("first run", "" + CONTENT_LOADER_TABLE + "DB CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CONTENT_TABLE);
        onCreate(sqLiteDatabase);
    }

    void addContents(List<ContentItem> refreshedContentItems, String serviceID, long expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        List<ContentItem> existingContents = getContentItems(serviceID);
        if (null != existingContents && existingContents.size() > 0) {
            // if already contents present for given service ID
            updateContents(refreshedContentItems,existingContents, serviceID,  expiryDate );
        } else {
            // first run for given service id
            insertContents(refreshedContentItems,serviceID,expiryDate);
        }
        db.close();
    }


    private void insertContents(List<ContentItem> refreshedContentItems, String serviceID, long expiryDate){
        // first run, no entry available fo given service id so all content item will be inserted
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            boolean allRowInserted = true;
            for (ContentItem contentItem : refreshedContentItems) {
                ContentValues values = new ContentValues();
                values.put(KEY_ID, contentItem.getId());
                values.put(KEY_SERVICE_ID, contentItem.getServiceId());
                values.put(KEY_RAW_CONTENT, contentItem.getRawData()); // Json String
                values.put(KEY_TAG_IDS, contentItem.getTags());
                values.put(KEY_VERSION_NUMBER, contentItem.getVersionNumber());

                long rowId = db.insert(CONTENT_TABLE, null, values);
                if (rowId == -1) {
                    allRowInserted = false;
                    Log.e("INS FAIL", CONTENT_TABLE);
                } else {
                    Log.i("INS SUC", "row id " + CONTENT_TABLE + " " + rowId);
                }
            }
            if (allRowInserted) {
                ContentValues values = new ContentValues();
                values.put(KEY_SERVICE_ID, serviceID);
                values.put(KEY_EXPIRE_TIMESTAMP, expiryDate);
                long rowId = db.insert(CONTENT_LOADER_TABLE, null, values);
                if (rowId == -1) {
                    allRowInserted = false;
                    Log.e("INS FAIL", CONTENT_LOADER_TABLE);
                } else {
                    Log.i("INS SUC", "row id " + CONTENT_LOADER_TABLE + " " + rowId);
                }
            }
            if (allRowInserted) {
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e("INS FAIL", "");
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    private void updateContents(List<ContentItem> refreshedContentItems,List<ContentItem> existingContentItems, String serviceID, long expiryDate){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            boolean anyRowUpdated = false;
            long versionNumberDB = 0;//default version in DB meaning content item does not exist
            for (ContentItem contentItem : refreshedContentItems) {
                // fetch the requested(newly downloaded) item from DB(if present)
                ContentValues values = new ContentValues();
                values.put(KEY_ID, contentItem.getId()+6);
                values.put(KEY_SERVICE_ID, contentItem.getServiceId());
                values.put(KEY_RAW_CONTENT, contentItem.getRawData()); // Json String
                values.put(KEY_TAG_IDS, contentItem.getTags());
                values.put(KEY_VERSION_NUMBER, contentItem.getVersionNumber());

                String selectQuery = "SELECT " + KEY_VERSION_NUMBER + " FROM " + CONTENT_TABLE
                        + " WHERE " + KEY_ID + " = \'" + contentItem.getId()+ "\' AND " + KEY_SERVICE_ID + " = \'" + contentItem.getServiceId()+"\'";
                Cursor cursor = db.rawQuery(selectQuery, null);
                cursor.moveToFirst();
                if (null != cursor && cursor.getCount()>0){ // if requested row present in DB
                    versionNumberDB=cursor.getLong(0);
                }
                if (versionNumberDB==0) {
                    //  if given id does not exist in table then insert row
                    long rowId = db.insert(CONTENT_TABLE, null, values);
                    if (rowId == -1) {
                        Log.e("INS FAIL", CONTENT_TABLE);
                    } else {
                        anyRowUpdated = true;
                        Log.i("INS SUC", "row id " + CONTENT_TABLE + " " + rowId);
                    }
                }else if (contentItem.getVersionNumber()>versionNumberDB){
                    // if given content exist in table its version is older then refreshed content then replace row
                    long rowId = db.replace(CONTENT_TABLE, null, values);
                    if (rowId == -1) {
                        Log.e("UPDATE FAIL", CONTENT_TABLE);
                    } else {
                        anyRowUpdated = true;
                        Log.i("UPDATE SUC", "row id " + CONTENT_TABLE + " " + rowId);
                    }
                }
            }
            if(anyRowUpdated){
                Log.i("UPDATE SUC", "content(s) with new version updated in DB");
                // ContentLoader table also need to be updated
                String insertOrReplaceContentLoader = "INSERT OR REPLACE INTO " + CONTENT_LOADER_TABLE + " VALUES ("
                        + serviceID + ","
                        + expiryDate +
                        ") WHERE " + KEY_SERVICE_ID + " = \'" + serviceID+"\'";
                Cursor cursorInsertOrReplaceContentItem = db.rawQuery(insertOrReplaceContentLoader, null);
                Log.i("UPDATE SUC", "Service ID "+serviceID+" Expiry Date updated");
            }else{
                Log.i("UPDATE NA", "Service ID "+serviceID+" contents are already up to date in DB");
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("INS FAIL", "");
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    //Delete Query
    private void removeContent(String serviceId) {
        String deleteQuery = "DELETE FROM " + CONTENT_TABLE + " where " + KEY_SERVICE_ID + "= " + serviceId;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(deleteQuery);
    }

    private List<ContentItem> getContentItems(String serviceId) {
        String selectQuery = "SELECT  * FROM " + CONTENT_TABLE + " WHERE " + KEY_SERVICE_ID + " = \'" + serviceId+"\'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<ContentItem> ContentItemList = new ArrayList<ContentItem>();
        if (cursor.moveToFirst()) {
            do {
                ContentItem contentItem = new ContentItem();
                contentItem.setId(cursor.getString(0));
                contentItem.setServiceId(cursor.getString(1));
                contentItem.setRawData(cursor.getString(2));
                contentItem.setTags(cursor.getString(3));
                contentItem.setVersionNumber(cursor.getLong(4));
                ContentItemList.add(contentItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ContentItemList;
    }
}
