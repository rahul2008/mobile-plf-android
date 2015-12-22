package com.philips.cdp.ui.catalog.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.SplashLauncher;
import com.philips.cdp.ui.catalog.cardviewpager.CardActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends CatalogActivity implements AdapterView.OnItemClickListener {

    private static final int REQUEST_CODE = 10;
    private HashMap<Integer, String> itemsMap = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTheme(new ThemeUtils(this.getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE)).getTheme());
    }

    private void createListView() {
        ListView listView = (ListView) findViewById(R.id.listView);
        addHeaderVersion(listView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getDemoItems().values().toArray(new String[1])));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        int key = getKeyFromValue((String) textView.getText());
        //We find the position from the value
        switch (key) {
            case 0:
                startActivity(new Intent(this, ActionButtonsActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, SplashLauncher.class));
                break;
            case 2:
                startActivityForResult(new Intent(this, ThemesActivity.class), REQUEST_CODE);
                break;
            case 3:
                startActivity(new Intent(this, ButtonsActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, DotNavigation.class));
                break;
            case 5:
                startActivity(new Intent(this, ImageNavigation.class));
                break;
            case 6:
                startActivity(new Intent(this, InputTextFieldsActivity.class));
                break;
            case 7:
                startActivity(new Intent(this, AboutScreenLauncher.class));
                break;
            case 8:
                startActivity(new Intent(this, TabBarDemo.class));
                break;

            case 9:
                startActivity(new Intent(this, SliderActivity.class));
                break;
            case 10:
                startActivity(new Intent(this, RatingBarLauncher.class));
                break;

            case 12:
                startActivity(new Intent(this, ActionBarLauncher.class));
                break;
            case 13:
                startActivity(new Intent(this, HamburgerActivity.class));
                break;

            case 14:
                startActivity(new Intent(this, SocialIconsActivity.class));
                break;

            case 15:
                startActivity(new Intent(this, TabViewWithViewPager.class));
                break;
            case 16:
                startActivity(new Intent(this, PopOverMenu.class));
                break;
            case 17:
                startActivity(new Intent(this, SpringBoardsctivity.class));
                break;
            case 19:
                startActivity(new Intent(this, CardActivity.class));
                break;
			case 18:
                startActivity(new Intent(this, TextLayoutInputFeildInlineForms.class));
                break;
            case 19:
                startActivity(new Intent(this, ProgressBar.class));
                break;
            default:
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

    private int getKeyFromValue(String value) {
        for (Map.Entry<Integer, String> entry : itemsMap.entrySet()) {
            if (entry.getValue().equals(value))
                return entry.getKey();
        }
        return 0;
    }

    private void addHeaderVersion(ListView lv) {
        int code = 0;
        try {
            code = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView version = new TextView(this);
        version.setText("Version:" + code);
        version.setBackgroundColor(Color.GRAY);
        version.setGravity(Gravity.CENTER);
        lv.addHeaderView(version, null, false);
    }

    private HashMap<Integer, String> getDemoItems() {
        itemsMap = new LinkedHashMap<Integer, String>();
        itemsMap.put(0, "Action Buttons");
        itemsMap.put(1, "Splash Screen");
        itemsMap.put(2, "Change Theme");
        itemsMap.put(3, "Buttons");
        itemsMap.put(4, "Dot Navigation");
        itemsMap.put(5, "Image Navigation");
        itemsMap.put(6, "Input Text Fields");
        itemsMap.put(7, "About Screen");
        itemsMap.put(8, "Tab Bar");
        itemsMap.put(9, "Sliders");
        itemsMap.put(10, "Rating Bar");
        itemsMap.put(12, "Action Bar Up");
        itemsMap.put(13,"Hamburger Menu");
        itemsMap.put(14,"Social Media Icons");
        itemsMap.put(15, "Lists View Demo");
        itemsMap.put(16, "Pop Over Menu");
        itemsMap.put(17,"Spring Board");
        itemsMap.put(17,"Spring Board");
        itemsMap.put(19, "Cards");
        itemsMap.put(17,"Spring Board");       
        itemsMap.put(18, "Inline Forms");
        itemsMap.put(19, "ProgressBar");
        return itemsMap;
    }



}
