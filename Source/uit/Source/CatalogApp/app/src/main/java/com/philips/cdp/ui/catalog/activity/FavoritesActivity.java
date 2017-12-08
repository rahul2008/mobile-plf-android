/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.ui.catalog.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.favorites.DataBaseHelper;
import com.philips.cdp.ui.catalog.favorites.FavoritesContract;
import com.philips.cdp.ui.catalog.favorites.FavoritesContract.FeedEntry;
import com.philips.cdp.ui.catalog.favorites.FavoritesPagerAdapter;
import com.philips.cdp.uikit.utils.TabUtils;

/**
 * <b></b> FavoritesActivity is class to demonstrate the use of Favorites with the help of adapters (FavoritesAdapter, FavoritesAdapterAll) and Database </b>
 * <p/>
 * <b></b> FavoritesAdapter is class to demonstrate the use of uikit_favorites_item_layout with an adapter </b>
 * <pre>
 * ImageView image = (ImageView) vi.findViewById(R.id.imagefav);
 * TextView value = (TextView) vi.findViewById(R.id.item_name);
 * final ImageView favorite = (ImageView) vi.findViewById(R.id.favorite);
 * FrameLayout frame = (FrameLayout) vi.findViewById(R.id.favoritecontainer);
 * frame.setOnClickListener(new View.OnClickListener() {// your code}
 * </pre>
 * <p/>
 * <b></b> FavoritesAdapterAll is class to demonstrate the use of uikit_favorites_item_layout with an adapter </b>
 * <p/>
 * <pre>
 * ImageView image = (ImageView) vi.findViewById(R.id.imagefav);
 * TextView value = (TextView) vi.findViewById(R.id.item_name);
 * final ImageView favorite = (ImageView) vi.findViewById(R.id.favorite);
 * FrameLayout frame = (FrameLayout) vi.findViewById(R.id.favoritecontainer);
 * frame.setOnClickListener(){//your code}
 * </pre>
 * <p/>
 * <b></b> DataBase Related classes are: DataBaseHelper, FavoritesContract</b>
 * <p/>
 * <b></b> Favorites is Demonstrated through Custom TabLayout and ViewPager for swapping of Pages</b>
 * <p/>
 * <b></b> In The Layout File Add a style to TabLayout as follows: </b>
 * <p/>
 * <b></b> Style Name: style="@style/FavTabLayout </b>
 * <p/>
 * <pre>&lt;android.support.design.widget.TabLayout
 * android:id="@+id/tab_layout"
 * style="@style/FavTabLayout"
 * android:layout_below="@id/some"/&gt;
 * </pre>
 */
public class FavoritesActivity extends CatalogActivity {
    TabLayout layout;
    Context context;
    DataBaseHelper mDbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_activity);
        context = this;
        TabUtils.disableActionbarShadow(this);
        layout = (TabLayout) findViewById(R.id.tab_layout);

        View tabContent1 = LayoutInflater.from(context).inflate(R.layout.uikit_fav_custom_tab_content, null);
        TextView tv1 = (TextView) tabContent1.findViewById(R.id.tabtext);

        TabLayout.Tab tab = layout.newTab();
        layout.addTab(tab);
        tab.setCustomView(tabContent1);

        tv1.setText("All items");

        View tabContent2 = LayoutInflater.from(context).inflate(R.layout.uikit_fav_custom_tab_content, null);
        TextView tv2 = (TextView) tabContent2.findViewById(R.id.tabtext);

        TabLayout.Tab tab1 = layout.newTab();
        layout.addTab(tab1);
        tab1.setCustomView(tabContent2);
        tv2.setText("Favorites");

      //  layout.addTab(layout.newTab().setText("All Items"));
      //  layout.addTab(layout.newTab().setText("Favorites"));


        layout.getTabAt(1).select();
       layout.getTabAt(0).select();
        setViewPager();

        try {
            mDbHelper = new DataBaseHelper(this);
            db = mDbHelper.getWritableDatabase();
            Cursor c = db.query(
                    FavoritesContract.FeedEntry.TABLE_NAME,  // The table to query
                    null,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );
            if (c.getCount() <= 0)
                insertIntoDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mDbHelper!=null)
                mDbHelper.close();
            if(db!=null)
                db.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TabUtils.adjustTabs(layout, this);
    }

    private void setViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final FavoritesPagerAdapter adapter = new FavoritesPagerAdapter(getSupportFragmentManager(), layout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(layout));

        layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(final TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(final TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }


    /**
     * Insert Values into the Database
     */
    public void insertIntoDatabase() {

        ContentValues values = new ContentValues();
        values.put(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME, "QuisQue");
        values.put(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE, "false");
        long newRowId;

        db.insertWithOnConflict(FeedEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        values = new ContentValues();
        values.put(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME, "Eget Odio");
        values.put(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE, "false");

        db.insertWithOnConflict(FeedEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        values = new ContentValues();
        values.put(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME, "Ac Lectus");
        values.put(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE, "false");

        db.insertWithOnConflict(FeedEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        values = new ContentValues();
        values.put(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME, "Faucibus");
        values.put(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE, "false");

        db.insertWithOnConflict(FeedEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        values = new ContentValues();
        values.put(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME, "Eget In");
        values.put(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE, "false");

        db.insertWithOnConflict(FeedEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        values = new ContentValues();
        values.put(FavoritesContract.FeedEntry.COLUMN_ITEM_NAME, "Pellentesque");
        values.put(FavoritesContract.FeedEntry.COLUMN_IS_FAVORITE, "false");

        db.insertWithOnConflict(FeedEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
