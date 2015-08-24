package com.philips.cdp.ui.catalog;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class BackgroundTest extends AppCompatActivity {

    private final static String THEME_EXTRA = "theme_extra";
    private final static String TAG = BackgroundTest.class.getSimpleName();

    private int mCurrentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCurrentID = R.style.PhilipsTheme_Default_Light_Blue;
        if (getIntent().getExtras() != null) {
            int extraID = getIntent().getIntExtra(THEME_EXTRA, -1);
            Log.d(TAG, " mCurrentID id =" + mCurrentID + " extraID=" + extraID);
            if (extraID != -1) {
                mCurrentID = extraID;
            }
        }
        Log.d(TAG, " mCurrentID id =" + mCurrentID);
        setTheme(mCurrentID);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_test);
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

    public void changeBackground(View v) {
        int id = v.getId();
        int extraID = -1;
        if (id == R.id.dark_blue) {
            extraID = R.style.PhilipsTheme_Default_Dark_Blue;
        } else if (id == R.id.light_blue) {
            extraID = R.style.PhilipsTheme_Default_Light_Blue;
        }
        if (mCurrentID != extraID) {
            Intent intent = new Intent(this, BackgroundTest.class);
            intent.putExtra(THEME_EXTRA, extraID);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Theme already applied", Toast.LENGTH_SHORT).show();
        }
    }
}
