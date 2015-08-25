package com.philips.cdp.ui.catalog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by 310213764 on 25-08-2015.
 */
public class SplashActivity extends AppCompatActivity {
    boolean mError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if(getIntent().hasExtra(SplashLauncher.SPLASH_MODE)) {
        int mode = getIntent().getIntExtra(SplashLauncher.SPLASH_MODE, -1);

            int layoutID = getLayoutID(mode);
            if(mode == -1) {
                mError = true;
                finish();
            } else {
                setContentView(layoutID);
            }

        } else {
            mError = true;
            finish();
        }
    }

    private int getLayoutID(int mode) {
        int layoutID = -1;
        switch (mode) {
            case R.id.lt:
                layoutID = com.philips.cdp.uikit.R.layout.splash_screen_logo_top;
                break;
            case R.id.lb:
                layoutID = com.philips.cdp.uikit.R.layout.splash_screen_logo_bottom;
                break;
            case R.id.lc_tt:
                layoutID = com.philips.cdp.uikit.R.layout.splash_screen_logo_center_tt;
                break;
            case R.id.lc_tb:
                layoutID = com.philips.cdp.uikit.R.layout.splash_screen_logo_center_tb;
                break;
        }
        return layoutID;
    }

    @Override
    public void finish() {
        if(mError) {
            Toast.makeText(this,"Something went wrong" , Toast.LENGTH_SHORT).show();
        }
        super.finish();
    }
}
