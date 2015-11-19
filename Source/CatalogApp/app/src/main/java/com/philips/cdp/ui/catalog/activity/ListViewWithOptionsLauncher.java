package com.philips.cdp.ui.catalog.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.CustomListView.ListViewWithOptions;
import com.philips.cdp.ui.catalog.R;

public class ListViewWithOptionsLauncher extends CatalogActivity {
    //LinearLayout ratingBarLayout;
    ListViewWithOptions adapter;
    ListView list;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_with_options);

        list=(ListView)findViewById(R.id.list);

        adapter=new ListViewWithOptions(this);
        list.setAdapter(adapter);
        list.setDividerHeight(1);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

