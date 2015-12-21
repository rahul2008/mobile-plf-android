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
    SQLiteDatabase db ;
    DataBaseHelper mDbHelper;
    public FavoritesAdapter(Context activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);

        mDbHelper = new DataBaseHelper(activity);
        db = mDbHelper.getReadableDatabase();
    }


    @Override
    public int getCount() {
        int count = -1;
        Cursor mCursor = null;
        try {
            mCursor = getFavFromDB();
            count = mCursor.getCount();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
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
                Cursor mCursor = null;
                try {
                    mCursor = getFavFromDB();
                    mCursor.moveToPosition(position);
                    String name = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME));
                    updateFavInfo(name);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mCursor.close();
                }
            }
        });

        image.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_location_pointer));
        favorite.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_solid_favorites_star));
        Cursor mCursor = null;
        try {
            mCursor = getFavFromDB();
            mCursor.moveToPosition(position);
            value.setText("Item 1 " + mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME)));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
         mCursor.close();
        }
        return vi;
    }

    void updateFavInfo(String name){
        DataBaseHelper mDbHelper = new DataBaseHelper(activity);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();// = mDbHelper.getReadableDatabase();
        Cursor mCursor = null;
        try {

            ContentValues cv = new ContentValues();
            cv.put(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE, "false");
            cv.put(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME, name);

            int i = db.update(FavoritesContract.FeedEntry.TABLE_NAME, cv, FavoritesContract.FeedEntry.COLUMN_ITEM_NAME + " = ?", new String[]{name});
            mCursor = getFavFromDB();
            notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDbHelper.close();
            db.close();
            mCursor.close();
        }
    }

    Cursor getFavFromDB(){

        String[] projection = {
                FavoritesContract.FeedEntry.COLUMN_ITEM_NAME,
                FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE
        };

        String whereClause = FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE+" = ?";
        String [] whereArgs = new String[] {"true"};
        Cursor c = null;
        try {
            c = db.query(
                    FavoritesContract.FeedEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereClause,                                // The columns for the WHERE clause
                    whereArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }

    public void closeConnections(){
        mDbHelper.close();
        db.close();
    }
}
