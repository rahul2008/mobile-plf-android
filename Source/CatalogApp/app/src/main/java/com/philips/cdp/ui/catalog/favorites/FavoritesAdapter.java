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

public class FavoritesAdapter extends BaseAdapter {
    public Context activity;
    SQLiteDatabase db ;
    DataBaseHelper mDbHelper;
    private LayoutInflater inflater = null;

    public FavoritesAdapter(Context activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

    /**
     * OnClick Of the Star to be Handled here
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
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
                    mCursor = getFavFromDB();
                    mCursor.moveToPosition(position);
                    String name = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME));
                    updateFavInfo(name);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(mCursor!=null)
                        mCursor.close();
                }
            }
        });

        image.setImageDrawable(VectorDrawable.create(activity, R.drawable.uikit_location_pointer));
        favorite.setImageDrawable(VectorDrawable.create(activity, R.drawable.uikit_solid_favorites_star));
        Cursor mCursor = null;
        try {
            mCursor = getFavFromDB();
            mCursor.moveToPosition(position);
            value.setText("Item 1 " + mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME)));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(mCursor!=null)
                mCursor.close();
        }
        return vi;
    }

    /**
     * Update Database with the new Value - Favorite or UnFavorite
     * @param name - The title of the entry that has to be Updated
     */
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
            if(mDbHelper!=null)
                mDbHelper.close();
            if(db!=null)
                db.close();
            if(mCursor!=null)
                mCursor.close();
        }
    }

    /**
     * Get All the Values from the DB that are marked as Favorite by the User. Initially All the Values will be false in the DB
     * @return
     */
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

    /**
     * Close the connection Objects after the activity is destroyed
     */
    public void closeConnections(){
        if(mDbHelper!=null)
            mDbHelper.close();
        if(db!=null)
            db.close();
    }
}
