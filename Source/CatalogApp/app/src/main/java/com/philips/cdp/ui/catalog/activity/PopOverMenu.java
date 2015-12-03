package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;

public class PopOverMenu extends CatalogActivity {

    private ListPopupWindow listpopupwindow;
    TextView tv;
    String[] value = new String[] {"1","2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_over_menu2);
        tv = (TextView)findViewById(R.id.text1);

        listpopupwindow = new ListPopupWindow(PopOverMenu.this);
        listpopupwindow.setAnchorView(tv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, value);
        listpopupwindow.setAdapter(adapter);
        //listpopupwindow.setHorizontalOffset(50);
        //listpopupwindow.setVerticalOffset(listpopupwindow.getAnchorView().getBottom() - listpopupwindow.getAnchorView().getTop() );

        //listpopupwindow.setDropDownGravity(Gravity.START);





//        listpopupwindow.setDropDownGravity(Gravity.RIGHT);
        listpopupwindow.setModal(true);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                //listpopupwindow.setVerticalOffset((listpopupwindow.getListView().getScrollY()) );

                listpopupwindow.show();
                registerForContextMenu(listpopupwindow.getListView());

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pop_over_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
