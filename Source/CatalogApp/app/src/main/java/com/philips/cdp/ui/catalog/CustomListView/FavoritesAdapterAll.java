package com.philips.cdp.ui.catalog.CustomListView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.DataBaseHelper;
import com.philips.cdp.ui.catalog.activity.FavoritesContract;
import com.philips.cdp.uikit.customviews.TintableImageView;
import com.philips.cdp.uikit.customviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class FavoritesAdapterAll extends BaseAdapter {
    private LayoutInflater inflater=null;
    public Context activity;
    Cursor mCursor;
    DataBaseHelper mDbHelper;//= new DataBaseHelper(activity);

    // Gets the data repository in write mode
    SQLiteDatabase db;// = mDbHelper.getReadableDatabase();

    public FavoritesAdapterAll(Context activity) {

        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        mCursor = getAllValuesFromDB();
        mDbHelper= new DataBaseHelper(activity);

        // Gets the data repository in write mode
        db = mDbHelper.getReadableDatabase();
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
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

        View vi=convertView;


        if(convertView==null)
            vi = inflater.inflate(R.layout.uikit_favorites_custom_layout, null);


        VectorDrawableImageView image = (VectorDrawableImageView) vi.findViewById(R.id.imagefav);

        TextView value = (TextView) vi.findViewById(R.id.item_name);
        final VectorDrawableImageView favorite = (VectorDrawableImageView) vi.findViewById(R.id.favorite);

        FrameLayout frame = (FrameLayout) vi.findViewById(R.id.favoritecontainer);
        mCursor = getAllValuesFromDB();
        if (mCursor == null) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }

        String name = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME));
        final String isFavorite = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE));

        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mCursor = getAllValuesFromDB();
                if (mCursor == null) {
                    //throw new IllegalStateException("this should only be called when the cursor is valid");
                    return;
                }
                if (!mCursor.moveToPosition(position)) {
                    //throw new IllegalStateException("couldn't move cursor to position " + position);
                    return;
                }

                String name1 = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME));
                String isItFavorite = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE));

                if (isItFavorite.equalsIgnoreCase("true")) {
                    favorite.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_outlined_star_favorites));
                    addToFavorites(name1, "false");
                } else {
                    favorite.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_solid_favorites_star));
                    addToFavorites(name1, "true");
                }
            }
        });

        image.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_location_pointer));
        //name.setText("Item 1");
        value.setText("Item 1 " + mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME)));
        if(isFavorite.equalsIgnoreCase("true")){
            favorite.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_solid_favorites_star));
        }else {
            favorite.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_outlined_star_favorites));
        }
        return vi;
    }





    void addToFavorites(String name, String isFavorite){
        ContentValues cv = new ContentValues();
        cv.put(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE,isFavorite);
        cv.put(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME, name);

        int i = db.update(FavoritesContract.FeedEntry.TABLE_NAME, cv, FavoritesContract.FeedEntry.COLUMN_ITEM_NAME + " = ?", new String[]{name});
    }

    Cursor getAllValuesFromDB(){


        DataBaseHelper mDbHelper= new DataBaseHelper(activity);

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();



// Define a projection that specifies which columns from the database
// you will actually use after this query.
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
}
