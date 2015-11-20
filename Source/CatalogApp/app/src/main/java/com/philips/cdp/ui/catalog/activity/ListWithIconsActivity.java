package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.CustomListView.ListViewWithIcons;
import com.philips.cdp.ui.catalog.R;

/**
 * Created by 310213373 on 11/18/2015.
 */
public class ListWithIconsActivity extends CatalogActivity {
    ListViewWithIcons mAdapter;
    ListView list;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_with_icons);

        list=(ListView)findViewById(R.id.list);
        list.setDividerHeight(1);
      //  TextView text=(TextView) findViewById(R.id.sectionheader);
      //  text.setText("Title Pallendia");
       // adapter=new ListViewWithIcons(this);
        mAdapter = new ListViewWithIcons(this);
        mAdapter.addSectionHeaderItem("Title Pallendia");
        mAdapter.addItem("Quisque ");
        mAdapter.addItem("Eget Odio ");
        mAdapter.addItem("Foscibus ");
        mAdapter.addItem("AC Lectus ");
        mAdapter.addItem("Pellentesque ");
        mAdapter.addSectionHeaderItem("Title Pallendia");
        mAdapter.addItem("Vestibullum ");
        mAdapter.addItem("Nulla Facilisi ");
        mAdapter.addItem("Tortor ");




       // setListAdapter(mAdapter);
        list.setAdapter(mAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
