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
import android.util.Log;

import com.philips.platform.appinfra.contentloader.model.ContentItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    private static final String KEY_LASTUPDATED_TIME = "lastupdatedtime";

    //table name
    private static final String CONTENT_LOADER_STATES = "ContentLoaderStates";


    public ContentDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTENT_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTENT_TABLE + "("
                + KEY_ID + " TEXT ,"
                + KEY_LASTUPDATED_TIME + "DATE ,"
                + KEY_SERVICE_ID + " TEXT,"
                + KEY_RAW_CONTENT + " TEXT,"
                + KEY_TAG_IDS + " TEXT,"
                + KEY_VERSION_NUMBER + " DATETIME,"
                + " PRIMARY KEY (" + KEY_ID + " , " + KEY_SERVICE_ID + ") )";

        sqLiteDatabase.execSQL(CREATE_CONTENT_TABLE);
        Log.d("first run", "" + CONTENT_TABLE + "DB CREATED");

        String CREATE_CONTENT_LOADER_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTENT_LOADER_STATES + "("
                + KEY_SERVICE_ID + " TEXT PRIMARY KEY,"
                + KEY_EXPIRE_TIMESTAMP + " DATETIME"
                + ")";
        sqLiteDatabase.execSQL(CREATE_CONTENT_LOADER_TABLE);
        Log.d("first run", "" + CONTENT_LOADER_STATES + "DB CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CONTENT_TABLE);
        onCreate(sqLiteDatabase);
    }

    protected void addContents(List<ContentItem> serverContentItems, Date mLastUpdatedTime, String serviceID, long expiryDate) {

        List<ContentItem> databaseContents = getContentItems(serviceID);
        if (null != databaseContents && databaseContents.size() > 0) {
            // if already contents present for given service ID
            updateContents(serverContentItems,mLastUpdatedTime, databaseContents, serviceID, expiryDate);
        } else {
            // first run for given service id
            insertContents(serverContentItems,mLastUpdatedTime, serviceID, expiryDate);
        }

    }


    private boolean insertContents(List<ContentItem> refreshedContentItems, Date mLastUpdatedTime, String serviceID, long expiryDate) {
        boolean insertOperation=false;
        // first run, no entry available fo given service id so all content item will be inserted
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            boolean allRowInserted = true;
            boolean isContentloaderStateTableUpdated = false;
            for (ContentItem contentItem : refreshedContentItems) {
                ContentValues values = getContentValues(contentItem);
                long rowId = db.insert(CONTENT_TABLE, null, values);
                if (rowId == -1) {
                    allRowInserted = false;
                    Log.e("INS FAIL", CONTENT_TABLE);
                } else {
                    Log.i("INS SUC", "row id " + CONTENT_TABLE + " " + rowId);
                }
            }
            if (allRowInserted) {
                ///
                isContentloaderStateTableUpdated = updateContentLoaderStateTable(db,mLastUpdatedTime, serviceID, expiryDate);
            }
            if (isContentloaderStateTableUpdated) {
                db.setTransactionSuccessful();
                insertOperation=true;
            }
        } catch (Exception e) {
            Log.e("INS FAIL", "");
        } finally {
            db.endTransaction();
        }
        db.close();
        return insertOperation;
    }

    private boolean updateContents(List<ContentItem> serverContentItems, Date mLastUpdatedTime, List<ContentItem> databaseContentItems, String serviceID, long expiryDate) {
        boolean SQLitetransaction = true;
        SQLiteDatabase db = this.getWritableDatabase();
        List<ContentItem> newContentItemToBeAddedFromServer = new ArrayList<>();
        List<ContentItem> newContentItemToBeUpdatedFromServer = new ArrayList<>();
        Set<String> serverItemIdSet = new HashSet<>();
        Set<String> dataBaseItemIdSet = new HashSet<>();
        Set<String> dataBaseItemIdSetTemp = new HashSet<>();
        Set<Long> serverItemVersionSet = new HashSet<>();
        Set<Long> dataBaseItemVersionSet = new HashSet<>();
        /////////////////TEST START

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
        for (ContentItem contentItem : serverContentItems) {
            serverItemIdSet.add(contentItem.getId());
            serverItemVersionSet.add(contentItem.getVersionNumber());
        }
        for (ContentItem contentItem : databaseContentItems) {
            dataBaseItemIdSet.add(contentItem.getId());
            dataBaseItemIdSetTemp.add(contentItem.getId());// tmp value as dataBaseItemIdSet will be modified after removeAll()
            dataBaseItemVersionSet.add(contentItem.getVersionNumber());
        }

        dataBaseItemIdSet.removeAll(serverItemIdSet); //  dataBaseItemIdSet reduce to Old item set to be removed from database
        serverItemIdSet.removeAll(dataBaseItemIdSetTemp);// serverItemIdSet reduce to  New item set to be added from server

        db.beginTransaction();
        // start of deletion of old items from DB
        if (dataBaseItemIdSet.size() > 0) { // one or more item to be deleted from db
            try {
                String[] whereClause = new String[dataBaseItemIdSet.size()];
                int idCount = 0;
                for (String id : dataBaseItemIdSet) {
                    whereClause[idCount++] = KEY_ID + " = \"" + id + "\"";
                }
                String formattedwhereClause = TextUtils.join(" OR ", whereClause);
                String deleteQuery = "delete from " + CONTENT_TABLE + " where " + formattedwhereClause;
                Log.w("deleteQuery:", deleteQuery);
                db.execSQL(deleteQuery);
                Log.w("deleteQuery:", "Success");
            } catch (Exception e) {
                SQLitetransaction = false;
                Log.w("deleteQuery:", e);
            }
        }
        // end of deletion of old items from DB

        // start of addition of new items in DB
        if (serverItemIdSet.size() > 0) { // one or more item to be added in db
            try {
                for (ContentItem contentItem : serverContentItems) {
                    if (serverItemIdSet.contains(contentItem.getId())) {
                        newContentItemToBeAddedFromServer.add(contentItem);
                    }
                }
                if (newContentItemToBeAddedFromServer.size() > 0) {
                    for (ContentItem contentItem : newContentItemToBeAddedFromServer) {
                        ContentValues values = getContentValues(contentItem);
                        long rowId = db.insert(CONTENT_TABLE, null, values);
                        if (rowId == -1) {
                            SQLitetransaction = false;
                            Log.e("INS FAIL", CONTENT_TABLE);
                        } else {
                            Log.i("INS SUC", "row id " + CONTENT_TABLE + " " + rowId);
                        }
                    }
                }
            } catch (Exception e) {
                SQLitetransaction = false;
                Log.w("insertQuery:", e);
            }
        }
        // end of addition of new items in DB

        // start of updating items based on item version number  changed
        serverItemVersionSet.removeAll(dataBaseItemVersionSet);// only modified latest version number to be
        if (serverItemVersionSet.size() > 0) { // one or more item to be updated in db
            try {
                for (ContentItem contentItem : serverContentItems) {
                    if (serverItemIdSet.contains(contentItem.getId())) {
                        newContentItemToBeUpdatedFromServer.add(contentItem);
                    }
                }
                if (newContentItemToBeUpdatedFromServer.size() > 0) {
                    long versionNumberDB = 0;
                    for (ContentItem contentItem : newContentItemToBeUpdatedFromServer) {
                        ContentValues values = getContentValues(contentItem);
                        long rowId = db.replace(CONTENT_TABLE, null, values);
                        if (rowId == -1) {
                            SQLitetransaction = false;
                            Log.e("UPDATE FAIL", CONTENT_TABLE);
                        } else {
                            Log.i("UPDATE SUC", "row id " + CONTENT_TABLE + " " + rowId);
                        }
                    }
                }
            } catch (Exception e) {
                SQLitetransaction = false;
                Log.w("updateQuery:", e);
            }
        }
        // end of updating items based on item version number  changed


        //
        boolean isContentLoaderStateTableUpdated = updateContentLoaderStateTable(db, mLastUpdatedTime, serviceID, expiryDate);
        if (isContentLoaderStateTableUpdated) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();

        db.close();
        return SQLitetransaction;
    }

    //Delete Query
    private void removeContent(String serviceId) {
        String deleteQuery = "DELETE FROM " + CONTENT_TABLE + " where " + KEY_SERVICE_ID + "= ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(deleteQuery,new String[]{serviceId});
    }

    private List<ContentItem> getContentItems(String serviceId) {
        String selectQuery = "SELECT  * FROM " + CONTENT_TABLE + " WHERE " + KEY_SERVICE_ID + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{serviceId});
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

    protected List<String> getAllContentIds(String serviceID) {
        ArrayList<String> Ids = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String getAllIDQuery = null;
        try {
            getAllIDQuery = "SELECT " + KEY_ID + " FROM " + CONTENT_TABLE + " WHERE " + KEY_SERVICE_ID + " = ?";
            Cursor cursor = db.rawQuery(getAllIDQuery, new String[]{serviceID});
            if (cursor.moveToFirst()) {
                do {
                    Ids.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("SELECT FAIL", getAllIDQuery);
        } finally {
            db.close();
        }
        return Ids;
    }

    protected List<ContentItem> getContentById(String serviceID, String[] contentIDs) {
        List<ContentItem> ContentItemList = new ArrayList<ContentItem>();
        SQLiteDatabase db = this.getWritableDatabase();
        String getContentByIdQuery = null;
        try {

            getContentByIdQuery = "SELECT * FROM " + CONTENT_TABLE + " WHERE " + KEY_SERVICE_ID + " = ? AND "+KEY_ID+ " IN (" + makePlaceholders(contentIDs.length) + ")";
            String[] params =  new  String[contentIDs.length+1]  ;
            params[0]= serviceID;
            System.arraycopy(contentIDs ,0,params,1,contentIDs.length);
            Cursor cursor = db.rawQuery(getContentByIdQuery,params );
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
        } catch (Exception e) {
            Log.e("SELECT FAIL", getContentByIdQuery);
        } finally {
            db.close();
        }
        return ContentItemList;
    }


    protected List<ContentItem> getContentByTagId(String serviceID, String[] tagIDs, String logicalGate) {
        List<ContentItem> ContentItemList = new ArrayList<ContentItem>();
        SQLiteDatabase db = this.getWritableDatabase();
        String getContentByIdQuery = null;
        try {
            if (tagIDs.length == 1) {
                getContentByIdQuery = "SELECT * FROM " + CONTENT_TABLE + " WHERE " + KEY_SERVICE_ID + " = ? AND " + KEY_TAG_IDS + " LIKE \'%" + tagIDs[0] + "%\'";
            } else if (tagIDs.length > 1) {
                String[] whereClause = new String[tagIDs.length];
                int idCount = 0;
                for (String id : tagIDs) {
                    whereClause[idCount++] = KEY_TAG_IDS + " LIKE \'%" + id + "%\'";
                }
                String formattedwhereClause = TextUtils.join(" " + logicalGate + " ", whereClause);
                getContentByIdQuery = "SELECT * FROM " + CONTENT_TABLE + " WHERE " + KEY_SERVICE_ID + " = ? AND (" + formattedwhereClause + " )";
            }
            Cursor cursor = db.rawQuery(getContentByIdQuery, new String[] {serviceID});
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
        } catch (Exception e) {
            Log.e("SELECT FAIL", getContentByIdQuery);
        } finally {
            db.close();
        }
        return ContentItemList;
    }

    private boolean updateContentLoaderStateTable(SQLiteDatabase db, Date mLastUpdatedTime, String serviceID, long expiryDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" , Locale.ENGLISH);
        ContentValues values = new ContentValues();
        values.put(KEY_SERVICE_ID, serviceID);
        values.put(KEY_LASTUPDATED_TIME  , dateFormat.format(mLastUpdatedTime));
        values.put(KEY_EXPIRE_TIMESTAMP, expiryDate);
        long rowId = db.replace(CONTENT_LOADER_STATES, null, values);
        if (rowId == -1) {
            Log.e("INS FAIL", CONTENT_LOADER_STATES);
        } else {
            Log.i("INS SUC", "row id " + CONTENT_LOADER_STATES + " " + rowId);
        }
        return rowId == -1 ? false : true;
    }


    protected long getContentLoaderServiceStateExpiry(String serviceID) {
        long expiryTime = 0l;
        SQLiteDatabase db = this.getWritableDatabase();
        String getContentLoaderServiceStateExpiryQuery = "SELECT " + KEY_EXPIRE_TIMESTAMP + " FROM " + CONTENT_LOADER_STATES + " WHERE " + KEY_SERVICE_ID + " = ?";
        Cursor cursor = db.rawQuery(getContentLoaderServiceStateExpiryQuery, new String[]{serviceID});
        if (cursor.moveToFirst()) {
            expiryTime = cursor.getLong(0);
        }
        cursor.close();
        return expiryTime;
    }

    private ContentValues getContentValues(ContentItem pContentItem) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, pContentItem.getId());
        values.put(KEY_SERVICE_ID, pContentItem.getServiceId());
        values.put(KEY_RAW_CONTENT, pContentItem.getRawData()); // Json String
        values.put(KEY_TAG_IDS, pContentItem.getTags());
        values.put(KEY_VERSION_NUMBER, pContentItem.getVersionNumber());
        return values;
    }

    protected void clearCacheForContentLoader(String serviceID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + CONTENT_LOADER_STATES + " WHERE " + KEY_SERVICE_ID + " = ?", new String[]{serviceID});
        db.execSQL("delete from " + CONTENT_TABLE + " WHERE " + KEY_SERVICE_ID + " = ?", new String[]{serviceID});
        db.close();
        Log.d("DEL SUC", "" + CONTENT_LOADER_STATES + " & " + CONTENT_TABLE);

    }
    String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }
}
