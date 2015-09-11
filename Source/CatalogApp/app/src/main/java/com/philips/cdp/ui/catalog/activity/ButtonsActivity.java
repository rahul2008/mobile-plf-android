package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.philips.cdp.ui.catalog.R;


public class ButtonsActivity extends CatalogActivity {

//    Button button;
//    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
//        button = (Button) findViewById(R.id.button);
//        button.setText("Changed Text");
//
//        button2 = (Button) findViewById(R.id.button2);
//        button2.setText("Changed Text");
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
}
