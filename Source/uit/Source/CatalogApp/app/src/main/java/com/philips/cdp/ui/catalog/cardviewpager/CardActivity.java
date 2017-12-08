/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.ui.catalog.cardviewpager;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.CatalogActivity;

/**
 *  <b>Find the below steps to use Cards</b><br>
 *      <pre>
 *          1.To support cards need to use Recycler View , call API setContentView use below layout code for reference<pre>
 *              &lt;?xml version="1.0" encoding="utf-8"?&gt
 &lt;LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
 android:layout_width="match_parent"
 android:layout_height="match_parent"
 android:background="?attr/uikit_gradWindowBackground"
 android:gravity="center"
 android:orientation="vertical"&gt;

 &lt;com.philips.cdp.ui.catalog.cardviewpager.CustomRecyclerView
 android:id="@+id/recyclerView"
 android:layout_width="match_parent"
 android:layout_height="wrap_content"
 android:layout_gravity="center"
 android:orientation="vertical" /&gt;
 &lt;/LinearLayout&gt;</pre>
 2. Create Adapter class which extends RecyclerView.Adapter&lt;CardAdapter.ViewHolder&gt;
 3. Override onCreateViewHolder using LayoutInflater return layout uikit_card_view from UIKitLib
 */
public class CardActivity extends CatalogActivity {

    RecyclerView recyclerView;
    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        handleOrientationView();
    }

    private void handleOrientationView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        Resources res = getResources();
        Configuration conf = res.getConfiguration();

        int orientation = conf.orientation;

        LinearLayoutManager llm = new LinearLayoutManager(this);

        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                adapter = new CardAdapter(this);
                recyclerView.setAdapter(adapter);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(llm);
                adapter = new CardAdapter(this);
                recyclerView.setAdapter(adapter);
                break;
        }
    }

}
