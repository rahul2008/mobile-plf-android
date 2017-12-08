/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.ui.catalog.favorites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Favorites.db";



    private static final String TEXT_TYPE = " TEXT";
   // private static final String BOOLEAN_TYPE = " BOOLEAN";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FavoritesContract.FeedEntry.TABLE_NAME + " (" +
                    FavoritesContract.FeedEntry._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    FavoritesContract.FeedEntry.COLUMN_ITEM_NAME + TEXT_TYPE + " NOT NULL" + COMMA_SEP + FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE
    + TEXT_TYPE + COMMA_SEP+ "UNIQUE(" + FavoritesContract.FeedEntry._ID + COMMA_SEP + FavoritesContract.FeedEntry.COLUMN_ITEM_NAME + ") ON CONFLICT REPLACE)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FavoritesContract.FeedEntry.TABLE_NAME;

    Context mContext;


    public DataBaseHelper(final Context context, final String name, final SQLiteDatabase.CursorFactory factory, final int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
