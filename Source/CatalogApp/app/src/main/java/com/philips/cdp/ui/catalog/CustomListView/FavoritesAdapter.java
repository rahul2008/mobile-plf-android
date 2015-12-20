package com.philips.cdp.ui.catalog.CustomListView;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.DataBaseHelper;
import com.philips.cdp.ui.catalog.activity.FavoritesContract;
import com.philips.cdp.uikit.customviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class FavoritesAdapter extends BaseAdapter {
    private LayoutInflater inflater=null;
    public Context activity;
    Cursor mCursor;

    public FavoritesAdapter(Context activity) {

        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        mCursor = getFavFromDB();

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

        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mCursor = getFavFromDB();
                if (mCursor == null) {
                    //throw new IllegalStateException("this should only be called when the cursor is valid");
                    return;
                }
                if (!mCursor.moveToPosition(position)) {
                    //throw new IllegalStateException("couldn't move cursor to position " + position);
                    return;
                }
                String name = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME));

                updateFavInfo(name);
            }
        });

        image.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_location_pointer));




        if (mCursor == null) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        value.setText("Item 1 " + mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME)));
        favorite.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_solid_favorites_star));

        return vi;
    }

    void updateFavInfo(String name){
        DataBaseHelper mDbHelper = new DataBaseHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();// = mDbHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE, "false");
        cv.put(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME, name);

        int i = db.update(FavoritesContract.FeedEntry.TABLE_NAME, cv, FavoritesContract.FeedEntry.COLUMN_ITEM_NAME + " = ?", new String[]{name});
    }




    Cursor getFavFromDB(){

        DataBaseHelper mDbHelper= new DataBaseHelper(activity);

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                FavoritesContract.FeedEntry.COLUMN_ITEM_NAME,
                FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE
        };

        String whereClause = FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE+" = ?";
        String [] whereArgs = new String[] {"true"};


        Cursor c = db.query(
                FavoritesContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        return c;
    }
}
