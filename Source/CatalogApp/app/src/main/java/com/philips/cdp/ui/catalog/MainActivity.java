package com.philips.cdp.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.philips.cdp.ui.catalog.activity.UiKitActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

public class MainActivity extends UiKitActivity implements AdapterView.OnItemClickListener {

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
        ListView listView = (ListView) findViewById(R.id.listView);
        String[] listItems = getResources().getStringArray(R.array.list_items);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, MiscellaneousButtonsActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, SplashLauncher.class));
                break;
            case 2:
                startActivity(new Intent(this, ThemesActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == ThemesActivity.RESULT_CODE_THEME_UPDATED) {
                finish();
                startActivity(getIntent());
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
