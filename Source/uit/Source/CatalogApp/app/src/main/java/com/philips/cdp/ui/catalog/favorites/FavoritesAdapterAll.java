/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.ui.catalog.favorites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;

public class FavoritesAdapterAll extends BaseAdapter {
    public Context activity;
    DataBaseHelper mDbHelper;
    SQLiteDatabase db;
    private LayoutInflater inflater = null;

    public FavoritesAdapterAll(Context activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDbHelper = new DataBaseHelper(activity);
        db = mDbHelper.getReadableDatabase();
    }

    @Override
    public int getCount() {
        Cursor mCursor = null;
        int count = -1;
        try {
            mCursor = getAllValuesFromDB();
            count = mCursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor!=null)
                mCursor.close();
        }
        return count;
    }

    @Override
    public Object getItem(final int position) {
        return null;
    }

    @Override
    public long getItemId(final int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.uikit_favorites_item_layout, null);

        ImageView image = (ImageView) vi.findViewById(R.id.imagefav);

        TextView value = (TextView) vi.findViewById(R.id.item_name);
        final ImageView favorite = (ImageView) vi.findViewById(R.id.favorite);
        FrameLayout frame = (FrameLayout) vi.findViewById(R.id.favoritecontainer);

        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Cursor mCursor = null;
                try {
                    mCursor = getAllValuesFromDB();
                    mCursor.moveToPosition(position);

                    String name1 = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME));
                    String isItFavorite = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE));

                    if (isItFavorite.equalsIgnoreCase("true")) {
                        favorite.setImageDrawable(VectorDrawable.create(activity, R.drawable.uikit_outlined_star_favorites));
                        addToFavorites(name1, "false");
                    } else {
                        favorite.setImageDrawable(VectorDrawable.create(activity, R.drawable.uikit_solid_favorites_star));
                        addToFavorites(name1, "true");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(mCursor!=null)
                        mCursor.close();
                }
            }
        });
        Cursor mCursor = null;
        try {
            mCursor = getAllValuesFromDB();
            mCursor.moveToPosition(position);
            String name = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME));
            String isFavorite = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE));
            image.setImageDrawable(VectorDrawable.create(activity, R.drawable.uikit_location_pointer));
            value.setText("Item 1 " + mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME)));
            if (isFavorite.equalsIgnoreCase("true")) {
                favorite.setImageDrawable(VectorDrawable.create(activity, R.drawable.uikit_solid_favorites_star));
            } else {
                favorite.setImageDrawable(VectorDrawable.create(activity,R.drawable.uikit_outlined_star_favorites));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor!=null)
                mCursor.close();
        }
        return vi;
    }

    /**
     * Add the Item as Favorite in the data Base after the User selects that as favorite
     * @param name
     * @param isFavorite
     */
    void addToFavorites(String name, String isFavorite) {
        DataBaseHelper mDbHelper = new DataBaseHelper(activity);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE, isFavorite);
            cv.put(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME, name);
            int i = db.update(FavoritesContract.FeedEntry.TABLE_NAME, cv, FavoritesContract.FeedEntry.COLUMN_ITEM_NAME + " = ?", new String[]{name});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mDbHelper!=null)
                mDbHelper.close();
            if(db!=null)
                db.close();
        }
    }

    /**
     * Get all the Values from the DB
     * @return
     */
    Cursor getAllValuesFromDB() {

        String[] projection = {
                FavoritesContract.FeedEntry.COLUMN_ITEM_NAME,
                FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE
        };

        Cursor c = db.query(
                FavoritesContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        return c;
    }

    /**
     * Close the connections once the Activity is destroyed.
     */
    public void closeConnections(){
        if(mDbHelper!=null)
            mDbHelper.close();
        if(db!=null)
            db.close();
    }
}
