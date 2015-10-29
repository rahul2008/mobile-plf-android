package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.costumviews.UikitSeekbar;

public class SliderActivity extends CatalogActivity {


    LinearLayout sliderLinearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        sliderLinearlayout = (LinearLayout)findViewById(R.id.sliderLinearlayout);
        UikitSeekbar seekbar = new UikitSeekbar(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(800, 800);
        seekbar.setLayoutParams(lp);
        sliderLinearlayout.addView(seekbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_slider, menu);
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
