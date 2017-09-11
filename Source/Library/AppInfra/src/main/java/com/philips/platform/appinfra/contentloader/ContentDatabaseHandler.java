/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.contentloader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.philips.platform.appinfra.contentloader.model.ContentItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Content Database Handler Class.
 */
class ContentDatabaseHandler extends SQLiteOpenHelper {

    private static ContentDatabaseHandler sInstance; // single instance accross application

    // Database name
    private static final String DATABASE_NAME = "AppInfraContentDB";
    // Database version
    private static final int DATABASE_VERSION = 3;

    //Table Name
    private static final String CONTENT_TABLE = "ContentTable";
    //Column Name
    private static final String KEY_ID = "contentID";
    private static final String KEY_SERVICE_ID = "serviceID";
    private static final String KEY_TAG_IDS = "tagIDs";
    private static final String KEY_EXPIRE_TIMESTAMP = "expire_at";
    private static final String KEY_RAW_CONTENT = "rawData";
    private static final String KEY_VERSION_NUMBER = "versionNumber";
    private static final String KEY_LAST_UPDATED_TIME = "lastupdatedtime";

    //table name
    private static final String CONTENT_LOADER_STATES = "ContentLoaderStates";
    private SQLiteDatabase db;

    public static synchronized ContentDatabaseHandler getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new ContentDatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    private ContentDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_CONTENT_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTENT_TABLE + "("
                + KEY_ID + " TEXT ,"
                + KEY_SERVICE_ID + " TEXT,"
                + KEY_RAW_CONTENT + " TEXT,"
                + KEY_TAG_IDS + " TEXT,"
                + KEY_VERSION_NUMBER + " DATETIME,"
                + KEY_LAST_UPDATED_TIME + " DATETIME ,"
                + " PRIMARY KEY (" + KEY_ID + " , " + KEY_SERVICE_ID + ") )";

        sqLiteDatabase.execSQL(CREATE_CONTENT_TABLE);
//        Log.d(AppInfraLogEventID.AI_CONTENT_LOADER,"first run"+" " + CONTENT_TABLE + "DB CREATED");

        final String CREATE_CONTENT_LOADER_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTENT_LOADER_STATES + "("
                + KEY_SERVICE_ID + " TEXT PRIMARY KEY,"
                + KEY_EXPIRE_TIMESTAMP + " DATETIME,"
                + KEY_LAST_UPDATED_TIME + " DATETIME "
                + ")";
        sqLiteDatabase.execSQL(CREATE_CONTENT_LOADER_TABLE);
//        Log.d(AppInfraLogEventID.AI_CONTENT_LOADER,"first run"+"" + CONTENT_LOADER_STATES + "DB CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CONTENT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CONTENT_LOADER_STATES);
        onCreate(sqLiteDatabase);
    }


    protected boolean addContents(List<ContentItem> serverContentItems, String serviceID, long lastUpdatedTime, long expiryDate, boolean isDownloadComplete) {
        boolean SQLitetransaction = true;
        final SQLiteDatabase db = this.getWritableDatabase();

        /////////////////TEST START
        //serverContentItems.remove(0);
      /*  ContentItem ci = new ContentItem();
        ci.setId("AnuragID");
        ci.setTags("tag1,tag2");
        ci.setRawData("{}");
        ci.setServiceId(serviceID);
        ci.setVersionNumber(serverContentItems.get(0).getVersionNumber());

        ContentItem ci2 = new ContentItem();
        ci2.setId("AnuragID2");
        ci2.setTags("tag1,tag2");
        ci2.setRawData("{}");
        ci2.setServiceId(serviceID);
        ci2.setVersionNumber(serverContentItems.get(1).getVersionNumber());
        serverContentItems.add(ci);
        serverContentItems.add(ci2);*/
        ////////////////TEST END
        try {
            // db.beginTransaction();
            if (null != serverContentItems && !serverContentItems.isEmpty()) {
                for (final ContentItem contentItem : serverContentItems) {
                    final ContentValues values = getContentValues(contentItem);
                    final long rowId = db.replace(CONTENT_TABLE, null, values);
                    if (rowId == -1) {
                        SQLitetransaction = false;
//                        Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"UPDATE FAIL"+CONTENT_TABLE);
                    } /*else {
                        Log.i(AppInfraLogEventID.AI_CONTENT_LOADER,"UPDATE Success row id " + CONTENT_TABLE + " " + rowId);
                    }*/
                }
            }
            if (isDownloadComplete) { // last iteration of recursion
                List<ContentItem>   databaseContentItems = getContentItems(serviceID);
//                Log.v(AppInfraLogEventID.AI_CONTENT_LOADER,"DELEET DB SIZE BEFORE DELETE= " + databaseContentItems.size());
                final Date date = new Date(lastUpdatedTime);
                db.delete(CONTENT_TABLE, KEY_SERVICE_ID + " = ? AND " + KEY_LAST_UPDATED_TIME + " != " + date.getTime(), new String[]{serviceID});
                databaseContentItems = getContentItems(serviceID);
//                Log.v(AppInfraLogEventID.AI_CONTENT_LOADER,"DELETE DB SIZE AFTER DELETE= " + databaseContentItems.size());
            }
            if (SQLitetransaction) {
                updateContentLoaderStateTable(db, lastUpdatedTime, serviceID, expiryDate);
            }
        } catch (Exception e) {
            SQLitetransaction = false;
//            Log.w(AppInfraLogEventID.AI_CONTENT_LOADER," Error in insertQuery:");
        } finally {
            if (db != null && db.isOpen()) {
                try {
                    db.close();
                } catch (Exception e) {
//                    Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"Error in insertQuery");
                }
            }
        }
        return SQLitetransaction;
    }


    private List<ContentItem> getContentItems(String serviceId) {
        Cursor cursor = null;
        final SQLiteDatabase db = this.getWritableDatabase();
        final List<ContentItem> ContentItemList = new ArrayList<ContentItem>();
        try {
            final String selectQuery = "SELECT  * FROM " + CONTENT_TABLE + " WHERE " + KEY_SERVICE_ID + " = ?";
            cursor = db.rawQuery(selectQuery, new String[]{serviceId});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    final ContentItem contentItem = getContentItemFromCursor(cursor);
                    ContentItemList.add(contentItem);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
//            Log.w(AppInfraLogEventID.AI_CONTENT_LOADER,"Error in selectQuery:");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                try {
                    cursor.close();
                } catch (Exception e) {
//                    Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"Content Iteams ");
                }
            }

        }
        return ContentItemList;
    }

    protected List<String> getAllContentIds(String serviceID) {
        Cursor cursor = null;
        final ArrayList<String> Ids = new ArrayList<String>();
        final SQLiteDatabase db = this.getWritableDatabase();
        String getAllIdQuery = null;
        try {
            getAllIdQuery = "SELECT " + KEY_ID + " FROM " + CONTENT_TABLE + " WHERE " + KEY_SERVICE_ID + " = ?";
            cursor = db.rawQuery(getAllIdQuery, new String[]{serviceID});
            if (null != cursor && cursor.moveToFirst()) {
                do {
                    Ids.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
//            Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"SELECT FAIL "+getAllIdQuery);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                try {
                    cursor.close();
                } catch (Exception e) {
//                    Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"SELECT FAIL");
                }
            }
        }
        return Ids;
    }

    protected List<ContentItem> getContentById(String serviceID, String[] contentIDs) {
        Cursor cursor = null;
        final List<ContentItem> ContentItemList = new ArrayList<ContentItem>();
        final SQLiteDatabase db = this.getWritableDatabase();
        String getContentByIdQuery = null;
        try {

            getContentByIdQuery = "SELECT * FROM " + CONTENT_TABLE + " WHERE " + KEY_SERVICE_ID + " = ? AND " + KEY_ID + " IN (" + makePlaceholders(contentIDs.length) + ")";
            final String[] params = new String[contentIDs.length + 1];
            params[0] = serviceID;
            System.arraycopy(contentIDs, 0, params, 1, contentIDs.length);
            cursor = db.rawQuery(getContentByIdQuery, params);
            if (null != cursor && cursor.moveToFirst()) {
                do {
                    final ContentItem contentItem = getContentItemFromCursor(cursor);
                    ContentItemList.add(contentItem);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
//            Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"SELECT FAIL "+getContentByIdQuery);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                try {
                    cursor.close();
                } catch (Exception e) {
//                    Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"SELECT FAIL ");
                }
            }

        }
        return ContentItemList;
    }


    protected List<ContentItem> getContentByTagId(String serviceID, String[] tagIDs, String logicalGate) {
        Cursor cursor = null;
        final List<ContentItem> ContentItemList = new ArrayList<ContentItem>();
        final SQLiteDatabase db = this.getWritableDatabase();
        String getContentByIdQuery = null;
        try {
            if (tagIDs.length == 1) {
                getContentByIdQuery = "SELECT * FROM " + CONTENT_TABLE + " WHERE " + KEY_SERVICE_ID + " = ? AND " + KEY_TAG_IDS + " LIKE \'%" + tagIDs[0] + "%\'";
            } else if (tagIDs.length > 1) {
                final String[] whereClause = new String[tagIDs.length];
                int idCount = 0;
                for (String id : tagIDs) {
                    whereClause[idCount++] = KEY_TAG_IDS + " LIKE \'%" + id + "%\'";
                }
                String formattedwhereClause = TextUtils.join(" " + logicalGate + " ", whereClause);
                getContentByIdQuery = "SELECT * FROM " + CONTENT_TABLE + " WHERE " + KEY_SERVICE_ID + " = ? AND (" + formattedwhereClause + " )";
            }
            cursor = db.rawQuery(getContentByIdQuery, new String[]{serviceID});
            if (null != cursor && cursor.moveToFirst()) {
                do {
                    ContentItem contentItem = getContentItemFromCursor(cursor);
                    ContentItemList.add(contentItem);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
//            Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"SELECT FAIL "+getContentByIdQuery);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                try {
                    cursor.close();
                } catch (Exception e) {
//                    Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"SELECT FAIL "+getContentByIdQuery);
                }
            }
        }
        return ContentItemList;
    }


    private boolean updateContentLoaderStateTable(SQLiteDatabase db, long mLastUpdatedTime, String serviceID, long expiryDate) {
        final ContentValues values = new ContentValues();
        values.put(KEY_SERVICE_ID, serviceID);
        values.put(KEY_EXPIRE_TIMESTAMP, expiryDate);
        values.put(KEY_LAST_UPDATED_TIME, mLastUpdatedTime);

        final long rowId = db.replace(CONTENT_LOADER_STATES, null, values);
       /* if (rowId == -1) {
            Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"Update FAIL "+CONTENT_LOADER_STATES);
        } else {
            Log.i(AppInfraLogEventID.AI_CONTENT_LOADER,"Update Success row id " + CONTENT_LOADER_STATES + " " + rowId);
        }*/
        return rowId != -1;
    }

    protected long getContentLoaderServiceStateExpiry(String serviceID) {
        long expiryTime = 0l;
        try {
            final SQLiteDatabase db = this.getWritableDatabase();
            final String getContentLoaderServiceStateExpiryQuery = "SELECT " + KEY_EXPIRE_TIMESTAMP + " FROM " + CONTENT_LOADER_STATES + " WHERE " + KEY_SERVICE_ID + " = ?";
            final Cursor cursor = db.rawQuery(getContentLoaderServiceStateExpiryQuery, new String[]{serviceID});
            if (null != cursor && cursor.moveToFirst()) {
                expiryTime = cursor.getLong(0);
            }
            if (cursor != null && !cursor.isClosed()) {
                try {
                    cursor.close();
                } catch (Exception e) {
//                    Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"ServiceStateExpiry FAIL");
                }
            }
        } catch (Exception e) {
//            Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"ServiceStateExpiry FAIL");
        }
        return expiryTime;
    }

    private ContentValues getContentValues(ContentItem pContentItem) {
        final ContentValues values = new ContentValues();
        values.put(KEY_ID, pContentItem.getId());
        values.put(KEY_SERVICE_ID, pContentItem.getServiceId());
        values.put(KEY_RAW_CONTENT, pContentItem.getRawData()); // Json String
        values.put(KEY_TAG_IDS, pContentItem.getTags());
        values.put(KEY_VERSION_NUMBER, pContentItem.getVersionNumber());
        values.put(KEY_LAST_UPDATED_TIME, pContentItem.getLastUpdatedTime());
        return values;
    }

    protected boolean clearCacheForContentLoader(String serviceID) {
        boolean result = true;
        try {
            final SQLiteDatabase db = this.getWritableDatabase();
            db.delete(CONTENT_TABLE, KEY_SERVICE_ID + " = ?", new String[]{serviceID});
            db.delete(CONTENT_LOADER_STATES, KEY_SERVICE_ID + " = ?", new String[]{serviceID});
//            Log.d(AppInfraLogEventID.AI_CONTENT_LOADER,"DEL Success " + CONTENT_LOADER_STATES + " & " + CONTENT_TABLE);

        } catch (Exception e) {
            result = false;
//            Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"DELETE FAIL ");
        } finally {
            if (db != null && db.isOpen()) {
                try {
                    db.close();
                } catch (Exception e) {
//                    Log.e(AppInfraLogEventID.AI_CONTENT_LOADER,"CacheForContent FAIL ");
                }
            }
        }
        return result;

    }

    private String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            final StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    private ContentItem getContentItemFromCursor(Cursor cursor) {
        final ContentItem contentItem = new ContentItem();
        contentItem.setId(cursor.getString(0));
        contentItem.setServiceId(cursor.getString(1));
        contentItem.setRawData(cursor.getString(2));
        contentItem.setTags(cursor.getString(3));
        contentItem.setVersionNumber(cursor.getLong(4));
        contentItem.setLastUpdatedTime(cursor.getLong(5));
        return contentItem;
    }
}
