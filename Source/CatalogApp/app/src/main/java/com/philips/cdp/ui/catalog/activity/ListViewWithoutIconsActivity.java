package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.CustomListView.ListViewWithIcons;
import com.philips.cdp.ui.catalog.CustomListView.ListViewWithoutIcons;
import com.philips.cdp.ui.catalog.R;

/**
 * Created by 310213373 on 11/18/2015.
 */
public class ListViewWithoutIconsActivity extends CatalogActivity {
    ListViewWithoutIcons adapter;
    ListView list;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_without_icons);

        list=(ListView)findViewById(R.id.list);

        adapter=new ListViewWithoutIcons(this);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
