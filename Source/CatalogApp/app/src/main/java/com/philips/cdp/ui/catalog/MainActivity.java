package com.philips.cdp.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.philips.cdp.ui.catalog.activity.UiKitActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

public class MainActivity extends UiKitActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private static final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTheme(ThemeUtils.getTheme(this));
    }

    private void createListView() {
        listView = (ListView) findViewById(R.id.listView);
        String[] listItems = getResources().getStringArray(R.array.list_items);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems));
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, MiscellaneousButtonsActivity.class));
                break;
            case 1:
                startActivityForResult(new Intent(this, BackgroundTest.class), REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == BackgroundTest.RESULT_CODE_THEME_UPDATED) {
                finish();
                startActivity(getIntent());
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
